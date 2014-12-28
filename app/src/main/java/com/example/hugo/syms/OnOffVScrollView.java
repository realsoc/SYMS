package com.example.hugo.syms;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Hugo on 22/12/2014.
 */
public class OnOffVScrollView extends ScrollView{
    private boolean enableScrolling = true;

    public boolean isEnableScrolling() {
        return enableScrolling;
    }

    public void setEnableScrolling(boolean enableScrolling) {
        this.enableScrolling = enableScrolling;
    }

    public OnOffVScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public OnOffVScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnOffVScrollView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isEnableScrolling()) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnableScrolling()) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }
}
