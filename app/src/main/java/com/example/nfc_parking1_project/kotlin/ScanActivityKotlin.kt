package com.example.nfc_parking1_project.kotlin


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.nfc_parking1_project.R
import com.example.nfc_parking1_project.databinding.ActivityscanBinding
import com.example.nfc_parking1_project.helper.Helper
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import org.tensorflow.lite.Tensor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.ObjectDetector


class ScanActivityKotlin:AppCompatActivity(){
    private lateinit var binding:ActivityscanBinding;
    private lateinit var objectDetector: ObjectDetector;
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>;
    private lateinit var bitmapBuffer: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activityscan);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider=cameraProvider)
        },ContextCompat.getMainExecutor(this));

        val options = ObjectDetector.ObjectDetectorOptions
            .builder()
            .setMaxResults(1)
            .setScoreThreshold(0.5f)
            .build();
        objectDetector = ObjectDetector.createFromFileAndOptions(this,"newmodel.tflite",options);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindPreview(cameraProvider: ProcessCameraProvider){
        val preview = Preview.Builder().build();
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)


        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280,720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->

            bitmapBuffer = Bitmap.createBitmap(
                imageProxy.width,
                imageProxy.height,
                Bitmap.Config.ARGB_8888
            )
            imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer) }
            if (bitmapBuffer != null) {
                val preview = findViewById<PreviewView>(R.id.camera_preview);
                val bitmap = preview.bitmap;
                val tensorImage = TensorImage.fromBitmap(bitmap);
                val results = objectDetector.detect(tensorImage);
                Log.d("Result",results.size.toString())
                if(results.size > 0)
                {
                    if(binding.parentLayout.childCount>1) binding.parentLayout.removeViewAt(1);
                    val result = results[0]
                    val element = Draw(this,rect = result.boundingBox,"License")
                    binding.parentLayout.addView(element);
                    imageProxy.close();
                }
                else
                {
                    imageProxy.close();
                }
                Helper.debugPrint(results);

            }

        }
        cameraProvider.bindToLifecycle(this,cameraSelector,imageAnalysis,preview)
    }

}