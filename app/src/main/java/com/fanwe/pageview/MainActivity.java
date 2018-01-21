package com.fanwe.pageview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fanwe.lib.looper.FLooper;
import com.fanwe.lib.looper.impl.FSimpleLooper;
import com.fanwe.lib.pagelayout.FPageLayout;

public class MainActivity extends AppCompatActivity
{
    private FPageLayout view_page;
    private TextView tv_center;

    private FLooper mLooper = new FSimpleLooper();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_page = findViewById(R.id.view_page);
        tv_center = findViewById(R.id.tv_center);

//        tv_center.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
//            }
//        });
        view_page.setPageView(tv_center);

        mLooper.start(1000, new Runnable()
        {
            @Override
            public void run()
            {
                view_page.requestLayout();
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
