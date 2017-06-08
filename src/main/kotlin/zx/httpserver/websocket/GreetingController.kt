package zx.httpserver.websocket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import zx.httpserver.storage.StorageService
import zx.httpserver.websocket.data.Greeting
import zx.httpserver.websocket.data.HelloMessage
import zx.httpserver.websocket.data.Pixel

/**
 * Created by zhengxiao on 07/06/2017.
 */


interface PixelService {
    fun handle(pixel: Pixel)
    fun list(): Array<Pixel>
}

@Service
class PixelServiceInMemImpl : PixelService {

    final val colSize = 32
    val pixels = Array(colSize * colSize, {
        Pixel(color = "#ffffff", uname = null, x = it % colSize, y = it / colSize)
    })

    override fun handle(pixel: Pixel) {
        pixels[pixel.x + pixel.y * colSize] = pixel
    }

    override fun list(): Array<Pixel> {
        return pixels
    }

}

@Controller
class GreetingController {

    @Autowired
    lateinit var storageService: StorageService

    @Autowired
    lateinit var pixelService: PixelService

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    fun greeting(message: HelloMessage): Greeting {
        Thread.sleep(1000)
        val greeting = Greeting("Hi, ${message.name} !")
        storageService.append(greeting)
        return greeting
    }

    @GetMapping("/greetings")
    @ResponseBody
    fun listGreetings(): List<Greeting> {
        return storageService.list()
    }

    @MessageMapping("/pixel")
    @SendTo("/topic/pixels")
    fun pixelHandle(pixel: Pixel): Pixel {
        pixelService.handle(pixel)
        return pixel
    }

    @GetMapping("/pixels")
    @ResponseBody
    fun listPixel(): Array<Pixel>{
        return pixelService.list()
    }
}