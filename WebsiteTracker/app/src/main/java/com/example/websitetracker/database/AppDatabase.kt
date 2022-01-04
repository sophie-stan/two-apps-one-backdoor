package com.example.websitetracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The AppDatabase extends the abstract RoomDatabase and is a singleton.
 *
 * It has only one table named "search" filled with Search entities, which can be queried through
 * the searchDao interface.
 */

@Database(entities = [Search::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val searchDao: SearchDao

    // companion object stands more or less for "static".
    companion object {

        @Volatile // The writes to INSTANCE are immediately seen by other threads.
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

            synchronized(this) {

                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE

                // INSTANCE is a singleton (only one common instance between threads)
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "navigation_history_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}
