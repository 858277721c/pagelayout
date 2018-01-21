package com.fanwe.lib.pagelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.fanwe.lib.touchhelper.FGestureDetector;
import com.fanwe.lib.touchhelper.FScroller;
import com.fanwe.lib.touchhelper.FTouchHelper;

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
    private FGestureDetector mGestureDetector;
    private FScroller mScroller;

    private ViewConfiguration mViewConfiguration;

    private GestureCallback mGestureCallback;

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

            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                if (mGestureCallback != null)
                {
                    mGestureCallback.onSingleTapUp(FGestureFrameLayout.this);
                }
                return super.onSingleTapUp(e);
            }
        });
    }

    public void setGestureCallback(GestureCallback gestureCallback)
    {
        mGestureCallback = gestureCallback;
    }

    protected final FTouchHelper getTouchHelper()
    {
        return mTouchHelper;
    }

    protected final FGestureDetector getGestureDetector()
    {
        return mGestureDetector;
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
        if (mTouchHelper.isNeedIntercept())
        {
            return true;
        }

        mTouchHelper.processTouchEvent(event);
        switch (event.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                if (shouldInterceptTouchEvent(event))
                {
                    mTouchHelper.setNeedIntercept(true);
                    FTouchHelper.requestDisallowInterceptTouchEvent(this, true);
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mTouchHelper.processTouchEvent(event);
        return getGestureDetector().onTouchEvent(event);
    }

    protected boolean shouldInterceptTouchEvent(MotionEvent event)
    {
        return false;
    }

    protected abstract boolean processMoveEvent(MotionEvent event);

    protected abstract void onActionUp(MotionEvent event, float velocityX, float velocityY);

    protected abstract void onComputeScroll(int dx, int dy);

    public interface GestureCallback
    {
        void onSingleTapUp(View view);
    }
}
