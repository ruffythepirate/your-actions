package backend.jobs

import io.vertx.ext.web.Router
import io.vertx.core.Future

class JobController(val router: Router) {

    init {
        router.get("/jobs").respond { ctx ->
            Future.succeededFuture(listOf<Job>())
        }
    }
}
