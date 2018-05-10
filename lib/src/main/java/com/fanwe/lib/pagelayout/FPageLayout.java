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
    private int mTouchSlop;

    private OnClickListener mOnClickListener;

    private void init()
    {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mGestureManager = new FGestureManager(getContext());
        mGestureManager.setCallback(mSimpleCallback);
        mPageViewBounds = new FViewBounds(null);
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
        mGestureManager.getScroller().setMaxScrollDistance(w);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        mPageViewBounds.layout();
    }

    private final FGestureManager.SimpleCallback mSimpleCallback = new FGestureManager.SimpleCallback()
    {
        @Override
        public boolean consumeDownEvent(MotionEvent event)
        {
            return true;
        }

        @Override
        public boolean shouldConsumeTouchEvent(MotionEvent event)
        {
            return canPull();
        }

        @Override
        public boolean onConsumeEvent(MotionEvent event)
        {
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
            if (mGestureManager.isClick(event))
            {
                if (mOnClickListener != null) mOnClickListener.onClick(FPageLayout.this);
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
            mPageView.offsetLeftAndRight(dx);
            mPageViewBounds.save();
        }
    };

    private boolean canPull()
    {
        final int dx = (int) mGestureManager.getTouchHelper().getDeltaXFrom(FTouchHelper.EVENT_DOWN);
        if (Math.abs(dx) < mTouchSlop)
        {
            return false;
        }

        final boolean checkDegreeX = mGestureManager.getTouchHelper().getDegreeXFrom(FTouchHelper.EVENT_DOWN) < 30;
        return checkDegreeX;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return mGestureManager.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return mGestureManager.onTouchEvent(event);
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

        final int count = getChildCount();
        if (count != 1)
        {
            throw new IllegalArgumentException(getClass().getSimpleName() + " must contains one child");
        }

        final View child = getChildAt(0);
        mPageView = child;
        mPageViewBounds.setView(child);
    }
}
