package top.dedicado.domain.services

import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Bucket
import com.google.cloud.storage.Storage
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.utils.io.toByteArray
import org.koin.core.component.KoinComponent
import top.dedicado.infrastructure.plugins.CustomBadRequestException
import top.dedicado.infrastructure.plugins.CustomIllegalArgumentException
import java.util.concurrent.TimeUnit

class StorageService(
    private val bucket: Bucket
): KoinComponent {

    suspend fun generatePublicUrl(pathToFile: String): String {
        return "https://storage.googleapis.com/${bucket.name}/$pathToFile"
    }

    suspend fun generateSignedUrl(pathToFile: String, expirationInMinutes: Long? = null): String {
        return try {
            val blobInfo = BlobInfo.newBuilder(bucket.name, pathToFile).build()
            val url = bucket.storage.signUrl(
                blobInfo,
                expirationInMinutes ?: 720,
                TimeUnit.MINUTES,
                Storage.SignUrlOption.withV4Signature()
            )
            url.toString()
        } catch (e: Exception) {
            generatePublicUrl(pathToFile)
        }
    }

    suspend fun getFileContent(pathToFile: String): ByteArray? {
        return try {
            val blob = bucket.storage.get(bucket.name, pathToFile)
            blob?.getContent()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun upload(multipartData: MultiPartData): String {
        val items = mutableListOf<PartData.FormItem>()
        var fileContent: ByteArray? = null
        var fileType: String? = null
        var originalFileName: String? = null

        multipartData.forEachPart { part ->
            when(part) {
                is PartData.FileItem -> {
                    fileContent = part.provider().toByteArray()
                    originalFileName = part.originalFileName ?: throw CustomIllegalArgumentException("Nome do arquivo não encontrado")
                    fileType = part.contentType?.toString() ?: throw CustomIllegalArgumentException("Tipo de arquivo não encontrado")
                }
                is PartData.FormItem -> {
                    items.add(part)
                }
                else -> throw CustomIllegalArgumentException("Formato de arquivo não suportado")
            }
            part.dispose()
        }

        if (fileContent == null) {
            throw CustomBadRequestException("Nenhum arquivo foi enviado")
        }

        val fileName = items.find { it.name == "fileName" }?.value ?: originalFileName
        val pathToFile = items.find { it.name == "pathToFile" }?.value ?: "public/$fileName"

        val blobInfoBuilder = BlobInfo.newBuilder(bucket.name, pathToFile)
            .setContentType(fileType)

        if (pathToFile.startsWith("public/")) {
            blobInfoBuilder.setAcl(listOf(
                com.google.cloud.storage.Acl.of(
                    com.google.cloud.storage.Acl.User.ofAllUsers(),
                    com.google.cloud.storage.Acl.Role.READER
                )
            ))
        }

        val blobInfo = blobInfoBuilder.build()
        bucket.storage.create(blobInfo, fileContent)

        return if (pathToFile.startsWith("public/")) {
            generatePublicUrl(pathToFile)
        } else {
            generateSignedUrl(pathToFile, 15)
        }
    }

    suspend fun delete(pathToFile: String): Unit {
        bucket.storage.delete(bucket.name, pathToFile)
    }
}