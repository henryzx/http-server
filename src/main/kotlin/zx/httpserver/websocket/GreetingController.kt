package zx.httpserver.websocket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import zx.httpserver.storage.StorageService

/**
 * Created by zhengxiao on 07/06/2017.
 */

@Controller
class GreetingController {

    @Autowired
    var storageService: StorageService? = null

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    fun greeting(message: HelloMessage): Greeting {
        Thread.sleep(1000)
        val greeting = Greeting("Hello, ${message.name} !")
        storageService?.append(greeting)
        return greeting
    }
}

@RestController
class GreetingRestController {

    @Autowired
    var storageService: StorageService? = null

    @GetMapping("/topic/list")
    fun listGreetings(): List<Greeting> {
        return storageService?.list() ?: emptyList()
    }
}