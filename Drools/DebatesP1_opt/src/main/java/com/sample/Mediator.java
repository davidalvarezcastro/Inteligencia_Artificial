package com.sample;

public class Mediator {

	public int turn;

	public Mediator() {
		turn = 0;
	}

	public void changeTurn() {
		if (turn == 0)
			turn = 1;
		else
			turn = 0;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int i) {
		turn = i;
	}

}