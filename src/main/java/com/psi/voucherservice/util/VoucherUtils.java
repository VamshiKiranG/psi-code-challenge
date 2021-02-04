package com.psi.voucherservice.util;

import com.psi.voucherservice.domain.VoucherDetail;

public class VoucherUtils {
    public static final String ACTIVE = "ACTIVE";
    public static final String REDEEMED = "REDEEMED";

    public static boolean validateVoucherRedeem(VoucherDetail voucherDetail) {
        return voucherDetail.getStatus().equals(REDEEMED);
    }

    public static boolean validateVoucherActive(VoucherDetail voucherDetail) {
        return voucherDetail.getStatus().equals(ACTIVE);
    }
}
