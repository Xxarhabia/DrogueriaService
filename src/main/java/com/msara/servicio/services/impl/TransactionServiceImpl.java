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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Override
    public TransactionSaleResponse buyProduct(Long userId, boolean generateVoucher) {
        DataManagementUtils dataManagementUtils = new DataManagementUtils();
        CartEntity cartFound = cartRepository.findByUserId(userId);
        List<CartItemEntity> cartItems = cartRepository.findById(cartFound.getId()).orElseThrow().getItems();
        if (cartItems.isEmpty()) {
            throw new RuntimeException("The cart is empty");
        }

        List<ProductEntity> products = new ArrayList<>();
        // agregamos el producto del carrito a la transacción
        // restamos la cantidad del stock del producto
        for (CartItemEntity cartItem : cartItems) {
            products.add(cartItem.getProduct());
            ProductEntity productInCart = productRepository.findById(cartItem.getProduct().getId()).orElseThrow();
            productInCart.setStock(productInCart.getStock() - cartItem.getQuantity());
            productRepository.save(productInCart);
        }
        CartItemEntity cartItemFound = cartItemRepository.findByCartId(cartFound.getId());
        cartItemRepository.deleteById(cartItemFound.getId());

        TransactionEntity transaction = TransactionEntity.builder()
                .reference(dataManagementUtils.referenceNumber())
                .typeTransaction(TransactionEnum.valueOf(TransactionEnum.SALE.name()))
                .dateInsertTransaction(String.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))))
                .dateUpdateTransaction(String.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))))
                .products(products)
                .build();

        //We look for the user to associate the transaction with the user
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //inicializamos las listas en caso de que no lo estén
        if(user.getTransactions() == null) {
            user.setTransactions(new ArrayList<>());
        }
        user.getTransactions().add(transaction);
        if(transaction.getUsers() == null) {
            transaction.setUsers(new ArrayList<>());
        }
        transaction.getUsers().add(user);

        System.out.println("antes de guardar trx");
        transactionRepository.save(transaction); //We save the transaction
        System.out.println("despues de guardar trx");

        if (generateVoucher) {
            String pdfVoucher;
            String htmlVoucher = voucherUtils.generateVoucherSale(transaction, user);
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

        return new TransactionSaleResponse(
                "SALE",
                "The transaction was processed successfully",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
    }

    @Override
    public void changeProduct() {

    }

    @Override
    public void returnProduct() {

    }
}
