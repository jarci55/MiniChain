package com.minichain.minichain;

public class MyTransactionNode extends SimulatedNode {

    private int lastBatch;
    private int ticksPerTransaction = 200;
    MyTransaction trans;


    public MyTransactionNode(SimulatedNetwork network, String id) {
        super(network, id);

        this.lastBatch = network.nodes.size();
    }

    public MyTransactionNode() {}

    public void setTicksPerTransaction(int ticks) {
        this.ticksPerTransaction = ticks;
    }




    @Override
    protected int executeSpecificLogic(int networkTick) {
        if(this.activeOutgoingConnections.size() > 0) {
            for(; this.lastBatch <= networkTick; this.lastBatch += this.ticksPerTransaction) {

                trans = new MyTransaction(this.getId(), getPayload(), networkTick);
                NodeSignal signal = new NodeSignalNewTransaction(this.getId(), trans.toString());

                this.parseSignal(signal, networkTick);
            }

        }

        return this.network.nodeThreadDelay;
    }


    public MyTransaction getTrans() {
        return trans;
    }
}

