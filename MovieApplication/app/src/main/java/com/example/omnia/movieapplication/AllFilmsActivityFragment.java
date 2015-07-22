package com.example.omnia.movieapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class AllFilmsActivityFragment extends Fragment {

    ArrayList<String> filmTitles = new ArrayList<String>();
    ArrayList<String> imageId = new ArrayList<String>();
    public AllFilmsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        filmTitles.add("Minions");
        filmTitles.add("Frozen");
        filmTitles.add("Beauty and the beast");
        filmTitles.add("Monsters inc");
        imageId.add("http://p1.pichost.me/i/40/1636199.jpg");
        imageId.add( "http://www4.pictures.zimbio.com/mp/qnDlR7TNXYMx.jpg");
        imageId.add("http://ecx.images-amazon.com/images/I/81etFyb9N-L._SL1500_.jpg");
        imageId.add("http://employers-lawyer.com/wp-content/uploads/2014/05/monstersinc.jpg");
        View rootView = inflater.inflate(R.layout.fragment_all_films, container, false);

        GridView filmView = (GridView)rootView.findViewById(R.id.all_films);
        CustomList filmAdapter = new CustomList(getActivity(),filmTitles,imageId);

        filmView.setAdapter(filmAdapter);


        return rootView;
    }
}
