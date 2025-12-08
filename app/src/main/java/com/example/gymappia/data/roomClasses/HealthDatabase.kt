package com.example.gymappia.data.roomClasses

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gymappia.data.roomClasses.WorkoutDao
import com.example.gymappia.data.roomClasses.WorkoutEntity

@Database(entities = [
    WorkoutEntity::class,
    FoodEntity::class,
    DailyMetricsEntity::class], version = 1, exportSchema = false)
@TypeConverters(
    ConvertersForRoom::class
)
abstract class HealthDatabase : RoomDatabase() {
    abstract fun exerciseDao(): WorkoutDao
    abstract fun foodDao(): FoodDao
    abstract fun dailyMetricsDao(): DailyMetricsDao

    companion object {
        @Volatile
        private var INSTANCE: HealthDatabase? = null

        fun getDatabase(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthDatabase::class.java,
                    "health_database"//database file name
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}