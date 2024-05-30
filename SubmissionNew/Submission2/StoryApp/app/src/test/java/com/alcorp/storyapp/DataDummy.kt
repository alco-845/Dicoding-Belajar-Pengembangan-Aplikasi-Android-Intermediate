package com.alcorp.storyapp

import com.alcorp.storyapp.data.remote.response.ListStoryResponse

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryResponse> {
        val items: MutableList<ListStoryResponse> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryResponse(
                "story-RLk-UA8wKx9HQogg",
                "name $i",
                "description $i",
                "https://story-api.dicoding.dev/images/stories/photos-1667977376926_KSWyFDWz.jpg",
                -7.8353467,
                110.343864
            )
            items.add(quote)
        }
        return items
    }

}