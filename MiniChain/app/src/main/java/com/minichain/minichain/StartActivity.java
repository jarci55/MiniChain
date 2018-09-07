package com.minichain.minichain;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;



public class StartActivity extends AppCompatActivity {
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> hashes = new ArrayList<String>();
    Bundle bundle;
    ListView mainList;
    RelativeLayout mainLayout;

    private static final String TAG = "MAIN";

    //public Button resultButton;
    String filename;
    String filename2;
    String payload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mainList = (ListView) findViewById(R.id.listView1);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        mainList.setAdapter(adapter);

        bundle = getIntent().getExtras();
        if (bundle!= null){
            filename = bundle.getString("TestSelected");
            filename2 = bundle.getString("ExpectedFile");
        }

        new MainTask().execute();
    }

    class MainTask extends AsyncTask<Void, String, Void>{

        private ArrayAdapter mAdapter;

        @Override
        protected void onPreExecute() {

            mAdapter = (ArrayAdapter) mainList.getAdapter();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                    int limit = 5000;

                    SimulatedNetwork network = new SimulatedNetwork();

                    ArrayList<String> watchNodes = new ArrayList<>();
                    ArrayList<Integer> watchSignals = new ArrayList<>();
                    ArrayList<String> watchSpecial = new ArrayList<>();
                    ArrayList<SimulatedNode> multipleNodes = new ArrayList<>();
                    ArrayList<String> nodeConnections = new ArrayList<>();

                    JsonReader reader;

                    try {
                        if (filename.equals("Custom.json")){
                            FileInputStream in = openFileInput(filename);
                            reader = new JsonReader(new InputStreamReader(in));
                        }
                        else{
                            InputStream in = getAssets().open(filename);
                            reader = new JsonReader(new InputStreamReader(in));
                        }
                    } catch (Exception e) {
                        System.out.println("Could not open configuration file '" + filename + "'. Recheck the filename and try again.");
                        return null;
                    }

                    try {
                        InputStream in2 = getAssets().open(filename2);
                        BufferedReader reader2 = new BufferedReader(new InputStreamReader(in2));

                        network.log.addOutputComparison(reader2);

                        System.out.println("(Output comparison file detected, testing will be performed.)");
                    } catch (Exception e) {
                        System.out.println("Could not open comparison file! Make sure to use an absolute path, not one relative to scenario json file.");
                        System.out.println("Will run this scenario without output comparisons.");
                    }

                    // Parse JSON configurations
                    reader.beginObject();

//        /*
////         * Parses inner JSON objects, and loads up configuration into the network. Possible values:
////         * name
////         * seed		(long)
////         * start 	(in ticks)
////         * end 		(in ticks)
////         * speed 	(in ticks)
////         * nodes		(describes an object with one node recipe)
////         * many_nodes 	(describes an object with multiple node recipes)
////         * logging		(describes what logging will be done to STDOUT)
////         *
////         * Detailed instructions can be found in dissertation
////         */
                    while (reader.hasNext()) {
                        switch (reader.nextName()) {
                            case "name":
                                System.out.println("Scenario name: " + reader.nextString());
                                break;
                            case "seed":
                                network.rng.setSeed(reader.nextLong());
                                break;
                            case "start":
                                network.currentTick = reader.nextInt();
                                break;
                            case "speed":
                                network.setTickSpeed(reader.nextInt());
                                break;
                            case "end":
                                limit = reader.nextInt();
                                break;
                            case "many_nodes":
                                reader.beginArray();
                                while (reader.hasNext()) {
                                    reader.beginObject();
                                    if (reader.nextName().equals("id_prefix")) {
                                        String prefix = reader.nextString();

                                        if (reader.nextName().equals("class")) {
                                            try {
                                                Class<?> nodeClass = Class.forName("com.minichain.minichain." + reader.nextString());

                                                int permission = 0;
                                                int count = 0;
                                                int upload = 0;
                                                int download = 0;
                                                int trans = 0;
                                                int minTrans = 0;
                                                String style = null;

                                                while (reader.hasNext()) {
                                                    switch (reader.nextName()) {
                                                        case "permission":
                                                            permission = reader.nextInt();
                                                            break;
                                                        case "count":
                                                            count = reader.nextInt();
                                                            break;
                                                        case "speedUpload":
                                                            upload = reader.nextInt();
                                                            break;
                                                        case "speedDownload":
                                                            download = reader.nextInt();
                                                            break;
                                                        case "transactionsPerSecond":
                                                            trans = reader.nextInt();
                                                            break;
                                                        case "minTransactions":
                                                            minTrans = reader.nextInt();
                                                            break;
                                                        case "style":
                                                            style = reader.nextString();
                                                            break;
                                                        default:
                                                            reader.nextString();
                                                    }
                                                }

                                                for (int a = 0; a < count; a++) {
                                                    SimulatedNode node = (SimulatedNode) nodeClass.newInstance();

                                                    node.setId(prefix + "_" + a);

                                                    if (upload > 0) {
                                                        node.setUploadSpeed(upload);
                                                    }
                                                    if (download > 0) {
                                                        node.setDownloadSpeed(download);
                                                    }

                                                    if (trans > 0) {
                                                        ((SimulatedSalesTransactionNode) node).setChangeForTransaction(count, trans);
                                                    }

                                                    if (minTrans > 0) {
                                                        ((SimulatedBlockWriteNode) node).setMinTransactions(minTrans);
                                                    }

                                                    if (style != null) {
                                                        ((SimulatedSalesTransactionNode) node).setStyle(style);
                                                    }

                                                    network.addNode(node);
                                                    network.addInitialAuth(node.getId(), permission);
                                                    multipleNodes.add(node);
                                                }
                                            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                                                System.out.println("Malformed JSON many_nodes, invalid class supplied: " + e.getMessage());
                                                return null;
                                            }
                                        } else {
                                            System.out.println("Malformed JSON many_nodes, class must be second field!");
                                            return null;
                                        }
                                    } else {
                                        System.out.println("Malformed JSON many_nodes, id_prefix must be first field!");
                                        return null;
                                    }
                                    reader.endObject();
                                }

                                reader.endArray();

                                break;
                            case "nodes":
                                // arrays of items
                                reader.beginArray();

                                while (reader.hasNext()) {
                                    reader.beginObject();

                                    SimulatedNode node = null;
                                    int connCost = 0;
                                    if (reader.nextName().equals("id")) {
                                        String id = reader.nextString();

                                        if (reader.nextName().equals("class")) {
                                            try {
                                                node = (SimulatedNode) Class.forName("com.minichain.minichain." + reader.nextString()).newInstance();
                                                node.setId(id);

                                                while (reader.hasNext()) {
                                                    switch (reader.nextName()) {
                                                        case "permission":
                                                            network.addInitialAuth(id, reader.nextInt());
                                                            break;
                                                        case "maxUploadConnections":
                                                            node.setUploadConnectionLimit(reader.nextInt());
                                                            break;
                                                        case "maxDownloadConnections":
                                                            node.setDownloadConnectionLimit(reader.nextInt());
                                                            break;
                                                        case "connectionCooldown":
                                                            node.setConnectionCooldown(reader.nextInt());
                                                            break;
                                                        case "connectionStale":
                                                            node.setConnectionStale(reader.nextInt());
                                                            break;
                                                        case "repetitionDisconnectThreshold":
                                                            node.setRepetitionDisconnectThreshold(reader.nextInt());
                                                            break;
                                                        case "connectionCost":
                                                            connCost = reader.nextInt();
                                                            break;
                                                        case "payload":
                                                            payload = reader.nextString();

                                                            break;

                                                        case "ticksPerTransaction":
                                                            if (node instanceof SimulatedTransactionNode) {
                                                                ((SimulatedTransactionNode) node).setTicksPerTransaction(reader.nextInt());
                                                            }
                                                            else if(node instanceof MyTransactionNode) {
                                                                ((MyTransactionNode) node).setTicksPerTransaction(reader.nextInt());
                                                            }
                                                                break;

                                                            default:
                                                            reader.nextString();
                                                    }
                                                }
                                            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                                                System.out.println("Malformed JSON node, invalid class supplied: " + e.getMessage());
                                                return null;
                                            }
                                        } else {
                                            System.out.println("Malformed JSON node, class must be second field!");
                                            return null;
                                        }
                                    } else {
                                        System.out.println("Malformed JSON node, id must be first field!");
                                        return null;
                                    }

                                    network.addNode(node);
                                    multipleNodes.add(node);

                                    if (connCost != 0) {
                                        network.nodeConnectionCosts.put(node.getId(), connCost);
                                    }

                                    reader.endObject();
                                }

                                reader.endArray();
                                break;

                            case "logging":
                                reader.beginObject();
                                while (reader.hasNext()) {
                                    switch (reader.nextName()) {
                                        case "signals":
                                            reader.beginArray();

                                            while (reader.hasNext()) {
                                                String sig = reader.nextString();
                                                try {
                                                    watchSignals.add(NodeSignal.class.getDeclaredField(sig + "_SIGNAL").getInt(null));
                                                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                                                    System.out.println("Invalid signal '" + sig + "' declared. Make sure to use signals without _SIGNAL appended.");
                                                    return null;
                                                }
                                            }

                                            reader.endArray();
                                            break;

                                        case "nodes":
                                            reader.beginArray();

                                            while (reader.hasNext()) {
                                                watchNodes.add(reader.nextString());
                                            }

                                            reader.endArray();

                                            break;
                                        case "special":
                                            reader.beginArray();
                                            while (reader.hasNext()) {
                                                watchSpecial.add(reader.nextString());
                                            }
                                            reader.endArray();
                                            break;
                                    }
                                }
                                reader.endObject();
                                break;

                            case "connections":
                                reader.beginArray();
                                while (reader.hasNext()) {
                                    nodeConnections.add(reader.nextString());
                                }
                                reader.endArray();
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }
                    }

                    reader.close();

                    network.log.addNodeWatch(watchNodes);
                    network.log.addSignalWatch(watchSignals);
                    network.log.addSpecialWatch(watchSpecial);

                    System.out.println("Scenario loaded successfully!");

                    for (SimulatedNode node : multipleNodes) {
                        node.addNetwork(network);
                        Log.d(TAG, "doInBackground: ADDING NODE NETWORK ");
                        }

                    if (nodeConnections.size() > 0) {
                        for (String conn : nodeConnections) {
                            String[] data = conn.split(" ");
                            SimulatedNode from = network.nodes.getOrDefault(data[0], null);
                            SimulatedNode to = network.nodes.getOrDefault(data[1], null);

                            int delay = network.getConnectionDelay(from.getId(), to.getId());
                            if (data.length == 4) {
                                delay = Integer.parseInt(data[3]);
                            }
                            switch (data[2]) {
                                case "U":
                                    to.establishDownloadConnection(from.getId(), delay);
                                    from.activeOutgoingConnections.put(to.getId(), delay);
                                    break;
                                case "D":
                                    to.establishUploadConnection(from.getId(), delay);
                                    from.activeIncomingConnections.put(to.getId(), delay);
                                    break;
                                case "UD":
                                case "DU":
                                    to.establishDownloadConnection(from.getId(), delay);
                                    to.establishUploadConnection(from.getId(), delay);

                                    from.activeOutgoingConnections.put(to.getId(), delay);
                                    from.activeIncomingConnections.put(to.getId(), delay);
                                    break;
                            }
                        }
                    }

                    System.out.println("Simulating network for " + limit + " ticks ...");

                    while (true) {

                        network.simulate();

                        for (SimulatedNode node : multipleNodes){
                            if (node instanceof MyTransactionNode) {
                                node.setPayload(payload);
                                if (((MyTransactionNode) node).trans != null) {
                                    System.out.println(((MyTransactionNode) node).getTrans().getPayload());
                                }
                            }
                        }

                        if (network.getCurrentTick() % 1000 == 0) {
                            System.out.println("NETWORK: " + network.getCurrentTick());
                            for (SimulatedNode mNode : multipleNodes) {
                                mNode.addNetwork(network);
                                Log.d(TAG, "doInBackground: mNode LOGGED");


                                HashMap<Integer, ArrayList<SimulatedJob>> signals = network.getAllSignals();
                                ArrayList<Integer> signalTimes = new ArrayList<>(signals.keySet());
                                Collections.sort(signalTimes);
                                Iterator<Integer> iterator = signalTimes.iterator();
                                while (iterator.hasNext()) {

                                    Integer key = iterator.next();


                                    ArrayList<SimulatedJob> jobs = signals.get(key);
                                    for (SimulatedJob job : jobs) {
                                        String target = job.getTarget();
                                        if (target.equals(mNode.getId())) {
                                            if (job.signal.getType() == 3) {
                                                if(!hashes.contains(job.signal.getHash())) {
                                                    hashes.add(job.signal.getHash());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            System.out.println(hashes);

                            for(String hash:hashes){
                                publishProgress(hash);
                            }
                        }
                        if (network.getCurrentTick() >= limit) {
                            break;
                        }
                    }

                    System.out.println("Simulation finished.");
                    network.log.getComparisonStats();

                    // == output statistics
                    // 		total_verified transactions
                    //		blocks_accepted
                    // 		average_block transaction count
                    //		average_block size

                    ArrayList<Integer> verifiedTransactions = network.getFinalVerifiedTransactions();
                    ArrayList<Integer> blocksAccepted = network.getFinalBlocksAccepted();
                    ArrayList<Integer> blockTransactionCounts = network.getFinalBlockTransactionCounts();
                    ArrayList<Integer> blockSizes = network.getFinalBlockSizes();


                    System.out.println("Statistics");
                    System.out.printf("%1$30s %2$10s %3$10s %4$10s %5$10s %6$10s\n", "variable", "mean", "mode", "median", "min", "max");
                    System.out.printf("%1$30s %2$10d %3$10d %4$10d %5$10d %6$10d\n", "Verified Transactions", StartActivity.mean(verifiedTransactions), StartActivity.mode(verifiedTransactions), StartActivity.median(verifiedTransactions), StartActivity.min(verifiedTransactions), StartActivity.max(verifiedTransactions));
                    System.out.printf("%1$30s %2$10d %3$10d %4$10d %5$10d %6$10d\n", "Blocks accepted", StartActivity.mean(blocksAccepted), StartActivity.mode(blocksAccepted), StartActivity.median(blocksAccepted), StartActivity.min(blocksAccepted), StartActivity.max(blocksAccepted));
                    System.out.printf("%1$30s %2$10d %3$10d %4$10d %5$10d %6$10d\n", "Transactions in blocks", StartActivity.mean(blockTransactionCounts), StartActivity.mode(blockTransactionCounts), StartActivity.median(blockTransactionCounts), StartActivity.min(blockTransactionCounts), StartActivity.max(blockTransactionCounts));
                    System.out.printf("%1$30s %2$10d %3$10d %4$10d %5$10d %6$10d\n", "Block sizes (bits)", StartActivity.mean(blockSizes), StartActivity.mode(blockSizes), StartActivity.median(blockSizes), StartActivity.min(blockSizes), StartActivity.max(blockSizes));

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            mAdapter.add(values[0]);

        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    public static int mean(ArrayList<Integer> values) {
        int count = values.size();
        int val = 0;


        for (int x : values) {
            val += x;
        }

        return val / (count > 0 ? count : 1);
    }

    public static int mode(ArrayList<Integer> values) {
        HashMap<Integer, Integer> frequencies = new HashMap<>();

        for (int val : values) {
            frequencies.put(val, frequencies.getOrDefault(val, 0) + 1);
        }

        int mostFrequent = -1;

        for (int candidate : frequencies.keySet()) {
            if (frequencies.get(candidate) > frequencies.getOrDefault(mostFrequent, 0)) {
                mostFrequent = candidate;
            }
        }

        return mostFrequent;
    }

    public static int median(ArrayList<Integer> values) {
        Collections.sort(values);
        return values.size() > 0 ? values.get(values.size() / 2) : 0;
    }

    public static int min(ArrayList<Integer> values) {
        int leastFrequent = -1;

        for (int count : values) {
            if (count < leastFrequent || leastFrequent == -1) {
                leastFrequent = count;
            }
        }

        return leastFrequent;
    }

    public static int max(ArrayList<Integer> values) {
        int mostFrequent = -1;

        for (int count : values) {
            if (count > mostFrequent) {
                mostFrequent = count;
            }
        }

        return mostFrequent;
    }

}

