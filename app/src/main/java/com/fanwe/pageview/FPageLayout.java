package com.fanwe.pageview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fanwe.lib.touchhelper.FTouchHelper;

/**
 * Created by Administrator on 2018/1/19.
 */

public class FPageLayout extends FGestureFrameLayout
{
    public FPageLayout(@NonNull Context context)
    {
        super(context);
    }

    public FPageLayout(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FPageLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    private View mViewCenterHorzontal;

    public void setViewCenterHorzontal(View viewCenterHorzontal)
    {
        mViewCenterHorzontal = viewCenterHorzontal;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        getScroller().setMaxScrollDistance(w);
    }

    @Override
    protected boolean canPull(FTouchHelper.Direction direction, MotionEvent event)
    {
        if (mViewCenterHorzontal == null)
        {
            return false;
        }
        return true;
    }

    @Override
    protected boolean processMoveEvent(MotionEvent event)
    {
        if (mViewCenterHorzontal == null)
        {
            return false;
        }

        int dx = (int) getTouchHelper().getDeltaXFrom(FTouchHelper.EVENT_LAST);
        int width = mViewCenterHorzontal.getWidth();
        int legalDx = getTouchHelper().getLegalDeltaX(mViewCenterHorzontal.getLeft(), 0, width, dx);
        mViewCenterHorzontal.offsetLeftAndRight(legalDx);

        return true;
    }

    @Override
    protected void onActionUp(MotionEvent event, float xvel, float yvel)
    {
        if (mViewCenterHorzontal == null)
        {
            return;
        }

        int left = mViewCenterHorzontal.getLeft();
        int width = mViewCenterHorzontal.getWidth();

        if (Math.abs(xvel) > getViewConfiguration().getScaledMinimumFlingVelocity() * 20)
        {
            if (xvel > 0)
            {
                getScroller().startScrollToX(left, width, -1);
            } else
            {
                getScroller().startScrollToX(left, 0, -1);
            }
        } else
        {
            if (left < width / 2)
            {
                getScroller().startScrollToX(left, 0, -1);
            } else
            {
                getScroller().startScrollToX(left, width, -1);
            }
        }

        invalidate();
    }

    @Override
    protected void onComputeScroll(int dx, int dy)
    {
        if (mViewCenterHorzontal == null)
        {
            return;
        }
        mViewCenterHorzontal.offsetLeftAndRight(dx);
    }
}
