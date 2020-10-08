package com.thesis.studyapp.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        // You can customize your authorization mapping here.
//        //messages.anyMessage().authenticated();
//        messages.anyMessage().permitAll();
//        messages.simpSubscribeDestMatchers("/subscribes").permitAll();
//    }
//
//    @Override
//    protected boolean sameOriginDisabled() {
//        return true;
//    }
}
