package io.haedoang.step1.coupon.application.service;

import io.haedoang.step1.coupon.application.CouponDownloadCommand;

public interface CouponDownloadService {
    Long download(CouponDownloadCommand command);
}
