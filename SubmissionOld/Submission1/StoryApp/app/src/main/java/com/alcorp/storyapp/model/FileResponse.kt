package com.alcorp.storyapp.model

import com.google.gson.annotations.SerializedName

data class FileResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)