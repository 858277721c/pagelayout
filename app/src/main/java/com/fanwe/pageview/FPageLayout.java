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
        int legalDx = getTouchHelper().getLegalDeltaX(mViewCenterHorzontal.getLeft(), -width, width, dx);
        mViewCenterHorzontal.offsetLeftAndRight(legalDx);

        return true;
    }
}
