package com.example.omnia.movieapplication;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import data.movie.MovieCursor;

/**
 * Created by omnia on 7/22/15.
 */
public class CustomList  extends CursorAdapter {



    public CustomList(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_film, parent, false);

        return view;
    }


    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.
        ImageView imageView = (ImageView) view.findViewById(R.id.one_film_image);

        Picasso.with(context).load(((MovieCursor)cursor).getPosterUrl()).into(imageView);

    }
}
