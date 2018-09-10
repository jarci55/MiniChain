package com.minichain.minichain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//Activity class containing list of pre-set scenarios

public class SelectScenario extends AppCompatActivity {


    ListView JSONlist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_scenario);
        JSONlist = (ListView) findViewById(R.id.JSON_list);

        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.jsonfiles));


        JSONlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              Intent intent = new Intent(SelectScenario.this, SelectJSON.class);
              intent.putExtra("TestName", JSONlist.getItemAtPosition(i).toString());
              startActivity(intent);

                }
    });
        JSONlist.setAdapter(adapter);





    }
}
