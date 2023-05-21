package com.creppyfm.server.authentication;

import com.creppyfm.server.model.User;
import com.creppyfm.server.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@AllArgsConstructor
public class OAuth2Controller {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    private CsrfTokenRepository csrfTokenRepository;

    private final static String authorizationRequestBaseUri = "http://localhost:8080/auth/";

    private ClientRegistration getRegistration(String provider) {
        return this.clientRegistrationRepository.findByRegistrationId(provider);
    }

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    public ResponseEntity<User> getUser(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/auth/{provider}")
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
            response.sendRedirect("/login");
        }
    }

    // This is in place to redirect the user back to the frontend after successful
    // login.
    // This should be removed from production build.

    @GetMapping("/")
    public RedirectView redirectToExternalUrl() {
        String externalUrl = "http://localhost:5173";
        return new RedirectView(externalUrl);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok().build();
    }
}