package com.msara.servicio.services.impl;

import com.msara.servicio.controllers.dto.response.TransactionSaleResponse;
import com.msara.servicio.domain.entities.*;
import com.msara.servicio.domain.enums.TransactionEnum;
import com.msara.servicio.domain.repositories.*;
import com.msara.servicio.services.interfaces.TransactionService;
import com.msara.servicio.utils.DataManagementUtils;
import com.msara.servicio.utils.DataManagementUtils.*;

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
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private TransactionRepository transactionRepository;
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
    public TransactionSaleResponse buyProduct(Long userId, boolean generateVoucher) throws SQLException {

        Connection connection = jdbcTemplate.getDataSource().getConnection();
        String procedure = "CALL drogueria_schema.sp_buy_transaction(?,?,?)";

        try (CallableStatement callableStatement = connection.prepareCall(procedure)) {
            connection.setAutoCommit(true);

            callableStatement.setLong(1, userId);
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            callableStatement.registerOutParameter(3, Types.BOOLEAN);
            callableStatement.execute();

            String message = callableStatement.getString(2);
            boolean status = callableStatement.getBoolean(3);

            return new TransactionSaleResponse(
                    "SALE",
                    message,
                    status,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        }
    }

    @Override
    public void changeProduct() {

    }

    @Override
    public void returnProduct() {

    }
}
