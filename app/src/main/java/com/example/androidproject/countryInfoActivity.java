package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Iterator;

public class countryInfoActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private String mCountryUrl;
    private JSONObject countryInfo;
    private String mCapital, mRegion, mSubRegion, mLanguages = "";
    private int mPopulation = 0;
    private static int FIRST_ELEMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_info);
        getSupportActionBar().setTitle("Country Info");

        TextView countryNameTextView = findViewById(R.id.countryNameView);
        String mCountryName;
        mQueue = Volley.newRequestQueue(this);
        mCountryName = getIntent().getStringExtra("value");
        mCountryUrl = String.format("https://restcountries.com/v3.1/name/%s", mCountryName);
        countryNameTextView.setText(mCountryName);
        fetchCountryInfo();
    }

    public void fetchCountryInfo() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                mCountryUrl,
                null,
                response -> {
                    //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            countryInfo = response.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    parseJsonAndUpdateUI(countryInfo);
                }, error -> {
            error.printStackTrace();
        });
        mQueue.add(request);
    }

    private void parseJsonAndUpdateUI(JSONObject countryObject) {
        TextView capitalTextView = findViewById(R.id.capitalView);
        TextView populationTextView = findViewById(R.id.populationView);
        TextView regionTextView = findViewById(R.id.regionView);
        TextView subRegionTextView =findViewById(R.id.subRegionView);
        TextView languagesTextView = findViewById(R.id.languagesView);

        try {

            mCapital = countryObject.getString("capital");
            mPopulation = countryObject.getInt("population");
            mRegion = countryObject.getString("region");
            mSubRegion = countryObject.getString("subregion");

            JSONObject lang = countryObject.getJSONObject("languages");
            StringBuilder str = new StringBuilder();
            Iterator keys = lang.keys();

            for(int i = 0; i < lang.length(); ++i){
                String k = keys.next().toString();

                    str.append(lang.getString(k) + ", ");

            }
            mLanguages = str.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(mCapital != null) { mCapital = mCapital.substring(2, mCapital.length() - 2); }
        if(mLanguages != null && mLanguages.length() > 2) { mLanguages = mLanguages.substring(0, mLanguages.length() - 2);}

        capitalTextView.setText("" + mCapital + "");
        populationTextView.setText("" + mPopulation + "");
        regionTextView.setText("" + mRegion+ "");
        subRegionTextView.setText("" + mSubRegion + "");
        languagesTextView.setText("" + mLanguages + "");
    }

}