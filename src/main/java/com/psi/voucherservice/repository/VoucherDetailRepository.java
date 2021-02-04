package com.psi.voucherservice.repository;

import com.psi.voucherservice.domain.VoucherDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoucherDetailRepository extends JpaRepository<VoucherDetail, Long> {

    @Query("select p from VoucherDetail p where p.voucherCode = :voucherCode")
    Optional<VoucherDetail> findByVoucherCode(@Param("voucherCode") String voucherCode);
}
