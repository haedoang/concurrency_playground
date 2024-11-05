package io.haedoang.step1.coupon.infra;

import io.haedoang.step1.base.error.ErrorCode;
import io.haedoang.step1.base.error.ServiceException;
import io.haedoang.step1.coupon.domain.CouponDownload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponValidator {

    private final CouponDownloadRepository couponDownloadRepository;

    public void validate(CouponDownload couponDownload) {
        if (isDownloaded(couponDownload)) {
            throw new ServiceException(ErrorCode.ALREADY_DOWNLOADED);
        }
    }

    private boolean isDownloaded(CouponDownload couponDownload) {
        return couponDownloadRepository.existsCouponDownloadByCouponAndUserId(couponDownload.getCoupon(), couponDownload.getUserId());
    }
}
