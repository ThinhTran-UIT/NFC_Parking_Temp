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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.nfc_parking1_project.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;

import java.io.IOException;
import java.util.List;

public class ScanActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    public Button buttonExit;
    public Button buttonConfirm;

    private static final String TAG="MainActivity";
    private SurfaceHolder surfaceHolder;
    private Mat mRgba;
    private Mat mGray;
    private CameraBridgeViewBase mOpenCvCameraView;
    private static final String LICENSE = "License";
    private BaseLoaderCallback mLoaderCallback =new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface
                        .SUCCESS:{
                    Log.i(TAG,"OpenCv Is loaded");
                    mOpenCvCameraView.enableView();
                }
                default:
                {
                    super.onManagerConnected(status);

                }
                break;
            }
        }
    };

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

        setContentView(R.layout.activity_scan);

        mOpenCvCameraView=(CameraBridgeViewBase) findViewById(R.id.camera_preview);
        surfaceHolder = mOpenCvCameraView.getHolder();
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()){
            //if load success
            Log.d(TAG,"Opencv initialization is done");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else{
            //if not loaded
            Log.d(TAG,"Opencv is not loaded. try again");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0,this,mLoaderCallback);
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView !=null){
            mOpenCvCameraView.disableView();
        }
    }

    public void onDestroy(){
        super.onDestroy();
        if(mOpenCvCameraView !=null){
            mOpenCvCameraView.disableView();
        }

    }

    public void onCameraViewStarted(int width ,int height){
        mRgba=new Mat(height,width, CvType.CV_8UC4);
        mGray =new Mat(height,width,CvType.CV_8UC1);
    }
    public void onCameraViewStopped(){
        mRgba.release();
    }
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        mRgba=inputFrame.rgba();
        mGray=inputFrame.gray();

        Mat result = new Mat();
        try {
            result = detection(mRgba);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }


    public Mat detection(Mat inputMat) throws IOException {
        ObjectDetector.ObjectDetectorOptions options = ObjectDetector.ObjectDetectorOptions
                .builder()
                .setMaxResults(1)
                .setScoreThreshold(0.7f)
                .build();
        ObjectDetector detector = ObjectDetector.createFromFileAndOptions(this,"newmodel.tflite",options);
        Log.d("Load model:","Model Detected");
        Bitmap imgBitMap = null;
        imgBitMap = Bitmap.createBitmap(inputMat.width(),inputMat.height(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(inputMat,imgBitMap);
        TensorImage imgToDetect = TensorImage.fromBitmap(imgBitMap);
        List<Detection> result = detector.detect(imgToDetect);
        Log.d("Result Length:",String.valueOf(result.size()));
        debugPrint(result);
//        Bitmap resultBitmap = drawDetectionResult(imgBitMap,result);
//        Log.d("Height:",String.valueOf(resultBitmap.getHeight()));
//        Log.d("Width:",String.valueOf(resultBitmap.getWidth()));
        if(result.size()>0)
        {
            Detection detection = result.get(0);
            RectF box = detection.getBoundingBox();
            Imgproc.rectangle(inputMat,new Point(box.left,box.top),new Point(box.right,box.bottom),new Scalar(0, 255, 0, 255),2);
        }

        detector.close();
        return inputMat;
    }
    public static void debugPrint(List<Detection> results)
    {
        for (Detection d: results) {
            RectF a = d.getBoundingBox();
            Log.d(TAG,"Detected object:");
            Log.d(TAG," bouding box:"+ "( "+a.left+ ","+ a.top+"),("+a.right+","+a.bottom+")");

            List<Category> categories = d.getCategories();
            for (Category c:categories) {
                Log.d(TAG,"Detect Label "+c.getLabel());
                Log.d(TAG,"Detect Score "+c.getScore());
            }
        }
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