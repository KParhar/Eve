package com.dankov.eve;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class WikiFragment extends ServiceFragment {

    AlertDialog.Builder dbBuilder;
    AlertDialog db;
    View dbView;

    EditText wikiSearch;
    Button wikiButton;

    FloatingActionButton openDB;
    TextView wikiArticle;
    TextView wikiTitle;

    //Character Codes
    final char titleChar = '£';


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inst = this;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wiki, container, false);

        wikiArticle = (TextView) view.findViewById(R.id.wikiArticle);
        wikiTitle = (TextView) view.findViewById(R.id.wikiTitle);
        dbBuilder = new AlertDialog.Builder(currActivity);
        dbView = getLayoutInflater().inflate(R.layout.wiki_send_data_db, null);

        wikiSearch = (EditText) dbView.findViewById(R.id.wikiSearch);
        wikiButton = (Button) dbView.findViewById(R.id.wikiButton);

        wikiSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendWikiData();
                    return true;
                }
                return false;
            }
        });
        wikiArticle.setMovementMethod(new ScrollingMovementMethod());

        wikiButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                sendWikiData();
            }
        });

        dbBuilder.setView(dbView);
        db = dbBuilder.create();

        openDB = view.findViewById(R.id.wikiOpenSearch);
        openDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.show();
            }
        });

        //db.show();

        return view;
    }

    public void sendWikiData() {
        if(!wikiSearch.getText().toString().isEmpty()) {
            sendSMS("eve wiki " + wikiSearch.getText().toString());
            wikiArticle.setText("Loading Article");
            wikiSearch.setText("");
            db.hide();
        } else {
            Toast.makeText(currActivity.getApplicationContext(), "Enter the Data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void recieveSMS(String text){
        //This won't run when were not using twilio trial
        if(text.startsWith("Sent from your Twilio")){
            int hyphenIndex = text.indexOf('-');
            //We add 2 to hyphen index because theres a space after it
            text = text.substring(hyphenIndex + 2);
        }

        //You cannot explicitly cast char to String
        if(text.contains(titleChar + "")){
            String title = "";
            int firstChar = text.indexOf(titleChar);
            title = text.substring(firstChar+1);
            int secondChar = title.indexOf(titleChar);
            title = title.substring(0,secondChar);
            wikiTitle.setText(title + "");
            text = text.substring(0,firstChar) + text.substring(secondChar+2);
        }

        wikiArticle.setText(text);

    }

    public String prefix(){ return "wiki "; }

}
