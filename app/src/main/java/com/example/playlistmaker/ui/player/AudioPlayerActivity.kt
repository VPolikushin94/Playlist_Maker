package com.example.playlistmaker.ui.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.models.AudioPlayerState
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel

import com.example.playlistmaker.util.DateTimeUtil
import org.koin.androidx.viewmodel.ext.android.viewModel


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var track: Track

    private val playerViewModel: AudioPlayerViewModel by viewModel()

    private lateinit var binding: ActivityAudioPlayerBinding
    private var isActivityCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parseIntent()

        isActivityCreated = checkSavedInstanceState(savedInstanceState)

        if (!isActivityCreated) {
            playerViewModel.prepare(track)
            isActivityCreated = true
        }

        setContent()
        setButtonsClickListeners()

        playerViewModel.playerState.observe(this) {
            showPlayButtonState(it)
        }
    }

    private fun checkSavedInstanceState(savedInstanceState: Bundle?): Boolean {
        return if (savedInstanceState != null) {
            savedInstanceState.getParcelable<Track>(TRACK_INFO)?.let {
                track = it
                setContent()
                if (playerViewModel.playerState.value is AudioPlayerState.Paused) {
                    showCurrentTrackTime((playerViewModel.playerState.value as AudioPlayerState.Paused).time)
                }
            }
            savedInstanceState.getBoolean(IS_ACTIVITY_CREATED, false)
        } else false
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
    }

    private fun setButtonsClickListeners() {
        binding.buttonPlayerBack.setOnClickListener {
            finish()
        }
        binding.buttonPlayerPlay.setOnClickListener {
            playerViewModel.playbackControl()
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(TRACK_INFO)) {
            throw RuntimeException("Track info is absent")
        }
        track = intent.getParcelableExtra(TRACK_INFO)!!
    }

    private fun setContent() {
        binding.tvPlayerTrackName.text = track.trackName
        binding.tvPlayerArtistName.text = track.artistName
        binding.tvPlayerTrackTime.text = track.trackTimeMillis
        if (track.collectionName.isEmpty()) {
            binding.groupPlayerTrackAlbum.isVisible = false
        } else {
            binding.groupPlayerTrackAlbum.isVisible = true
            binding.tvPlayerTrackAlbum.text = track.collectionName
        }
        binding.tvPlayerTrackYear.text =
            track.releaseDate.substring(FIRST_DIGIT_OF_YEAR, LAST_DIGIT_OF_YEAR)
        binding.tvPlayerTrackGenre.text = track.primaryGenreName
        binding.tvPlayerTrackCountry.text = track.country

        Glide.with(applicationContext)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.ic_player_track_placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(
                    resources.getDimensionPixelSize(R.dimen.player_album_radius)
                )
            )
            .into(binding.ivPlayerTrackImage)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(TRACK_INFO, track)
        outState.putBoolean(IS_ACTIVITY_CREATED, isActivityCreated)
    }

    private fun showCurrentTrackTime(time: String) {
        binding.tvPlayerTrackCurrentTime.text = time
    }

    private fun showPlayButtonState(playerState: AudioPlayerState) {
        when (playerState) {
            is AudioPlayerState.Default -> binding.buttonPlayerPlay.isEnabled = false
            is AudioPlayerState.Prepared -> {
                binding.buttonPlayerPlay.isEnabled = true
                binding.buttonPlayerPlay.setImageResource(R.drawable.ic_play_button)
                showCurrentTrackTime(DateTimeUtil.getFormatTime(TIME_START))
            }
            is AudioPlayerState.Paused -> binding.buttonPlayerPlay.setImageResource(R.drawable.ic_play_button)
            is AudioPlayerState.Playing -> {
                binding.buttonPlayerPlay.setImageResource(R.drawable.ic_pause_button)
                showCurrentTrackTime(playerState.time)
            }
        }
    }

    companion object {
        private const val FIRST_DIGIT_OF_YEAR = 0
        private const val LAST_DIGIT_OF_YEAR = 4

        private const val TRACK_INFO = "TRACK_INFO"
        private const val IS_ACTIVITY_CREATED = "IS_ACTIVITY_CREATED"

        private const val TIME_START = 0

        fun newInstance(context: Context, track: Track): Intent {
            return Intent(context, AudioPlayerActivity::class.java).apply {
                putExtra(TRACK_INFO, track)
            }
        }
    }
}