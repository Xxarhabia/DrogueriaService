package com.msara.servicio.services.impl;

import com.msara.servicio.controllers.dto.request.TransactionAnnulmentRequest;
import com.msara.servicio.controllers.dto.response.TransactionResponse;
import com.msara.servicio.domain.entities.*;
import com.msara.servicio.domain.repositories.*;
import com.msara.servicio.services.interfaces.TransactionService;

import com.msara.servicio.utils.VoucherUtils;
import com.msara.servicio.utils.pdf.PdfUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VoucherUtils voucherUtils;
    @Autowired
    private PdfUtils pdfUtils;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public TransactionResponse buyProduct(Long userId, boolean generateVoucher) throws SQLException {

        Connection connection = jdbcTemplate.getDataSource().getConnection();
        String procedure = "CALL drogueria_schema.sp_buy_transaction(?,?,?,?,?)";

        try (CallableStatement callableStatement = connection.prepareCall(procedure)) {
            connection.setAutoCommit(true);

            callableStatement.setLong(1, userId);
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            callableStatement.registerOutParameter(3, Types.BOOLEAN);
            callableStatement.registerOutParameter(4, Types.BIGINT);
            callableStatement.registerOutParameter(5, Types.DOUBLE);
            callableStatement.execute();

            String message = callableStatement.getString(2);
            boolean status = callableStatement.getBoolean(3);
            Long transactionId = callableStatement.getLong(4);
            double total = callableStatement.getDouble(5);

            if (generateVoucher && status) {
                TransactionEntity transaction = transactionRepository.findById(transactionId).orElseThrow(
                        () -> new RuntimeException("Transaction not found"));
                UserEntity user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                String pdfVoucher;
                String htmlVoucher = voucherUtils.generateVoucherSale(transaction, user, total);
                try {
                    pdfVoucher = pdfUtils.generatePdf(htmlVoucher);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                VoucherEntity voucher = VoucherEntity.builder()
                        .htmlVoucher(htmlVoucher)
                        .pdfVoucher(pdfVoucher)
                        .build();
                voucherRepository.save(voucher);
            }

            return new TransactionResponse(
                    "SALE",
                    message,
                    status,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        }
    }

    @Transactional
    @Override
    public TransactionResponse returnProduct(Long userId, TransactionAnnulmentRequest transactionAnnulmentRequest) throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        String procedure = "CALL drogueria_schema.sp_annulment_transaction(?,?,?,?)";

        try (CallableStatement callableStatement = connection.prepareCall(procedure)) {
            connection.setAutoCommit(true);

            callableStatement.setString(1, transactionAnnulmentRequest.reference());
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            callableStatement.registerOutParameter(3, Types.BOOLEAN);
            callableStatement.registerOutParameter(4, Types.BIGINT);
            callableStatement.execute();

            String message = callableStatement.getString(2);
            boolean status = callableStatement.getBoolean(3);
            Long transactionId = callableStatement.getLong(4);

            if (transactionAnnulmentRequest.generateVoucher() && status) {
                TransactionEntity transaction = transactionRepository.findById(transactionId).orElseThrow(
                        () -> new RuntimeException("Transaction not fount"));
                UserEntity user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                String pdfVoucher;
                String htmlVoucher = voucherUtils.generateVoucherAnnulment(transaction, user);
                try {
                    pdfVoucher = pdfUtils.generatePdf(htmlVoucher);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                VoucherEntity voucher = VoucherEntity.builder()
                        .htmlVoucher(htmlVoucher)
                        .pdfVoucher(pdfVoucher)
                        .build();
                voucherRepository.save(voucher);

            }

            return new TransactionResponse(
                    "ANNULMENT",
                    message,
                    status,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        }

    }

    @Override
    public void changeProduct() {

    }
}
