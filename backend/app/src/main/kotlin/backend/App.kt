package backend

import io.vertx.core.Vertx
import io.vertx.ext.web.Router

fun main() {
    
    val vertx = Vertx.vertx()
    vertx.deployVerticle(Server())
}
