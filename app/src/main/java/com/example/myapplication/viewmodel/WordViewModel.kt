package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Word
import com.example.myapplication.data.WordRepository
import com.example.myapplication.data.WordRoomDatabase
import kotlinx.coroutines.launch

class WordViewModel(application: Application): AndroidViewModel(application){

    // declare a reference to the repository to get data from it
    private val repository: WordRepository

    // LiveData gives updated words when they change
    val allWords: LiveData<List<Word>>

    init {
        // Get reference from the WordRoomDatabase
        val wordDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = WordRepository(wordDao)
        allWords = repository.allWords
    }

    //viewModelScope is a coroutine scope for viewModel
    fun insert(word: Word) = viewModelScope.launch{
        repository.insert(word)
    }

}