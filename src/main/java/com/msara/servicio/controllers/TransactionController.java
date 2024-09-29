package com.msara.servicio.controllers;

import com.msara.servicio.controllers.dto.request.TransactionAnnulmentRequest;
import com.msara.servicio.controllers.dto.request.TransactionSaleRequest;
import com.msara.servicio.controllers.dto.response.TransactionResponse;
import com.msara.servicio.domain.repositories.UserRepository;
import com.msara.servicio.services.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/buy")
    public ResponseEntity<TransactionResponse> saleTransaction(
            @RequestBody TransactionSaleRequest transactionRequest) throws SQLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userRepository.findUserByEmail(username).orElseThrow().getId();

        return new ResponseEntity<TransactionResponse>(
                transactionService.buyProduct(userId, transactionRequest.generateVoucher()), HttpStatus.CREATED);
    }

    @PutMapping("/annulment")
    public ResponseEntity<TransactionResponse> annulmentTransaction (
            @RequestBody TransactionAnnulmentRequest transactionAnnulmentRequest) throws SQLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userRepository.findUserByEmail(username).orElseThrow().getId();

        return new ResponseEntity<TransactionResponse>(
                transactionService.returnProduct(userId, transactionAnnulmentRequest), HttpStatus.OK);
    }

}
