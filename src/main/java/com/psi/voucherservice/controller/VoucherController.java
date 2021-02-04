package com.psi.voucherservice.controller;

import com.psi.voucherservice.domain.VoucherDetail;
import com.psi.voucherservice.exception.DuplicateRecordFoundException;
import com.psi.voucherservice.exception.RecordNotFoundException;
import com.psi.voucherservice.exception.ValdationException;
import com.psi.voucherservice.service.VoucherService;
import com.psi.voucherservice.util.VoucherUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.psi.voucherservice.util.VoucherUtils.validateVoucherActive;
import static com.psi.voucherservice.util.VoucherUtils.validateVoucherRedeem;

@RestController
@RequestMapping("/api")
public class VoucherController {

    @Autowired
    VoucherService service;

    @GetMapping("/vouchers")
    public ResponseEntity<List<VoucherDetail>> getAllVouchers() {
        List<VoucherDetail> voucherDetails = new ArrayList<>();
        service.findAll().forEach(voucherDetails::add);
        if (voucherDetails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(voucherDetails, HttpStatus.OK);
    }


    @GetMapping("/vouchers/{id}")
    public ResponseEntity<VoucherDetail> getVoucherById(@PathVariable("id") long id) {
        Optional<VoucherDetail> voucherData = service.findById(id);
        if (voucherData.isPresent()) {
            return new ResponseEntity<>(voucherData.get(), HttpStatus.OK);
        } else {
            throw new RecordNotFoundException("Voucher not found with id : " + id);
        }
    }

    @GetMapping("/vouchers/{voucherCode}")
    public ResponseEntity<VoucherDetail> getVoucherByVoucherCode(@PathVariable("voucherCode") String voucherCode) {
        Optional<VoucherDetail> voucherData = service.findByVoucherCode(voucherCode);
        if (voucherData.isPresent()) {
            return new ResponseEntity<>(voucherData.get(), HttpStatus.OK);
        } else {
            throw new RecordNotFoundException("Voucher not found with id : " + voucherCode);
        }
    }


    @PostMapping("/vouchers")
    public ResponseEntity<VoucherDetail> validateAndCreateVoucher(@RequestBody VoucherDetail voucherDetail) {

        Optional<VoucherDetail> byVoucherCode = service.findByVoucherCode(voucherDetail.getVoucherCode());
        if (byVoucherCode.isPresent()) {
            throw new DuplicateRecordFoundException("Duplicate Record Found with the voucher code " + voucherDetail.getVoucherCode());
        }
        if (!validateVoucherActive(voucherDetail)) {
            throw new ValdationException("Invalid Status. Status must be ACTIVE for new Voucher");
        }

        VoucherDetail _result = service
                .save(new VoucherDetail(voucherDetail.getVoucherCode(), voucherDetail.getStatus(), voucherDetail.getDescription()));
        return new ResponseEntity<>(_result, HttpStatus.CREATED);

    }

    @PutMapping("/vouchers/redeem/{voucherCode}")
    public ResponseEntity<VoucherDetail> redeemVoucher(@PathVariable("voucherCode") String voucherCode) {
        Optional<VoucherDetail> voucherDetail = service.findByVoucherCode(voucherCode);

        if (voucherDetail.isPresent()) {
            VoucherDetail detail = voucherDetail.get();
            if (validateVoucherActive(detail)) {
                detail.setStatus(VoucherUtils.REDEEMED);
                return new ResponseEntity<>(service.save(detail), HttpStatus.OK);
            } else {
                throw new ValdationException("The voucher status is not ACTIVE.");
            }
        } else {
            throw new RecordNotFoundException("Voucher code not found " + voucherCode);
        }
    }

    @PutMapping("/vouchers/cancel/{voucherCode}")
    public ResponseEntity<VoucherDetail> cancelVoucher(@PathVariable("voucherCode") String voucherCode) {
        Optional<VoucherDetail> voucherDetail = service.findByVoucherCode(voucherCode);

        if (voucherDetail.isPresent()) {
            VoucherDetail detail = voucherDetail.get();
            if (validateVoucherRedeem(detail)) {
                detail.setStatus(VoucherUtils.ACTIVE);
                return new ResponseEntity<>(service.save(detail), HttpStatus.OK);
            } else {
                throw new ValdationException("The Voucher status is not REDEEMED");
            }
        } else {
            throw new RecordNotFoundException("Voucher code not found " + voucherCode);
        }
    }

}
