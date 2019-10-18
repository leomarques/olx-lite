package br.com.olx.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ads")
data class AdRoom(
        @PrimaryKey val id: String,
        val thumbUrl: String,
        val title: String,
        val description: String,
        val price: String,
        val oldPrice: String,
        val date: String,
        val location: String,
        val isFeatured: Boolean
)
