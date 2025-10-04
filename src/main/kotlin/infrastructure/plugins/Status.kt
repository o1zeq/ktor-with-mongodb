package top.dedicado.infrastructure.plugins

import com.google.firebase.auth.AuthErrorCode
import com.google.firebase.auth.FirebaseAuthException
import com.mongodb.MongoWriteException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlin.RuntimeException

class AlreadyExistsException(message: String) : RuntimeException(message)
class CustomIllegalArgumentException(message: String): RuntimeException(message)
class UserAlreadyExistsException(message: String) : RuntimeException(message)
class NotFoundException(message: String) : RuntimeException(message)
class UnauthorizedException(message: String) : RuntimeException(message)
class ForbiddenException(message: String) : RuntimeException(message)
class GoneException(message: String): RuntimeException(message)
class CustomBadRequestException(message: String): RuntimeException(message)

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureStatus() {
    install(StatusPages) {

        exception<RequestValidationException> { call, cause ->
            val errorMessages = cause.reasons.joinToString()
            call.respondError(HttpStatusCode.BadRequest, errorMessages)
        }

        exception<MissingFieldException> { call, cause ->
            val missingFields = cause.missingFields.joinToString(", ")
            call.respondError(HttpStatusCode.BadRequest, "O corpo da requisição está incompleto. Campos obrigatórios ausentes: $missingFields")
        }

        exception<BadRequestException> { call, cause ->
            call.respondError(HttpStatusCode.BadRequest, cause.cause?.message ?: "Formato de requisição inválido.")
        }

        exception<NotFoundException> { call, cause ->
            call.respondError(HttpStatusCode.NotFound, cause.message ?: "Recurso não encontrado")
        }

        exception<UserAlreadyExistsException> { call, cause ->
            call.respondError(HttpStatusCode.Conflict, cause.message ?: "O recurso já existe")
        }

        exception<CustomIllegalArgumentException> { call, cause ->
            call.respondError(HttpStatusCode.BadRequest, cause.message ?: "Argumento inválido na requisição")
        }

        exception<UnauthorizedException> { call, cause ->
            call.respondError(HttpStatusCode.Unauthorized, cause.message ?: "Requisição não autorizada")
        }

        exception<ForbiddenException> { call, cause ->
            call.respondError(HttpStatusCode.Forbidden, cause.message ?: "Acesso negado")
        }

        exception<GoneException> { call, cause ->
            call.respondError(HttpStatusCode.Gone, cause.message ?: "Este recurso não está mais disponível")
        }

        exception<FirebaseAuthException> { call, cause ->
            val (statusCode, message) = when (cause.authErrorCode) {
                AuthErrorCode.USER_NOT_FOUND, AuthErrorCode.EMAIL_NOT_FOUND ->
                    HttpStatusCode.NotFound to "O usuário especificado não foi encontrado."
                AuthErrorCode.EMAIL_ALREADY_EXISTS, AuthErrorCode.PHONE_NUMBER_ALREADY_EXISTS ->
                    HttpStatusCode.Conflict to "Não foi possível registrar-se com o e-mail ou número de celular informado."
                AuthErrorCode.INVALID_ID_TOKEN, AuthErrorCode.EXPIRED_ID_TOKEN ->
                    HttpStatusCode.Unauthorized to "Token de autenticação inválido ou expirado."
                AuthErrorCode.USER_DISABLED ->
                    HttpStatusCode.Unauthorized to "O usuário está desativado ou bloqueado"
                else -> {
                    call.application.log.error("Erro inesperado do Firebase Auth não mapeado: ${cause.errorCode.name}", cause)
                    HttpStatusCode.InternalServerError to "Ocorreu um erro com o serviço de autenticação."
                }
            }
            call.respondError(statusCode, message)
        }

        exception<MongoWriteException> { call, cause ->
            if (cause.error.code == 11000) {
                call.respondError(HttpStatusCode.Conflict, "Já existe um registro com os dados fornecidos.")
            } else {
                call.application.log.error("Erro de escrita no MongoDB não tratado", cause)
                call.respondError(HttpStatusCode.InternalServerError, "Ocorreu um erro ao salvar os dados no servidor.")
            }
        }

        exception<Throwable> { call, cause ->
            call.application.log.error("Erro não tratado capturado pelo StatusPages", cause)
            call.respondError(HttpStatusCode.InternalServerError, "Ocorreu um erro interno inesperado.")
        }
    }
}

private suspend fun ApplicationCall.respondError(statusCode: HttpStatusCode, message: String) {
    respond(statusCode, mapOf("message" to message))
}