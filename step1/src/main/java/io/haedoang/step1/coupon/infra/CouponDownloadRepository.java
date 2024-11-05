package io.haedoang.step1.coupon.infra;

import io.haedoang.step1.coupon.domain.Coupon;
import io.haedoang.step1.coupon.domain.CouponDownload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponDownloadRepository extends JpaRepository<CouponDownload, Long> {

    boolean existsCouponDownloadByCouponAndUserId(Coupon coupon, String userId);
}
