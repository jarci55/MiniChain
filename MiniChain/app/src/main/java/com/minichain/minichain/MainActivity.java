package com.minichain.minichain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// Interface class - contains 2 Buttons pointing to selecting pre-set scenarios or to create custom ones

public class MainActivity extends AppCompatActivity {

    public Button selectButton;
    public Button createButton;

    public void init () {
        selectButton = (Button) findViewById(R.id.selectButton);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch = new Intent(MainActivity.this, SelectScenario.class);
                startActivity(launch);
            }
        });
        createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch = new Intent(MainActivity.this, CreateScenario.class);
                startActivity(launch);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
}
