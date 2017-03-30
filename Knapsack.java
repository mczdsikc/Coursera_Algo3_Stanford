package knapsack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Knapsack {
	
	private int size;
	private int num;
	private HashMap<Integer, Item> items;
	private Stack<Entry> stack;
	private Queue<Entry> queue;
	private HashMap<Integer, HashMap<Integer, Entry>> map; // id, size
	
	public Knapsack() {
		stack = new Stack<>();
		queue = new ConcurrentLinkedQueue<>();
		try {
			Scanner input = new Scanner(new File("knapsack_big.txt"));//knapsack1
			Scanner line = new Scanner(input.nextLine());
			size = line.nextInt();
			num = line.nextInt();
			items = new HashMap<>(num+num/2);
			map = new HashMap<>(num+num/2);
			for(int i = 1; i <= num; i++){
				map.put(i, new HashMap<>());
			}
			int j = 1;
			while(input.hasNextLine()){
				line = new Scanner(input.nextLine());
				Item item = new Item(j, line.nextInt(), line.nextInt());
				items.put(j++, item);
				line.close();
			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void compute() {
		Item item = items.get(num);
		Entry entry = new Entry(num, item.value, item.weight, size);
		stack.push(entry);
		queue.offer(entry);
		map.get(entry.id).put(size, entry);
		while (!queue.isEmpty()) {
			entry = queue.poll();
			if(entry.id == 1){
				if(entry.weight < entry.size)
					entry.total = entry.value;
				else 
					entry.total = 0;
				continue;
			}
			item = items.get(entry.id - 1);
			Entry entry2;
			if(!map.get(item.id).containsKey(entry.size)){
				entry2 = new Entry(item.id, item.value, item.weight, entry.size);
				stack.push(entry2);
				queue.offer(entry2);
				map.get(entry2.id).put(entry2.size, entry2);
			}
			int size = entry.size - entry.weight;
			if(size <= 0)
				continue;
			if(!map.get(item.id).containsKey(size)){
				entry2 = new Entry(item.id, item.value, item.weight, size);
				stack.push(entry2);
				queue.offer(entry2);
				map.get(entry2.id).put(size, entry2);
			}
		}
		while(!stack.isEmpty()){
			entry = stack.pop();
//			System.out.println(entry.id);
			if(entry.id == 1){
				continue;
			}
			if(map.get(entry.id).get(entry.size).total != -1)
				continue;
			int total1 = map.get(entry.id - 1).get(entry.size).total;
			int size = entry.size - entry.weight;
			if(size <= 0){
				entry.total = total1;
			} else {
				int total2 = map.get(entry.id - 1).get(size).total;
				entry.total = total1 > total2 + entry.value ? total1 : total2 + entry.value;
			}
//			map.get(entry.id).put(entry.size, entry);
		}
		System.out.println(entry.total);// 
	}

	public static void main(String[] args) {
		Knapsack test = new Knapsack();
		test.compute();

	}

	private class Item{
		private int id;
		private int value;
		private int weight;
		public Item(int id, int value, int weight) {
			this.id = id;
			this.value = value;
			this.weight = weight;
		}
	}
	
	private class Entry{
		private int id;
		private int value;
		private int weight;
		private int size;
		private int total;
		public Entry(int id, int value, int weight, int size) {
			this.size = size; this.id = id; 
			this.weight = weight; this.value = value;
			this.total = -1;
		}
	}
}
