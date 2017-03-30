package clustering;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BigClustering {

	public int numNodes, numBits, numParts = 0;
//	public int remain;
	public HashMap<Integer, Nodes> mapCostNode;
	Queue<Nodes> queue;
	public int[] arrayNodes;// can be removed
	int root;
	
	public BigClustering() {
		queue = new ConcurrentLinkedQueue<>();
		try(Scanner input = new Scanner(new File("clusteringbig.txt"))) {
			Scanner line = new Scanner(input.nextLine());
			numNodes = line.nextInt();
//			remain = numNodes;
			numBits = line.nextInt();
			mapCostNode = new HashMap<>(numNodes+numNodes/2);
			arrayNodes = new int[numNodes];
			
			int j = 0;
			while (input.hasNextLine()) {
				line = new Scanner(input.nextLine());
				while(line.hasNextInt()){
					arrayNodes[j] = arrayNodes[j] * 2 + line.nextInt();
				}
				mapCostNode.put(arrayNodes[j], new Nodes(arrayNodes[j], false, arrayNodes[j], 0));
				j++;
			}
			line.close();
		} catch (FileNotFoundException e) { e.printStackTrace(); }
	}
	
	private void getCluster(Nodes node) {
		node.isDone = true;
		int cost = node.cost;
		root = node.getRoot();
		for(int j = 0; j < numBits; j++){
			int one = (((cost >> j) & 1) == 1) ? (cost - (1 << j)) : (cost + (1 << j));
			Nodes nNode = mapCostNode.get(one);
			if(nNode != null && !nNode.isDone && !nNode.isInQueue){
				queue.offer(nNode);
				nNode.isInQueue = true;
				union(root, nNode.getRoot());
//				System.out.println(--remain);
			}
			for(int k = 0; k < numBits; k++){
				if(k == j)
					continue;
				int two = (((one >> k) & 1) == 1) ? (one - (1 << k)) : (one + (1 << k));
				nNode = mapCostNode.get(two);
				if(nNode != null && !nNode.isDone && !nNode.isInQueue){
					queue.offer(nNode);
					nNode.isInQueue = true;
					union(root, nNode.getRoot());
//					System.out.println(--remain);
				}
			}
		}
	}

	private void union(int root1, int root2) {
		Nodes n1 = mapCostNode.get(root1);
		Nodes n2 = mapCostNode.get(root2);
		if(n1.hop == n2.hop){
			n1.hop++;
			n2.parent = root1;
		}else if (n1.hop < n2.hop) {
			n1.parent = root2;
			root = root2;
		}else {
			n2.parent = root1;
		}
	}

	private void compute() {
		Iterator<Integer> it = mapCostNode.keySet().iterator();
		while(it.hasNext()){
			Nodes node = mapCostNode.get(it.next());
			if(node.isDone)
				continue;
//			System.out.println(--remain);
			numParts++;
			queue.offer(node);
			while(!queue.isEmpty()){
				Nodes n = queue.poll();
				getCluster(n);
			}
//			System.out.println("cluster " + numParts + ": " + root);
		}

		System.out.println(numParts);
	}

	public static void main(String[] args) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// format
		System.out.println("construct: " + df.format(new Date()));// new Date() to get current time
		BigClustering test = new BigClustering();
		System.out.println("start computing: " + df.format(new Date()));// new Date() to get current time
		test.compute();
		System.out.println("end: " + df.format(new Date()));// new Date() to get current time
	}

	private class Nodes{
		int cost;
		boolean isDone;
		boolean isInQueue;
		int parent;
		int hop;
		public Nodes(int c, boolean b, int p, int h) {
			cost = c;
			isDone = b;
			parent = p;
			hop = h;
		}
		public int getRoot() {
			int root = parent;
			while(mapCostNode.get(root).parent != root)
				root = mapCostNode.get(root).parent;
			return root;
		}
	}
}

