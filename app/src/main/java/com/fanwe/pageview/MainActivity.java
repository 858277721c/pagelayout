package com.fanwe.pageview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fanwe.lib.looper.Looper;
import com.fanwe.lib.looper.impl.FSimpleLooper;
import com.fanwe.lib.pagelayout.FPageLayout;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private FPageLayout view_page;
    private Button btn;

    private Looper mLooper = new FSimpleLooper();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_page = findViewById(R.id.view_page);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i(TAG, "onClick:" + v);
            }
        });
        view_page.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i(TAG, "onClick:" + v);
            }
        });

        mLooper.start(new Runnable()
        {
            @Override
            public void run()
            {
                view_page.setLayoutParams(view_page.getLayoutParams());
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mLooper.stop();
    }
}
