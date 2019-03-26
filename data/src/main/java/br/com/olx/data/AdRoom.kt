package br.com.olx.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ads")
data class AdRoom(@PrimaryKey val id: String)