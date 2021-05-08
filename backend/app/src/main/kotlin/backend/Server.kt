package backend

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import backend.jobs.JobController

public class Server: AbstractVerticle() {
    override fun start() {
        println("Starting Web server...")
        val router = Router.router(vertx)
        JobController(router);
        vertx.createHttpServer()
        .requestHandler(router)
        .listen(8080)
    }
}
