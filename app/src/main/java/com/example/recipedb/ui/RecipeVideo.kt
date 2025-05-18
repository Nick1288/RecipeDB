package com.example.recipedb.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun RecipeVideoScreen(youtubeUrl: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Watch the Cooking Video", style = MaterialTheme.typography.titleLarge)

        // ✅ Embedded YouTube Player
        EmbeddedYouTubePlayer(youtubeUrl = youtubeUrl)

        // Optional fallback: open in YouTube app
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, youtubeUrl.toUri())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Open in YouTube")
        }
    }
}


@Composable
fun EmbeddedYouTubePlayer(youtubeUrl: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val videoId = remember(youtubeUrl) {
        youtubeUrl.toUri().getQueryParameter("v") ?: ""
    }

    AndroidView(
        factory = {
            val playerView = YouTubePlayerView(context)
            lifecycleOwner.lifecycle.addObserver(playerView)
            playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
            playerView
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

