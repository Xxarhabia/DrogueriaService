package com.msara.servicio.domain.entities;

import com.msara.servicio.domain.enums.TransactionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
@Builder
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String reference;

    @Column(name = "type_trx")
    @Enumerated(EnumType.STRING)
    private TransactionEnum typeTransaction;

    @Column(name = "date_insert_trx")
    private String dateInsertTransaction;

    @Column(name = "date_update_trx")
    private String dateUpdateTransaction;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "transaction_products", joinColumns = @JoinColumn(name = "transaction_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<ProductEntity> products = new ArrayList<>();

    @ManyToMany(mappedBy = "transactions")
    private List<UserEntity> users = new ArrayList<>();
}
