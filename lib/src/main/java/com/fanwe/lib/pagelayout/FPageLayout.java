package com.fanwe.lib.pagelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.fanwe.lib.gesture.FGestureManager;
import com.fanwe.lib.gesture.FScroller;
import com.fanwe.lib.gesture.FTouchHelper;

public class FPageLayout extends FrameLayout
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
    private final ViewPosition mPageViewPosition = new ViewPosition();
    private FGestureManager mGestureManager;
    private FScroller mScroller;

    private OnClickListener mOnClickListener;

    private FGestureManager getGestureManager()
    {
        if (mGestureManager == null)
        {
            mGestureManager = new FGestureManager(new FGestureManager.Callback()
            {
                @Override
                public boolean shouldConsumeEvent(MotionEvent event)
                {
                    return canPull();
                }

                @Override
                public boolean onEventConsume(MotionEvent event)
                {
                    final int left = mPageView.getLeft();
                    final int minLeft = 0;
                    final int maxLeft = mPageView.getWidth();
                    final int dx = (int) getGestureManager().getTouchHelper().getDeltaX();
                    final int legalDx = FTouchHelper.getLegalDelta(left, minLeft, maxLeft, dx);

                    mPageView.offsetLeftAndRight(legalDx);
                    mPageViewPosition.save(mPageView);

                    return true;
                }

                @Override
                public void onEventFinish(boolean hasConsumeEvent, VelocityTracker velocityTracker, MotionEvent event)
                {
                    if (getGestureManager().getTouchHelper().isClick(event, getContext()))
                    {
                        if (mOnClickListener != null)
                            mOnClickListener.onClick(FPageLayout.this);
                    }

                    velocityTracker.computeCurrentVelocity(1000);
                    final float velocityX = velocityTracker.getXVelocity();

                    final int left = mPageView.getLeft();
                    final int minLeft = 0;
                    final int maxLeft = mPageView.getWidth();

                    if (Math.abs(velocityX) > ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity() * 20)
                    {
                        if (velocityX > 0)
                        {
                            getScroller().scrollToX(left, maxLeft, -1);
                        } else
                        {
                            getScroller().scrollToX(left, minLeft, -1);
                        }
                    } else
                    {
                        if (left < (minLeft + maxLeft) / 2)
                        {
                            getScroller().scrollToX(left, minLeft, -1);
                        } else
                        {
                            getScroller().scrollToX(left, maxLeft, -1);
                        }
                    }

                    invalidate();
                }
            });
        }
        return mGestureManager;
    }

    private FScroller getScroller()
    {
        if (mScroller == null)
        {
            mScroller = new FScroller(new Scroller(getContext()));
            mScroller.setCallback(new FScroller.Callback()
            {
                @Override
                public void onScrollStateChanged(boolean isFinished)
                {

                }

                @Override
                public void onScroll(int lastX, int lastY, int currX, int currY)
                {
                    final int dx = currX - lastX;
                    mPageView.offsetLeftAndRight(dx);
                    mPageViewPosition.save(mPageView);
                }
            });
        }
        return mScroller;
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener)
    {
        mOnClickListener = onClickListener;
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
        mPageViewPosition.layout(mPageView);
    }

    private boolean canPull()
    {
        final int dx = (int) getGestureManager().getTouchHelper().getDeltaXFromDown();
        final int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        if (Math.abs(dx) < touchSlop)
            return false;

        final boolean checkDegreeX = getGestureManager().getTouchHelper().getDegreeXFromDown() < 30;
        return checkDegreeX;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return getGestureManager().onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return getGestureManager().onTouchEvent(event);
    }

    @Override
    public void computeScroll()
    {
        super.computeScroll();
        if (getScroller().computeScrollOffset())
            invalidate();
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        final int count = getChildCount();
        if (count != 1)
            throw new IllegalArgumentException(getClass().getSimpleName() + " must contains one child");

        final View child = getChildAt(0);
        mPageView = child;
    }
}
