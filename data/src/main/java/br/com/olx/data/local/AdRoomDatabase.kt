package br.com.olx.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AdRoom::class],
    version = 1,
    exportSchema = false
)
abstract class AdRoomDatabase : RoomDatabase() {

    abstract fun adsDao(): AdRoomDao

    companion object {

        @Volatile
        private var INSTANCE: AdRoomDatabase? = null

        fun getInstance(context: Context): AdRoomDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AdRoomDatabase::class.java, "AdRoom.db")
                .build()
    }
}
