package com.kritikagopalakrishnan.myindividualapplication;

import android.view.MotionEvent;

public class RotationGestureDetector {
    private static final int INVALID_POINTER_ID = -1;
    private float fX, fY, sX, sY;
    private int ptrID1, ptrID2;
    private float mAngle = 90;
    boolean goneFlag = false;

    private OnRotationGestureListener mListener;

    public float getAngle() {
        return mAngle;
    }

    public RotationGestureDetector(OnRotationGestureListener listener){
        mListener = listener;
        ptrID1 = INVALID_POINTER_ID;
        ptrID2 = INVALID_POINTER_ID;
    }

    public boolean onTouchEvent(MotionEvent event){
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                ptrID1 = event.getPointerId(event.getActionIndex());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                ptrID2 = event.getPointerId(event.getActionIndex());
                if(ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID)
                {
                    mAngle +=90;
                }
                if (mListener != null) {
                    mListener.OnRotation(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //do nothing!
                break;
            case MotionEvent.ACTION_UP:
                ptrID1 = event.getPointerId(event.getActionIndex());
                break;
            case MotionEvent.ACTION_POINTER_UP:
                ptrID2 = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_CANCEL:
                ptrID1 = INVALID_POINTER_ID;
                ptrID2 = INVALID_POINTER_ID;
                break;
        }
        return true;
    }


    public static interface OnRotationGestureListener {
        public void OnRotation(RotationGestureDetector rotationDetector);
    }
}