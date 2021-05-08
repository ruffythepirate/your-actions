package backend

import io.vertx.core.AbstractVerticle

public class Server: AbstractVerticle() {
    override fun start() {
        vertx.createHttpServer().requestHandler { req ->
            req.response()
            .putHeader("content-type", "text/plain")
            .end("Hello world")
        }.listen(8080)
    }
}
