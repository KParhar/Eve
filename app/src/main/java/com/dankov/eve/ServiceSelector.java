package com.dankov.eve;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceSelector extends MainActivity.ScreenFragment {

    public static final int NUMBER_SERVICES = 3;
    Button services[] = new Button[NUMBER_SERVICES];

    public ServiceSelector() {
        //initApps((LinearLayout) (getActivity().findViewById(R.id.service_selector_layout)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_service_selector, container, false);
    }

    public void initApps(LinearLayout layout) {
        for(int i = 0; i < NUMBER_SERVICES; i++) {
            services[i] = new Button(getActivity());
            services[i].setText("App " + i);

            //layout.addView(services[i]);
        }
    }

}
