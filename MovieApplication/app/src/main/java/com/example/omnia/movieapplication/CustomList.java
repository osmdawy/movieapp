package com.example.omnia.movieapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by omnia on 7/22/15.
 */
public class CustomList extends ArrayAdapter<String> {
    private final Activity context;

    private  ArrayList<String> imagesUrl;
    private  ArrayList<String> ids;
    public CustomList(Activity context, ArrayList<String> imagesUrl) {
        super(context, R.layout.list_item_film, imagesUrl);
        this.context = context;
        this.imagesUrl = imagesUrl;
        ids = new ArrayList<String>();

    }


    public ArrayList<String> getImagesUrl() {
        return imagesUrl;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item_film, null, true);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.one_film_image);
        Picasso.with(context).load(imagesUrl.get(position)).into(imageView);
        return rowView;
    }
}
