package com.androidghost.prenews;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;


public class Latest extends AppCompatActivity {
    String url = "https://beebom.com/wp-json/wp/v2/posts?categories=151";
    List<Object> list;
    Gson gson;
    ProgressDialog progressDialog;
    ListView postList;
    Map<String,Object> mapPost;
    Map<String,Object> mapTitle;
    int postID;
    String postTitle[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest);
        Button latest,world,tech;
        latest =(Button)findViewById(R.id.latest);
        world =(Button)findViewById(R.id.world);
        tech =(Button)findViewById(R.id.tech);
        Button home =(Button)findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent act = new Intent(Latest.this,MainActivity.class);

                startActivity(act);
            }
        });
        latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent act = new Intent(Latest.this,Latest.class);

                startActivity(act);
            }
        });
        tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent act = new Intent(Latest.this,Tech.class);

                startActivity(act);
            }
        });
        world.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent act = new Intent(Latest.this,World.class);

                startActivity(act);
            }
        });


        postList = (ListView)findViewById(R.id.postList);
        progressDialog = new ProgressDialog(Latest.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                gson = new Gson();
                list = (List) gson.fromJson(s, List.class);
                postTitle = new String[list.size()];

                for(int i=0;i<list.size();++i){
                    mapPost = (Map<String,Object>)list.get(i);
                    mapTitle = (Map<String, Object>) mapPost.get("title");
                    postTitle[i] = (String) mapTitle.get("rendered");
                }

                postList.setAdapter(new ArrayAdapter(Latest.this,android.R.layout.simple_list_item_1,postTitle));
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Latest.this, "Some error occurred", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Latest.this);
        rQueue.add(request);

        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mapPost = (Map<String,Object>)list.get(position);
                postID = ((Double)mapPost.get("id")).intValue();

                Intent intent = new Intent(getApplicationContext(),Post.class);
                intent.putExtra("id", ""+postID);
                startActivity(intent);
            }
        });
    }
}