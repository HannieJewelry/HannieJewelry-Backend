package hanniejewelry.vn.notification.sms;

import hanniejewelry.vn.shared.utils.ExternalApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final ExternalApiClient externalApiClient;

    @Value("${speedsms.access-token:}")
    private String speedSmsAccessToken;

    @Value("${textbee.device-id:}")
    private String textBeeDeviceId;

    @Value("${textbee.api-key:}")
    private String textBeeApiKey;

    public boolean sendOtpViaTextBee(String phone, String otp) {
        String url = "https://api.textbee.dev/api/v1/gateway/devices/" + textBeeDeviceId + "/send-sms";
        String realPhone = phone.startsWith("+") ? phone : "+84" + phone.replaceFirst("^0", "");
        String payload = String.format("""
        {
          "recipients": ["%s"],
          "message": "Your OTP verification code is: %s"
        }
    """, realPhone, otp);

        var headers = Map.of("x-api-key", textBeeApiKey);

        try {
            var typeRef = new ParameterizedTypeReference<Map<String, Object>>() {};
            Map<String, Object> resp = externalApiClient.post(url, headers, payload, typeRef);

            Map<String, Object> data = (Map<String, Object>) resp.get("data");
            if (data != null && Boolean.TRUE.equals(data.get("success"))) {
                return true;
            }
            System.err.println("TextBee OTP sending error: " + resp);
        } catch (Exception e) {
            System.err.println("TextBee API error: " + e.getMessage());
        }
        return false;
    }

    public boolean sendOtp(String phone, String otp) {
        String url = "https://api.speedsms.vn/index.php/sms/send";
        String payload = String.format("""
            {
              "to": ["%s"],
              "content": "Your OTP verification code is: %s",
              "sms_type": 5,
              "sender": "691eb0a4fd9e0c96"
            }
        """, phone, otp);

        String basicAuth = "Basic " + java.util.Base64.getEncoder().encodeToString((speedSmsAccessToken + ":x").getBytes());
        var headers = Map.of("Authorization", basicAuth);

        try {
            var typeRef = new ParameterizedTypeReference<Map<String, Object>>() {};
            Map<String, Object> resp = externalApiClient.post(url, headers, payload, typeRef);
            Object status = resp.get("status");
            if (!"error".equals(status)) return true;
            System.err.println("SpeedSMS OTP sending error: " + resp);
        } catch (Exception e) {
            System.err.println("SpeedSMS API error: " + e.getMessage());
        }
        return false;
    }
}