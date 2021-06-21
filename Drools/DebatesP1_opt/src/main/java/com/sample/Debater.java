package com.sample;

public class Debater {

	String name;
	int player;
	int age;
	int credibility;
	int attack;
	boolean switched = false;

	public Debater(String name, int player,
                     int age, int credibility,
                     int attack) {
		super();
		this.name = name;
		this.player = player;
		this.age = age;
		this.credibility = credibility;
		this.attack = attack;
	}

	public void getDamage(int points) {
		credibility -= points;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int a) {
		this.age = a;
	}

	public int getCredibility() {
		return credibility;
	}

	public void setCredibility(int cred) {
		this.credibility = cred;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}
	
	public boolean getSwitched() {
		return switched;
	}

	public void setSwitched(boolean switched) {
		this.switched = switched;
	}

	public void changeSwitch() {
		this.switched = !this.switched;
	}

	public String toString() {
		return this.name + "(cred=" + ((this.credibility < 0)? 0 : this.credibility) + ")";
	}
}