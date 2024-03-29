package com.example.demoproject

import android.content.SearchRecentSuggestionsProvider


class SimpleSearchSuggestionsProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        private val LOG_TAG = SimpleSearchSuggestionsProvider::class.java.simpleName
        const val AUTHORITY = "com.example.demoproject.SimpleSearchSuggestionsProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }
}