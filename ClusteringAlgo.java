package clustering;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ClusteringAlgo {

	public int numNodes, numParts = 500;
	public int[] mParent;
	public PriorityQueue<Edge> mQueue = new PriorityQueue<>(new Comparator<Edge>() {
		@Override
		public int compare(Edge o1, Edge o2) {
			if(o1.cost < o2.cost)
				return -1;
			if(o1.cost > o2.cost)
				return 1;
			return 0;
		}
	});
	
	public ClusteringAlgo() {
		try(Scanner input = new Scanner(new File("clustering1.txt"))) {
//			input = new Scanner(new File("clustering1.txt"));
			Scanner line = new Scanner(input.nextLine());
			numNodes = line.nextInt();
			mParent = new int[numNodes + 1];
			for(int j = 1; j < mParent.length; j++){
				mParent[j] = j;
			}
			while (input.hasNextLine()) {
				line = new Scanner(input.nextLine());
				int node1 = line.nextInt();
				int node2 = line.nextInt();
				int cost = line.nextInt();
				mQueue.add(new Edge(node1, node2, cost));
			}
			line.close();
		} catch (FileNotFoundException e) { e.printStackTrace(); }
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClusteringAlgo test = new ClusteringAlgo();
		test.compute();
	}

	private void compute() {
		Edge edge = null;
		while(numParts > 4){
			edge = mQueue.poll();
			int node1 = edge.node1;
			int node2 = edge.node2;
			if(mParent[node1] == mParent[node2])
				continue;
			numParts--;
			int old = mParent[node1];
			for(int j = 1; j < mParent.length; j++){
				if(mParent[j] == old){
					mParent[j] = mParent[node2];
				}
			}
		}
		int node1, node2;
		do{
			edge = mQueue.poll();
			node1 = edge.node1;
			node2 = edge.node2;
		} while(mParent[node1] == mParent[node2]);

		System.out.println(edge.cost);//
	}

	class Edge{
		int node1, node2;
		long cost;
		public Edge(int n1, int n2, long c) {
			node1 = n1;
			node2 = n2;
			cost = c;
		}
	}
}
