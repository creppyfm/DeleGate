package com.creppyfm.server.authentication;

import com.creppyfm.server.enumerated.Provider;
import com.creppyfm.server.model.User;
import com.creppyfm.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GitHubEmailFetcher gitHubEmailFetcher;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String providerString = userRequest.getClientRegistration().getRegistrationId();
        Provider provider = providerString.equalsIgnoreCase("google") ?
                Provider.GOOGLE : Provider.GITHUB;

        String providerId = provider == Provider.GOOGLE ?
                oAuth2User.getAttribute("sub").toString() : oAuth2User.getAttribute("id").toString();

        User user = userRepository.findByProviderAndProviderId(provider, providerId);

        if(user == null) {
            user = new User();
            user.setProviderId(providerId);

            processOAuth2User(userRequest, oAuth2User, user, provider, providerId);

            userRepository.save(user);
        }

        return new CustomOAuth2User(oAuth2User);
    }

    private void processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User, User user, Provider provider, String providerId) {
        String fullName = oAuth2User.getAttribute("name");

        if (provider == Provider.GOOGLE) {
            if (fullName != null) {

                String[] names = fullName.split(" ");
                user.setFirstName(names[0]);
                user.setLastName(names[1]);
            } else {
                if (oAuth2User.getAttribute("given_name") != null) {
                    user.setFirstName(oAuth2User.getAttribute("given_name"));
                }
                if (oAuth2User.getAttribute("family_name") != null) {
                    user.setLastName(oAuth2User.getAttribute("family_name"));
                }
            }
            user.setEmail(oAuth2User.getAttribute("email"));

        } else {
            if (fullName != null) {
                String[] names = fullName.split(" ");
                user.setFirstName(names[0]);
                user.setLastName(names[1]);
            } else {
                user.setFirstName(oAuth2User.getAttribute("login"));
            }
            String userEmail = gitHubEmailFetcher.fetchGitHubEmail(userRequest.getAccessToken().getTokenValue());
            user.setEmail(userEmail);
        }
        user.setProvider(provider);
        user.setProviderId(providerId);
    }


}
