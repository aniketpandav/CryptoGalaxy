package com.example.majorproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.majorproject.Adapters.SearchAdapter;
import com.example.majorproject.Models.CryptoDataModel;
import com.example.majorproject.databinding.ActivitySearchCurrencyBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class SearchCurrency extends AppCompatActivity {
    ActivitySearchCurrencyBinding binding;
    ArrayList<CryptoDataModel> data;
    String previousActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchCurrencyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //set focus in edittable search
        binding.etSearchView.requestFocus();

        previousActivity = getIntent().getStringExtra("passTo");
        data = new ArrayList<>();

        binding.toolbarSearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.etSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){
                    binding.searchLodingAnimation.setVisibility(View.VISIBLE);
                    setSearchData(charSequence);
                }
                else{
                    binding.searchLodingAnimation.setVisibility(View.INVISIBLE);
                    binding.searchRecycler.setAdapter(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    //Search data from there
    private void setSearchData(CharSequence charSequnce) {
       data.clear();
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder().build();
        AndroidNetworking.initialize(this,okHttpClient);

        AndroidNetworking.setParserFactory(new GsonParserFactory());

        AndroidNetworking.get("https://api.coingecko.com/api/v3/search?x_cg_demo_api_key=&query=")
                .addQueryParameter("query", charSequnce.toString())
                .addQueryParameter("x_cg_demo_api_key", getString(R.string.COINGECKO_API_KEY))
                .setTag("Market")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("coins");
                            for (int i=0; i< jsonArray.length(); i++){

                                JSONObject apiData = jsonArray.getJSONObject(i);

                                CryptoDataModel model = new CryptoDataModel();
                                model.setName(apiData.getString("name"));
                                model.setSymbol(apiData.getString("symbol"));
                                model.setImage(apiData.getString("large"));
                                model.setMarket_cap_rank(apiData.getString("market_cap_rank"));
                                model.setId(apiData.getString("id"));
                                model.setType("usd");
                                data.add(model);
                            }
                            SearchAdapter adapter = new SearchAdapter(SearchCurrency.this, data, previousActivity);
                            binding.searchLodingAnimation.setVisibility(View.INVISIBLE);
                            binding.searchRecycler.setAdapter(adapter);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.e("ErrMarketApi", anError.getErrorBody());
                    }
                });
    }
}