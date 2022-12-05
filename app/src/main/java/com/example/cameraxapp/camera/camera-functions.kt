package com.example.cameraxapp.camera

import android.content.Context
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//This file contains all the functions required for the working of the camerax app

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener(
            {
                continuation.resume(future.get())
            },
            executor
        )
    }
}

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)

suspend fun ImageCapture.takePicture(executor: Executor): File {
    val photoFile = withContext(Dispatchers.IO) {
        kotlin.runCatching {
            File.createTempFile("image", "jpg")
        }.getOrElse { ex ->
            Log.e("TakePicture", "Failed to create temporary file", ex)
            File("/dev/null")
        }
    }

    return suspendCoroutine { continuation ->
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        takePicture(
            outputOptions, executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    continuation.resume(photoFile)
                }

                override fun onError(ex: ImageCaptureException) {
                    Log.e("TakePicture", "Image capture failed", ex)
                    continuation.resumeWithException(ex)
                }
            }
        )
    }
}

suspend fun flash(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    cameraSelector: CameraSelector,
    previewViewUseCase: UseCase,
) {
    val camera = context.getCameraProvider()
        .bindToLifecycle(lifecycleOwner, cameraSelector, previewViewUseCase)

    val cameraControl = camera.cameraControl
    val cameraInfo = camera.cameraInfo

    if(cameraInfo.hasFlashUnit()){
        cameraControl.enableTorch(true)
    }
    else{
        cameraControl.enableTorch(false)
    }
}

suspend fun flipCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    cameraSelector: CameraSelector,
    previewViewUseCase: UseCase,
){
    val camera = context.getCameraProvider().bindToLifecycle(lifecycleOwner, cameraSelector, previewViewUseCase)

    val cameraControl = camera.cameraControl
    if(camera.cameraInfo.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA){
        CameraSelector.DEFAULT_FRONT_CAMERA
    }
    else{
        CameraSelector.DEFAULT_BACK_CAMERA
    }
}