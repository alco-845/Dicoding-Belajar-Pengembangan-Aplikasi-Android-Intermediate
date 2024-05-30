package com.alcorp.storyapp.utils

import com.alcorp.storyapp.model.ListStoryResponse

object DataDummy {
    fun generateDummyStory(): List<ListStoryResponse>{
        val item = arrayListOf<ListStoryResponse>()

        for (i in 0 until 10){
            val story = ListStoryResponse(
                "story-RLk-UA8wKx9HQogg",
                "z",
                "obat lagi",
                "https://story-api.dicoding.dev/images/stories/photos-1667977376926_KSWyFDWz.jpg",
                -7.8353467,
                110.343864
            )
            item.add(story)
        }
        return item
    }
}