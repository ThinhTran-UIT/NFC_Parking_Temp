package com.example.nfc_parking1_project.kotlin


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.nfc_parking1_project.R
import com.example.nfc_parking1_project.api.HistoryAPI
import com.example.nfc_parking1_project.api.MessageResponse
import com.example.nfc_parking1_project.databinding.ActivityscanBinding
import com.example.nfc_parking1_project.helper.ConvertTextLicense
import com.example.nfc_parking1_project.helper.ObjectDetectorHelper
import com.example.nfc_parking1_project.model.History
import com.google.common.util.concurrent.ListenableFuture
import org.tensorflow.lite.task.vision.detector.Detection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ScanActivityKotlin : AppCompatActivity(), ObjectDetectorHelper.DetectorListener {

    private lateinit var binding: ActivityscanBinding;
    private lateinit var objectDetectorHelper: ObjectDetectorHelper;
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>;
    private lateinit var bitmapBuffer: Bitmap
    private var imageAnalyzer: ImageAnalysis? = null
    private var preview: Preview? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var token:String=""
    private var cardId:String=""
    val regex = "\\d{2}-\\w\\d-\\d{4,5}".toRegex()
    private lateinit var cameraExecutor: ExecutorService

    private val TAG = "ObjectDetection"
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        val bundle = intent.extras
        try{
            token = bundle?.getString("token").toString()
            cardId = bundle?.getString("cardId").toString()
        }catch (e:Exception){
            Log.d(TAG, e.message.toString());
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activityscan);
        binding.tvCardId.text = cardId;
        objectDetectorHelper = ObjectDetectorHelper(
            context = this@ScanActivityKotlin,
            objectDetectorListener = this
        )
        ConvertTextLicense.init();
        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Wait for the views to be properly laid out
        binding.cameraPreview.post {
            // Set up the camera and its use cases
            setUpCamera()
        }

        binding.btnRescan.setOnClickListener {
            ConvertTextLicense.destroyMapping();
            binding.tvNotifyScan.text = "";
            binding.tvPlateIdResult.text = "";
        }
        binding.btnExit.setOnClickListener{
            finish();
        }

        binding.btnConfirm.setOnClickListener{
           confirmCheckout();
        }
    }


    private fun confirmCheckout() {
        val cardId = binding.tvCardId.text.toString()
        val licensPlate = binding.tvPlateIdResult.text.toString()
        val history = History(licensPlate,cardId);
        Log.d(TAG,history.toString())
        if(history.licenseNumber.isEmpty()){
            binding.tvNotifyScan.text = "License number not found!"
        }else{
            Log.d(TAG,token);
        }
        try{
            HistoryAPI.historyApi.createHistory(token, history)
                .enqueue(object : Callback<MessageResponse> {
                    override fun onResponse(
                        call: Call<MessageResponse>,
                        response: Response<MessageResponse>
                    ) {
                        try {
                            if (response.code() == 200) {
                                val messageResponse = response.body()
                                if (messageResponse.success) {
                                    Toast.makeText(baseContext, messageResponse.message, Toast.LENGTH_SHORT)
                                        .show()
                                    finish()
                                } else {
                                    Toast.makeText(baseContext, messageResponse.message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            } else {
                                Log.d(TAG, response.message())
                                Toast.makeText(baseContext,"Server Error!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } catch (e: java.lang.Exception) {
                            Log.d(TAG, e.message!!)
                        }
                    }

                    override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                        Log.d(TAG, t.message!!)
                    }
                })
        }catch (e:Exception)
        {
            Log.d(TAG, e.message!!)
        }


    }



    override fun onResume() {
        super.onResume()
        binding.tvPlateIdResult.text = "";

        ConvertTextLicense.destroyMapping();
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this@ScanActivityKotlin)
        cameraProviderFuture.addListener(
            {
                // CameraProvider
                cameraProvider = cameraProviderFuture.get()

                // Build and bind the camera use cases
                bindCameraUseCases()
            },
            ContextCompat.getMainExecutor(this@ScanActivityKotlin)
        )
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCameraUseCases() {

        // CameraProvider
        val cameraProvider =
            cameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector - makes assumption that we're only using the back camera
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        // Preview. Only using the 4:3 ratio because this is the closest to our models
        preview =
            Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(binding.cameraPreview.display.rotation)
                .build()

        // ImageAnalysis. Using RGBA 8888 to match how our models work
        imageAnalyzer =
            ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(binding.cameraPreview.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(cameraExecutor) { image ->
                        if (!::bitmapBuffer.isInitialized) {
                            // The image rotation and RGB image buffer are initialized only once
                            // the analyzer has started running
                            bitmapBuffer = Bitmap.createBitmap(
                                image.width,
                                image.height,
                                Bitmap.Config.ARGB_8888
                            )
                        }
                        detectObjects(image)
                        image.close();
                    }
                }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun detectObjects(image: ImageProxy) {
        // Copy out RGB bits to the shared bitmap buffer
        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }

        val imageRotation = image.imageInfo.rotationDegrees
        // Pass Bitmap and rotation to the object detector helper for processing and detection
        objectDetectorHelper.detect(bitmapBuffer, imageRotation, image)
    }

    override fun onResults(
        results: MutableList<Detection>?,
        inferenceTime: Long,
        imageHeight: Int,
        imageWidth: Int
    ) {
        this@ScanActivityKotlin?.runOnUiThread {

            // Pass necessary information to OverlayView for drawing on the canvas
            binding.overlay.setResults(
                results ?: LinkedList<Detection>(),
                imageHeight,
                imageWidth
            )
            val textLicense = ConvertTextLicense.getTextRecognize()
            if(textLicense!=null && ConvertTextLicense.isDetected)
            {
                binding.tvPlateIdResult.text = textLicense;
                Log.d("Text License", textLicense);
            }
            else if(ConvertTextLicense.isDetected && textLicense==null)
            {
                binding.tvNotifyScan.text = "Can't recognize. Please re-scan!"
            }



            // Force a redraw
            binding.overlay.invalidate()
        }
    }


    override fun onError(error: String) {
        this@ScanActivityKotlin?.runOnUiThread {
            Toast.makeText(this@ScanActivityKotlin, error, Toast.LENGTH_SHORT).show()
        }
    }


}