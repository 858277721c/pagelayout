package com.fanwe.lib.pagelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fanwe.lib.touchhelper.FTouchHelper;

public class FPageLayout extends FGestureFrameLayout
{
    public FPageLayout(Context context)
    {
        super(context);
    }

    public FPageLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FPageLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    private View mPageView;

    public void setPageView(View pageView)
    {
        mPageView = pageView;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        getScroller().setMaxScrollDistance(w);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        FViewPosition position = FViewPosition.get(mPageView);
        if (position != null)
        {
            position.layout();
        }
    }

    @Override
    protected boolean shouldInterceptTouchEvent(MotionEvent event)
    {
        final float dx = getTouchHelper().getDeltaXFrom(FTouchHelper.EVENT_DOWN);
        return dx > getViewConfiguration().getScaledTouchSlop();
    }

    @Override
    protected boolean processMoveEvent(MotionEvent event)
    {
        if (mPageView == null)
        {
            return false;
        }

        final int left = mPageView.getLeft();
        final int minLeft = 0;
        final int maxLeft = mPageView.getWidth();
        final int dx = (int) getTouchHelper().getDeltaXFrom(FTouchHelper.EVENT_LAST);

        final int legalDx = getTouchHelper().getLegalDeltaX(left, minLeft, maxLeft, dx);
        mPageView.offsetLeftAndRight(legalDx);
        FViewPosition.get(mPageView).savePosition();

        return true;
    }

    @Override
    protected void onActionUp(MotionEvent event, float velocityX, float velocityY)
    {
        if (mPageView == null)
        {
            return;
        }

        final int left = mPageView.getLeft();
        final int minLeft = 0;
        final int maxLeft = mPageView.getWidth();

        if (Math.abs(velocityX) > getViewConfiguration().getScaledMinimumFlingVelocity() * 20)
        {
            if (velocityX > 0)
            {
                getScroller().startScrollToX(left, maxLeft, -1);
            } else
            {
                getScroller().startScrollToX(left, minLeft, -1);
            }
        } else
        {
            if (left < maxLeft / 2)
            {
                getScroller().startScrollToX(left, minLeft, -1);
            } else
            {
                getScroller().startScrollToX(left, maxLeft, -1);
            }
        }

        invalidate();
    }

    @Override
    protected void onComputeScroll(int dx, int dy)
    {
        if (mPageView == null)
        {
            return;
        }
        mPageView.offsetLeftAndRight(dx);
        FViewPosition.get(mPageView).savePosition();
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
}
