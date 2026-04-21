package com.kimplecker.snake;

import java.util.LinkedList;

public class Movement {

	private static LinkedList<Integer> trail = new LinkedList<Integer>();

	public static boolean move(Player player, int direction, int[][] loadedMap) {
		int count = player.getCount();
		int lastY = player.getPosY();
		int lastX = player.getPosX();

		trail.addFirst(player.getPosX());
		trail.add(1, player.getPosY());

		if (trail.size() > count + 2) {
			trail.subList(count + 2, trail.size()).clear();
		}

		loadedMap[trail.get(count)][trail.get(1 + count)] = 0;

		if (count > 1) {
			loadedMap[player.getPosX()][player.getPosY()] = 3;
		}

		switch (direction) {

		case 1: // left
			if (loadedMap[player.getPosX()][player.getPosY() - 1] == 2) {
				player.setCount(player.getCount() + 2);
			}
			player.setPosY(player.getPosY() - 1);
			break;

		case 2: // right
			if (loadedMap[player.getPosX()][player.getPosY() + 1] == 2) {
				player.setCount(player.getCount() + 2);
			}
			player.setPosY(player.getPosY() + 1);
			break;

		case 3: // up
			if (loadedMap[player.getPosX() - 1][player.getPosY()] == 2) {
				player.setCount(player.getCount() + 2);
			}
			player.setPosX(player.getPosX() - 1);
			break;

		case 4: // down
			if (loadedMap[player.getPosX() + 1][player.getPosY()] == 2) {
				player.setCount(player.getCount() + 2);
			}
			player.setPosX(player.getPosX() + 1);
			break;

		default:
			return false;
		}

		if (loadedMap[player.getPosX()][player.getPosY()] != 1
				&& loadedMap[player.getPosX()][player.getPosY()] != 3) {

			loadedMap[player.getPosX()][player.getPosY()] = 4;
			return false;

		} else {
			player.setPosX(lastX);
			player.setPosY(lastY);
			return true;
		}
	}

	public static void remove() {
		trail.clear();
	}
}