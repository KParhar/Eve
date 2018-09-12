package com.dankov.eve;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class DirectionsFragment extends ServiceFragment {
    AlertDialog.Builder dbBuilder;
    AlertDialog db;
    View dbView;

    EditText directionsOrigin, directionsDestination;
    Button directionsButton;

    TextView directionsArticle;
    FloatingActionButton openDB;
    public DirectionsFragment(){
        prefix = "<D>";
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_directions, container, false);

        directionsArticle = (TextView) view.findViewById(R.id.directionsArticle);

        dbBuilder = new AlertDialog.Builder(currActivity);
        dbView = getLayoutInflater().inflate(R.layout.directions_send_data_db, null);

        directionsOrigin = (EditText) dbView.findViewById(R.id.directionsOrigin);
        directionsDestination = (EditText) dbView.findViewById(R.id.directionsDestination);
        directionsButton = (Button) dbView.findViewById(R.id.directionsButton);

        directionsButton.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendDirectionsData();
                    return true;
                }
                return false;
            }
        });
        directionsButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                sendDirectionsData();
            }
        });

        dbBuilder.setView(dbView);
        db = dbBuilder.create();


        openDB = view.findViewById(R.id.directionsOpenSearch);
        openDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.show();
            }
        });

        return view;
    }
    public void sendDirectionsData() {
        if(!directionsDestination.getText().toString().isEmpty() || !directionsOrigin.getText().toString().isEmpty()) {
            sendSMS("eve directions " + directionsOrigin.getText().toString() + "," + directionsDestination.getText().toString());
            directionsArticle.setText("");
            directionsDestination.setText("");
            directionsOrigin.setText("");
            db.hide();
        } else {
            Toast.makeText(currActivity.getApplicationContext(), "Enter the Data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void recieveSMS(String text) {
        directionsArticle.setText(text);
    }
    public String prefix(){
        return prefix;
    }
}