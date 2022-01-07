package com.practice.auth;

import com.practice.persistence.UserEntity;
import com.practice.persistence.UserRepository;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

@Singleton
public class JDBCAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCAuthenticationProvider.class);
    private final UserRepository users;

    public JDBCAuthenticationProvider(UserRepository users) {
        this.users = users;
    }


    @Override
    public Publisher<AuthenticationResponse> authenticate(
            @Nullable final HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Mono.<AuthenticationResponse>create(emitter -> {
            final String identity = (String) authenticationRequest.getIdentity();
            LOG.info("User {} tried to login", identity);

           final Optional<UserEntity> maybeUser = users.findByEmail(identity);
            if(maybeUser.isPresent()) {
                LOG.info("Found user: {}", maybeUser.get().getEmail());
                String secret = (String) authenticationRequest.getSecret();
                if (maybeUser.get().getPassword().equals(secret)) {
                    //pass
                    LOG.info("User Logged in");
                    final HashMap<String, Object> attributes = new HashMap<>();
                    attributes.put("hair_color", "brown");
                    attributes.put("language", "en");
                    emitter.success(AuthenticationResponse
                            .success(identity, Collections.singletonList("ROLE_USER"), attributes));
                } else {
                LOG.info("Wrong password for user {}", maybeUser.get().getPassword());
                }
            } else {
                LOG.info("NO user found with email {} and password {}", authenticationRequest.getIdentity()
                        , authenticationRequest.getSecret());
            }

            emitter.error(new AuthenticationException(new AuthenticationFailed("Wrong username or password")));
        });
    }
}