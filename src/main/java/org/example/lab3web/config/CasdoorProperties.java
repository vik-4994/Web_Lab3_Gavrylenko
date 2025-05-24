package org.example.lab3web.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "casdoor")
public class CasdoorProperties {
    private String connectEndpoint;
    private String connectClientId;
    private String connectClientSecret;
    private String redirectUri;
    private String loginEndpoint;
    private String tokenEndpoint;
}
