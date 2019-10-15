package com.example.myapplication.data

import androidx.lifecycle.LiveData

// The DAO is passed into the repository instead of the database
class WordRepository(private val wordDao: WordDao) {

    //
    val allWords: LiveData<List<Word>> = wordDao.getAllWords()

    /* The suspend modifier tells the compiler that this must be called from a
     coroutine or another suspend function. */
    suspend fun insert (word: Word){
        wordDao.insert(word)
    }
}