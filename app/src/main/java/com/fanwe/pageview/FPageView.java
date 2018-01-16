package com.fanwe.pageview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.fanwe.lib.touchhelper.FTouchHelper;

/**
 * Created by Administrator on 2018/1/16.
 */
public class FPageView extends FrameLayout
{
    public FPageView(@NonNull Context context)
    {
        super(context);
    }

    public FPageView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FPageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    private FTouchHelper mTouchHelper = new FTouchHelper();


}
