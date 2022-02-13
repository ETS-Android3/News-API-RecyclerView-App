package com.samim.bbcnewsdemoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kotlin.jvm.Throws;


public class BBCNews {
    Context context;
    public BBCNews(Context context){
        this.context = context;
    }
    RecyclerView recyclerView;
    Adapter adapter;
    BBCNewsClass news;
    ArrayList<BBCNewsClass> classList;
    public  static  JSONArray newsJSONArray;

    public void getNews(String countryCode, String category, Boolean bbcSelected){
        String selected = "";
        String apiKey = "24cc2b7108f8458c9ef3cdc440f326ee";
        if (!bbcSelected){
            selected = "country="+countryCode+"&category="+category;
        }else {
            selected = "sources=bbc-news";
        }
        final String URL_news = "https://newsapi.org/v2/top-headlines?"+selected+"&apiKey="+apiKey;
        final ProgressDialog loading = new ProgressDialog(context);
         loading.setMessage(context.getResources().getString(R.string.loading));
         loading.setCanceledOnTouchOutside(true);
         loading.show();
         JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL_news, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    Log.d("JSON", String.valueOf(response));
                    loading.dismiss();
                    String totalResults = response.getString("totalResults");
                    String status = response.getString("status");
                    if (status.equals("ok")){
                        recyclerView = (RecyclerView) ((Activity)context).findViewById(R.id.recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        classList =  new ArrayList<>();

                        newsJSONArray = response.getJSONArray("articles");
                        for (int i=0; i<newsJSONArray.length(); i++) {
                            JSONObject params = newsJSONArray.getJSONObject(i);

                            JSONObject source = params.getJSONObject("source");
                            String id   = source.getString("id");
                            String name = source.getString("name");

                            String author = params.getString("author");
                            String title = params.getString("title");
                            String description = params.getString("description");
                            String url = params.getString("url");
                            String urlToImage = params.getString("urlToImage");
                            String publishedAt = params.getString("publishedAt");
                            String content = params.getString("content");

                            news = new BBCNewsClass();
                            news.author = author;
                            news.title = title;
                            news.description = description;
                            news.url = url;
                            news.urlToImage = urlToImage;
                            news.publishedAt = publishedAt;
                            news.content = content;

                            classList.add(news);
                        }
                        adapter = new Adapter(context, classList);
                        recyclerView.setAdapter(adapter);
                    }else if (status.equals("error")){
                        String message = response.getString("message");
                        String code = response.getString("code");
                        MSG(message, ((Activity)context).getResources().getString(R.string.error));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                MSG(String.valueOf(error), ((Activity)context).getResources().getString(R.string.error));
                VolleyLog.d( ((Activity)context).getResources().getString(R.string.error),  ((Activity)context).getResources().getString(R.string.error)+" : " + error.getMessage());
                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(context,
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();
                }
            }
        })
         {
             @Override
             public String getBodyContentType() {
                 // return "application/json; charset=utf-8";
                 return "application/x-www-form-urlencoded";
             }

             @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                 HashMap<String, String> headers = new HashMap<String, String>();
                 headers.put("User-Agent", "ozilla/5.0");
                 return headers;
             }
         };

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        queue.add(req);
    }


    public void MSG (String message,String title){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }



}
