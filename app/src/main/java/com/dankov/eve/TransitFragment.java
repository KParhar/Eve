package com.dankov.eve;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
public class TransitFragment extends ServiceFragment {

    AlertDialog.Builder dbBuilder;
    AlertDialog db;
    View dbView;

    EditText transitSearch;
    Button transitButton;

    TextView transitArticle;
    FloatingActionButton openDB;

    public TransitFragment(){
        prefix = "<Tr>";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transit, container, false);

        transitArticle = (TextView) view.findViewById(R.id.transitArticle);

        dbBuilder = new AlertDialog.Builder(currActivity);
        dbView = getLayoutInflater().inflate(R.layout.transit_send_data_db, null);

        transitSearch = (EditText) dbView.findViewById(R.id.transitArticle);
        transitButton = (Button) dbView.findViewById(R.id.transitButton);

        transitSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendWikiData();
                    return true;
                }
                return false;
            }
        });

        transitButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                sendWikiData();
            }
        });

        dbBuilder.setView(dbView);
        db = dbBuilder.create();


        openDB = view.findViewById(R.id.transitOpenSearch);
        openDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.show();
            }
        });

        return view;
    }
    public void sendWikiData() {
        if(!transitSearch.getText().toString().isEmpty()) {
            sendSMS("wiki " + transitSearch.getText().toString());
            db.hide();
            transitArticle.setText("Loading Article");
            transitSearch.setText("");
            db.hide();
        } else {
            Toast.makeText(currActivity.getApplicationContext(), "Enter the Data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void recieveSMS(String text) {

    }

    @Override
    public String prefix() {
        return prefix;
    }
}