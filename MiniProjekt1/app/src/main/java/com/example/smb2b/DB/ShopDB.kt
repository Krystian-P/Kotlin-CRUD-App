package com.example.smb2b.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ShopE::class], version = 3)
abstract class ShopDB: RoomDatabase() {


    abstract fun shopDao(): ShopDao?

    companion object {
        private var INSTANCE: ShopDB?= null

        val migration_1_2: Migration = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE userinfo ADD COLUMN phone TEXT DEFAULT ''")
            }
        }

        fun getAppDatabase(context: Context): ShopDB? {

            if(INSTANCE == null ) {

                INSTANCE = Room.databaseBuilder<ShopDB>(
                    context.applicationContext, ShopDB::class.java, "AppDBB"
                )
                    .addMigrations(migration_1_2)
                    .allowMainThreadQueries()
                    .build()

            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}