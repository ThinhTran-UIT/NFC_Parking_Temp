package com.example.nfc_parking1_project.helper;

import static com.example.nfc_parking1_project.R.color.green;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.nfc_parking1_project.R;

import org.tensorflow.lite.task.vision.detector.Detection;

import java.util.LinkedList;
import java.util.List;


public class OverlayView extends View {

    private List<Detection> result = new LinkedList<Detection>();
    private Paint boxPaint = new Paint();
    private Paint textBackgroundPaint = new Paint();
    private Paint textPaint =  new Paint();
    private static final String LICENSE = "License";
    private Float scaleFactor = 1f;

    @SuppressLint("ResourceAsColor")
    private void initPaints() {
        textBackgroundPaint.setColor(Color.BLACK);
        textBackgroundPaint.setStyle(Paint.Style.FILL);
        textBackgroundPaint.setTextSize(50f);

        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(50f);

        boxPaint.setColor(green);
        boxPaint.setStrokeWidth(8F);
        boxPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (Detection d:result) {
            RectF boundingBox = d.getBoundingBox();
            float top = boundingBox.top * scaleFactor;
            float bottom = boundingBox.bottom * scaleFactor;
            float left = boundingBox.left * scaleFactor;
            float right = boundingBox.right * scaleFactor;

            // Draw bounding box around detected objects
            RectF drawableRect = new RectF(left, top, right, bottom);
            canvas.drawRect(drawableRect, boxPaint);

            // Create text to display alongside detected objects
//            val drawableText =
//                    result.categories[0].label + " " +
//                            String.format("%.2f", result.categories[0].score)
//
//            // Draw rect behind display text
//            textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)
//            val textWidth = bounds.width()
//            val textHeight = bounds.height()
//            canvas.drawRect(
//                    left,
//                    top,
//                    left + textWidth + Companion.BOUNDING_RECT_TEXT_PADDING,
//                    top + textHeight + Companion.BOUNDING_RECT_TEXT_PADDING,
//                    textBackgroundPaint
//            )
//
//            // Draw text for detected object
//            canvas.drawText(drawableText, left, top + bounds.height(), textPaint)
        }
    }

    private Rect bounds = new Rect();
    public OverlayView(Context context) {
        super(context);
    }

    public OverlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OverlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
