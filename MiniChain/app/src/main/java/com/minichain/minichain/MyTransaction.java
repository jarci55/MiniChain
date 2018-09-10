package com.minichain.minichain;

public class MyTransaction extends Transaction {

    //Amended DummyTransaction class adding the payload in the constructor

    private Integer networkTick;



    public MyTransaction(String origin, String payload, Integer networkTick) {
        super(origin, payload);

        this.networkTick = networkTick;


    }
}