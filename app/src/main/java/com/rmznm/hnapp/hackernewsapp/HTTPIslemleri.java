package com.rmznm.hnapp.hackernewsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HTTPIslemleri {
    String TAG="asd";
    Gson gson=new Gson();
    void haberleriCek(final Context ctx, final ListView listView) {
        new Thread(
                new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        String haberlerJson=getStringDataFromURL("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
                        ArrayList<Haber> haberler=new ArrayList<>();
                        int haberIdleri[]=gson.fromJson(haberlerJson,int[].class);
                        int sayac=0;
                        for (int i : haberIdleri) {
                            if(sayac++==20)break;
                            haberler.add(haberDetayCek(i));
                        }
                        ((Activity)ctx).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<String> isimler=haberler.stream().map(x->x.title).collect(Collectors.toList());
                                ArrayAdapter aa=new ArrayAdapter(ctx,android.R.layout.simple_list_item_1,isimler);
                                listView.setAdapter(aa);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d(TAG, "onItemClick: "+position);
                                        Intent i=new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(haberler.get(position).url));
                                        ctx.startActivity(i);
                                    }
                                });
                            }
                        });
                    }
                }
        ).start();
    }


    ArrayList<Haber> haberListeCek(){
        ArrayList<Haber> haberler=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String haberlerJson=getStringDataFromURL("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");

                int haberIdleri[]=gson.fromJson(haberlerJson,int[].class);
                int sayac=0;
                for (int i : haberIdleri) {
                    if(sayac++==20)break;
                    haberler.add(haberDetayCek(i));
//                    Log.d(TAG, haberDetayCek(i).title);
                }
            }
        }).start();
        return haberler;
    }

    Haber haberDetayCek(int haberId){
        String data=getStringDataFromURL("https://hacker-news.firebaseio.com/v0/item/"+haberId+".json?print=pretty");
        //Log.d(TAG, data);
        return gson.fromJson(data,Haber.class);
    }

    private String getStringDataFromURL(String sUrl){
        Gson gson=new Gson();
        try {
            URL url = new URL(sUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            String data="";
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                data+=inputLine;
            }
            in.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

}
