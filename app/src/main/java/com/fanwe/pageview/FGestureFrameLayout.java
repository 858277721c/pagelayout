package com.fanwe.pageview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.fanwe.lib.touchhelper.FTouchHelper;

/**
 * Created by Administrator on 2018/1/16.
 */
public abstract class FGestureFrameLayout extends FrameLayout
{
    public FGestureFrameLayout(@NonNull Context context)
    {
        super(context);
        init();
    }

    public FGestureFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public FGestureFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private FTouchHelper mTouchHelper = new FTouchHelper();

    private void init()
    {
    }

    protected final FTouchHelper getTouchHelper()
    {
        return mTouchHelper;
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent ev)
    {
        if (mTouchHelper.isNeedIntercept())
        {
            return true;
        }

        mTouchHelper.processTouchEvent(ev);
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                releaseProcess();
                break;
            case MotionEvent.ACTION_MOVE:
                mTouchHelper.saveDirection();

                if (canPull(mTouchHelper.getDirection()))
                {
                    mTouchHelper.setNeedIntercept(true);
                    FTouchHelper.requestDisallowInterceptTouchEvent(this, true);
                }
                break;
        }
        return mTouchHelper.isNeedIntercept();
    }

    private void releaseProcess()
    {
        mTouchHelper.setNeedCosume(false);
        mTouchHelper.setNeedIntercept(false);
        FTouchHelper.requestDisallowInterceptTouchEvent(this, false);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mTouchHelper.processTouchEvent(event);
        switch (event.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                mTouchHelper.saveDirection();

                if (mTouchHelper.isNeedCosume())
                {
                    if (processMoveEvent(event))
                    {
                    } else
                    {
                        releaseProcess();
                    }
                } else
                {
                    if (mTouchHelper.isNeedIntercept() || canPull(mTouchHelper.getDirection()))
                    {
                        mTouchHelper.setNeedCosume(true);
                        mTouchHelper.setNeedIntercept(true);
                        FTouchHelper.requestDisallowInterceptTouchEvent(this, true);
                    } else
                    {
                        releaseProcess();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                releaseProcess();
                break;
            default:
                break;
        }

        return mTouchHelper.isNeedCosume() || event.getAction() == MotionEvent.ACTION_DOWN;
    }

    protected abstract boolean canPull(FTouchHelper.Direction direction);

    protected abstract boolean processMoveEvent(MotionEvent event);
}
