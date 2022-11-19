package com.example.nfc_parking1_project.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.example.nfc_parking1_project.R;
import com.example.nfc_parking1_project.helper.OverlayView;
import com.google.common.util.concurrent.ListenableFuture;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public abstract class ScanActivity extends AppCompatActivity implements ImageAnalysis.Analyzer{

    private ListenableFuture <ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    public Button buttonExit;
    public Button buttonConfirm;
    private OverlayView overlayView;
    private static final String TAG="MainActivity";
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private static final String LICENSE = "License";




    public ScanActivity(){
        Log.i(TAG,"Instantiated new "+this.getClass());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int MY_PERMISSIONS_REQUEST_CAMERA=0;
        // if camera permission is not given it will ask for it on device
        if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(ScanActivity.this, new String[] {Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }

        setContentView(R.layout.activityscan);
        previewView = findViewById(R.id.camera_preview);
        overlayView = new OverlayView(this);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());

        //Start button Exit
        buttonExit = (Button) findViewById(R.id.btn_exit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Start button Confirm
        buttonConfirm = (Button) findViewById(R.id.btn_confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }


    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(getExecutor(), this::analyze);

        //bind to lifecycle:
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview,imageAnalysis);
    }



    @Override
    public void analyze(@NonNull ImageProxy image) {
        Log.d("TAG", "analyze: got the frame at: " + image.getImageInfo().getTimestamp());
        final Bitmap bitmap = previewView.getBitmap();
        bitmap.setConfig(Bitmap.Config.ARGB_8888);
        try {
            detection(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.close();
    }



    public void detection(Bitmap imgBitMap) throws IOException {

        ObjectDetector.ObjectDetectorOptions options = ObjectDetector.ObjectDetectorOptions
                .builder()
                .setMaxResults(1)
                .setScoreThreshold(0.5f)
                .build();
        ObjectDetector detector = ObjectDetector.createFromFileAndOptions(this,"newmodel.tflite",options);

        Log.d("Load model:","Model Detected");
//        Bitmap imgBitMap = null;
//        imgBitMap = Bitmap.createBitmap(inputMat.width(),inputMat.height(),Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(inputMat,imgBitMap);
        TensorImage imgToDetect = TensorImage.fromBitmap(imgBitMap);
        List<Detection> result = detector.detect(imgToDetect);
        Log.d("Result Length:",String.valueOf(result.size()));
        float score = debugPrint(result);
//
//        Log.d("Height:",String.valueOf(resultBitmap.getHeight()));
//        Log.d("Width:",String.valueOf(resultBitmap.getWidth()));
        if(result.size()>0)
        {
            Detection detection = result.get(0);
            RectF box = detection.getBoundingBox();
//            Imgproc.rectangle(inputMat,new Point(box.left,box.top),new Point(box.right,box.bottom),new Scalar(0, 255, 0, 255),2);
//            Imgproc.putText(inputMat,String.valueOf(score),new Point(box.left,box.top),3,1,new Scalar(255, 0, 0, 255),2);
        }

        detector.close();
//        return inputMat;
    }
    public static float debugPrint(List<Detection> results)
    {
        float score =0;
        for (Detection d: results) {
            RectF a = d.getBoundingBox();
            Log.d(TAG,"Detected object:");
            Log.d(TAG," bouding box:"+ "( "+a.left+ ","+ a.top+"),("+a.right+","+a.bottom+")");

            List<Category> categories = d.getCategories();
            for (Category c:categories) {
                Log.d(TAG,"Detect Label "+c.getLabel());
                Log.d(TAG,"Detect Score "+c.getScore());
                score = c.getScore();

            }
        }
        return score;
    }
    public static float maximum(float[] array) {
        if (array.length <= 0)
            throw new IllegalArgumentException("The array is empty");
        float max = array[0];
        for (int i = 1; i < array.length; i++)
            if (array[i] > max)
                max = array[i];
        return max;
    }
    public  Bitmap drawDetectionResult(Bitmap bitmap,List<Detection> result)
    {

        Bitmap outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(outputBitmap);
        canvas = surfaceHolder.lockCanvas();
        Paint pen = new Paint();
        pen.setTextAlign(Paint.Align.LEFT);

        for (Detection d:result) {
            pen.setColor(Color.RED);
            pen.setStrokeWidth(8F);
            pen.setStyle(Paint.Style.STROKE);
            RectF box = d.getBoundingBox();
            canvas.drawRect(box,pen);
            Rect tagSize = new Rect(0,0,0,0);
            pen.setStyle(Paint.Style.FILL_AND_STROKE);
            pen.setColor(Color.YELLOW);
            pen.setStrokeWidth(2F);
            pen.setStrokeWidth(2F);
            pen.setTextSize(96F);
            pen.getTextBounds(LICENSE,0,LICENSE.length(),tagSize);
            Float fontSize = pen.getTextSize()* box.width()/tagSize.width();
            if(fontSize<pen.getTextSize())
            {
                pen.setTextSize(fontSize);
            }
            float margin = (box.width()-tagSize.width())/2.0F;
            if(margin<0F)
            {
                margin = 0F;
            }
            canvas.drawText(
                    LICENSE,box.left+margin,box.top+tagSize.height()*1F,pen);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
        return outputBitmap;
    }

}