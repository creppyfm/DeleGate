package com.creppyfm.server.authentication;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@CrossOrigin
@RestController
@AllArgsConstructor
@Tag(name = "OAuth2 Controller")
public class OAuth2Controller {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    private CsrfTokenRepository csrfTokenRepository;

    /*
    * Modify to point to deployed server url
    * */
    private final static String authorizationRequestBaseUri = "https://delegate.herokuapp.com/login/oauth2/code";

    private ClientRegistration getRegistration(String provider) {
        return this.clientRegistrationRepository.findByRegistrationId(provider);
    }

    @GetMapping("/login/oauth2/code/{provider}")
    public void login(@PathVariable String provider, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ClientRegistration clientRegistration = getRegistration(provider);

        if (clientRegistration != null) {

            String redirectUri = authorizationRequestBaseUri + "/" + provider;

            OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode()
                    .clientId(clientRegistration.getClientId())
                    .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                    .redirectUri(redirectUri)
                    .scope(String.valueOf(clientRegistration.getScopes()))
                    .state(csrfTokenRepository.loadToken(request).getToken());

            String url = builder.build().getAuthorizationRequestUri();
            response.sendRedirect(url);
        } else {
            response.sendRedirect("/");
        }
    }

    @GetMapping("/users/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok().build();
    }
}
