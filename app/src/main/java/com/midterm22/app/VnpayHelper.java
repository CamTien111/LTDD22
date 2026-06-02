package com.midterm22.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class VnpayHelper {

    public static String createPaymentUrl(String orderId,
                                          String amountInput,
                                          String selectedBank,
                                          String language) {

        final String VERSION = "2.1.0";
        final String COMMAND = "pay";
        final String ORDER_TYPE = "other";

        long paymentAmount = Long.parseLong(amountInput) * 100L;

        Map<String, String> params = new HashMap<>();

        params.put("vnp_Version", VERSION);
        params.put("vnp_Command", COMMAND);
        params.put("vnp_TmnCode", Config.vnp_TmnCode);
        params.put("vnp_Amount", String.valueOf(paymentAmount));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", orderId);
        params.put("vnp_OrderInfo", "Thanh toan don hang:" + orderId);
        params.put("vnp_OrderType", ORDER_TYPE);
        params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        params.put("vnp_IpAddr", "127.0.0.1");

        if (selectedBank != null && !selectedBank.trim().isEmpty()) {
            params.put("vnp_BankCode", selectedBank);
        }

        params.put(
                "vnp_Locale",
                (language == null || language.isEmpty()) ? "vn" : language
        );

        // Thời gian theo múi giờ Việt Nam
        TimeZone vnTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        dateFormat.setTimeZone(vnTimeZone);

        Calendar createCalendar = Calendar.getInstance(vnTimeZone);
        String createDate = dateFormat.format(createCalendar.getTime());

        Calendar expireCalendar = (Calendar) createCalendar.clone();
        expireCalendar.add(Calendar.MINUTE, 15);
        String expireDate = dateFormat.format(expireCalendar.getTime());

        params.put("vnp_CreateDate", createDate);
        params.put("vnp_ExpireDate", expireDate);

        System.out.println("Create Date: " + createDate);
        System.out.println("Expire Date: " + expireDate);

        List<String> sortedKeys = new ArrayList<>(params.keySet());
        Collections.sort(sortedKeys);

        StringBuilder hashBuilder = new StringBuilder();
        StringBuilder queryBuilder = new StringBuilder();

        try {
            for (int index = 0; index < sortedKeys.size(); index++) {

                String key = sortedKeys.get(index);
                String value = params.get(key);

                if (value == null || value.isEmpty()) {
                    continue;
                }

                hashBuilder
                        .append(key)
                        .append("=")
                        .append(URLEncoder.encode(value, "US-ASCII"));

                queryBuilder
                        .append(URLEncoder.encode(key, "US-ASCII"))
                        .append("=")
                        .append(URLEncoder.encode(value, "US-ASCII"));

                if (index < sortedKeys.size() - 1) {
                    hashBuilder.append("&");
                    queryBuilder.append("&");
                }
            }

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        String secureHash =
                Config.hmacSHA512(Config.secretKey, hashBuilder.toString());

        queryBuilder.append("&vnp_SecureHash=").append(secureHash);

        return Config.vnp_PayUrl + "?" + queryBuilder.toString();
    }
}