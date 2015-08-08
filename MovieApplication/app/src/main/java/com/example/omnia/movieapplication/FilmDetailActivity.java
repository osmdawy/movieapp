package com.example.omnia.movieapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class FilmDetailActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        Intent intent = getIntent();
        String message = intent.getStringExtra(getString(R.string.film_id));

        new FetchFilmTask().execute(message);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_film_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void changeView(String[] result){
        TextView title = (TextView)findViewById(R.id.film_original_title);
        title.setText(result[0]);
        TextView overview = (TextView)findViewById(R.id.film_overview);
        overview.setText(result[1]);
        ImageView imageView = (ImageView)findViewById(R.id.film_image);
        Picasso.with(this).load(result[2]).into(imageView);
        float rating = Float.parseFloat(result[3]);
        rating *= (5.0/10.0);
        RatingBar filmRating = (RatingBar) findViewById(R.id.film_rating);
        filmRating.setRating(rating);
        TextView release_date = (TextView)findViewById(R.id.film_release_date);
        release_date.setText(result[4]);
    }
    private class FetchFilmTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected String[] doInBackground(String... params) {
            String id = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                Uri.Builder builder = new Uri.Builder();
                builder.encodedPath("http://api.themoviedb.org/3/movie/");
                builder.appendEncodedPath(id);
                builder.appendQueryParameter("api_key",AllFilmsActivityFragment.API_KEY);
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
                String oneMovie = buffer.toString();
                return getFilmDetails(oneMovie);

            }catch (MalformedURLException e){
                Log.e("MalformedURLException", e.getMessage());
            }catch (IOException e){
                Log.e("IOException",e.getMessage());
            }catch (JSONException e){
                Log.e("JSONException",e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            changeView(result);

        }

        public String[] getFilmDetails(String jsonStr) throws JSONException {
            String [] result = new String [5];
            final String FILM_IMAGE = "poster_path";
            JSONObject filmJson = new JSONObject(jsonStr);
            result[0] = filmJson.getString("original_title");
            result[1] = filmJson.getString("overview");
            result[2] = "http://image.tmdb.org/t/p/w185/"+filmJson.getString(FILM_IMAGE);
            result[3] = filmJson.getString("vote_average");
            result[4] = filmJson.getString("release_date");
            return result;
        }
    }
}
