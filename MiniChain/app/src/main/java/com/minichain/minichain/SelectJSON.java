package com.minichain.minichain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectJSON extends AppCompatActivity {


    android.support.v7.widget.Toolbar toolbar;
    Button backButton;
    Button runTestButton;
    Button viewTestButton;
    Button viewExpectedButton;
    Bundle bundle;
    String current;
    String[] arr = new String[2];
    String selectedTest = "";
    String selectedExpected = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_json);
        toolbar = findViewById(R.id.toolbar);
        bundle = getIntent().getExtras();
        if (bundle!= null){
            current = bundle.getString("TestName");
            toolbar.setTitle(current);
        }

        selectTestFile();

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectJSON.this, SelectScenario.class);
                startActivity(intent);
            }
        });
        runTestButton = (Button) findViewById(R.id.runTestButton);
        runTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectJSON.this, StartActivity.class);
                intent.putExtra("TestSelected", selectedTest);
                intent.putExtra("ExpectedFile", selectedExpected);
                startActivity(intent);
            }
        });

        viewTestButton = (Button) findViewById(R.id.viewTestButton);
        viewTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectJSON.this, ViewTest.class);
                intent.putExtra("TestSelected", selectedTest);
                startActivity(intent);
            }
        });

        viewExpectedButton = (Button) findViewById(R.id.viewComparisonFileButton);
        if (selectedTest.equals("Tests/default.json")){
            viewExpectedButton.setVisibility(View.INVISIBLE);
        }
        viewExpectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectJSON.this, ViewExpected.class);
                intent.putExtra("ExpectedFile", selectedExpected);
                startActivity(intent);
            }
        });




    }
    public void selectTestFile(){

        if (current.equals("Scenario_1")){
            selectedTest = "Tests/1_test.json";
            selectedExpected = "Expected/1_expected.txt";
        }
        else if (current.equals("Scenario_2")){
            selectedTest = "Tests/2_test.json";
            selectedExpected = "Expected/2_expected.txt";
        }
        else if (current.equals("Scenario_3")){
            selectedTest = "Tests/3_test.json";
            selectedExpected = "Expected/3_expected.txt";
        }
        else if (current.equals("Scenario_4")){
            selectedTest = "Tests/4_test.json";
            selectedExpected = "Expected/4_expected.txt";
        }
        else if (current.equals("Scenario_5")){
            selectedTest = "Tests/5_test.json";
            selectedExpected = "Expected/5_expected.txt";
        }
        else if (current.equals("Scenario_6")){
            selectedTest = "Tests/6_test.json";
            selectedExpected = "Expected/6_expected.txt";
        }
        else if (current.equals("Scenario_7")){
            selectedTest = "Tests/7_test.json";
            selectedExpected = "Expected/7_expected.txt";
        }
        else if (current.equals("Scenario_8")){
            selectedTest = "Tests/8_test.json";
            selectedExpected = "Expected/8_expected.txt";
        }
        else if (current.equals("Scenario_9")){
            selectedTest = "Tests/9_test.json";
            selectedExpected = "Expected/9_expected.txt";
        }
        else if (current.equals("Scenario_10")){
            selectedTest = "Tests/10_test.json";
            selectedExpected = "Expected/10_expected.txt";
        }
        else if (current.equals("Default_Scenario")){
            selectedTest = "Tests/default.json";
        }
        else if (current.equals("Large_Scale_Scenario")){
            selectedTest = "Tests/large_scale_test.json";
        }

    }
}
