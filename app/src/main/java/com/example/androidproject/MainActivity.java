package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> countryList = new ArrayList<>();
    private String mUrl = "https://restcountries.com/v3.1/all";
    private RequestQueue mQueue;
    JSONObject countryInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);
        listView = findViewById(R.id.countryListView);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countryList);
        listView.setAdapter(arrayAdapter);
        fetchCountryData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                Intent intent = new Intent(MainActivity.this, countryInfoActivity.class);
                intent.putExtra("value", arrayAdapter.getItem(position).toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList("COUNTRY_LIST", countryList);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void fetchCountryData() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                mUrl,
                null,
                response -> {
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            countryInfo = response.getJSONObject(i);
                            countryList.add(countryInfo.getJSONObject("name").getString("common"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> {
                    error.printStackTrace();
        });
        //  Lisätään volley request queueen
        mQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search for country");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}