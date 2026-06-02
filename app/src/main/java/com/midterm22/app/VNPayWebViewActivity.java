package com.midterm22.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VNPayWebViewActivity extends AppCompatActivity {

    public static final String EXTRA_PAYMENT_URL = "payment_url";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView paymentWebView = new WebView(this);
        setContentView(paymentWebView);

        String paymentLink = getIntent().getStringExtra(EXTRA_PAYMENT_URL);

        if (paymentLink == null || paymentLink.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy liên kết thanh toán", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        paymentWebView.getSettings().setJavaScriptEnabled(true);

        paymentWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                String url = request.getUrl().toString();

                if (!url.contains("vnpay_return.jsp")) {
                    return false;
                }

                Uri resultUri = Uri.parse(url);
                String responseCode = resultUri.getQueryParameter("vnp_ResponseCode");

                Intent data = new Intent();

                boolean paymentSuccess = "00".equals(responseCode);

                if (paymentSuccess) {
                    Toast.makeText(
                            VNPayWebViewActivity.this,
                            "Thanh toán thành công!",
                            Toast.LENGTH_LONG
                    ).show();

                    data.putExtra("payment_result", "success");
                } else {
                    Toast.makeText(
                            VNPayWebViewActivity.this,
                            "Thanh toán không thành công!",
                            Toast.LENGTH_LONG
                    ).show();

                    data.putExtra("payment_result", "failed");
                }

                setResult(RESULT_OK, data);
                finish();

                return true;
            }
        });

        paymentWebView.loadUrl(paymentLink);
    }
}