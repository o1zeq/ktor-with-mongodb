package top.dedicado.infrastructure.di.modules

import com.google.cloud.storage.Bucket
import com.google.firebase.cloud.StorageClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import top.dedicado.infrastructure.firebase.FirebaseConstants.FIREBASE_APP
import top.dedicado.infrastructure.firebase.FirebaseConstants.SUITE_BUCKET

val storageModule = module {

    single<Bucket>(named(SUITE_BUCKET)) {
        StorageClient.getInstance(
            get(named(FIREBASE_APP))
        ).bucket(SUITE_BUCKET)
    }
}