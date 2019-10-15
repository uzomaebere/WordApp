package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.security.AccessControlContext

@Database(entities = [Word::class], version = 1)
public abstract class WordRoomDatabase: RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase {
            //        val tempInstance = INSTANCE
            //        if (tempInstance != null){
            //            return tempInstance
            //        }
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }
        private class WordDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback(){

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)

                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.wordDao())
                    }
                }
            }

            suspend fun populateDatabase(wordDao: WordDao) {

                // delete all the content
                wordDao.deleteAll()

                // Add new words
                var word = Word("Japan")
                wordDao.insert(word)

                word = Word("Nigeria")
                wordDao.insert(word)

                word = Word("Senegal")
                wordDao.insert(word)
            }
        }

}