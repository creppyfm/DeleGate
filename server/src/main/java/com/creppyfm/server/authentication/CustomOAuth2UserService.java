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

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) super.loadUser(userRequest);

        String providerString = userRequest.getClientRegistration().getRegistrationId();
        Provider provider = providerString.equalsIgnoreCase("google") ?
                Provider.GOOGLE :
                Provider.GITHUB;

        String providerId = oAuth2User.getAttribute("id");

        User user = userRepository.findByProviderAndProviderId(provider, providerId);

        if(user == null) {
            user = new User();
            user.setProviderId(providerId);
            String[] names = oAuth2User.getName().split(" ");
            user.setFirstName(names[0]);
            user.setLastName(names[1]);
            user.setEmail(oAuth2User.getEmail());
            if(provider.equals(Provider.GOOGLE)) {
                user.setProvider(Provider.GOOGLE);
            } else {
                user.setProvider(Provider.GITHUB);
            }

            userRepository.save(user);
        }

        return new CustomOAuth2User(oAuth2User);
    }
}
