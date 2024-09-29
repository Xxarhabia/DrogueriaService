package com.msara.servicio.controllers;

import com.msara.servicio.controllers.dto.request.CartRequest;
import com.msara.servicio.controllers.dto.response.CartAddItemResponse;
import com.msara.servicio.domain.repositories.UserRepository;
import com.msara.servicio.services.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartServiceImpl cartService;

    @PostMapping("/add")
    public ResponseEntity<CartAddItemResponse> addProductInCart(@RequestBody CartRequest cartRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long idUser = userRepository.findUserByEmail(username).orElseThrow().getId();

        return new ResponseEntity<>(cartService.addProductToCart(idUser, cartRequest), HttpStatus.CREATED);
    }
}
