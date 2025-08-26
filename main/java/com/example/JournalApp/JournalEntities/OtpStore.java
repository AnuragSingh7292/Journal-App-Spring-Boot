package com.example.JournalApp.JournalEntities;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpStore {

    // Store OTPs with userIdentifier as key
    private Map<String, OtpEntry> otpMap = new ConcurrentHashMap<>();

    public void saveOtp(String userIdentifier, String otp, long durationMillis) {
        long expiryTime = System.currentTimeMillis() + durationMillis; // add current time here
        otpMap.put(userIdentifier, new OtpEntry(userIdentifier,otp, expiryTime));
    }


    public OtpEntry getOtpEntry(String userIdentifier) {
        return otpMap.get(userIdentifier);
    }

    public void removeOtp(String userIdentifier) {
        otpMap.remove(userIdentifier);
    }

    // Inner class to store OTP and expiry time
    public static class OtpEntry {
        private String userIdentification;
        private String otp;
        private long expiryTime;

        public OtpEntry(String userIdentification , String otp, long expiryTime) {
            this.userIdentification = userIdentification;
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
        public String getUserIdentification() { return userIdentification; }
        public String getOtp() { return otp; }
        public long getExpiryTime() { return expiryTime; }
    }
}

