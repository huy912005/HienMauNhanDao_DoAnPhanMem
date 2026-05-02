package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.service.OtpService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpServiceImpl implements OtpService {
    private static final int EXPIRE_MINS = 5;
    private final Map<String, OtpData> otpCache = new ConcurrentHashMap<>();

    private static class OtpData {
        String otp;
        long expiryTime;

        OtpData(String otp, long expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
    }

    @Override
    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        long expiryTime = System.currentTimeMillis() + (EXPIRE_MINS * 60 * 1000);
        otpCache.put(email, new OtpData(otp, expiryTime));
        return otp;
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        OtpData data = otpCache.get(email);
        if (data == null) {
            return false;
        }
        if (System.currentTimeMillis() > data.expiryTime) {
            otpCache.remove(email);
            return false;
        }
        if (data.otp.equals(otp)) {
            otpCache.remove(email);
            return true;
        }
        return false;
    }
}
