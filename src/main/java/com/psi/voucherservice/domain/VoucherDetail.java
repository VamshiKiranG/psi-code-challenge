package com.psi.voucherservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "voucherDetail")
public class VoucherDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "voucherCode", unique = true)
    private String voucherCode;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private String status;

    public VoucherDetail(String voucherCode, String status, String description) {
        this.voucherCode = voucherCode;
        this.status = status;
        this.description = description;

    }
}
