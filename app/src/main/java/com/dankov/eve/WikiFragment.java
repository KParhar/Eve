package com.dankov.eve;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class WikiFragment extends ServiceFragment {

    Button wikiButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wiki, container, false);

        wikiButton = (Button) view.findViewById(R.id.wikiButton);
        wikiButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                sendSMS("Test message 2");
            }
        });

        return view;
    }

}
