package com.minichain.minichain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

//Class displying the txt file of expected outcome

public class ViewExpected extends AppCompatActivity {
    TextView textView;
    Bundle bundle;
    String selectedExpected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expected);

        bundle = getIntent().getExtras();
        if (bundle!= null){
            selectedExpected = bundle.getString("ExpectedFile");

        }

        textView = (TextView) findViewById(R.id.textViewExpected);

        String txt = "";

        try{

            InputStream is = getAssets().open(selectedExpected);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            txt = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView.setText(txt);


    }
}
