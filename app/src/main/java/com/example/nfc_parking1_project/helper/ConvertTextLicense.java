package com.example.nfc_parking1_project.helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.CountDownTimer;

import java.util.HashMap;
import java.util.Map;


public class ConvertTextLicense {
    public ConvertTextLicense(){};
    public static boolean isDetected = false;
    public static String result = null;
    public static Map<String, Integer> licenseMapping = new HashMap<>();
    public static boolean isTimerRunning = false;


    public static void init()
    {
        isDetected=false;
    }

    public static void destroyMapping(){
        licenseMapping.clear();
        isDetected=false;
        isTimerRunning=false;
        result=null;
    }

    public static void setResult()
    {
        result = getMaxEntryInMapBasedOnValue(licenseMapping).getKey().toString();
        isDetected=true;
    }


    public static  String getTextRecognize()
    {
        if(isDetected)
        {
            return result;
        }
        return null;

    }

    //Create count down time for detection




    public void convertText(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int asci = (int) text.charAt(i);
            if ((asci > 47 && asci < 58) || (asci > 64 && asci < 91) ) {
                if (result.length() == 2) {
                    if (asci > 64 && asci < 91 && asci != 87) {
                        result.append(text.charAt(i));
                        continue;
                    } else
                        break;
                }
                if (result.length() == 4) {
                    result.append("-");
                }
                if (result.length() >= 5) {
                    if (asci > 64 && asci < 91) {
                        break;
                    }
                }
                result.append(text.charAt(i));
            }
        }
        String key = result.toString();

        if (key.length()>=8) {
            if (licenseMapping.containsKey(key)) {
                try{
                    licenseMapping.put(key, licenseMapping.get(key) + 1);
                }catch (Exception e)
                {

                }

            } else {
                licenseMapping.put(key, 1);
            }

        }
    }

    //Find the license with higest indentical value
    // Find the entry with highest value
    public static <K, V extends Comparable<V>>
    Map.Entry<K, V>
    getMaxEntryInMapBasedOnValue(Map<K, V> map) {

        // To store the result
        Map.Entry<K, V> entryWithMaxValue = null;

        // Iterate in the map to find the required entry
        for (Map.Entry<K, V> currentEntry :
                map.entrySet()) {

            if (
                // If this is the first entry, set result as
                // this
                    entryWithMaxValue == null

                            // If this entry's value is more than the
                            // max value Set this entry as the max
                            || currentEntry.getValue().compareTo(
                            entryWithMaxValue.getValue())
                            > 0) {

                entryWithMaxValue = currentEntry;
            }
        }
        // Return the entry with highest value
        return entryWithMaxValue;
    }


    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();/*www.ja v  a  2s  . c  om*/
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    private Bitmap adjustedContrast(Bitmap src, double value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap

        // create a mutable empty bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        // create a canvas so that we can draw the bmOut Bitmap from source bitmap
        Canvas c = new Canvas();
        c.setBitmap(bmOut);

        // draw bitmap to bmOut from src bitmap so we can modify it
        c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));


        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }

                G = Color.green(pixel);
                G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }

                B = Color.blue(pixel);
                B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

}
