package com.rmznm.hnapp.hackernewsapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView=findViewById(R.id.HaberListe);
        HTTPIslemleri httpIslemleri=new HTTPIslemleri();

        httpIslemleri.haberleriCek(this,listView);



    }
}
