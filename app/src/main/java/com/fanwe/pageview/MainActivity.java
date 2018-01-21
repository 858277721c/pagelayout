package com.fanwe.pageview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fanwe.lib.pagelayout.FPageLayout;

public class MainActivity extends AppCompatActivity
{
    private FPageLayout view_page;
    private TextView tv_center;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_page = findViewById(R.id.view_page);
        tv_center = findViewById(R.id.tv_center);

        view_page.setPageView(tv_center);
    }
}
