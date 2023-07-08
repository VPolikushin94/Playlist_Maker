package com.example.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.RuntimeException


class AudioPlayerActivity : AppCompatActivity() {

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

    private val mediaPlayer = MediaPlayer()
    private var playerCurrentState = AudioPlayerState.DEFAULT

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        parseIntent()
        preparePlayer()
        setViews()
        buttonPlayerPlay.isEnabled = false
        setContent()
        setButtonsClickListeners()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun setButtonsClickListeners() {
        buttonPlayerBack.setOnClickListener {
            finish()
        }
        buttonPlayerPlay.setOnClickListener {
            playbackControl()
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
        tvPlayerTrackTime.text = getFormatTime(track.trackTimeMillis)
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

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerCurrentState = AudioPlayerState.PREPARED
            buttonPlayerPlay.isEnabled = true
        }
        mediaPlayer.setOnCompletionListener {
            playerCurrentState = AudioPlayerState.PREPARED
            buttonPlayerPlay.setImageResource(R.drawable.ic_play_button)
            tvPlayerTrackCurrentTime.text = getFormatTime(TIME_START)
        }
    }

    private fun startPlayer() {
        playerCurrentState = AudioPlayerState.PLAYING
        mediaPlayer.start()
        startTimer()
        buttonPlayerPlay.setImageResource(R.drawable.ic_pause_button)
    }

    private fun pausePlayer() {
        playerCurrentState = AudioPlayerState.PAUSED
        mediaPlayer.pause()
        handler.removeCallbacks(createUpdateTimerTask())
        buttonPlayerPlay.setImageResource(R.drawable.ic_play_button)
    }

    private fun startTimer() {
        handler.post(createUpdateTimerTask())
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerCurrentState == AudioPlayerState.PLAYING) {
                    tvPlayerTrackCurrentTime.text = getFormatTime(mediaPlayer.currentPosition)
                    handler.postDelayed(this, UPDATE_TIMER_DELAY)
                }
            }
        }
    }

    private fun playbackControl() {
        when(playerCurrentState) {
            AudioPlayerState.PAUSED, AudioPlayerState.PREPARED -> startPlayer()
            AudioPlayerState.PLAYING -> pausePlayer()
            else -> throw RuntimeException("Media Player is not prepared")
        }
    }

    private fun getFormatTime(time: Int) =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)

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

    companion object {
        private const val FIRST_DIGIT_OF_YEAR = 0
        private const val LAST_DIGIT_OF_YEAR = 4

        private const val TRACK_INFO = "TRACK_INFO"

        private const val UPDATE_TIMER_DELAY = 300L

        private const val TIME_START = 0
    }
}