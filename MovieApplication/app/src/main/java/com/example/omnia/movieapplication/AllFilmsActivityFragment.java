package com.example.omnia.movieapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class AllFilmsActivityFragment extends Fragment {


    CustomList filmAdapter;

    public AllFilmsActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateFilms();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(),SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return true;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_all_films, container, false);

        GridView filmView = (GridView)rootView.findViewById(R.id.all_films);

        filmAdapter = new CustomList(getActivity(),new ArrayList<String>());
        filmView.setAdapter(filmAdapter);
        filmView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String film_id = filmAdapter.getIds().get(position);
                Intent intent = new Intent(getActivity(), FilmDetailActivity.class);
                intent.putExtra(getString(R.string.film_id),film_id);
                startActivity(intent);
            }
        });
        return rootView;
    }
    public void updateFilms(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String order = sharedPref.getString(getString(R.string.order_key), getString(R.string.pref_order));
        String pageNum = "1";
        new FetchFilmsTask().execute(order, ApiKey.API_KEY, pageNum);
    }
    private class FetchFilmsTask extends AsyncTask<String, Void, ArrayList<String>[]>{
        @Override
        protected  ArrayList<String>[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                Uri.Builder builder = new Uri.Builder();
                builder.encodedPath("http://api.themoviedb.org/3/discover/movie");

                builder.appendQueryParameter("sort_by", params[0]);
                builder.appendQueryParameter("api_key", params[1]);
                builder.appendQueryParameter("page", params[2]);
                URL url = new URL(builder.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                String allMovies = buffer.toString();
                return convertJsonIntoArrays(allMovies);

            }catch (MalformedURLException e){
                Log.e("MalformedURLException",e.getMessage());
            }catch (IOException e){
                Log.e("IOException",e.getMessage());
            }catch (JSONException e){
                Log.e("JSONException",e.getMessage());
            }

            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<String>[] arrayLists) {
//            filmAdapter.clear();
//            Log.v("check titles",arrayLists[1].get(0));
            filmAdapter.getIds().clear();

            filmAdapter.getImagesUrl().clear();
            filmAdapter.getIds().addAll(arrayLists[0]);
            filmAdapter.getImagesUrl().addAll(arrayLists[1]);
            filmAdapter.notifyDataSetChanged();
        }
    }
    private ArrayList<String>[] convertJsonIntoArrays(String jsonResult) throws JSONException {
        final String FILMS_LIST = "results";
        final String FILM_ID = "id";
        final String FILM_TITLE = "title";
        final String FILM_IMAGE = "poster_path";
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> imageUrls = new ArrayList<String>();

        JSONObject forecastJson = new JSONObject(jsonResult);
        JSONArray filmsArray = forecastJson.getJSONArray(FILMS_LIST);
        for (int i =0;i<filmsArray.length();i++) {
            JSONObject film = filmsArray.getJSONObject(i);
            ids.add(film.getString(FILM_ID));
            imageUrls.add("http://image.tmdb.org/t/p/w185/"+film.getString(FILM_IMAGE));

        }
        int size = ids.size();

        ArrayList<String>[] result = new ArrayList[2];
        result[0] =ids;
        result[1] = imageUrls;
        return result;
    }
}
