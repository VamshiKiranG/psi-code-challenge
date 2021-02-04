package com.psi.voucherservice.service.impl;

import com.psi.voucherservice.domain.VoucherDetail;
import com.psi.voucherservice.repository.VoucherDetailRepository;
import com.psi.voucherservice.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    VoucherDetailRepository repository;

    @Override
    public List<VoucherDetail> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<VoucherDetail> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public VoucherDetail save(VoucherDetail detail) {
        return repository.save(detail);
    }

    @Override
    public Optional<VoucherDetail> findByVoucherCode(String voucherCode) {
        return repository.findByVoucherCode(voucherCode);
    }
}
