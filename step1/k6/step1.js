import http from 'k6/http';
import { check, fail, sleep } from 'k6';
import { randomString } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export default function () {
    const url = 'http://localhost:8080/coupon/download';
    const payload = JSON.stringify({
        couponId: '941cdf94-6a5a-4e0d-9ae5-214044c01e0c',
        userId: `${randomString(10)}`,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(url, payload, params);

    const checkResult = check(res, {
        'status is 201': (r) => r.status === 201
    });

    if (!checkResult) {
        if (res.status >= 400 && res.status < 500) {
            fail(`Request failed with status ${res.status} (4xx error)`);
        } else if (res.status >= 500) {
            fail(`Request failed with status ${res.status} (5xx error)`);
        }
    }

    sleep(0.5);
}