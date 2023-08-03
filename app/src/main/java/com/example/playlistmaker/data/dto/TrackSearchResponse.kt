package com.example.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName

data class TrackSearchResponse(

    @SerializedName("results")
    val trackList: ArrayList<TrackDto>
) : Response()