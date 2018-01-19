package com.fanwe.pageview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.fanwe.lib.touchhelper.FScroller;
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
    private FScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private ViewConfiguration mViewConfiguration;

    private void init()
    {
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

    @Override
    public void computeScroll()
    {
        super.computeScroll();
        if (getScroller().computeScrollOffset())
        {
            onComputeScroll();
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (mTouchHelper.isNeedIntercept())
        {
            return true;
        }

        getVelocityTracker().addMovement(ev);
        mTouchHelper.processTouchEvent(ev);
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                releaseProcess();
                break;
            case MotionEvent.ACTION_MOVE:
                mTouchHelper.saveDirection();

                if (canPull(mTouchHelper.getDirection(), ev))
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

        if (mVelocityTracker != null)
        {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        getVelocityTracker().addMovement(event);
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
                    if (mTouchHelper.isNeedIntercept() || canPull(mTouchHelper.getDirection(), event))
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
                getVelocityTracker().computeCurrentVelocity(1000);
                onActionUp(event, getVelocityTracker().getXVelocity(), getVelocityTracker().getYVelocity());

                releaseProcess();
                break;
            default:
                break;
        }

        return mTouchHelper.isNeedCosume() || event.getAction() == MotionEvent.ACTION_DOWN;
    }

    protected abstract boolean canPull(FTouchHelper.Direction direction, MotionEvent event);

    protected abstract boolean processMoveEvent(MotionEvent event);

    protected abstract void onActionUp(MotionEvent event, float xvel, float yvel);

    protected abstract void onComputeScroll();
}
