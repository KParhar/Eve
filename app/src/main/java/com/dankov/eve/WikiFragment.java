package com.dankov.eve;


import android.os.Bundle;
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
public class WikiFragment extends ServiceFragment {

    EditText wikiSearch;
    Button wikiButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wiki, container, false);

        wikiSearch = (EditText) view.findViewById(R.id.wikiSearch);
        wikiButton = (Button) view.findViewById(R.id.wikiButton);

        wikiSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendWikiData();
                    return true;
                }
                return false;
            }
        });

        wikiButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view) {
                sendWikiData();
            }
        });

        return view;
    }

    void sendWikiData() {
        if(!wikiSearch.getText().toString().isEmpty()) {
            sendSMS("wiki " + wikiSearch.getText().toString());
            wikiSearch.setText("");
        } else {
            Toast.makeText(currActivity.getApplicationContext(), "Enter the Data", Toast.LENGTH_SHORT).show();
        }
    }

}
