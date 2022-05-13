package com.zzp.rubbish.util

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zzp.rubbish.data.Task

@Dao
interface TaskDao {
    @Insert
    fun insertTask(task: Task): Long

    @Query("select * from Task where time = :time")
    fun selectTaskByTime(time: String): List<Task>
}