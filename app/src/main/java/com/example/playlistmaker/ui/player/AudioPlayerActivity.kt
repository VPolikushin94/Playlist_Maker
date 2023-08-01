package com.example.playlistmaker.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.util.DateTimeUtil
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.api.player.AudioPlayerPresenter
import com.example.playlistmaker.presentation.impl.AudioPlayerPresenterImpl
import com.example.playlistmaker.presentation.api.player.AudioPlayerView
import com.example.playlistmaker.ui.search.SearchActivity
import kotlin.RuntimeException


class AudioPlayerActivity : AppCompatActivity(), AudioPlayerView {

    private lateinit var buttonPlayerBack: ImageButton
    private lateinit var buttonPlayerPlay: ImageButton
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

    private lateinit var audioPlayerPresenter: AudioPlayerPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        parseIntent()
        setViews()
        audioPlayerPresenter = AudioPlayerPresenterImpl(track, Creator.audioPlayerInteractor, this)
        setContent()
        setButtonsClickListeners()
    }

    override fun onPause() {
        super.onPause()
        audioPlayerPresenter.pause()
    }

    override fun onDestroy() {
        audioPlayerPresenter.release()
        super.onDestroy()
    }

    private fun setButtonsClickListeners() {
        buttonPlayerBack.setOnClickListener {
            finish()
        }
        buttonPlayerPlay.setOnClickListener {
            audioPlayerPresenter.playbackControl()
        }
    }

    private fun setViews() {
        buttonPlayerBack = findViewById(R.id.button_player_back)
        buttonPlayerPlay = findViewById(R.id.button_player_play)
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
        if (!intent.hasExtra(SearchActivity.TRACK_INFO)) {
            throw RuntimeException("Track info is absent")
        }
        track = intent.getParcelableExtra(SearchActivity.TRACK_INFO)!!
    }

    private fun setContent() {
        tvPlayerTrackName.text = track.trackName
        tvPlayerArtistName.text = track.artistName
        tvPlayerTrackTime.text = DateTimeUtil.getFormatTime(track.trackTimeMillis)
        if (track.collectionName.isEmpty()) {
            groupPlayerTrackAlbum.isVisible = false
        } else {
            groupPlayerTrackAlbum.isVisible = true
            tvPlayerTrackAlbum.text = track.collectionName
        }
        tvPlayerTrackYear.text =
            track.releaseDate.substring(FIRST_DIGIT_OF_YEAR, LAST_DIGIT_OF_YEAR)
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

        savedInstanceState.getParcelable<Track>(TRACK_INFO)?.let {
            track = it
            setContent()
        }
    }


    override fun showCurrentTrackTime(time: String) {
        tvPlayerTrackCurrentTime.text = time
    }

    override fun setButtonPlayerPlayEnabled(isEnabled: Boolean) {
        buttonPlayerPlay.isEnabled = isEnabled
    }

    override fun showPlayButtonState(isPlayBtn: Boolean) {
        if (isPlayBtn) {
            buttonPlayerPlay.setImageResource(R.drawable.ic_play_button)
        } else {
            buttonPlayerPlay.setImageResource(R.drawable.ic_pause_button)
        }
    }

    companion object {
        private const val FIRST_DIGIT_OF_YEAR = 0
        private const val LAST_DIGIT_OF_YEAR = 4

        private const val TRACK_INFO = "TRACK_INFO"
    }
}