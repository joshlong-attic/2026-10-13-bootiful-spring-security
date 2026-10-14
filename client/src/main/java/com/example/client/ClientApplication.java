package com.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.annotation.ClientRegistrationId;
import org.springframework.security.oauth2.client.web.client.support.OAuth2RestClientHttpServiceGroupConfigurer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.registry.ImportHttpServices;

@ImportHttpServices(MessageClient.class)
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean
    OAuth2RestClientHttpServiceGroupConfigurer oAuth2RestClientHttpServiceGroupConfigurer(
            OAuth2AuthorizedClientManager auth2AuthorizedClientManager) {
        return OAuth2RestClientHttpServiceGroupConfigurer.from(auth2AuthorizedClientManager);
    }
}

@Controller
@ResponseBody
class ClientController {


    private final MessageClient messageClient;

    ClientController(MessageClient message) {
        this.messageClient = message;
    }

    @GetMapping("/client")
    Message message(
//            @RegisteredOAuth2AuthorizedClient("spring") OAuth2AuthorizedClient auth2AuthorizedClient
    ) {
        return messageClient.message(
//                auth2AuthorizedClient.getAccessToken().getTokenValue()
        );
    }
}

record Message(String message) {
}

@ClientRegistrationId("spring")
interface MessageClient {

    @GetExchange("http://localhost:8081/message")
    Message message();
}

/*
@Component
class MessageClient {

    private final RestClient http;

    MessageClient(RestClient.Builder http, OAuth2AuthorizedClientManager auth2AuthorizedClientManager) {
        var oAuth2ClientHttpRequestInterceptor = new OAuth2ClientHttpRequestInterceptor(
                auth2AuthorizedClientManager);
        oAuth2ClientHttpRequestInterceptor
                .setClientRegistrationIdResolver(_ -> "spring");
        this.http = http
                .requestInterceptor(oAuth2ClientHttpRequestInterceptor)
                .build();
    }

    Message message() {
        return this.http
                .get()
                .uri("http://localhost:8081/message")
//                .headers(h -> h.setBearerAuth(at))
                .retrieve()
                .body(Message.class);
    }
}*/