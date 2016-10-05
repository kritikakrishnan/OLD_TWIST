package com.kritikagopalakrishnan.myindividualapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class EditPic extends Activity implements RotationGestureDetector.OnRotationGestureListener {
    public static Bitmap bitmap;
    //DrawView draw1 =null;
    DrawView draw;
    //DrawView drawrotate;
    public static int a = 1;
    private MediaPlayer clapmusic = new MediaPlayer();
    private int length = 0;
    public Boolean fartonear = true;
    private RotationGestureDetector mRotationDetector;
    public GestureDetector gestureDetector;
    Bitmap bitmaptorotate;
    int nomorerotation = 0;
    boolean downloading =false;
    public static Bitmap b;
    String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String filename = getIntent().getStringExtra("BitmapImage");
        try {
            FileInputStream is = this.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        draw = new DrawView(getApplicationContext());
        draw.setBackgroundColor(Color.WHITE);
        draw.setDrawingCacheEnabled(true);
        Bitmap original = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.frame);
        b = Bitmap.createScaledBitmap(original, bitmap.getWidth() + 170, bitmap.getHeight() + 170, false);
        setContentView(draw);

        mRotationDetector = new RotationGestureDetector(this);
        draw.setOnTouchListener(new OnSwipeTouchListener(EditPic.this) {
            @Override
            public void onSwipeLeft() {

                nomorerotation = 1;
                SensorManager mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                Sensor ProximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                if (ProximitySensor != null) {
                    System.out.println("Sensor.TYPE_PROXIMITY Available");
                    mySensorManager.registerListener(
                            ProximitySensorListener,
                            ProximitySensor,
                            SensorManager.SENSOR_DELAY_NORMAL);

                } else {
                    System.out.println("Sensor.TYPE_PROXIMITY NOT Available");
                }
            }
            public void onSwipeRight() {
                try {
                    downloading = true;
                    setContentView(draw);
                    MediaStore.Images.Media.insertImage(getContentResolver(), draw.getDrawingCache(), "oldtwist.jpg", null);
                    Intent finishedintent = new Intent(getApplicationContext(), WelcomePage.class);
                    startActivity(finishedintent);
                } catch (Exception e) {

                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mRotationDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void OnRotation(RotationGestureDetector rotationDetector) {
        float angle = rotationDetector.getAngle();


        draw.RotateBitmap(bitmap, angle);
        setContentView(draw);

    }


    public void download(View v)
    {
        try {
            String filename = "editor";
            File sd = Environment.getExternalStorageDirectory();
            File dest = new File(sd, filename);
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final SensorEventListener ProximitySensorListener
            = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                System.out.println("******* Proximity Event" + event.values[0]);
                if (event.values[0] <4) {
                    //near
                    if(fartonear==true) {

                        System.out.println("in near \n");
                        draw.Bitmapedit(a, true);
                        android.view.ViewGroup.LayoutParams lp = new android.view.ViewGroup.LayoutParams(700, 700);
                        setContentView(draw);
                        fartonear=false;
                    }
                } else {
                    //far
                    fartonear=true;

                }

            }
        }

    };

    public class DrawView extends View
    {
        Paint paint;
        private int mWidth;
        private int mHeight;
        Bitmap bitmappu = null;
        boolean filterdraw;
        Matrix matrix = new Matrix();
        private float mangle=2;
        boolean firsttime = true;
        Bitmap rotatedBitmap = null;
        Bitmap rotatedFrame = null;

        public DrawView(Context context, AttributeSet attrs)
        {
            super(context, attrs);
            this.setDrawingCacheEnabled(true);
        }
        public DrawView(Context context)
        {
            super(context);
            paint = new Paint();
            this.setDrawingCacheEnabled(true);
        }
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
            mHeight = View.MeasureSpec.getSize(heightMeasureSpec);

            setMeasuredDimension(mWidth, mHeight);
        }
        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);


            int cxnew = 0;
            int cynew = 0;
            if(rotatedBitmap!=null) {
               cxnew  = (mWidth - rotatedBitmap.getWidth()) >> 1;
                cynew = (mHeight - rotatedBitmap.getHeight()) >> 1;
            }

            this.setDrawingCacheEnabled(true);
            if(mangle%90 == 0 && nomorerotation==0&& downloading == false) {
                canvas.drawBitmap(rotatedFrame, cxnew-85, cynew-85, paint);
                canvas.drawBitmap(rotatedBitmap, cxnew, cynew, null);
            }
            else if (nomorerotation==1 && downloading == false){
                    canvas.drawBitmap(rotatedBitmap, cxnew, cynew, paint);
            }
            else if ( downloading == false)
            {
                int cx = 0;
                int cy = 0;
                    cx  = (mWidth - bitmap.getWidth()) >> 1;
                cy = (mHeight - bitmap.getHeight()) >> 1;
                canvas.drawBitmap(b, cx-85, cy-85, paint);
                canvas.drawBitmap(bitmap, cx, cy, paint);

            }
            else
            {
                canvas.drawBitmap(rotatedFrame, cxnew-85, cynew-85, null);
                canvas.drawBitmap(rotatedBitmap, cxnew, cynew, paint);
            }


        }

        public Bitmap get(){
            return this.getDrawingCache();
        }
        public void Bitmapedit( int filter, boolean filterbool) {
            filterdraw=filterbool;
            if (EditPic.a < 7) {
                EditPic.a = EditPic.a + 1;
            } else {
                EditPic.a = 1;
            }
            switch (filter) {
                case 7:
                    ColorMatrix colorMatrix0 = new ColorMatrix();
                    colorMatrix0.setSaturation(1);
                    paint.setColorFilter(new ColorMatrixColorFilter(
                            getgrayscaleColorMatrix()));
                    break;
                case 1:
                    ColorMatrix colorMatrix1 = new ColorMatrix();
                    colorMatrix1.setSaturation(1);
                    paint.setColorFilter(new ColorMatrixColorFilter(
                            getsepiaColorMatrix()));

                    break;
                case 2:
                    ColorMatrix colorMatri2 = new ColorMatrix();
                    colorMatri2.setSaturation(1);
                    paint.setColorFilter(new ColorMatrixColorFilter(
                            getbinaryColorMatrix()));
                    break;
                case 3:
                    ColorMatrix colorMatrix3 = new ColorMatrix();
                    colorMatrix3.setSaturation(1);
                    paint.setColorFilter(new ColorMatrixColorFilter(
                            getinvertColorMatrix()));
                    break;
                case 4:
                    ColorMatrix colorMatrix4 = new ColorMatrix();
                    colorMatrix4.setSaturation(1);
                    paint.setColorFilter(new ColorMatrixColorFilter(
                            getalphablueColorMatrix()));
                    break;
                case 5:
                    ColorMatrix colorMatrix5 = new ColorMatrix();
                    colorMatrix5.setSaturation(1);
                    paint.setColorFilter(new ColorMatrixColorFilter(
                            getalphapinkColorMatrix()));
                    //  canvas.drawBitmap(bitmap1, 0, 0, paint);
                    //   return bitmap1;
                    break;
                case 6:
                    ColorMatrix colorMatrix6 = new ColorMatrix();
                    colorMatrix6.setSaturation(1);
                    paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix6));
                    //  return bitmap1;
                    break;
            }


        }

        private ColorMatrix getgrayscaleColorMatrix() {
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            Toast.makeText(getApplicationContext(), "Grayscale", Toast.LENGTH_SHORT).show();
            return colorMatrix;
        }

        private ColorMatrix getsepiaColorMatrix() {
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);

            ColorMatrix colorScale = new ColorMatrix();
            colorScale.setScale(1, 1, 0.8f, 1);

            // Convert to grayscale, then apply brown color
            colorMatrix.postConcat(colorScale);
            Toast.makeText(getApplicationContext(), "Sepia", Toast.LENGTH_SHORT).show();
            return colorMatrix;
        }

        private ColorMatrix getbinaryColorMatrix() {
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);

            float m = 255f;
            float t = -255 * 128f;
            ColorMatrix threshold = new ColorMatrix(new float[]{
                    m, 0, 0, 1, t,
                    0, m, 0, 1, t,
                    0, 0, m, 1, t,
                    0, 0, 0, 1, 0
            });

            // Convert to grayscale, then scale and clamp
            colorMatrix.postConcat(threshold);
            Toast.makeText(getApplicationContext(), "Binary", Toast.LENGTH_SHORT).show();
            return colorMatrix;
        }

        private ColorMatrix getinvertColorMatrix() {
            Toast.makeText(getApplicationContext(), "Invert", Toast.LENGTH_SHORT).show();
            return new ColorMatrix(new float[]{
                    -1, 0, 0, 0, 255,
                    0, -1, 0, 0, 255,
                    0, 0, -1, 0, 255,
                    0, 0, 0, 1, 0
            });
        }

        private ColorMatrix getalphablueColorMatrix() {
            Toast.makeText(getApplicationContext(), "Alpha-Blue", Toast.LENGTH_SHORT).show();
            return new ColorMatrix(new float[]{
                    0, 0, 0, 0, 0,
                    0.3f, 0, 0, 0, 50,
                    0, 0, 0, 0, 255,
                    0.2f, 0.4f, 0.4f, 0, -30
            });
        }

        private ColorMatrix getalphapinkColorMatrix() {
            Toast.makeText(getApplicationContext(), "Alpha-Pink", Toast.LENGTH_SHORT).show();
            return new ColorMatrix(new float[]{
                    0, 0, 0, 0, 255,
                    0, 0, 0, 0, 0,
                    0.2f, 0, 0, 0, 50,
                    0.2f, 0.2f, 0.2f, 0, -20
            });
        }
        public void RotateBitmap( Bitmap bitmaptorot, float angle)
        {
            matrix.reset();
            mangle = angle;
            int py;
            int px;
System.out.println("mWidth"+mWidth);
            // Scale and maintain aspect ratio given a desired width
            // BitmapScaler.scaleToFitWidth(bitmap, 100);


            Bitmap scaledBitmap;


            // Scale and maintain aspect ratio given a desired height
            // BitmapScaler.scaleToFitHeight(bitmap, 100);

            int ratio = bitmaptorot.getWidth()/bitmaptorot.getHeight();
            matrix.postRotate(angle);

    px = mHeight / 2;
    System.out.println("in case 1");
    int factorw = px / bitmaptorot.getHeight();
    py = bitmaptorot.getWidth()*factorw;
   // scaledBitmap = Bitmap.createScaledBitmap(bitmaptorot, px, py, true);


if(nomorerotation ==0) {
    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    rotatedFrame = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
}

        }
    }
}


