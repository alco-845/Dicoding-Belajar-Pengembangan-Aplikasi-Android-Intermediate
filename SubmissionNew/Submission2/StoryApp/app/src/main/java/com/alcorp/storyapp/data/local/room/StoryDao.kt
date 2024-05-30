package com.alcorp.storyapp.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alcorp.storyapp.data.remote.response.ListStoryResponse

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryResponse>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStoryResponse>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}