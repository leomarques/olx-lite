package br.com.olx.data.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AdRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<AdRoom>)

    @Query("SELECT * FROM ads")
    fun allAds(): DataSource.Factory<Int, AdRoom>
}
