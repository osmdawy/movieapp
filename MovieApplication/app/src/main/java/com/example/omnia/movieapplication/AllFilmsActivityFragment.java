package com.example.omnia.movieapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import data.movie.MovieCursor;
import data.movie.MovieSelection;



/**
 * A placeholder fragment containing a simple view.
 */
public class AllFilmsActivityFragment extends Fragment {


    CustomList filmAdapter;
    private static final int MOVIE_ADAPTER_ID  = 300;
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
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_all_films, container, false);
        final GridView filmView = (GridView) rootView.findViewById(R.id.all_films);
        updateFilms();
        MovieSelection where = new MovieSelection();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String order = sharedPref.getString(getString(R.string.order_key), getString(R.string.pref_order));
        if(order.equals("popularity.desc")){
            where.orderByPopularity(true);
        }else {
            where.orderByVoteAverage(true);
        }
        MovieCursor movie = where.query(getActivity().getContentResolver());
        filmAdapter = new CustomList(getActivity(),movie,0);
        filmView.setAdapter(filmAdapter);
        filmView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieCursor cursor = (MovieCursor) filmView.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), FilmDetailActivity.class);
                    intent.putExtra(getString(R.string.film_id), cursor.getId());
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    public void updateFilms() {

        new FetchFilmsTask(getActivity()).execute();
    }
}
