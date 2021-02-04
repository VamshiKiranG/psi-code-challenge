package com.psi.voucherservice.service;

import com.psi.voucherservice.domain.VoucherDetail;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoucherService {
    List<VoucherDetail> findAll();

    Optional<VoucherDetail> findById(Long id);

    VoucherDetail save(VoucherDetail detail);

    Optional<VoucherDetail> findByVoucherCode(@Param("voucherCode") String voucherCode);
}
