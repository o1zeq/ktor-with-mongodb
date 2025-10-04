plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.gradleup.shadow)
}

group = "top.dedicado"
version = "0.0.1"

application {
    mainClass = "top.dedicado.ApplicationKt"
}

repositories {
    mavenCentral()
}

dependencies {
    // ktor
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.sse)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.request.validation)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.netty)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.cio)

    implementation(libs.ktor.utils)

    // koin
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)

    // mongodb
    implementation(libs.mongodb.driver.core)
    implementation(libs.mongodb.driver.sync)
    implementation(libs.bson)

    // firebase
    implementation(libs.firebase.admin)

    // aws
    implementation(libs.sqs)
    implementation(libs.s3)

    // kotlinx
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.guava)

    // others
    implementation(libs.logback.classic)
    implementation(libs.jbcrypt)

    implementation(libs.google.cloud.secret.manager)

    implementation(libs.dotenv.kotlin)

    // tests
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
