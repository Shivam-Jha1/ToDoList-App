package com.coder178.notes.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.coder178.notes.Dao.NotesDao
import com.coder178.notes.Model.Notes


@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun MyNotesDao(): NotesDao

    companion object {
        @Volatile
        var INSTANCE: NotesDatabase? = null

        fun getDatabaseInstance(context: Context): NotesDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val roomDatabaseInstance =
                    Room.databaseBuilder(context, NotesDatabase::class.java, "MyNotes")
                        .allowMainThreadQueries().build()
                INSTANCE = roomDatabaseInstance
                return return roomDatabaseInstance
            }
        }
    }
}