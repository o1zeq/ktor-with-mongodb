package top.dedicado.infrastructure.di.modules

import org.koin.dsl.module
import top.dedicado.infrastructure.jwt.JWTProvider

val jwtModule = module {

    factory {
        JWTProvider(get(), get())
    }
}