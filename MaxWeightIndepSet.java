package maxweightindepset;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MaxWeightIndepSet {
	
	public int num;
	public int[] weight, max;
	public boolean[] isIn;
	
	public MaxWeightIndepSet() {
		try(Scanner input = new Scanner(new File("mwis.txt"))){
			Scanner line = new Scanner(input.nextLine());
			num = line.nextInt();
			weight = new int[num + 1];
			max = new int[num + 1];
			isIn = new boolean[num + 1];
			int j = 1;
			while(input.hasNextLine()){
				line = new Scanner(input.nextLine());
				weight[j++] = line.nextInt();
			}
			line.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void compute() {
		max[1] = weight[1];
		int j;
		for(j = 2; j < max.length; j++){
			max[j] = (max[j-1] > max[j-2] + weight[j]) ? max[j-1] : (max[j-2] + weight[j]);
		}
		for(j = isIn.length - 1; j > 1; j--){
			if(max[j-2] + weight[j] > max[j-1]){
				isIn[j] = true;
				j--;
			}
		}
		if(j == 1)
			isIn[j] = true;
		System.out.print(isIn[1] ? 1 : 0);
		System.out.print(isIn[2] ? 1 : 0);
		System.out.print(isIn[3] ? 1 : 0);
		System.out.print(isIn[4] ? 1 : 0);
		System.out.print(isIn[17] ? 1 : 0);
		System.out.print(isIn[117] ? 1 : 0);
		System.out.print(isIn[517] ? 1 : 0);
		System.out.println(isIn[997] ? 1 : 0);// 10100110
	}

	public static void main(String[] args) {
		MaxWeightIndepSet test = new MaxWeightIndepSet();
		test.compute();
	}

}
