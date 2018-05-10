package com.fanwe.lib.pagelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.fanwe.lib.touchhelper.FGestureManager;
import com.fanwe.lib.touchhelper.FTouchHelper;

public class FPageLayout extends FrameLayout
{
    public FPageLayout(Context context)
    {
        super(context);
        init();
    }

    public FPageLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public FPageLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private View mPageView;
    private FViewBounds mPageViewBounds;
    private FGestureManager mGestureManager;

    private GestureCallback mGestureCallback;

    private void init()
    {
        mGestureManager = new FGestureManager(getContext());
        mGestureManager.setCallback(mGestureManagerCallback);
        mPageViewBounds = new FViewBounds(null);
    }

    public void setGestureCallback(GestureCallback gestureCallback)
    {
        mGestureCallback = gestureCallback;
    }

    public void setPageView(View pageView)
    {
        mPageView = pageView;
        mPageViewBounds.setView(pageView);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mGestureManager.getScroller().setMaxScrollDistance(w);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        mPageViewBounds.layout();
    }

    private final FGestureManager.Callback mGestureManagerCallback = new FGestureManager.Callback()
    {
        @Override
        public boolean shouldInterceptTouchEvent(MotionEvent event)
        {
            return false;
        }

        @Override
        public void onTagInterceptChanged(boolean intercept)
        {

        }

        @Override
        public boolean consumeDownEvent(MotionEvent event)
        {
            return false;
        }

        @Override
        public boolean shouldConsumeTouchEvent(MotionEvent event)
        {
            return true;
        }

        @Override
        public void onTagConsumeChanged(boolean consume)
        {

        }

        @Override
        public boolean onConsumeEvent(MotionEvent event)
        {
            if (mPageView == null)
            {
                return false;
            }

            final int left = mPageView.getLeft();
            final int minLeft = 0;
            final int maxLeft = mPageView.getWidth();
            final int dx = (int) mGestureManager.getTouchHelper().getDeltaXFrom(FTouchHelper.EVENT_LAST);
            final int legalDx = mGestureManager.getTouchHelper().getLegalDeltaX(left, minLeft, maxLeft, dx);

            mPageView.offsetLeftAndRight(legalDx);
            mPageViewBounds.save();

            return true;
        }

        @Override
        public void onConsumeEventFinish(MotionEvent event)
        {
            if (mPageView == null)
            {
                return;
            }

            mGestureManager.getVelocityTracker().computeCurrentVelocity(1000);
            final float velocityX = mGestureManager.getVelocityTracker().getXVelocity();

            final int left = mPageView.getLeft();
            final int minLeft = 0;
            final int maxLeft = mPageView.getWidth();

            if (Math.abs(velocityX) > ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity() * 20)
            {
                if (velocityX > 0)
                {
                    mGestureManager.getScroller().startScrollToX(left, maxLeft, -1);
                } else
                {
                    mGestureManager.getScroller().startScrollToX(left, minLeft, -1);
                }
            } else
            {
                if (left < maxLeft / 2)
                {
                    mGestureManager.getScroller().startScrollToX(left, minLeft, -1);
                } else
                {
                    mGestureManager.getScroller().startScrollToX(left, maxLeft, -1);
                }
            }

            invalidate();
        }

        @Override
        public void onComputeScroll(int dx, int dy, boolean finish)
        {
            if (mPageView == null)
            {
                return;
            }
            mPageView.offsetLeftAndRight(dx);
            mPageViewBounds.save();
        }
    };

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        final boolean result = mGestureManager.onInterceptTouchEvent(ev);
        return super.onInterceptTouchEvent(ev) || result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        final boolean result = mGestureManager.onTouchEvent(event);
        return super.onTouchEvent(event) || result;
    }

    @Override
    public void computeScroll()
    {
        super.computeScroll();
        if (mGestureManager.computeScroll())
        {
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        if (getChildCount() > 0)
        {
            setPageView(getChildAt(0));
        }
    }

    public interface GestureCallback
    {
        void onSingleTapUp();
    }
}
