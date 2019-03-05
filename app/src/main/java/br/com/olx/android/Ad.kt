package br.com.olx.android

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ads")
data class Ad(@PrimaryKey val id: String)