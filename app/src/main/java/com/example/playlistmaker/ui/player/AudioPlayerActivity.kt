package com.example.playlistmaker.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.util.DateTimeUtil
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.player.models.AudioPlayerState
import com.example.playlistmaker.ui.search.SearchActivity
import kotlin.RuntimeException


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var track: Track

    private lateinit var playerViewModel: AudioPlayerViewModel

    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parseIntent()

        playerViewModel = ViewModelProvider(this, AudioPlayerViewModel.getAudioPlayerFactory())[AudioPlayerViewModel::class.java]
        playerViewModel.prepare(track)

        setContent()
        setButtonsClickListeners()

        playerViewModel.playerState.observe(this) {
            showPlayButtonState(it)
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
    }

    override fun onDestroy() {
        playerViewModel.release()
        super.onDestroy()
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
        if (!intent.hasExtra(SearchActivity.TRACK_INFO)) {
            throw RuntimeException("Track info is absent")
        }
        track = intent.getParcelableExtra(SearchActivity.TRACK_INFO)!!
    }

    private fun setContent() {
        binding.tvPlayerTrackName.text = track.trackName
        binding.tvPlayerArtistName.text = track.artistName
        binding.tvPlayerTrackTime.text = DateTimeUtil.getFormatTime(track.trackTimeMillis)
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
            .transform(RoundedCorners(8))
            .into(binding.ivPlayerTrackImage)
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


    private fun showCurrentTrackTime(time: String) {
        binding.tvPlayerTrackCurrentTime.text = time
    }

    private fun showPlayButtonState(playerState: AudioPlayerState) {
        when(playerState) {
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

        private const val TIME_START = 0
    }
}