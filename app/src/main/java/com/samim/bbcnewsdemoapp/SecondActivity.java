package com.samim.bbcnewsdemoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class SecondActivity extends AppCompatActivity {
    ImageView imageViewl;
    TextView titleTxt, descTxt, dateTxt, authorTxt, contentTxt;
    String title, desc, date, author, content, imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        imageViewl = (ImageView) findViewById(R.id.imageView);
        titleTxt = (TextView) findViewById(R.id.new_d_title);
        descTxt = (TextView) findViewById(R.id.new_d_desc);
        authorTxt = (TextView) findViewById(R.id.news_d_author);
        dateTxt = (TextView) findViewById(R.id.news_d_publish_date);
        contentTxt = (TextView) findViewById(R.id.news_d_content);

        getData();
        setData();

    }

    private void getData(){
        if (getIntent().hasExtra("title") && getIntent().hasExtra("desc") && getIntent().hasExtra("date") && getIntent().hasExtra("image")){
            title = getIntent().getStringExtra("title");
            desc  = getIntent().getStringExtra("desc");
            author  = getIntent().getStringExtra("author");
            date  = getIntent().getStringExtra("date");
            content  = getIntent().getStringExtra("content");
            imageUrl  = getIntent().getStringExtra("image");
        }else {
            Toast.makeText(getApplicationContext(), "There's no data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        if (title != null)
           titleTxt.setText(title);
        if (desc != null)
           descTxt.setText(desc);
        if (author != null)
           authorTxt.setText(author);
        if (date != null)
           dateTxt.setText(date);
        if (!content.equals("null"))
           contentTxt.setText(content);

        Glide.with(this)
                .load(imageUrl.trim())
                .placeholder(R.drawable.bbc)
                .error(R.drawable.bbc)
                .override(600, 400)
                .into(imageViewl);
    }

}