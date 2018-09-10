package com.minichain.minichain;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

//class containing Activity displaying the JSON file of preset scenarios

public class ViewTest extends AppCompatActivity {

    Context context;
    TextView textView;
    String[] filelist;
    Bundle bundle;
    String selectedTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);
        bundle = getIntent().getExtras();
        if (bundle!= null){
            selectedTest = bundle.getString("TestSelected");

        }

        textView = (TextView) findViewById(R.id.textViewTest);

        try {

            String jsonString = IOHelper.stringFromAsset(this, selectedTest);
            JSONObject scenario = new JSONObject(jsonString);

            String result = "";
            result += "Name : " + scenario.getString("name") + "\n" +
                    "Seed : " + scenario.getString("seed") + "\n" +
                    "Duration : " + scenario.getString("end") + "\n";


            if (scenario.has("nodes")){
            JSONArray nodes =scenario.getJSONArray("nodes");

            System.out.println(result);

            for (int i = 0; i < nodes.length(); i++) {
                JSONObject node = nodes.getJSONObject(i);

                result += "id : " + node.getString("id") + "\n" +
                        "class : " + node.getString("class") + "\n";
                }

            }
            else if (scenario.has("many_nodes")) {
                JSONArray nodes = scenario.getJSONArray("many_nodes");

                System.out.println(result);

                for (int i = 0; i < nodes.length(); i++) {
                    JSONObject node = nodes.getJSONObject(i);

                    result += "id : " + node.getString("id_prefix") + "\n" +
                            "count : " + node.getInt("count") + "\n";
                }
            }
            textView.setText(result);
        } catch (Exception e) {
            Log.d("ReadPlacesFeedTask", e.getLocalizedMessage());
        }
    }
}

//
//
//
//        try {
//             filelist = assetManager.list("Tests");
//            InputStream is = am.open(assetFileName);
//            String result = IOHelper.stringFromStream(is);
//            is.close();
//            return result;
//
//
//            if (filelist == null) {
//                // dir does not exist or is not a directory
//            }
//            else {
//                System.out.println(getAssets().open(filelist[1]).toString());
//
//                textView.;
//            }
//
//            // if(filelistInSubfolder == null) ............
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
