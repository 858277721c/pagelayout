package com.fanwe.lib.pagelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
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
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        mTouchHelper.processTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
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
