package com.example.cameraxapp.camera

import android.util.Log
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

//This function renders the camera preview and captures  the image

@ExperimentalCoroutinesApi
@Composable
fun CaptureImage(
    modifier: Modifier = Modifier,
    onImageFile: (File) -> Unit = { },
) {
    val context = LocalContext.current
        Box(modifier = modifier) {
            val lifecycleOwner = LocalLifecycleOwner.current
            val coroutineScope = rememberCoroutineScope()
            var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
            val imageCaptureUseCase by remember {
                mutableStateOf(
                    ImageCapture
                        .Builder()
                        .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .build()

                )
            }
            val cameraSelector: MutableState<CameraSelector> = remember {
                mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA)
            }
            Box {
                ImagePreview(
                    modifier = Modifier.fillMaxSize(),
                    onUseCase = {
                        previewUseCase = it
                    }
                )
                Column () {
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
                                    //ToDo: Add  flash toggling logic
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(510.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(80.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(40.dp)
                                    .background(Color.DarkGray)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = "Change Camera",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clickable {
                                            //ToDo: Add correct camera flip logic, this doesnt work
                                            coroutineScope.launch {
                                                if(cameraSelector.value == CameraSelector.DEFAULT_BACK_CAMERA){
                                                    cameraSelector.value = CameraSelector.DEFAULT_FRONT_CAMERA
                                                }
                                                else{
                                                    cameraSelector.value = CameraSelector.DEFAULT_BACK_CAMERA
                                                }
                                            }
                                        }
                                )
                            }
                            Spacer(modifier = Modifier.width(30.dp))
                            FloatingActionButton(
                                onClick = {
                                    coroutineScope.launch {
                                        imageCaptureUseCase.takePicture(context.executor).let {
                                            onImageFile(it)
                                        }
                                    }
                                },
                                backgroundColor = Color.White
                            ) {

                            }
                        }
                    }
            }
            LaunchedEffect(previewUseCase) {
                val cameraProvider = context.getCameraProvider()
                try {
                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector.value, previewUseCase, imageCaptureUseCase
                    )
                } catch (ex: Exception) {
                    Log.e("CameraCapture", "Failed to bind camera use cases", ex)
                }
            }
        }
}
