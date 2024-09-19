package com.msara.servicio.utils;

import com.msara.servicio.domain.entities.ProductEntity;
import com.msara.servicio.domain.entities.TransactionEntity;
import com.msara.servicio.domain.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Locale;


@Component
public class VoucherUtils {

    @Autowired
    private ITemplateEngine templateEngine;

    public String generateVoucherSale(TransactionEntity transaction, UserEntity user, double total) {
        Context context = new Context(Locale.getDefault());

        //user data
        context.setVariable("username", user.getName());
        context.setVariable("userAddress", user.getAddress());
        context.setVariable("userEmail", user.getEmail());

        //transaction data
        context.setVariable("typeTrx", "Venta");
        context.setVariable("reference", transaction.getReference());
        context.setVariable("dateTrx", transaction.getDateInsertTransaction());

        //product data
        context.setVariable("transactionItems", transaction.getTransactionItems());

        //tota;
        context.setVariable("total", total);

        return templateEngine.process("VoucherSale", context);
    }
}
