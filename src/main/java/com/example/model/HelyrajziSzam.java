package com.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaborsornyei on 16. 02. 27..
 */
public class HelyrajziSzam {
    private String hrsz;
    private List<MuvelesiAg> muvelesiAgList;
    private List<Erdekeltseg> erdekeltsegList;

    public HelyrajziSzam(String hrsz) {
        this.hrsz = hrsz;
        muvelesiAgList = new ArrayList<>();
        erdekeltsegList = new ArrayList<>();
    }

    public String getHrsz() {
        return hrsz;
    }

    public HelyrajziSzam setHrsz(String hrsz) {
        this.hrsz = hrsz;
        return this;
    }

    public List<MuvelesiAg> getMuvelesiAgList() {
        return muvelesiAgList;
    }

    public HelyrajziSzam setMuvelesiAgList(List<MuvelesiAg> muvelesiAgList) {
        this.muvelesiAgList = muvelesiAgList;
        return this;
    }

    public List<Erdekeltseg> getErdekeltsegList() {
        return erdekeltsegList;
    }

    public HelyrajziSzam setErdekeltsegList(List<Erdekeltseg> erdekeltsegList) {
        this.erdekeltsegList = erdekeltsegList;
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
