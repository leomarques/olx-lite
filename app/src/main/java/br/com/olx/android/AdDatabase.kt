package br.com.olx.android

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Ad::class],
    version = 1,
    exportSchema = false
)
abstract class AdDatabase : RoomDatabase() {

    abstract fun adsDao(): AdDao

    companion object {

        @Volatile
        private var INSTANCE: AdDatabase? = null

        fun getInstance(context: Context): AdDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AdDatabase::class.java, "Ad.db")
                .build()
    }
}