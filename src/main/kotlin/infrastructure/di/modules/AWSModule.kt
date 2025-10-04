package top.dedicado.infrastructure.di.modules

import org.koin.dsl.module
import top.dedicado.infrastructure.aws.AWSSqs

val awsModule = module {

    single {
        AWSSqs(get(), get())
    }
}