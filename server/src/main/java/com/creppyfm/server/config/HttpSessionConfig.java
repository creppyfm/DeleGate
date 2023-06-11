package com.creppyfm.server.config;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.session.data.mongo.JdkMongoSessionConverter;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.time.Duration;


@Configuration(proxyBeanMethods = false)
@EnableMongoHttpSession(maxInactiveIntervalInSeconds = 2592000) //30 days converted to seconds
public class HttpSessionConfig {

    //Dotenv dotenv = Dotenv.load();

    @Autowired
    private MongoClient mongoClient;
    @Bean
    public JdkMongoSessionConverter jdkMongoSessionConverter() {
        return new JdkMongoSessionConverter(Duration.ofDays(30));
    }

    @Bean
    public DefaultCookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("None");
        serializer.setUseSecureCookie(true);
        return serializer;
    }

    @Bean
    public MongoOperations mongoTemplate() {
        return new MongoTemplate(mongoClient, System.getenv("MONGO_DATABASE"));
    }

}
