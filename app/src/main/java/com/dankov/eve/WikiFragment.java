package com.dankov.eve;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;


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
    View view;

    LinearLayout rl;
    TextView summaryTitle;
    //Character Codes
    final char titleChar = '£';
    TextView[] TOCTable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inst = this;
        prefix = "<W>";
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wiki, container, false);
        rl = (LinearLayout) view.findViewById(R.id.scrolled_wiki);
        wikiArticle = (TextView) view.findViewById(R.id.wikiArticle);
        wikiTitle = (TextView) view.findViewById(R.id.wikiTitle);
        summaryTitle = (TextView) view.findViewById(R.id.WikiSummary);

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
            wikiArticle.setText("");
            wikiSearch.setText("");
            wikiTitle.setText("Loading Article");
            summaryTitle.setVisibility(TextView.INVISIBLE);
            db.hide();
            if(TOCTable != null) {
                for (int i = 0; i < TOCTable.length; i++) {
                    TOCTable[i].setVisibility(Button.GONE);
                }
            }
            TOCTable = null;
            TOCTitleString = "";

        } else {
            Toast.makeText(currActivity.getApplicationContext(), "Enter the Data", Toast.LENGTH_SHORT).show();
        }
    }
    boolean isTOCTitle = false;
    String TOCTitleString = "";
    @Override
    public void recieveSMS(String text){
        if(summaryTitle.getVisibility() == TextView.INVISIBLE){
            summaryTitle.setVisibility(TextView.VISIBLE);
        }
        //This won't run when were not using twilio trial
        if(text.startsWith("Sent from your Twilio")){
            int hyphenIndex = text.indexOf('-');
            //We add 2 to hyphen index because theres a space after it
            text = text.substring(hyphenIndex + 2);
        }
        if(text.contains("<W>")) {
            text = text.replaceAll("<W>","");
            //You cannot explicitly cast char to String
            if (text.contains(titleChar + "")) {
                String title = "";
                int firstChar = text.indexOf(titleChar);
                title = text.substring(firstChar + 1);
                int secondChar = title.indexOf(titleChar);
                title = title.substring(0, secondChar);
                wikiTitle.setText(title + "");
                text = text.substring(0, firstChar) + text.substring(secondChar + firstChar + 2);
            }
            if (text.contains("<E>")) {
                text = text.replaceFirst("<E>", "");
                wikiArticle.setText(text);
            } else {
                wikiArticle.append(text);
            }
        }
        if(text.contains("<T>")){
            isTOCTitle = true;
            text = text.replaceFirst("<T>","");
        }
        if(text.contains("</T>")){
            isTOCTitle = false;
            text = text.replaceFirst("</T>","");
            TOCTitleString += text;
            processTOCTitle();

        }
        if(isTOCTitle){
            TOCTitleString += text;
        }

    }
    private void processTOCTitle(){
        String remainder = TOCTitleString;
        int numberOfEntries = StringUtils.countMatches(TOCTitleString,"/S");
        TOCTable = new TextView[numberOfEntries*2];
        for(int i = 0; i < numberOfEntries*2;i++){
            String title = remainder.substring(remainder.indexOf("/S")+2,remainder.indexOf("/s"));
           title = title.replaceAll("_"," ");
           remainder = remainder.substring(remainder.indexOf("/s")+2);
            Button b = new Button(getActivity());
            TextView textV = new TextView(getActivity());
            textV.setText("Nice");
            b.setText(title);
            rl.addView(b);
            rl.addView(textV);
            TOCTable[i] = b;
            i++;
            TOCTable[i] = textV;
            textV.setVisibility(TextView.GONE);
        }
    }
    public String prefix(){ return "wiki "; }

}
