package android.example.com;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;


public final class QueryUtils{


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    /**
     * Return a list of {@link //Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<EarthQuakes> extractEarthquakes(String urlString) {

        // Create an empty ArrayList that we can start adding earthquakes to
        String jsonResponse = null;

        URL url = createUrl(urlString);
        try{
            jsonResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "ERROR Closing input stream", e);
        }
        ArrayList<EarthQuakes> earthquakes = extractFromJson(jsonResponse);

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.


        // Return the list of earthquakes
        return earthquakes;
    }
    private static ArrayList<EarthQuakes> extractFromJson(String jsonString){
        ArrayList<EarthQuakes> earthquakes = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray features = root.getJSONArray("features");
            for(int i=0; i< features.length(); i++){
                JSONObject earthquakeObj = features.getJSONObject(i);
                JSONObject property = earthquakeObj.getJSONObject("properties");
                double mag = property.getDouble("mag");
                String location = property.getString("place");
                long time = property.getLong("time");
                EarthQuakes earthquake = new EarthQuakes(location, mag, time);
                earthquakes.add(earthquake);
            }
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }
    private static URL createUrl(String requestUrl){
        URL url = null;
        try{
            url = new URL(requestUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG, "Error response code: "+ urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG, "Problem receiving internet data ", e);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}