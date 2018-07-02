package com.HR_AI;

import java.util.Arrays;
import java.util.Scanner;

public class BotClean {

	static char[][] toCharacterArray(String[] Stringgrid) {
		char[][] grid = new char[5][5];
		for (int i = 0; i < Stringgrid.length; i++)
			for (int j = 0; j < Stringgrid.length; j++)
				grid[i][j] = Stringgrid[i].charAt(j);
		return grid;
	}

	static int calculateDistance(int posr, int posc, int i, int j) {
		int distance = -1, r, c;
		r = Math.abs(posr - i);
		c = Math.abs(posc - j);
		distance = r + c;
		return distance;
	}

	static int[][] computeDistanceMatrix(int posr, int posc, char[][] board) {
		int[][] distance = new int[5][5];
		int total = 0;

		// initializing to -1
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				distance[i][j] = -1;

		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				if (board[i][j] == 'd') {
					distance[i][j] = calculateDistance(posr, posc, i, j);
				}
			}
		return distance;
	}

	static int[] sortDistance(int[][] distMatrix) {
		int sortedDistance[] = new int[25];
		int counter = 0;
		// initialize to 999
		for (int i = 0; i < 25; i++)
			sortedDistance[i] = 999;
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++) {
				if (distMatrix[i][j] != -1) {
					sortedDistance[counter++] = distMatrix[i][j];
				}
			}
		Arrays.sort(sortedDistance);
		return sortedDistance;

	}

	// Sort the distance and then select the minimum distance,find its
	// coordinates in distanceMatrix by comparing the distace and then compute
	// the next move

	static void next_move(int posr, int posc, String[] board) {
		int vertical, horizontal, flag = 0;
		char[][] newGrid = toCharacterArray(board);

		/*
		 * for (int i = 0; i < 5; i++) { System.out.println(); for (int j = 0; j
		 * < 5; j++) System.out.print(newGrid[i][j] +"  "); }
		 */
		int[][] distMatrix = computeDistanceMatrix(posr, posc, newGrid);

		/*
		 * for (int i = 0; i < 5; i++) { System.out.println(); for (int j = 0; j
		 * < 5; j++) System.out.print(distMatrix[i][j] +"  "); }
		 */

		int[] distanceSorted = sortDistance(distMatrix);
		/*for (int i = 0; i < 25; i++)
			System.out.print(distanceSorted[i] + "  ");
		*/for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (distMatrix[i][j] == distanceSorted[0]) {
					// up and down
					if ((vertical = i - posr) < 0)
						// for(;vertical<0;vertical++)
						System.out.println("UP");
					else if (vertical > 0)
						// for(;vertical>0;vertical--)
						System.out.println("DOWN");
					// left and right
					else if ((horizontal = j - posc) < 0)
						// for(;horizontal<0;horizontal++)
						System.out.println("LEFT");
					else if (horizontal > 0)
						// for(;horizontal>0;horizontal--)
						System.out.println("RIGHT");
					else
						System.out.println("CLEAN");
					flag = 1;
					break;
				}
			}
			if(flag!=0)
				break;
		}
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int[] pos = new int[2];
		String board[] = new String[5];
		for (int i = 0; i < 2; i++)
			pos[i] = in.nextInt();
		for (int i = 0; i < 5; i++)
			board[i] = in.next();
		next_move(pos[0], pos[1], board);
	}
}
