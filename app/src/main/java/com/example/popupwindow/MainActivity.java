package com.example.popupwindow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tv_hello;
    private List<PathItem> pathItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_hello = findViewById(R.id.tv_hello);

        pathItemList = new ArrayList<>();
        pathItemList.add(new PathItem().name("苏宁易购").imageResId(R.drawable.ic_snyg).backgroundResId(R.drawable.bg_blue_oval));
        pathItemList.add(new PathItem().name("天猫超市").imageResId(R.drawable.ic_tmcs).backgroundResId(R.drawable.bg_blue_oval));
        pathItemList.add(new PathItem().name("天猫国际").imageResId(R.drawable.ic_tmgj).backgroundResId(R.drawable.bg_blue_oval));
        pathItemList.add(new PathItem().name("聚划算").imageResId(R.drawable.ic_jhs).backgroundResId(R.drawable.bg_blue_oval));

        tv_hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PathPopupWindow popupWindow = new PathPopupWindow(MainActivity.this,pathItemList);
                popupWindow.setOnPathItemClickListener(new PathPopupWindow.OnPathItemClickListener() {
                    @Override
                    public void onItemClick(int position, PathItem item) {
                        Toast.makeText(MainActivity.this,"点击了--->"+item.name,Toast.LENGTH_LONG).show();
                    }
                });
                popupWindow.show(v);
            }
        });
    }
}
