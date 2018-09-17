package com.example.feng.xgs.main.nearby.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.feng.xgs.R;

public class New2Activity extends AppCompatActivity {
    private TextView tv,tv_toolbar_title;
    private ImageView new_image,iv_toolbar_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new2);
        tv = findViewById(R.id.tv_new);
        new_image = findViewById(R.id.new_image);
        iv_toolbar_back  = findViewById(R.id.iv_toolbar_back);
        tv_toolbar_title = findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("新手指引");
        Bundle b=getIntent().getExtras();
        String a= b.getString("1");
        String type = b.getString("type");
        String position = b.getString("position");
        if (position.equals("1.")){
            new_image.setBackgroundResource(R.mipmap.image11);
        }else if (position.equals("2.")){
            new_image.setBackgroundResource(R.mipmap.image22);
        }
        else if (position.equals("3.")){
            new_image.setBackgroundResource(R.mipmap.image33);
        }else if (position.equals("5.")){
            new_image.setBackgroundResource(R.mipmap.image44);
        }else{
            new_image.setBackgroundResource(R.mipmap.image55);
        }
        tv.setText(a);
        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
