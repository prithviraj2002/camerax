package com.example.memeplay.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.memeplay.R
import com.example.memeplay.model.Meme

@Composable
fun MainScreen (viewModel: ScreenViewModel){

    val meme : Meme = viewModel.getMeme()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MemeImage(pic = meme)
        }
    }
}

@Composable
fun MemeImage(pic: Meme){
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(pic.preview[0])
            .crossfade(true)
            .build(),
        contentDescription = "meme image",
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(200.dp)
    )
}

