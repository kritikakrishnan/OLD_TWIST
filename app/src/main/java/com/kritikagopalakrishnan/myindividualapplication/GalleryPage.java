package com.kritikagopalakrishnan.myindividualapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.LruCache;


public class GalleryPage extends Activity {
    Uri selectedImage;
    private final static int RESULT_SELECT_IMAGE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String TAG = "GalleryUtil";

    String mCurrentPhotoPath;
    File photoFile = null;
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //Pick Image From Gallery
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_SELECT_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SELECT_IMAGE:

                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    selectedImage = data.getData();

                   Thread thread = new Thread(new MyRunnable());
                    thread.run();


                }
                else
                {
                    Intent backintent = new Intent(GalleryPage.this, WelcomePage.class);
                    startActivity(backintent);
                }
        }
    }
    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            // check if it's run in main thread, or background thread

            //in background thread

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    int newHeight = 185;
                    final float densityMultiplier = getApplicationContext().getResources().getDisplayMetrics().density;
                    int h = (int) (newHeight * densityMultiplier);
                    int w = (int) (h * bitmap.getWidth() / ((double) bitmap.getHeight()));

                    bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
                    try {
                        //Write file
                        String filename = "bitmap.png";
                        FileOutputStream stream = getApplication().openFileOutput(filename, Context.MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        //Cleanup
                        stream.close();
                        bitmap.recycle();

                        //Pop intent
                        Intent in1 = new Intent(getApplicationContext(), EditPic.class);
                        in1.putExtra("BitmapImage", filename);
                        startActivity(in1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });
        }
    }
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

}
