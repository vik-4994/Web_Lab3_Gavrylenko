package org.example.lab3web.controller;

import jakarta.servlet.http.*;
import org.apache.hc.core5.ssl.SSLContexts;
import org.example.lab3web.config.CasdoorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;


@RestController
public class AuthController {

    @Autowired
    private CasdoorProperties casdoorProperties;

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        String authorizeUrl = casdoorProperties.getConnectEndpoint() + casdoorProperties.getLoginEndpoint()
                + "?client_id=" + casdoorProperties.getConnectClientId()
                + "&response_type=code"
                + "&redirect_uri=" + casdoorProperties.getRedirectUri()
                + "&scope=openid profile email"
                + "&org_name=built-in";

        response.sendRedirect(authorizeUrl);
    }

    @GetMapping("/callback")
    public void callback(@RequestParam String code, HttpServletResponse response) throws IOException {
        System.out.println("Got code: " + code);

        RestTemplate restTemplate = getUnsafeRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", casdoorProperties.getRedirectUri());
        params.add("client_id", casdoorProperties.getConnectClientId());
        params.add("client_secret", casdoorProperties.getConnectClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                casdoorProperties.getConnectEndpoint() + casdoorProperties.getTokenEndpoint(),
                request,
                Map.class
        );

        if (tokenResponse.getStatusCode().is2xxSuccessful()) {
            String accessToken = (String) tokenResponse.getBody().get("access_token");

            if (accessToken != null) {
                Cookie cookie = new Cookie("access_token", accessToken);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

        response.sendRedirect("/index.html");
    }


    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        boolean loggedIn = false;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    loggedIn = true;
                    break;
                }
            }
        }

        result.put("loggedIn", loggedIn);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getUserInfo(@CookieValue(value = "access_token", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No token"));
        }

        try {
            RestTemplate restTemplate = getUnsafeRestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> userInfo = restTemplate.exchange(
                    casdoorProperties.getConnectEndpoint() + "/api/get-account",
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            return ResponseEntity.ok(userInfo.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }


    private RestTemplate getUnsafeRestTemplate() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            return new RestTemplate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create unsafe RestTemplate", e);
        }
    }
}
