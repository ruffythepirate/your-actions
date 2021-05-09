package backend.jobs

import io.vertx.ext.web.Router
import io.vertx.core.Future
import java.util.*

class JobController(val router: Router) {

    init {
        router.get("/jobs").respond { ctx ->
            Future.succeededFuture(listOf<Job>())
        }
        router.post("/jobs").handler { ctx ->
            val newId = UUID.randomUUID().toString()
            ctx.response().putHeader("Location", newId).end()
        }
    }
}
