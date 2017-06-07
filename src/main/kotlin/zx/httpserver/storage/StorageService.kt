package zx.httpserver.storage

import org.springframework.stereotype.Service
import zx.httpserver.websocket.Greeting

/**
 * Created by zhengxiao on 07/06/2017.
 */
interface StorageService {
    fun append(message: Greeting)

    fun list(page: Int = 0, limit: Int = 100): List<Greeting>

    fun count(): Long
}

@Service
class StorageServiceImp : StorageService {

    val list = ArrayList<Greeting>()

    override fun append(message: Greeting) {
        list.add(message)
    }

    override fun list(page: Int, limit: Int): List<Greeting> {
        return list.subList(Math.max(list.size - 1 - limit, 0), Math.max(list.size - 1, 0)).reversed()
    }

    override fun count(): Long {
        return list.size.toLong()
    }


}