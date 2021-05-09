package backend.jobs

import backend.Server
import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.ServerSocket


@ExtendWith(VertxExtension::class)
class JobControllerIT {

    lateinit var vertx: Vertx
    var port: Int = 0

    @BeforeEach
    fun setup (context: VertxTestContext) {
        vertx = Vertx.vertx()
        val socket = ServerSocket(0)
        port = socket.localPort
        socket.close()
        val options = DeploymentOptions()
            .setConfig(
                JsonObject().put("http.port", port)
            )
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        vertx.deployVerticle(Server(), options, context.succeedingThenComplete())
    }

    @AfterEach
    fun teardown(context: VertxTestContext) {
        vertx.close(context.succeedingThenComplete())
    }

    @Test
    fun shouldGetJob_whenCreatedOne() {
        val job = Job(id = null, status = JobStatus.Pending)

        val mapper = com.fasterxml.jackson.databind.ObjectMapper()
        val jobAsJson = mapper.writeValueAsString(job)

        RestAssured.given()
            .body(jobAsJson)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/jobs")
            .then()
            .statusCode(200)
            .header("Location", not(Matchers.isEmptyOrNullString()))
    }
}