package com.kritikagopalakrishnan.myindividualapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WelcomePage  extends Activity {
    //View my_View= findViewById(R.id.imageView);;
    OnSwipeTouchListener onSwipeTouchListener;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_welcome_page);
        final View view = findViewById(R.id.imageView);
        view.setOnTouchListener(new OnSwipeTouchListener(WelcomePage.this) {
            @Override
            public void onSwipeLeft() {
                GoToGallery();
            }

            public void onSwipeRight() {
                GoToInstructions();
            }
        });
        TextView mTextView = (TextView) findViewById(R.id.swipe);
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.WHITE, Color.DKGRAY,
                Shader.TileMode.CLAMP);
        mTextView.getPaint().setShader(myShader);
    }

    public void GoToGallery() {
        Intent intent = new Intent(this, GalleryPage.class);
        startActivity(intent);
    }

    public void GoToInstructions() {
        Intent intent = new Intent(this, Instructions.class);
        startActivity(intent);
    }
}