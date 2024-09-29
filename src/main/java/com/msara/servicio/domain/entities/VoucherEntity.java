package com.msara.servicio.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vouchers")
@Builder
public class VoucherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "html_voucher", columnDefinition = "TEXT")
    private String htmlVoucher;

    @Column(name = "pdf_voucher", columnDefinition = "TEXT")
    private String pdfVoucher;
}
