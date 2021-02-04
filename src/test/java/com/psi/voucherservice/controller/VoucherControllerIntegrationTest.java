package com.psi.voucherservice.controller;

import com.psi.voucherservice.VoucherserviceApplication;
import com.psi.voucherservice.domain.VoucherDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = VoucherserviceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VoucherControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;


    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void contextLoads() {

    }

    @Test
    public void testGetAllVouchers() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "api/vouchers",
                HttpMethod.GET, entity, String.class);
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetVoucherById() {
        VoucherDetail detail = restTemplate.getForObject(getRootUrl() + "api/vouchers/1", VoucherDetail.class);
        assertNotNull(detail);
    }

    @Test
    public void testGetVoucherByCode() {
        String voucherCode = "JAVA-CERT-2022";
        VoucherDetail detail = restTemplate.getForObject(getRootUrl() + "api/vouchers/" + voucherCode, VoucherDetail.class);
        assertNotNull(detail);
    }

    @Test
    public void testCreateVoucher() {
        VoucherDetail detail = new VoucherDetail();
        detail.setVoucherCode("JAVA-CERT-2023");
        detail.setDescription("Java Certification 2023");
        detail.setStatus("ACTIVE");
        ResponseEntity<VoucherDetail> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/vouchers/", detail, VoucherDetail.class);
        assertNotNull(postResponse);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());
    }

    @Test
    public void testRedeemVoucherSuccess() {
        String voucherCode = "JAVA-CERT-2021";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/api/vouchers/redeem/" + voucherCode,
                HttpMethod.PUT, entity, String.class);
        String body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(body.contains("REDEEMED"));
    }

    @Test
    public void testRedeemVoucherFailure() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        VoucherDetail detail = new VoucherDetail();
        String voucherCode = "JAVA-CERT-2025";
        detail.setVoucherCode(voucherCode);
        detail.setDescription("Java Certification 2025");
        detail.setStatus("ACTIVE");
        ResponseEntity<VoucherDetail> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/vouchers/", detail, VoucherDetail.class);
        assertNotNull(postResponse);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());

        ResponseEntity<String> redeemResponse = restTemplate.exchange(getRootUrl() + "/api/vouchers/redeem/" + voucherCode,
                HttpMethod.PUT, entity, String.class);

        String redeemBody = redeemResponse.getBody();
        assertNotNull(redeemResponse);
        assertEquals(HttpStatus.OK, redeemResponse.getStatusCode());
        assertNotNull(redeemBody);
        assertTrue(redeemBody.contains("REDEEMED"));

        //Try to redeem the voucher which has already Redeemed, should give error message
        ResponseEntity<String> errorRedeemResponse = restTemplate.exchange(getRootUrl() + "/api/vouchers/redeem/" + voucherCode,
                HttpMethod.PUT, entity, String.class);

        String errorRedeemBody = errorRedeemResponse.getBody();
        assertNotNull(errorRedeemBody);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorRedeemResponse.getStatusCode());
        assertNotNull(errorRedeemBody);
        assertTrue(errorRedeemBody.contains("The voucher status is not ACTIVE."));

    }

    @Test
    public void testCancelVoucherSuccess() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        VoucherDetail detail = new VoucherDetail();
        String voucherCode = "JAVA-CERT-2026";
        detail.setVoucherCode(voucherCode);
        detail.setDescription("Java Certification 2026");
        detail.setStatus("ACTIVE");
        ResponseEntity<VoucherDetail> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/vouchers/", detail, VoucherDetail.class);
        assertNotNull(postResponse);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());

        ResponseEntity<String> redeemResponse = restTemplate.exchange(getRootUrl() + "/api/vouchers/redeem/" + voucherCode,
                HttpMethod.PUT, entity, String.class);

        String redeemBody = redeemResponse.getBody();
        assertNotNull(redeemResponse);
        assertEquals(HttpStatus.OK, redeemResponse.getStatusCode());
        assertNotNull(redeemBody);
        assertTrue(redeemBody.contains("REDEEMED"));

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/api/vouchers/cancel/" + voucherCode,
                HttpMethod.PUT, entity, String.class);
        String cancelBody = response.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(cancelBody);
        assertTrue(cancelBody.contains("ACTIVE"));
    }


    @Test
    public void testCancelVoucherFailure() {

        VoucherDetail detail = new VoucherDetail();
        String voucherCode = "JAVA-CERT-2027";
        detail.setVoucherCode(voucherCode);
        detail.setDescription("Java Certification 2027");
        detail.setStatus("ACTIVE");
        ResponseEntity<VoucherDetail> postResponse = restTemplate.postForEntity(getRootUrl() + "/api/vouchers/", detail, VoucherDetail.class);
        assertNotNull(postResponse);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());


        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/api/vouchers/cancel/" + voucherCode,
                HttpMethod.PUT, entity, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        String body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("The Voucher status is not REDEEMED"));
    }


}