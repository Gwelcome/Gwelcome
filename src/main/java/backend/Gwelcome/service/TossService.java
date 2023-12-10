package backend.Gwelcome.service;

import backend.Gwelcome.dto.toss.PaymentResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@NoArgsConstructor
@Transactional(readOnly = true)
public class TossService {

    @Value("${toss.secret_key}")
    private String secret_key;

    private static final String tossPaymentSuccessUrl = "https://api.tosspayments.com/v1/payments/confirm";
    @Transactional
    public PaymentResponseDTO payment(String orderId, String paymentKey, long amount) throws JSONException, JsonProcessingException {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = new String(Base64.getEncoder().encode((secret_key + ":").getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId",orderId);
        jsonObject.put("paymentKey",paymentKey);
        jsonObject.put("amount",amount);

        HttpEntity<String> stringHttpEntity = new HttpEntity<>(jsonObject.toString(), headers);

        ResponseEntity<String> response = rt.exchange(
                tossPaymentSuccessUrl,
                HttpMethod.POST,
                stringHttpEntity,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        PaymentResponseDTO paymentResponseDTO = objectMapper.readValue(response.getBody(), PaymentResponseDTO.class);
        return paymentResponseDTO;
    }
}