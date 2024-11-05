package io.haedoang.step1.coupon.application.service;

import io.haedoang.step1.base.error.ServiceException;
import io.haedoang.step1.coupon.application.CouponDownloadCommand;
import io.haedoang.step1.coupon.domain.Coupon;
import io.haedoang.step1.coupon.domain.CouponDownload;
import io.haedoang.step1.coupon.infra.CouponDownloadRepository;
import io.haedoang.step1.coupon.infra.CouponRepository;
import io.haedoang.step1.coupon.infra.CouponValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.haedoang.step1.base.error.ErrorCode.NOT_EXIST_RECORD;

@Service
@RequiredArgsConstructor
public class CouponDownloadServiceImpl implements CouponDownloadService {

    private final CouponRepository couponRepository;
    private final CouponDownloadRepository couponDownloadRepository;
    private final CouponValidator couponValidator;

    @Transactional
    @Override
    public Long download(CouponDownloadCommand command) {

        Coupon coupon = getCoupon(command);

        CouponDownload couponDownload = CouponDownload.from(coupon).by(command.userId());

        couponValidator.validate(couponDownload);

        coupon.used();
        couponDownloadRepository.save(couponDownload);

        return couponDownload.getKey();
    }

    private Coupon getCoupon(CouponDownloadCommand command) {
        return couponRepository.findByCouponId(command.couponId())
                .orElseThrow(() -> new ServiceException(NOT_EXIST_RECORD));
    }

}
