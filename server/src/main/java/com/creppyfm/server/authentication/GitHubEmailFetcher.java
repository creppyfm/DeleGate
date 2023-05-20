package com.creppyfm.server.authentication;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GitHubEmailFetcher {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    public String fetchGitHubEmail(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GitHubEmail[]> response = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                createHeaders(accessToken),
                GitHubEmail[].class
        );


        GitHubEmail[] emailArray = response.getBody();
        if (emailArray != null) {
            for (GitHubEmail email : emailArray) {
                log.info("GitHub Email: " + email);
            }
        }

        for (GitHubEmail emailObject : emailArray) {
            if (emailObject.isPrimary()) {
                return emailObject.getEmail();
            }
        }
        return null;
    }

    private HttpEntity<String> createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken);
        return new HttpEntity<>(headers);
    }
}
