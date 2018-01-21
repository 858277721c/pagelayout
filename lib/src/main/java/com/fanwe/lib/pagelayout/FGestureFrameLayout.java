package com.fanwe.lib.pagelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
    private VelocityTracker mVelocityTracker;
    private ViewConfiguration mViewConfiguration;
    private GestureDetector mGestureDetector;

    private void init()
    {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener()
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

    protected final VelocityTracker getVelocityTracker()
    {
        if (mVelocityTracker == null)
        {
            mVelocityTracker = VelocityTracker.obtain();
        }
        return mVelocityTracker;
    }

    protected final ViewConfiguration getViewConfiguration()
    {
        if (mViewConfiguration == null)
        {
            mViewConfiguration = ViewConfiguration.get(getContext());
        }
        return mViewConfiguration;
    }

    private void releaseVelocityTracker()
    {
        if (mVelocityTracker != null)
        {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
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
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        mTouchHelper.processTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mTouchHelper.processTouchEvent(event);
        getVelocityTracker().addMovement(event);

        boolean result = mGestureDetector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            getVelocityTracker().computeCurrentVelocity(1000);
            float velocityX = getVelocityTracker().getXVelocity();
            float velocityY = getVelocityTracker().getYVelocity();
            onActionUp(event, velocityX, velocityY);
            releaseVelocityTracker();
        }

        return result;
    }

    protected abstract boolean processMoveEvent(MotionEvent event);

    protected abstract void onActionUp(MotionEvent event, float xvel, float yvel);

    protected abstract void onComputeScroll(int dx, int dy);

    protected final static void synchronizeMargin(View view, boolean update)
    {
        MarginLayoutParams params = getMarginLayoutParams(view);
        if (params == null)
        {
            return;
        }

        final int left = view.getLeft();
        final int top = view.getTop();

        boolean changed = false;
        if (params.leftMargin != left)
        {
            params.leftMargin = left;
            changed = true;
        }
        if (params.topMargin != top)
        {
            params.topMargin = top;
            changed = true;
        }

        if (changed)
        {
            if (update)
            {
                view.setLayoutParams(params);
            }
        }
    }

    /**
     * 获得view的MarginLayoutParams，返回值可能为null
     *
     * @param view
     * @return
     */
    private static MarginLayoutParams getMarginLayoutParams(View view)
    {
        if (view == null)
        {
            return null;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && params instanceof MarginLayoutParams)
        {
            return (MarginLayoutParams) params;
        } else
        {
            return null;
        }
    }
}
