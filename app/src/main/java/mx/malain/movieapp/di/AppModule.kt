package mx.malain.movieapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mx.malain.movieapp.App
import mx.malain.movieapp.data.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): App {
        return app as App
    }

    @Singleton
    @Provides
    fun provideApiKey(): String {
        return "214edc3b36d08224b8548348b05f8656"
    }

    @Singleton
    @Provides
    fun provideDatabase(
        app: App
    ): AppDatabase {
        val db = Room
            .databaseBuilder(
                app, AppDatabase::class.java, "moviedb"
            )
            .fallbackToDestructiveMigration()
            .build()
        return db
    }
}