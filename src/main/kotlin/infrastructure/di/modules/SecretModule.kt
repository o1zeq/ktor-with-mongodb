package top.dedicado.infrastructure.di.modules

import org.koin.dsl.module
import top.dedicado.infrastructure.secrets.SecretKeys
import top.dedicado.infrastructure.secrets.SecretsProvider

val secretModule = module {

    single<SecretKeys> {
        SecretsProvider.load(get(), get())
    }
}