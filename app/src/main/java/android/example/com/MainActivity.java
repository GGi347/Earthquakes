package android.example.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EarthquakeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView earthquakeLV = (ListView) findViewById(R.id.list);
        mAdapter = new EarthquakeAdapter(this,  new ArrayList<EarthQuakes>());
        earthquakeLV.setAdapter(mAdapter);
        String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";
        EarthquakeAsynTask task = new EarthquakeAsynTask();
        task.execute(url);

    }
    private class EarthquakeAsynTask extends AsyncTask<String, Void, List<EarthQuakes>>{

        @Override
        protected List<EarthQuakes> doInBackground(String... urls) {
                if(urls.length < 1 || urls[0] == null){
                    return null;
                }
                ArrayList<EarthQuakes> earthquakes = QueryUtils.extractEarthquakes(urls[0]);



            return earthquakes;

        }
        protected void onPostExecute(List<EarthQuakes> earthquakes){
            mAdapter.clear();
            if (earthquakes != null && !earthquakes.isEmpty()) {
                mAdapter.addAll(earthquakes);
            }
        }
    }

}
