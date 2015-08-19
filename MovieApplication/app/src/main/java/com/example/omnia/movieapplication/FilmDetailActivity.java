package com.example.omnia.movieapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import data.movie.MovieCursor;
import data.movie.MovieSelection;


public class FilmDetailActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        Intent intent = getIntent();
        Long id = intent.getLongExtra(getString(R.string.film_id), 1);
        String[] result = parseResultFromCursor(id);
        changeView(result);
    }
    public String[] parseResultFromCursor(long movieId){
        MovieSelection where = new MovieSelection();
        where.id(movieId);
        MovieCursor cursor = where.query(getContentResolver());
        cursor.moveToFirst();
        String [] result = new String[5];
        if(cursor!=null) {
            result[0] = cursor.getTitle();
            result[1] = cursor.getPlotSynopsis();
            result[2] = cursor.getPosterUrl();
            result[3] = cursor.getVoteAverage() + "";
            result[4] = cursor.getReleaseDate();
        }
        cursor.close();
        return  result;
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

}
