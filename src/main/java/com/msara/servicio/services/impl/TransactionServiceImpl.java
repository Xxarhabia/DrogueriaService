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
        List<CartItemEntity> cartItems = cartRepository.findItemsByUserId(userId);
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

        TransactionEntity transaction = TransactionEntity.builder()
                .reference(dataManagementUtils.referenceNumber())
                .typeTransaction(TransactionEnum.valueOf(TransactionEnum.SALE.name()))
                .dateInsertTransaction(String.valueOf(LocalDateTime.now()))
                .dateUpdateTransaction(String.valueOf(LocalDateTime.now()))
                .products(products)
                .build();

        //We look for the user to associate the transaction with the user
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getTransactions().add(transaction);
        transaction.getUser().add(user);
        transactionRepository.save(transaction); //We save the transaction

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

        //we clean the cart
        cartItems.clear();
        CartEntity cart = CartEntity.builder()
                .items(cartItems)
                .build();
        cartRepository.save(cart);

        return new TransactionSaleResponse(
                "SALE", "The transaction was processed successfully", LocalDateTime.now());
    }

    @Override
    public void changeProduct() {

    }

    @Override
    public void returnProduct() {

    }
}
