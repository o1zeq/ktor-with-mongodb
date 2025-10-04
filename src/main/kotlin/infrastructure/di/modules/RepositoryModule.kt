package top.dedicado.infrastructure.di.modules

import org.koin.core.qualifier.named
import org.koin.dsl.module
import top.dedicado.domain.repositories.component.ActivityRepository
import top.dedicado.domain.repositories.principal.ContactRepository
import top.dedicado.domain.repositories.principal.MembershipRepository
import top.dedicado.domain.repositories.principal.ProjectRepository
import top.dedicado.domain.repositories.principal.SessionRepository
import top.dedicado.domain.repositories.principal.SubscriptionRepository
import top.dedicado.domain.repositories.principal.AccountRepository
import top.dedicado.infrastructure.mongodb.MongoDBConstants.ACTIVITY_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.CONTACT_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.MEMBERSHIP_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.PROJECT_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.SESSION_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.SUBSCRIPTION_COLLECTION
import top.dedicado.infrastructure.mongodb.MongoDBConstants.USER_COLLECTION
import top.dedicado.infrastructure.repositories.component.ActivityRepositoryImp
import top.dedicado.infrastructure.repositories.principal.ContactRepositoryImp
import top.dedicado.infrastructure.repositories.principal.MembershipRepositoryImp
import top.dedicado.infrastructure.repositories.principal.ProjectRepositoryImp
import top.dedicado.infrastructure.repositories.principal.SessionRepositoryImp
import top.dedicado.infrastructure.repositories.principal.SubscriptionRepositoryImp
import top.dedicado.infrastructure.repositories.principal.AccountRepositoryImp

val repositoryModule = module {
    single<ActivityRepository> {
        ActivityRepositoryImp(get(named(ACTIVITY_COLLECTION)))
    }

    single<AccountRepository> {
        AccountRepositoryImp(get(named(USER_COLLECTION)))
    }
    single<ContactRepository> {
        ContactRepositoryImp(get(named(CONTACT_COLLECTION)))
    }
    single<MembershipRepository> {
        MembershipRepositoryImp(get(named(MEMBERSHIP_COLLECTION)))
    }
    single<ProjectRepository> {
        ProjectRepositoryImp(get(named(PROJECT_COLLECTION)))
    }
    single<SessionRepository> {
        SessionRepositoryImp(get(named(SESSION_COLLECTION)))
    }
    single<SubscriptionRepository> {
        SubscriptionRepositoryImp(get(named(SUBSCRIPTION_COLLECTION)))
    }
}