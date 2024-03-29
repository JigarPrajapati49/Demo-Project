package com.example.db

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE product_table ADD COLUMN shadow INTEGER NOT NULL DEFAULT(0)")
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): ProductDatabase {
        return Room.databaseBuilder(appContext, ProductDatabase::class.java, "productDB")
            .allowMainThreadQueries()
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun productDao(appDatabase: ProductDatabase): ProductDao {
        return appDatabase.productDao()
    }

}