package com.psi.voucherservice;

import com.psi.voucherservice.domain.VoucherDetail;
import com.psi.voucherservice.repository.VoucherDetailRepository;
import com.psi.voucherservice.util.VoucherUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class VoucherserviceApplication {


    @Autowired
    private VoucherDetailRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(VoucherserviceApplication.class, args);
    }


    // spring calls after the initialization of bean properties
    @PostConstruct
    private void initDb() {
        VoucherDetail detail = new VoucherDetail();
        detail.setVoucherCode("JAVA-CERT-2021");
        detail.setDescription("JAVA-CERT-2021 certification code");
        detail.setStatus(VoucherUtils.ACTIVE);
        repository.save(detail);

        VoucherDetail detail2 = new VoucherDetail();
        detail2.setVoucherCode("AWS-CERT-2021");
        detail2.setDescription("AWS-CERT-2021 certification code");
        detail2.setStatus(VoucherUtils.ACTIVE);
        repository.save(detail2);

        VoucherDetail detail3 = new VoucherDetail();
        detail3.setVoucherCode("KOTLIN-CERT-2021");
        detail3.setDescription("KOTLIN-CERT-2021 certification code");
        detail3.setStatus(VoucherUtils.ACTIVE);
        repository.save(detail3);
    }

}
