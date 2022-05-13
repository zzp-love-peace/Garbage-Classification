package com.zzp.rubbish.util

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zzp.rubbish.data.Task
import com.zzp.rubbish.util.MyApplication.Companion.context

@Database(version = 1, entities = [Task::class], exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        private var instance: TaskDatabase? = null

        @Synchronized
        fun getDatabase(): TaskDatabase {
            instance?.let { return it }
            return Room.databaseBuilder(context,
                TaskDatabase::class.java, "task_database").build().apply { instance = this }
        }
    }
}