package com.example.nfc_parking1_project.helper;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.vision.detector.Detection;

import java.util.List;

public class Helper {
    private static final String TAG = "Helper";
    public static Bitmap bitmapFromRgba(int width, int height, byte[] bytes) {
        int[] pixels = new int[bytes.length / 4];
        int j = 0;

        for (int i = 0; i < pixels.length; i++) {
            int R = bytes[j++] & 0xff;
            int G = bytes[j++] & 0xff;
            int B = bytes[j++] & 0xff;
            int A = bytes[j++] & 0xff;

            int pixel = (A << 24) | (R << 16) | (G << 8) | B;
            pixels[i] = pixel;
        }


        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    public static float debugPrint(List<Detection> results, TensorImage tensorImage)
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
        Log.d(TAG," TensorImage:"+ tensorImage.getWidth() +"x" + tensorImage.getHeight());
        return score;
    }
}
