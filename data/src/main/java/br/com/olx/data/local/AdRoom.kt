package br.com.olx.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ads")
data class AdRoom(@PrimaryKey val id: String)
