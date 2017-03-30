/* 
 * Question 1 
In this programming problem and the next you'll code up the clustering algorithm from lecture for computing a max-spacing k-clustering. Download the text file here. This file describes a distance function (equivalently, a complete graph with edge costs). It has the following format: 
[number_of_nodes] 
[edge 1 node 1] [edge 1 node 2] [edge 1 cost] 
[edge 2 node 1] [edge 2 node 2] [edge 2 cost] 
... 
There is one edge (i,j) for each choice of 1≤i<j≤n, where n is the number of nodes. For example, the third line of the file is "1 3 5250", indicating that the distance between nodes 1 and 3 (equivalently, the cost of the edge (1,3)) is 5250. You can assume that distances are positive, but you should NOT assume that they are distinct. 
 
Your task in this problem is to run the clustering algorithm from lecture on this data set, where the target number k of clusters is set to 4. What is the maximum spacing of a 4-clustering? 
 
ADVICE: If you're not getting the correct answer, try debugging your algorithm using some small test cases. And then post them to the discussion forum! 
 */  

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
