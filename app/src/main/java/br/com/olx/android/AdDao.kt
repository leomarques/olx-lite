package br.com.olx.android

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AdDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Ad>)

    @Query("SELECT * FROM ads")
    fun allAds(): DataSource.Factory<Int, Ad>
}
