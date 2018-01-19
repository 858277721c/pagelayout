package com.fanwe.pageview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

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

    @Override
    protected boolean canPull(FTouchHelper.Direction direction)
    {
        return false;
    }

    @Override
    protected boolean processMoveEvent(MotionEvent event)
    {
        return false;
    }
}
