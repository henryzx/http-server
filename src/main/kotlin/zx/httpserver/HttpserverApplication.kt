package zx.httpserver

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels


@Controller
class FileUploadController {

    val logger = LoggerFactory.getLogger(FileUploadController::class.java)

    @GetMapping("/")
    fun index(model: Model): String {
        //model.addAttribute("message", "hello from spring")
//        return "uploadForm"
        return "file-post-demo"
    }

    @PostMapping("/")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile,
                         redirectAttributes: RedirectAttributes): String {

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.originalFilename + "!")
        try {
            val buffer = ByteBuffer.allocateDirect(4 * 1024 * 1024)
            val inChannel = Channels.newChannel(file.inputStream)
            var readCountTotal: Long = 0
            while (true) {
                val readCount = inChannel.read(buffer)
                if (readCount <= 0) break
                readCountTotal += readCount
                logger.info("read $readCount")
                buffer.clear()
            }
            logger.info("read done total $readCountTotal")
            inChannel.close()
        } catch(e: Exception) {
            logger.info("error while read file", e)
        }
        logger.info("change!")
        return "redirect:/"
    }

    @PostMapping("/uploader")
    fun handleFile(body: RequestEntity<InputStream>,
                   redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + "a file" + "!")

        return "redirect:/"
    }

}

@SpringBootApplication
class HttpserverApplication

fun main(args: Array<String>) {
    SpringApplication.run(HttpserverApplication::class.java, *args)
}
