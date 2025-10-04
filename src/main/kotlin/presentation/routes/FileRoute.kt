package top.dedicado.presentation.routes

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.get
import top.dedicado.domain.services.StorageService

fun Route.fileRoute() {
    route("/files") {
        get("/serve/{pathToFile...}") {
            val pathToFile = call.parameters.getAll("pathToFile")?.joinToString("/")
                ?: throw BadRequestException("Caminho do arquivo não encontrado")

            val service = call.get<StorageService>()
            val fileContent = service.getFileContent(pathToFile)

            if (fileContent != null) {
                val contentType = when {
                    pathToFile.endsWith(".png") -> ContentType.Image.PNG
                    pathToFile.endsWith(".jpg", true) || pathToFile.endsWith(".jpeg", true) -> ContentType.Image.JPEG
                    pathToFile.endsWith(".gif") -> ContentType.Image.GIF
                    pathToFile.endsWith(".pdf") -> ContentType.Application.Pdf
                    else -> ContentType.Application.OctetStream
                }

                call.respondBytes(fileContent, contentType)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Arquivo não encontrado"))
            }
        }

        get("/public") {
            val pathToFile = call.request.queryParameters["pathToFile"]
                ?: throw BadRequestException("Caminho do arquivo não encontrado")
            val service = call.get<StorageService>()

            val response = if (pathToFile.startsWith("public/")) {
                service.generatePublicUrl(pathToFile)
            } else {
                "/files/serve/$pathToFile"
            }

            call.respond(HttpStatusCode.OK, mapOf("url" to response))
        }

        authenticate("jwt") {
            get("") {
                val pathToFile = call.request.queryParameters["pathToFile"]
                    ?: throw BadRequestException("Caminho do arquivo não encontrado")
                val service = call.get<StorageService>()

                val response = try {
                    service.generateSignedUrl(pathToFile)
                } catch (e: Exception) {
                    "/files/serve/$pathToFile"
                }

                call.respond(HttpStatusCode.OK, mapOf("url" to response))
            }

            post("") {
                val multipartData = call.receiveMultipart()
                val service = call.get<StorageService>()
                val response = service.upload(multipartData)

                call.respond(HttpStatusCode.OK, mapOf("url" to response))
            }

            delete("") {
                val pathToFile = call.request.queryParameters["pathToFile"]
                    ?: throw BadRequestException("Caminho do arquivo não encontrado")
                val service = call.get<StorageService>()
                service.delete(pathToFile)

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}