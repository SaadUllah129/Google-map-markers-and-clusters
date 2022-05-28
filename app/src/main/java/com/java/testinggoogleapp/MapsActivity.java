package com.java.testinggoogleapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.java.testinggoogleapp.Utils.Credentials;
import com.java.testinggoogleapp.databinding.ActivityMapsBinding;
import com.java.testinggoogleapp.listeners.JSONPlaceholder;
import com.java.testinggoogleapp.model.MyItem;
import com.java.testinggoogleapp.model.Post;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ArrayList<Post> addresses;
    private ClusterManager<MyItem> clusterManager;
    int zoom = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JSONPlaceholder jsonPlaceholder = retrofit.create(JSONPlaceholder.class);
        Call<JsonObject> addresses = jsonPlaceholder.getPost("","");
        addresses.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null){
                    readLocation(response.body().toString());

                } else {
                }
            }
            @Override
            public void onFailure(@NonNull  Call<JsonObject> call, @NonNull  Throwable t) {
                Log.v("API_TESTING",t.getMessage());
            }

        });
        mapFragment.getMapAsync(this);

    }
    private void setUpClusterer() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude()), zoom));

        clusterManager = new ClusterManager<MyItem>(this, mMap);
        mMap.setOnCameraIdleListener(clusterManager);
        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                if (zoom >= 7)
                    zoom = 3;
                else
                    zoom += 2;
                Log.i("ITEM",cluster.getItems()+"");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), zoom));
                return false;
            }
        });
        mMap.setOnMarkerClickListener(clusterManager);
        // Add cluster items (markers) to the cluster manager.
        addItems();
    }
    private void addItems() {

        double lat ;
        double lng ;

        for (int i = 0; i < addresses.size(); i++) {
            double offset = i / 60d;
            lat = addresses.get(i).getLatitude() + offset;
            lng = addresses.get(i).getLongitude() + offset;
            MyItem offsetItem = new MyItem(lat, lng, "Name " + addresses.get(i).getName(), "Address " + addresses.get(i).getAddressOne());
            clusterManager.addItem(offsetItem);
        }
    }

    //TODO: modify this function according to your APIs data
    private void readLocation(String body) {
        try{
            addresses = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(body);
            JSONArray jsonArray = jsonObject.getJSONArray("locationData");
            for (int i=0; i<jsonArray.length();i++ ){
                JSONObject obj = new JSONObject(jsonArray.get(i).toString());
                addresses.add(new Post(
                        obj.getString("id"),
                        obj.getString("addressOne"),
                        obj.getString("name"),
                        Double.parseDouble(obj.getString("latitude")),
                        Double.parseDouble(obj.getString("longitude"))
                ));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if (addresses.size()>0){
            setUpClusterer();
        }


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //you can learn more about getIoSettings on
//        https://developers.google.com/android/reference/com/google/android/gms/maps/UiSettings
//        Must visit it to know more builtin features
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setTrafficEnabled(true); // traffic enables
        mMap.setBuildingsEnabled(true);
        mMap.setMinZoomPreference(3f);
        mMap.setMaxZoomPreference(10f);
    }
}