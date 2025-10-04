package top.dedicado.infrastructure.di.modules

import org.koin.core.qualifier.named
import org.koin.dsl.module
import top.dedicado.domain.services.AuthService
import top.dedicado.domain.services.ProducerService
import top.dedicado.domain.services.RegisterService
import top.dedicado.domain.services.StorageService
import top.dedicado.domain.services.component.ActivityService
import top.dedicado.domain.services.principal.ContactService
import top.dedicado.domain.services.principal.MembershipService
import top.dedicado.domain.services.principal.ProjectService
import top.dedicado.domain.services.principal.SessionService
import top.dedicado.domain.services.principal.SubscriptionService
import top.dedicado.domain.services.principal.AccountService
import top.dedicado.infrastructure.firebase.FirebaseConstants.SUITE_BUCKET

val serviceModule = module {
    single {
        ActivityService(get())
    }

    single {
        AccountService(get(), get())
    }
    single {
        ProducerService(get(), get())
    }
    single {
        ContactService(get())
    }
    single {
        MembershipService(get())
    }
    single {
        ProjectService(get())
    }
    single {
        SessionService(get())
    }
    single {
        SubscriptionService(get())
    }

    single {
        AuthService(get(), get(), get())
    }
    single {
        RegisterService(get(), get())
    }
    single {
        StorageService(get(named(SUITE_BUCKET)))
    }
}