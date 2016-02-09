package com.example.veyndan.readerforxkcd.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.veyndan.readerforxkcd.model.Comic;
import com.example.veyndan.readerforxkcd.R;
import com.example.veyndan.readerforxkcd.adapter.MainAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {

    public HomeFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        List<Comic> comics = new ArrayList<>();
        comics.add(new Comic(
                "https://imgs.xkcd.com//comics//super_bowl_context.png",
                "Super Bowl Context",
                (short) 1640,
                "Why did the chicken cross the road? It begins over five thousand years ago with " +
                        "the domestication of the red junglefowl in southeast Asia and the " +
                        "development of paved roads in the Sumerian city of Ur."
        ));
        comics.add(new Comic(
                "https://imgs.xkcd.com//comics//to_taste.png",
                "To Taste",
                (short) 1639,
                "Look, recipe, if I knew how much was gonna taste good, I wouldn't need you."
        ));
        comics.add(new Comic(
                "https://imgs.xkcd.com//comics//backslashes.png",
                "Backslashes",
                (short) 1638,
                "I searched my .bash_history for the line with the highest ratio of special " +
                        "characters to regular alphanumeric characters, and the winner was: cat " +
                        "out.txt | grep -o \\\"[[(].*[])][^)]]*$\\\" ... I have no memory of this " +
                        "and no idea what I was trying to do, but I sure hope it worked."
        ));
        comics.add(new Comic(
                "https://imgs.xkcd.com//comics//salt_mine.png",
                "Salt Mine",
                (short) 1637,
                "This one is a little bland. Pass the saltshaker?"
        ));
        comics.add(new Comic(
                "https://imgs.xkcd.com//comics//xkcd_stack.png",
                "XKCD Stack",
                (short) 1636,
                "This site requires Sun Java 6.0.0.1 (32-bit) or higher. You have Macromedia " +
                        "Java 7.3.8.1\\u00c2\\u00be (48-bit). Click here [link to java.com main " +
                        "page] to download an installer which will run fine but not really " +
                        "change anything."
        ));
        comics.add(new Comic(
                "https://imgs.xkcd.com//comics//birdsong.png",
                "Birdsong",
                (short) 1635,
                "Maybe if I put it in a box for a while with a speaker playing some pleasant " +
                        "pastoral music, I can reprogram it."
        ));
        comics.add(new Comic(
                "https://imgs.xkcd.com//comics//in_case_of_emergency.png",
                "In Case of Emergency",
                (short) 1634,
                "I keep first aid kits in those emergency lockers. Sure, it's expensive to have " +
                        "them installed in the wall, but at least for those ones there's no need " +
                        "to pay extra for safety glass."
        ));
        comics.add(new Comic(
                "https://imgs.xkcd.com//comics//possible_undiscovered_planets.png",
                "Possible Undiscovered Planets",
                (short) 1633,
                "Superman lies near the bird\\/plane boundary over a range of distances, " +
                        "which explains the confusion."
        ));
        recyclerView.setAdapter(new MainAdapter(getActivity(), comics));
        return rootView;
    }

}
