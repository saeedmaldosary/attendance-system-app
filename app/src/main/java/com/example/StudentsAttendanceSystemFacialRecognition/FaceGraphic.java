package com.example.StudentsAttendanceSystemFacialRecognition;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.StudentsAttendanceSystemFacialRecognition.components.GraphicOverlay;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;

public class FaceGraphic extends GraphicOverlay.Graphic {
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -100.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private final Paint facePaintPosition;
    private final Paint paintID;
    private final Paint paintBox;

    private volatile FirebaseVisionFace visionFaceFirebase;
    private String res;

    public FaceGraphic(GraphicOverlay overlay, FirebaseVisionFace face, String res) {
        super(overlay);
        this.res = res;
        visionFaceFirebase = face;
        final int selectedColor = Color.GREEN;

        facePaintPosition = new Paint();
        facePaintPosition.setColor(selectedColor);

        paintID = new Paint();
        paintID.setColor(selectedColor);
        paintID.setTextSize(ID_TEXT_SIZE);

        paintBox = new Paint();
        paintBox.setColor(selectedColor);
        paintBox.setStyle(Paint.Style.STROKE);
        paintBox.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    @Override
    public void draw(Canvas canvas) {
        FirebaseVisionFace face = visionFaceFirebase;
        if (face == null) {
            return;
        }

        float x = translateX(face.getBoundingBox().centerX());
        float y = translateY(face.getBoundingBox().centerY());
        if(res != null)
            canvas.drawText("id: " + res, x + ID_X_OFFSET, y - 3 * ID_Y_OFFSET, paintID);

        // Draws a bounding box around the face
        float xOffset = scaleX(face.getBoundingBox().width() / 2.0f);
        float yOffset = scaleY(face.getBoundingBox().height() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;
        canvas.drawRect(left, top, right, bottom, paintBox);
    }

    public Rect boundingBox() {
        FirebaseVisionFace face = visionFaceFirebase;
        if (face == null) {
            return null;
        }

        float x = translateX(face.getBoundingBox().centerX());
        float y = translateY(face.getBoundingBox().centerY());

        // Bounding box around the face
        float offsetX = scaleX(face.getBoundingBox().width() / 2.0f);
        float offsetY = scaleY(face.getBoundingBox().height() / 2.0f);
        float left = x - offsetX;
        float top = y - offsetY;
        float right = x + offsetX;
        float bottom = y + offsetY;
        return new Rect((int) left, (int) top, (int) right, (int) bottom);
    }
}
