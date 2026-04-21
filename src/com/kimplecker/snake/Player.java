package com.kimplecker.snake;
public class Player {
	private String name;
	private int posX;
	private int posY;
	private int count;

	public Player(String name, int posX, int posY, int count) {
		this.name = name;
		this.posX = posX;
		this.posY = posY;
		this.count = count;

	}

	public String getname() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
