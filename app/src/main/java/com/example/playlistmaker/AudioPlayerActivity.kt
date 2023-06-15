package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var buttonPlayerBack: ImageButton
    private lateinit var tvPlayerTrackName: TextView
    private lateinit var tvPlayerArtistName: TextView
    private lateinit var tvPlayerTrackCurrentTime: TextView
    private lateinit var tvPlayerTrackTime: TextView
    private lateinit var tvPlayerTrackAlbum: TextView
    private lateinit var tvPlayerTrackYear: TextView
    private lateinit var tvPlayerTrackGenre: TextView
    private lateinit var tvPlayerTrackCountry: TextView
    private lateinit var groupPlayerTrackAlbum: Group
    private lateinit var ivPlayerTrackImage: ImageView

    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        parseIntent()
        setViews()
        setContent()

        buttonPlayerBack.setOnClickListener {
            finish()
        }
    }

    private fun setViews() {
        buttonPlayerBack = findViewById(R.id.button_player_back)
        tvPlayerTrackName = findViewById(R.id.tv_player_track_name)
        tvPlayerArtistName = findViewById(R.id.tv_player_artist_name)
        tvPlayerTrackCurrentTime = findViewById(R.id.tv_player_track_current_time)
        tvPlayerTrackTime = findViewById(R.id.tv_player_track_time)
        tvPlayerTrackAlbum = findViewById(R.id.tv_player_track_album)
        tvPlayerTrackYear = findViewById(R.id.tv_player_track_year)
        tvPlayerTrackGenre = findViewById(R.id.tv_player_track_genre)
        tvPlayerTrackCountry = findViewById(R.id.tv_player_track_country)
        groupPlayerTrackAlbum = findViewById(R.id.group_player_track_album)
        ivPlayerTrackImage = findViewById(R.id.iv_player_track_image)
    }

    private fun parseIntent() {
        if(!intent.hasExtra(SearchActivity.TRACK_INFO)) {
            throw RuntimeException("Track info is absent")
        }
        track = intent.getParcelableExtra(SearchActivity.TRACK_INFO)!!
    }

    private fun setContent() {
        tvPlayerTrackName.text = track.trackName
        tvPlayerArtistName.text = track.artistName
        tvPlayerTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        if(track.collectionName.isEmpty()) {
            groupPlayerTrackAlbum.isVisible = false
        } else {
            groupPlayerTrackAlbum.isVisible = true
            tvPlayerTrackAlbum.text = track.collectionName
        }
        tvPlayerTrackYear.text = track.releaseDate.substring(FIRST_DIGIT_OF_YEAR, LAST_DIGIT_OF_YEAR)
        tvPlayerTrackGenre.text = track.primaryGenreName
        tvPlayerTrackCountry.text = track.country

        Glide.with(applicationContext)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.ic_player_track_placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(ivPlayerTrackImage)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(TRACK_INFO, track)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.getParcelable<Track>(TRACK_INFO)?.let{
            track = it
            setContent()
        }
    }
    companion object {
        private const val FIRST_DIGIT_OF_YEAR = 0
        private const val LAST_DIGIT_OF_YEAR = 4

        private const val TRACK_INFO = "TRACK_INFO"
    }
}