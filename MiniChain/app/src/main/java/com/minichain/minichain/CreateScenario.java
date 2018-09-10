package com.minichain.minichain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CreateScenario extends AppCompatActivity {


    String result = "{ \"name\": \"Custom\",\n" +
                " \"seed\": 123456789 ,\n" +
                " \"start\": 0,\n" +
                " \"end\": 5000,\n" +
                " \"speed\": 1,\n" +
                " \"nodes\": [ \n" +
                " {\"id\": \"node_1\",\n" +
                " \"class\": \"SimulatedReadNode\",\n" +
                " \"permission\": 0\n" +
                " },\n" +
                " {\"id\": \"node_2\",\n" +
                " \"class\": \"SimulatedBlockWriteNode\",\n" +
                " \"permission\": 2\n" +
                " },\n" +
                " {\"id\": \"node_3\",\n" +
                " \"class\": \"MyTransactionNode\",\n" +
                " \"payload\": \"ExamplePayload\", \n" +
                " \"permission\": 1,\n" +
                " \"ticksPerTransaction\": 500 \n" +
                " }],\n" +
                "  \"logging\": {\n" +
                "  \"nodes\": [\"node_1\", \"node_2\"]\n" +
                "   } \n "+
                "}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scenario);
        Button startButton = (Button) findViewById(R.id.startCustomButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateScenario.this, StartActivity.class);
                intent.putExtra("TestSelected", "Custom.json");
                startActivity(intent);
            }
        });


    }
    public void writeJson(View view) {
        System.out.println(result);
        IOHelper.writeToFile(this, "Custom.json", result);
    }
}
