package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.ConsumerData
import com.example.playlistmaker.domain.search.models.Track

interface TracksConsumer {
    fun consume(consumerData: ConsumerData<List<Track>>)
}