package com.example.gymappia.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [
    ExerciseEntity::class,
    FoodEntity::class
                     ], version = 1, exportSchema = false)
@TypeConverters(
    ConvertersForRoom::class
)
abstract class HealthDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDAO
    abstract fun foodDao(): FoodDao

    companion object {
        @Volatile
        private var INSTANCE: HealthDatabase? = null

        fun getDatabase(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthDatabase::class.java,
                    "exercise_database"//database file name
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
