package me.martichou.unswayed.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.martichou.unswayed.models.BackedFolder

@Database(entities = [BackedFolder::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun backedFolderDao(): BackedFolderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "unswayed"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}