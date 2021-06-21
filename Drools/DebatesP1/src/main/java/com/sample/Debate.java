package com.sample;

import java.util.ArrayList;

public class Debate {

	ArrayList<Debater> debaters;

	public Debate(Debater d0, Debater d1) {
		super();
		debaters = new ArrayList<Debater>();
		debaters.add(d0);
		debaters.add(d1);
	}

	public ArrayList<Debater> getDebaters() {
		return debaters;
	}

	public void setDebaters(ArrayList<Debater> d) {
		debaters = d;
	}
}
