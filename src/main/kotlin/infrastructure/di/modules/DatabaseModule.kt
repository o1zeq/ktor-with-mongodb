package top.dedicado.infrastructure.di.modules

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import top.dedicado.infrastructure.mongodb.MongoDBConstants.COMPONENT_DB
import top.dedicado.infrastructure.mongodb.MongoDBConstants.PRINCIPAL_DB
import top.dedicado.infrastructure.secrets.SecretKeys

val databaseModule = module {
    single<MongoDatabase>(named(COMPONENT_DB)) {
        val uri = get<SecretKeys>().componentUri
        MongoClients.create(uri).getDatabase(COMPONENT_DB)
    }
    single<MongoDatabase>(named(PRINCIPAL_DB)) {
        val uri = get<SecretKeys>().principalUri
        MongoClients.create(uri).getDatabase(PRINCIPAL_DB)
    }
}