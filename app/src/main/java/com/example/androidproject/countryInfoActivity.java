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
    private String mNativeName, mCapital, mRegion, mSubRegion, mCurrency, mLanguages = "";
    private int mPopulation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_info);
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
        TextView nativeNameTextView = (TextView) findViewById(R.id.nativeNameView);
        TextView capitalTextView = (TextView) findViewById(R.id.capitalView);
        TextView populationTextView = (TextView) findViewById(R.id.populationView);
        TextView regionTextView = (TextView) findViewById(R.id.regionView);
        TextView subRegionTextView = (TextView) findViewById(R.id.subRegionView);
        TextView currencyTextView = (TextView) findViewById(R.id.currencyView);
        TextView languagesTextView = (TextView) findViewById(R.id.languagesView);

        try {
            mNativeName = countryObject.getJSONObject("name").getJSONObject("nativeName").getJSONObject("fin").getString("common");
            mCapital = countryObject.getString("capital");
            mPopulation = countryObject.getInt("population");
            mRegion = countryObject.getString("region");
            mSubRegion = countryObject.getString("subregion");
            mCurrency = countryObject.getJSONObject("currencies").getJSONObject("EUR").getString("name");

            JSONObject lang = countryObject.getJSONObject("languages");
            StringBuilder str = new StringBuilder();
            Iterator keys = lang.keys();

            for(int i = 0; i < lang.length(); ++i){
                String k = keys.next().toString();
                System.out.println(k);
                str.append(lang.getString(k) + ", ");
            }
            mLanguages = str.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        nativeNameTextView.setText("" + mNativeName);
        capitalTextView.setText("" + mCapital.substring(2, mCapital.length() - 2));
        populationTextView.setText("" + mPopulation);
        regionTextView.setText("" + mRegion);
        subRegionTextView.setText("" + mSubRegion);
        currencyTextView.setText("" + mCurrency);
        languagesTextView.setText("" + mLanguages.substring(0, mLanguages.length() - 2));
    }

}