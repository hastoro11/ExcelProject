package com.example.model;

import java.math.BigDecimal;

/**
 * Created by gaborsornyei on 16. 02. 27..
 */
public class MuvelesiAg {
	private String megnevezes;
	private double terulet;
	private String kivettMegnevezes;

	public String getMegnevezes() {
		return megnevezes;
	}

	public MuvelesiAg setMegnevezes(String megnevezes) {
		this.megnevezes = megnevezes;
		return this;
	}

	public double getTerulet() {
		return terulet;
	}

	public void setTerulet(double terulet) {
		this.terulet = terulet;
	}

	public String getKivettMegnevezes() {
		return kivettMegnevezes;
	}

	public MuvelesiAg setKivettMegnevezes(String kivettMegnevezes) {
		this.kivettMegnevezes = kivettMegnevezes;
		return this;
	}
}
