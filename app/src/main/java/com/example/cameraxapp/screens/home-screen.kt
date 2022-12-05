package com.example.cameraxapp.screens

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.cameraxapp.camera.CaptureImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

//This composable wraps all the content

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoilApi::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val emptyImageUri = Uri.parse("file://dev/null")
    var imageUri by remember { mutableStateOf(emptyImageUri) }
    if (imageUri != emptyImageUri) {
        Box(
            modifier = modifier.background(color = Color.Black),
        ) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                elevation = 0.dp,
                backgroundColor = Color.DarkGray
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings icon",
                    tint = Color.LightGray,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.width(110.dp))
                Text(
                    text = "CAMERAX",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    ),
                )
                Spacer(modifier = Modifier.width(100.dp))
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Flash Icon",
                    tint = Color.LightGray,
                    modifier = Modifier.clickable {

                    }
                )
            }
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberImagePainter(imageUri),
                contentDescription = "Captured image"
            )
            Button(
                modifier = Modifier.align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.LightGray
                ),
                onClick = {
                    imageUri = emptyImageUri
                }
            ) {
                Text("Retry!")
            }
        }
    } else {
        CaptureImage(
            modifier = modifier,
            onImageFile = { file ->
                imageUri = file.toUri()
            },
        )
    }
}