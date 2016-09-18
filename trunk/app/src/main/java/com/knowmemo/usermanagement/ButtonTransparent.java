package com.knowmemo.usermanagement;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by User on 2016/2/23.
 */
public class ButtonTransparent implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN ){
            v.getBackground().setAlpha(128); // onfoucs 50% transparent

        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            v.getBackground().setAlpha(255); // leave and back to original color
        }
        return false;
    }

}
