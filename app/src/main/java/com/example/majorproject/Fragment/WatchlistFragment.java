package com.example.majorproject.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.majorproject.Adapters.MarketAdapter;
import com.example.majorproject.Models.CryptoDataModel;
import com.example.majorproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import okhttp3.OkHttpClient;


public class WatchlistFragment extends Fragment {

    RecyclerView recyclerView;
    LottieAnimationView progressBar;
    RelativeLayout emptyListLayout;
    LottieAnimationView emptyListAnim;
    AppCompatButton btnViewMarket;
    ArrayList<CryptoDataModel> data;
    MarketAdapter adapter;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    SwipeRefreshLayout refresh;

    String coins;
    public WatchlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);

        //Intailized Widget & Variables
        recyclerView = view.findViewById(R.id.watchlistRecycler);
        progressBar = view.findViewById(R.id.watchlistLoadingAnimation);
        refresh = view.findViewById(R.id.watchlistRefresh);
        data = new ArrayList<>();
        emptyListLayout = view.findViewById(R.id.layoutEmptyWatchlist);
        btnViewMarket = view.findViewById(R.id.btnWatchlistEmpty);
        emptyListAnim = view.findViewById(R.id.lottieEmptyWatchlist);

        //Initializing Realm and app and user and Mongo client and database
        Realm.init(getContext());
        app = new App(new AppConfiguration.Builder(getContext().getString(R.string.MONGO_APP_ID)).build());
        user = app.currentUser();
        mongoClient = user.getMongoClient(getContext().getString(R.string.MONGO_DB_SERVICE_NAME));
        mongoDatabase = mongoClient.getDatabase(getContext().getString(R.string.MONGO_DATABASE_NAME ));

        //Call setWatchlistRecycler() that set and find watchlist data
        progressBar.setVisibility(View.VISIBLE);
        setWatchlistRecycler(getContext());



        //Refresh Recycler data
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.setAdapter(null);
                progressBar.setVisibility(View.VISIBLE);
                setWatchlistRecycler(getContext());
                refresh.setRefreshing(false);
            }
        });

        //set button view
        btnViewMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavView);
                bottomNavigationView.setSelectedItemId(R.id.bottomOptMarket);
            }
        });

        return view;
    }
    public void filterApiWithWatchlistData(String coins, Context context){
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder().build();

        AndroidNetworking.initialize(context,okHttpClient);

        AndroidNetworking.setParserFactory(new GsonParserFactory());

        AndroidNetworking.get("https://api.coingecko.com/api/v3/coins/markets?x_cg_demo_api_key=&vs_currency=usd&ids=&sparkline=false&locale=en")
                .addQueryParameter("x_cg_demo_api_key", context.getString(R.string.COINGECKO_API_KEY))
                .addQueryParameter("ids",coins)
                .setTag("Watchlist")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0; i< response.length(); i++){
                            try {
                                JSONObject apiData = response.getJSONObject(i);

                                CryptoDataModel model = new CryptoDataModel();
                                model.setName(apiData.getString("name"));
                                model.setSymbol(apiData.getString("symbol"));
                                model.setImage(apiData.getString("image"));
                                model.setCurrent_price(apiData.getDouble("current_price"));
                                model.setPrice_change_percentage_24h(apiData.getDouble("price_change_percentage_24h"));
                                model.setType("usd");
                                model.setId(apiData.getString("id"));
                                data.add(model);

                            } catch (JSONException e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                throw new RuntimeException(e);
                            }
                        }

                        adapter = new MarketAdapter(getContext(), data);
                        progressBar.setVisibility(View.INVISIBLE);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e("ErrWatchlistApi", anError.getErrorBody());
                    }
                });
    }

    public void setWatchlistRecycler(Context context){

        SharedPreferences preferences = getContext().getSharedPreferences("MajorProject", Context.MODE_PRIVATE);
        String email = preferences.getString("email",null);

        MongoCollection<Document> collection = mongoDatabase.getCollection(context.getString(R.string.MONGO_DB_USER_COLLECTION));


        if(email!=null) {
            Document document = new Document("user_id", user.getId()).append("email",email);
            collection.find(document)
                    .first()
                    .getAsync(new App.Callback<Document>() {
                        @Override
                        public void onResult(App.Result<Document> result) {
                            if (result.isSuccess()) {
                                if (result.get() != null) {
                                    data.clear();
                                    List<Document> watchlist = result.get().getList("watchlist", Document.class);
                                    if (watchlist != null) {
                                        emptyListLayout.setVisibility(View.GONE);
                                        for (Document watchlistItem : watchlist) {
                                            String coin_name = watchlistItem.getString("coin_name");
                                            String coin_symbol = watchlistItem.getString("coin_symbol");
                                            String id = watchlistItem.getString("coin_id");
                                            //Filter api and get data that are added in watchlist
                                            if (coins != null) {
                                                coins = coins + "," + id;
                                            } else {
                                                coins = id;
                                            }
                                        }
                                        if (coins != null) {
                                            filterApiWithWatchlistData(coins, context);
                                        }else {
                                            emptyListLayout.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);
                                            emptyListAnim.playAnimation();
                                        }
                                    }else {
                                        emptyListLayout.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        emptyListAnim.playAnimation();
                                    }
                                }
                            } else {
                                Log.e("ErrWatchlistDatabase", result.getError().toString());
                            }
                        }
                    });
        }


       /* StringRequest request = new StringRequest(Request.Method.GET, apiurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = jsonObjectData.getJSONArray("cryptoCurrencyList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject apiData = jsonArray.getJSONObject(i);

                        CryptoDataModel model = new CryptoDataModel();
                        model.setName(apiData.getString("name"));
                        model.setSymbol(apiData.getString("symbol"));

                        JSONArray arrayQuots = apiData.getJSONArray("quotes");
                        for (int j = 0;j < arrayQuots.length(); j++) {
                            JSONObject apiData2 = arrayQuots.getJSONObject(j);
                            model.setCurrent_price(apiData2.getDouble("price"));
                            model.setPrice_change_percentage_24h(apiData2.getDouble("percentChange24h"));
                            model.setImage("https://cryptologos.cc/logos/"+ apiData.getString("slug").toLowerCase() +"-"+apiData.getString("symbol").toLowerCase()+"-logo.png");
                            model.setType("usd");
                        }

                        //Watchlist data get
                        MongoCollection<Document> collection = mongoDatabase.getCollection(getString(R.string.MONGO_DB_USER_COLLECTION));
                        Document filteredData = new Document("user_id",user.getId()).append("watchlist.coin_name",apiData.getString("name"));

                        collection.find(filteredData).first().getAsync(new App.Callback<Document>() {
                            @Override
                            public void onResult(App.Result<Document> result) {
                                if(result.isSuccess()){
                                    if(result.get()!=null){
                                        data.add(model);
                                    }
                                }else{
                                    Log.e("ErrWatchlistFind", result.getError().toString());
                                }
                                counterQuery++;
                                if(counterQuery== jsonArray.length()){
                                    adapter = new MarketAdapter(getContext(), data);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                error.printStackTrace();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);*/
    }

  /*  public void filterApiWithWatchlistData(String coin_name){
        //Get Data from watchlist records
        String api = "https://api.coingecko.com/api/v3/coins/" + coin_name;
        StringRequest request = new StringRequest(Request.Method.GET, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objData = new JSONObject(response);

                    //Get currency name and symbol of first object that here objData
                    CryptoDataModel model = new CryptoDataModel();
                    model.setSymbol(objData.getString("symbol"));

                    //Set objLocalization  Of objData
                    JSONObject objLocalization = objData.getJSONObject("localization");
                    model.setName(objLocalization.getString("en"));

                    //Get JSONObject of images from objData
                    JSONObject objImg = objData.getJSONObject("image");
                    model.setImage(objImg.getString("large"));

                    //Get objMarketData JSONObject from objData
                    JSONObject objMarketData = objData.getJSONObject("market_data");
                    //Get objCurrentPrice JSONObject from objMarketData
                    JSONObject objCurrentPrice = objMarketData.getJSONObject("current_price");

                    model.setCurrent_price(objCurrentPrice.getDouble("usd"));

                    //setPrice_change_percentage_24h() to the JSONObject objMarketData
                    model.setPrice_change_percentage_24h(objMarketData.getDouble("price_change_percentage_24h"));

                    model.setType("usd");

                    data.add(model);
                } catch (JSONException e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.e("GETERR",e.getMessage().toString());
                }
                adapter = new MarketAdapter(getContext(), data);
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.getMessage()!=null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.e("ErrWatchlistDataApi", error.getMessage().toString());
                }
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }*/
}
