package androidapps.ntippa.org.myfirstlocationapp;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName() ;
    private GoogleMap mMap;

    MarkerOptions place1;

    static final CameraPosition SEATTLE = CameraPosition.builder()
                                            .target(new LatLng(40.555376, -74.381778))
                                            .zoom(10).bearing(0).tilt(45).build();

    LatLng place2 = new LatLng(40.558474, -74.380813);
    LatLng place3 = new LatLng(40.557527, -74.353853);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        place1 = new MarkerOptions().position(new LatLng(47.00,-125.00)).title("place1");



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG,"onMapReady");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
      //  LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newCameraPosition(SEATTLE));
       // mMap.addMarker(new MarkerOptions().position(place2).title("A"));
//        Polyline result_polyline = mMap.addPolyline(new PolylineOptions().geodesic(true).add(place2).add(place3));
//
//
//        Log.d(TAG, "LatLng from result polyline::" + result_polyline.getPoints().size());
//
//        List<LatLng> input = new ArrayList<>();
//        input.add(place2);
//        input.add(place3);
//
//        String encode_output = PolyUtil.encode(input);
//        Log.d(TAG, "encode done");
//
//        List<LatLng> decode_output = PolyUtil.decode(encode_output);
//
//        Log.d(TAG," No.of latlng after decode" + decode_output.size());

       // PolyUtil.encode(result_polyline.getPoints());//result_polyline.toString();


        // Origin of route
        //String str_origin = "origin="+origin.latitude+","+origin.longitude;
        String origin_city = "Boston";
        String dest_city = "Edison";
        //String str_origin = "origin="+place2.latitude+","+place2.longitude;
        String str_origin = "origin="+origin_city;

        // Destination of route
       // String str_dest = "destination="+place3.latitude+","+place3.longitude;
        String str_dest = "destination="+dest_city;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        //String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;
        String parameters = str_origin+"&"+str_dest+"&"+sensor;


        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
       // String url = "https://maps.googleapis.com/maps/api/directions/json?origin=Chicago,IL&destination=Los+Angeles,CA&key=AIzaSyDIBV8W11iTh9ylkE9IQNCCIgoJY2r5EfI
        Log.d(TAG, "json output::" + url);

        new DownloadTask().execute(url);

    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG,"JSON::" + result);
        }
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception download url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

}
