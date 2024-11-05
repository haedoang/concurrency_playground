package io.haedoang.step1.coupon.ui;

import io.haedoang.step1.coupon.application.CouponDownloadCommand;
import io.haedoang.step1.coupon.application.service.CouponDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponDownloadService couponDownloadService;

    @PostMapping("/download")
    public ResponseEntity<Void> download(@RequestBody CouponDownloadCommand command) {

        Long response = couponDownloadService.download(command);
        return ResponseEntity.created(URI.create("/coupon/download/" + response)).build();
    }
}
