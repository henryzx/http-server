package zx.httpserver.websocket

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

/**
 * Created by zhengxiao on 07/06/2017.
 */

@Controller
class GreetingController{

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    fun greeting(message: HelloMessage): Greeting{
        Thread.sleep(1000)
        return Greeting("Hello, ${message.name} !")
    }
}
