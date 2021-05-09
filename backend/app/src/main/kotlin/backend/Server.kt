package backend

import backend.jobs.JobController
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.ext.web.Router

class Server: AbstractVerticle() {
    override fun start(startPromise: Promise<Void>) {
        println("Starting Web server...")
        val router = Router.router(vertx)
        JobController(router);
        vertx.createHttpServer()
        .requestHandler(router)
        .listen ( config().getInteger("http.port", 8080)) {
            if (it.succeeded()) {
                startPromise.complete()
            } else {
                startPromise.fail(it.cause())
            }
        }
    }
}
