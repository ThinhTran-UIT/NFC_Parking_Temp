/*
 * Copyright 2022 The TensorFlow Authors.  All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.nfc_parking1_project.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.core.graphics.BitmapCompat
import androidx.core.graphics.toRect
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import org.tensorflow.lite.DataType
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.support.image.ops.TransformToGrayscaleOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class ObjectDetectorHelper(
    var threshold: Float = 0.8f,
    var numThreads: Int = 1,
    var maxResults: Int = 1,
    var currentDelegate: Int = 0,
    var currentModel: Int = 0,

    val context: Context,
    val objectDetectorListener: DetectorListener?
) {

    // For this example this needs to be a var so it can be reset on changes. If the ObjectDetector
    // will not change, a lazy val would be preferable.
    private var objectDetector: ObjectDetector? = null
    private var convertTextLicense:ConvertTextLicense?=null
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    init {
        setupObjectDetector()
        convertTextLicense = ConvertTextLicense();
    }

    fun clearObjectDetector() {
        objectDetector = null
    }

    // Initialize the object detector using current settings on the
    // thread that is using it. CPU and NNAPI delegates can be used with detectors
    // that are created on the main thread and used on a background thread, but
    // the GPU delegate needs to be used on the thread that initialized the detector
    fun setupObjectDetector() {
        // Create the base options for the detector using specifies max results and score threshold
        val optionsBuilder =
            ObjectDetector.ObjectDetectorOptions.builder()
                .setScoreThreshold(threshold)
                .setMaxResults(maxResults)

        // Set general detection options, including number of used threads
        val baseOptionsBuilder = BaseOptions.builder().setNumThreads(numThreads)

        // Use the specified hardware for running the model. Default to CPU
        when (currentDelegate) {
            DELEGATE_CPU -> {
                // Default
            }
            DELEGATE_GPU -> {
                if (CompatibilityList().isDelegateSupportedOnThisDevice) {
                    baseOptionsBuilder.useGpu()
                } else {
                    objectDetectorListener?.onError("GPU is not supported on this device")
                }
            }
            DELEGATE_NNAPI -> {
                baseOptionsBuilder.useNnapi()
            }
        }

        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

//        val modelName = "newmodel.tflite"
////            when (currentModel) {
////                MODEL_MOBILENETV1 -> "mobilenetv1.tflite"
////                MODEL_EFFICIENTDETV0 -> "efficientdet-lite0.tflite"
////                MODEL_EFFICIENTDETV1 -> "efficientdet-lite1.tflite"
////                MODEL_EFFICIENTDETV2 -> "efficientdet-lite2.tflite"
////                else -> "mobilenetv1.tflite"
////            }

        try {
            objectDetector =
                ObjectDetector.createFromFileAndOptions(
                    context,
                    "newmodel.tflite",
                    optionsBuilder.build()
                )
        } catch (e: IllegalStateException) {
            objectDetectorListener?.onError(
                "Object detector failed to initialize. See error logs for details"
            )
            Log.e("Test", "TFLite failed to load model with error: " + e.message)
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun detect(image: Bitmap, imageRotation: Int, imageProxy:ImageProxy) {
        if (objectDetector == null) {
            setupObjectDetector()
        }

        // Inference time is the difference between the system time at the start and finish of the
        // process
        var inferenceTime = SystemClock.uptimeMillis()

        // Create preprocessor for the image.
        // See https://www.tensorflow.org/lite/inference_with_metadata/
        //            lite_support#imageprocessor_architecture
        val imageProcessor =
            ImageProcessor.Builder()
                .add(Rot90Op(-imageRotation / 90))
                .build()

        // Preprocess the image and convert it into a TensorImage for detection.
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(image))

        val results = objectDetector?.detect(tensorImage)
        if (results!!.size > 0) {
            val result = results[0];
            val boundingBox = result.boundingBox
            val boundingBoxInt = boundingBox.toRect()

            Log.d("Helper","Image width"+ tensorImage.width)
            Log.d("Helper","Image height"+ tensorImage.height)
            Log.d("Helper","Bounding Box"+ boundingBox.width())
            Log.d("Helper","Bounding Box height"+ boundingBox.height())
            Log.d("Helper","Bounding Box X"+ boundingBox.left)
            Log.d("Helper","Bounding Box XINt"+ boundingBoxInt.left)
            Log.d("Helper","Bounding Box Y"+ boundingBox.top)

            if(boundingBox.left >0 && boundingBox.top > 0)
            {
                if(boundingBoxInt.left+boundingBoxInt.width()<tensorImage.bitmap.width && boundingBoxInt.height()<tensorImage.bitmap.height)
                {
                    val imageBitmap = Bitmap.createBitmap(tensorImage.bitmap,boundingBox.left.toInt(),boundingBox.top.toInt(),boundingBoxInt.width(),boundingBoxInt.height())
                    val bitmap = ConvertTextLicense.toGrayscale(imageBitmap);
                    if(convertTextLicense!!.numberLicenseDetected <50)
                    {
                        val resultText = recognizer.process(bitmap, 0)
                            .addOnSuccessListener { text ->
                                val textRecognition = text.text;
                                convertTextLicense?.convertText(textRecognition.toString());
                                val textReplaced = textRecognition.toString()
                                Log.d("Text Record",textReplaced)
                            }
                    }
                }
            }

        }
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime
        objectDetectorListener?.onResults(
            results,
            inferenceTime,
            tensorImage.height,
            tensorImage.width
        )
    }

    interface DetectorListener {
        fun onError(error: String)
        fun onResults(
            results: MutableList<Detection>?,
            inferenceTime: Long,
            imageHeight: Int,
            imageWidth: Int
        )
    }

    fun bitmapToTensorImageForRecognition(
        bitmapIn: Bitmap,
        width: Int,
        height: Int,
        mean: Float,
        std: Float
    ): TensorImage {
        val imageProcessor =
            ImageProcessor.Builder()
                .add(ResizeOp(height, width, ResizeOp.ResizeMethod.BILINEAR))
                .add(TransformToGrayscaleOp())
                .add(NormalizeOp(mean, std))
                .build()
        var tensorImage = TensorImage(DataType.FLOAT32)

        tensorImage.load(bitmapIn)
        tensorImage = imageProcessor.process(tensorImage)

        return tensorImage
    }




    companion object {
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1
        const val DELEGATE_NNAPI = 2
        const val MODEL_MOBILENETV1 = 0
        const val MODEL_EFFICIENTDETV0 = 1
        const val MODEL_EFFICIENTDETV1 = 2
        const val MODEL_EFFICIENTDETV2 = 3
    }
}
