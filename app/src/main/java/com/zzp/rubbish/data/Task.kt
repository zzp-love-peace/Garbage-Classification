package com.zzp.rubbish.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(val place: String, val type: String,val rubbishType: String, val time: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}