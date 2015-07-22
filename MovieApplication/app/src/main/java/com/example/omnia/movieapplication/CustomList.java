package com.example.omnia.movieapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by omnia on 7/22/15.
 */
public class CustomList extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> filmTitles;
    private final ArrayList<String> imagesUrl;
    public CustomList(Activity context, ArrayList<String> filmTitles, ArrayList<String> imagesUrl) {
        super(context, R.layout.list_item_film, filmTitles);
        this.context = context;
        this.filmTitles = filmTitles;
        this.imagesUrl = imagesUrl;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item_film, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.film_title);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.one_film_image);
        txtTitle.setText(filmTitles.get(position));
        Picasso.with(context).load(imagesUrl.get(position)).into(imageView);
        return rowView;
    }
}
