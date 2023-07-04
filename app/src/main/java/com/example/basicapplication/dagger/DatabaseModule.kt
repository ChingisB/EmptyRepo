package com.example.basicapplication.dagger


import android.content.Context
import com.example.data.SingleThreadScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
interface DatabaseModule {

    companion object {

        @Singleton
        @Provides
        fun provideRealm(@SingleThreadScheduler scheduler: Scheduler, context: Context): Realm {
            val future = CompletableFuture<Realm>()
            Realm.init(context)

            scheduler.scheduleDirect{
                val realm = Realm.getDefaultInstance()
                future.complete(realm)
            }

            return future.get()
        }

        @Singleton
        @SingleThreadScheduler
        @Provides
        fun provideSchedulersIO(): Scheduler{
            return Schedulers.from(Executors.newSingleThreadExecutor())
        }
    }
}