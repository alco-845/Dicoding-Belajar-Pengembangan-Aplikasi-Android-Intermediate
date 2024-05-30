package com.alcorp.storyapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alcorp.storyapp.model.User
import com.alcorp.storyapp.model.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val ID = stringPreferencesKey("userid")
        private val NAME = stringPreferencesKey("name")
        private val TOKEN = stringPreferencesKey("token")
        private val STATE = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    fun getUserData(): Flow<UserResponse> {
        return dataStore.data.map { preferences ->
            UserResponse(
                preferences[ID] ?: "",
                preferences[NAME] ?: "",
                preferences[TOKEN] ?: ""
            )
        }
    }

    suspend fun saveUserData(user: UserResponse) {
        dataStore.edit { preferences ->
            preferences[ID] = user.userId
            preferences[NAME] = user.name
            preferences[TOKEN] = user.token
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}