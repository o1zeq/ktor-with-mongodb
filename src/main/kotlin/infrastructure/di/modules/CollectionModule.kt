package top.dedicado.infrastructure.di.modules

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.koin.core.qualifier.named
import org.koin.dsl.module
import top.dedicado.infrastructure.mongodb.MongoDBConstants.ACTIVITY_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.APPOINTMENT_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.ATTACHMENT_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.COMPONENT_DB
import top.dedicado.infrastructure.mongodb.MongoDBConstants.CONTACT_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.LOCATION_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.MEMBERSHIP_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.NOTE_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.USER_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.PRINCIPAL_DB
import top.dedicado.infrastructure.mongodb.MongoDBConstants.PROJECT_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.SESSION_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.SUBSCRIPTION_COLLECTION

val collectionModule = module {
    single<MongoCollection<Document>>(named(ACTIVITY_COLLECTION)) {
        get<MongoDatabase>(named(COMPONENT_DB)).getCollection(ACTIVITY_COLLECTION)
    }
    single<MongoCollection<Document>>(named(APPOINTMENT_COLLECTION)) {
        get<MongoDatabase>(named(COMPONENT_DB)).getCollection(APPOINTMENT_COLLECTION)
    }
    single<MongoCollection<Document>>(named(ATTACHMENT_COLLECTION)) {
        get<MongoDatabase>(named(COMPONENT_DB)).getCollection(ATTACHMENT_COLLECTION)
    }
    single<MongoCollection<Document>>(named(LOCATION_COLLECTION)) {
        get<MongoDatabase>(named(COMPONENT_DB)).getCollection(LOCATION_COLLECTION)
    }
    single<MongoCollection<Document>>(named(NOTE_COLLECTION)) {
        get<MongoDatabase>(named(COMPONENT_DB)).getCollection(NOTE_COLLECTION)
    }

    single<MongoCollection<Document>>(named(CONTACT_COLLECTION)) {
        get<MongoDatabase>(named(PRINCIPAL_DB)).getCollection(CONTACT_COLLECTION)
    }
    single<MongoCollection<Document>>(named(MEMBERSHIP_COLLECTION)) {
        get<MongoDatabase>(named(PRINCIPAL_DB)).getCollection(MEMBERSHIP_COLLECTION)
    }
    single<MongoCollection<Document>>(named(PROJECT_COLLECTION)) {
        get<MongoDatabase>(named(PRINCIPAL_DB)).getCollection(PROJECT_COLLECTION)
    }
    single<MongoCollection<Document>>(named(SESSION_COLLECTION)) {
        get<MongoDatabase>(named(PRINCIPAL_DB)).getCollection(SESSION_COLLECTION)
    }
    single<MongoCollection<Document>>(named(SUBSCRIPTION_COLLECTION)) {
        get<MongoDatabase>(named(PRINCIPAL_DB)).getCollection(SUBSCRIPTION_COLLECTION)
    }
    single<MongoCollection<Document>>(named(USER_COLLECTION)) {
        get<MongoDatabase>(named(PRINCIPAL_DB)).getCollection(USER_COLLECTION)
    }
}