package com.example.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaborsornyei on 16. 02. 27..
 */
public class Erdekeltseg {
    private String tipus;
    private String erdekelt;
    private String hanyadStr;
    private double hanyad;
    private List<MuvelesiAg> muvelesiAgList;

    public Erdekeltseg() {
        muvelesiAgList = new ArrayList<>();
    }

    public String getTipus() {
        return tipus;
    }

    public Erdekeltseg setTipus(String tipus) {
        this.tipus = tipus;
        return this;
    }

    public String getErdekelt() {
        return erdekelt;
    }

    public Erdekeltseg setErdekelt(String erdekelt) {
        this.erdekelt = erdekelt;
        return this;
    }

    public String getHanyadStr() {
        return hanyadStr;
    }

    public Erdekeltseg setHanyadStr(String hanyadStr) {
        this.hanyadStr = hanyadStr;
        return this;
    }

    public double getHanyad() {
        return hanyad;
    }

    public Erdekeltseg setHanyad(double hanyad) {
        this.hanyad = hanyad;
        return this;
    }

    public List<MuvelesiAg> getMuvelesiAgList() {
        return muvelesiAgList;
    }

    public Erdekeltseg setMuvelesiAgList(List<MuvelesiAg> muvelesiAgList) {
        this.muvelesiAgList = muvelesiAgList;
        return this;
    }

    public double getOsszTerulet() {
        double result = 0;
        for (MuvelesiAg muvelesiAg : muvelesiAgList) {
            result += muvelesiAg.getTerulet();
        }
        return result;
    }
}
