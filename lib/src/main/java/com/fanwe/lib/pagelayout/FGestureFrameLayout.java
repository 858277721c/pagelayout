package com.fanwe.lib.pagelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.fanwe.lib.touchhelper.FGestureDetector;
import com.fanwe.lib.touchhelper.FScroller;
import com.fanwe.lib.touchhelper.FTouchHelper;

/**
 * Created by Administrator on 2018/1/16.
 */
public abstract class FGestureFrameLayout extends FrameLayout
{
    public FGestureFrameLayout(Context context)
    {
        super(context);
        init();
    }

    public FGestureFrameLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public FGestureFrameLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private FTouchHelper mTouchHelper = new FTouchHelper();
    private FScroller mScroller;
    private FGestureDetector mGestureDetector;
    private ViewConfiguration mViewConfiguration;


    private void init()
    {
        mGestureDetector = new FGestureDetector(getContext(), new FGestureDetector.Callback()
        {
            @Override
            public boolean onDown(MotionEvent e)
            {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
            {
                return processMoveEvent(e2);
            }

            @Override
            public void onActionUp(MotionEvent event, float velocityX, float velocityY)
            {
                super.onActionUp(event, velocityX, velocityY);
                FGestureFrameLayout.this.onActionUp(event, velocityX, velocityY);
            }
        });
    }

    protected final FTouchHelper getTouchHelper()
    {
        return mTouchHelper;
    }

    protected final FScroller getScroller()
    {
        if (mScroller == null)
        {
            mScroller = new FScroller(getContext());
        }
        return mScroller;
    }

    protected final ViewConfiguration getViewConfiguration()
    {
        if (mViewConfiguration == null)
        {
            mViewConfiguration = ViewConfiguration.get(getContext());
        }
        return mViewConfiguration;
    }

    @Override
    public void computeScroll()
    {
        super.computeScroll();
        if (getScroller().computeScrollOffset())
        {
            onComputeScroll(getScroller().getDeltaX(), getScroller().getDeltaY());
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        mTouchHelper.processTouchEvent(event);
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mTouchHelper.processTouchEvent(event);
        return mGestureDetector.onTouchEvent(event);
    }

    protected abstract boolean processMoveEvent(MotionEvent event);

    protected abstract void onActionUp(MotionEvent event, float velocityX, float velocityY);

    protected abstract void onComputeScroll(int dx, int dy);
}
