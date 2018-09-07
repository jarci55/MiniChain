package com.minichain.minichain;

public class MyTransaction extends Transaction {

    private Integer networkTick;
    String myTransaction;


    public MyTransaction(String origin, String payload, Integer networkTick) {
        super(origin, payload);

        this.networkTick = networkTick;


    }
}