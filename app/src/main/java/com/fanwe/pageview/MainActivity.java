package com.fanwe.pageview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

        tv_center.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });
        view_page.setPageView(tv_center);
    }
}
