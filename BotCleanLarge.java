package com.HR_AI;

import java.util.Arrays;
import java.util.Scanner;

public class BotCleanLarge {

/*	static char[][] toCharacterArray(int row, int col, String[] Stringgrid) {
		char[][] grid = new char[row][col];
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

	static int[][] computeDistanceMatrix(int posr, int posc, int row, int col, char[][] board) {
		int[][] distance = new int[row][col];

		// initializing to -1
		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++)
				distance[i][j] = -1;

		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++) {
				if (board[i][j] == 'd') {
					distance[i][j] = calculateDistance(posr, posc, i, j);
				}
			}
		return distance;
	}

	static int[] sortDistance( int row, int col, int[][] distMatrix) {
		int sortedDistance[] = new int[row*col];
		int counter=0;
		// initialize to 999
		for (int i = 0; i < row*col; i++)
			sortedDistance[i] = 999999;
		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++) {
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

	static void next_move(int posr, int posc, int dimh, int dimw, String[] board) {
		int vertical, horizontal, flag = 0;
		char[][] newGrid = toCharacterArray(dimh, dimw, board);

		for (int i = 0; i < 5; i++) {
			System.out.println();
			for (int j = 0; j < 5; j++)
				System.out.print(newGrid[i][j] + "  ");
		}

		int[][] distMatrix = computeDistanceMatrix(posr, posc, dimh, dimw, newGrid);

		for (int i = 0; i < 5; i++) {
			System.out.println();
			for (int j = 0; j < 5; j++)
				System.out.print(distMatrix[i][j] + "  ");
		}

		int[] distanceSorted = sortDistance(dimh, dimw, distMatrix);

		for (int i = 0; i < 25; i++)
			System.out.print(distanceSorted[i] + "  ");
		for (int i = 0; i < dimh; i++) {
			for (int j = 0; j < dimw; j++) {
				if (distMatrix[i][j] == distanceSorted[0]) {
					// up and down
					if ((horizontal = j - posc) < 0)
						// for(;vertical<0;vertical++)
						System.out.println("LEFT");
					else if (horizontal > 0)
						// for(;vertical>0;vertical--)
						System.out.println("RIGHT");
					// left and right
					else if ((vertical = i - posr) < 0)
						// for(;horizontal<0;horizontal++)
						System.out.println("UP");
					else if (vertical > 0)
						// for(;horizontal>0;horizontal--)
						System.out.println("DOWN");
					else
						System.out.println("CLEAN");

					flag = 1;
					break;
				}
			}

			if (flag != 0)
				break;
		}
	}
*/
	
	static char[][] toCharacterArray(int row, int col, String[] Stringgrid) {
		char[][] grid = new char[row][col];
		for (int i = 0; i < Stringgrid.length; i++)
			for (int j = 0; j < Stringgrid.length; j++)
				grid[i][j] = Stringgrid[i].charAt(j);
		return grid;
	}
	
	static void next_move(int posr, int posc, int dimh, int dimw, String[] board1)
	{
			char [][]board=toCharacterArray(dimh, dimw, board1);
	       int i,j;
	       if(board[posr][posc] == 'd')
	       {
	    	   System.out.println("CLEAN\n");
	       }
	       else
	       {
	              for(i=0;i<dimh;i++)
	              {
	                     for(j=0;j<dimw;j++)
	                     {
	                           if(board[i][j] == 'd')
	                           {
	                                  //printf("%d %d\n",i,j);
	                                  if(posc>j)
	                                  {
	                                	  System.out.println("LEFT\n");
	                                  }
	                                  else if(posc<j)
	                                  {
	                                	  System.out.println("RIGHT\n");
	                                  }
	                                  else
	                                  {
	                                         if(posr>i)
	                                         {
	                                        	 System.out.println("UP\n");
	                                         }
	                                         else if(posr<i)
	                                         {
	                                        	 System.out.println("DOWN\n");
	                                         }     
	                                         else
	                                         {
	                                        	 System.out.println("CLEAN\n");
	                                         }
	                                  }
	                                  return;
	                           }
	                     }
	              }
	       }
	}
	
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int[] pos = new int[2];
		int[] dim = new int[2];
		for (int i = 0; i < 2; i++)
			pos[i] = in.nextInt();
		for (int i = 0; i < 2; i++)
			dim[i] = in.nextInt();
		String board[] = new String[dim[0]];
		for (int i = 0; i < dim[0]; i++)
			board[i] = in.next();
		next_move(pos[0], pos[1], dim[0], dim[1], board);
	}

}
