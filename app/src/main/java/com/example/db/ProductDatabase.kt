package com.example.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.model.Product

@Database(
    entities = [Product::class], version = 2
)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    /*companion object {
        @Volatile
        private var INSTANCE: ProductDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE product_table ADD COLUMN shadow INTEGER NOT NULL DEFAULT(0)")
            }
        }

        fun getDatabase(context: Context): ProductDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        ProductDatabase::class.java,
                        "productDB"
                    )
                        .allowMainThreadQueries()
                        .addMigrations(MIGRATION_1_2)
                        .build()
                }
            }
            return INSTANCE!!
        }
    }*/
}