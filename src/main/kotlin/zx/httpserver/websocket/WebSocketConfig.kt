package zx.httpserver.websocket

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry

/**
 * Created by zhengxiao on 07/06/2017.
 */

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : AbstractWebSocketMessageBrokerConfigurer() {

    override fun registerStompEndpoints(registry: StompEndpointRegistry?) {
        registry?.addEndpoint("/greetings-websocket")?.withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry?) {
        registry?.enableSimpleBroker("/topic", "/pixel")
        registry?.setApplicationDestinationPrefixes("/app")
    }

}