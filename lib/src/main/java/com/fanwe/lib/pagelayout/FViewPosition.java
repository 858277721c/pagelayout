package com.fanwe.lib.pagelayout;

import android.view.View;

final class FViewPosition
{
    private int mLeft;
    private int mTop;

    public FViewPosition()
    {
        reset();
    }

    public int getLeft()
    {
        return mLeft;
    }

    public int getTop()
    {
        return mTop;
    }

    public boolean hasPosition()
    {
        return mLeft != Integer.MIN_VALUE && mTop != Integer.MIN_VALUE;
    }

    public void reset()
    {
        mLeft = Integer.MIN_VALUE;
        mTop = Integer.MIN_VALUE;
    }

    public boolean save(View view)
    {
        if (view == null)
            return false;

        mLeft = view.getLeft();
        mTop = view.getTop();
        return true;
    }

    public boolean layout(View view)
    {
        if (view == null)
            return false;

        if (!hasPosition())
            return false;

        view.layout(mLeft, mTop, mLeft + view.getMeasuredWidth(), mTop + view.getMeasuredHeight());
        return true;
    }
}
