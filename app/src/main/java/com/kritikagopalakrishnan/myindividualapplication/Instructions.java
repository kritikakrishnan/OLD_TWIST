package com.kritikagopalakrishnan.myindividualapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class Instructions extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions2);
        final View view1 = findViewById(R.id.imageView1);
        view1.setOnTouchListener(new OnSwipeTouchListener(Instructions.this) {
            @Override
            public void onSwipeLeft() {
                Intent galintent = new Intent(getApplicationContext(), GalleryPage.class);
                startActivity(galintent);

            }

        });
    }

}
