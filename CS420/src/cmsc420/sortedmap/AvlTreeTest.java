package cmsc420.sortedmap;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;


public class AvlTreeTest {
	
	public static void main(String[] args) {
		AvlGTree<String, Integer> tree = new AvlGTree<>(new AvlComparator(), 1);
		tree.put("10", 10);
		tree.put("20", 20);
		tree.put("30", 30);
		tree.put("40", 40);
		tree.put("50", 50);
		tree.put("25", 25);
		System.out.println("Preorder traversal" +
                " of constructed tree is : ");
//		tree.preOrder(tree.root);
		
		testEntrySetSimple();
		
	}
	
	public static void testEntrySetSimple() {
		AvlGTree<Integer, String> avlgtree = new AvlGTree();
		TreeMap<Integer, String> tm = new TreeMap();
		avlgtree.put(2, null);
		avlgtree.put(3, null);
		avlgtree.put(4, null);
		
		
		tm.put(2, null);
		tm.put(3, null);
		tm.put(4, null);
		
		
		System.out.println(tm.entrySet());
//		System.out.println(avlgtree.getRoot() + " " + avlgtree.getRoot().getLeft() + " "
//				+ avlgtree.getRoot().getRight());
		System.out.println(avlgtree.entrySet());
		//assertEquals(avlgtree.entrySet(), tm.entrySet());
	}
	
	@Test
	public void testAvlRandom1() {
		Random rand = new Random();

		int  n = rand.nextInt(500) + 1;
		
		AvlGTree<Integer, Integer> avl = new AvlGTree();
		TreeMap<Integer, Integer> tm = new TreeMap();
		
		for (int i = 0; i < 10; i++) {
			n = (rand.nextInt(500) + 1);
			System.out.println(i+ " " + n);
			
			avl.put(n, 0);
			tm.put(n, 0);
			
		}
		
		System.out.println(avl.entrySet());
		System.out.println(tm.entrySet());
		
		assertEquals(avl.entrySet(), tm.entrySet());
	}
	
	@Test
	public void testAvlContains1() {
		Random rand = new Random();

		int  n = rand.nextInt(500) + 1;
		
		int val1 = 0; 
		int val2 = 0; 
		int val9 = 0;
		
		AvlGTree<Integer, Integer> avl = new AvlGTree();
		TreeMap<Integer, Integer> tm = new TreeMap();
		
		for (int i = 0; i < 10; i++) {
			n = (rand.nextInt(500) + 1);
			System.out.println(i+ " " + n);
			
			avl.put(n, 0);
			tm.put(n, 0);
			
			if (i == 1) {
				val1 = n;
			}
			
			if (i == 2) {
				val2 = n;
			}
			
			if (i == 9) {
				val9 = n;
			}
			
		}
		
		assertTrue(avl.containsKey(val1));
		assertTrue(tm.containsKey(val1));
		
		assertTrue(avl.containsKey(val2));
		assertTrue(tm.containsKey(val2));
		
		assertTrue(avl.containsKey(val9));
		assertTrue(tm.containsKey(val9));
	}
	
	@Test
	public void testAvlFirstKey() {
		Random rand = new Random();

		int  n = rand.nextInt(500) + 1;
		
		AvlGTree<Integer, Integer> avl = new AvlGTree();
		TreeMap<Integer, Integer> tm = new TreeMap();
		
		for (int i = 0; i < 10; i++) {
			n = (rand.nextInt(500) + 1);
			//System.out.println(i+ " " + n);
			
			avl.put(n, 0);
			tm.put(n, 0);
			
		}
		
		assertEquals(avl.firstKey(), tm.firstKey());
	}
	
	@Test
	public void testBasicEquality() {
		SortedMap<String, Integer> avl = new AvlGTree<>(3);
		SortedMap<String, Integer> tree = new TreeMap<>();
		Set<Map.Entry<String, Integer>> e1 = avl.entrySet();
		Set<Map.Entry<String, Integer>> e2 = tree.entrySet();
		// Some random input on both data structure
		avl.put("One", 1);
		tree.put("One", 1);
		//structural equals
		boolean cond1 = (avl.equals(tree) && tree.equals(avl));
		//toString
		boolean cond2 = (avl.toString().equals(tree.toString()));
		//hashCode
		boolean cond3 = (avl.hashCode() == tree.hashCode());
		//entry set equals
		boolean cond4 = (e1.equals(e2) && e2.equals(e1));
		
		assertTrue(cond1);
		assertTrue(cond2);
		assertTrue(cond3);
		assertTrue(cond4);
	}
	
	@Test
	public void testMoreAdvancedEquality() {
		SortedMap<Integer, Integer> avl = new AvlGTree<>(3);
		SortedMap<Integer, Integer> tree = new TreeMap<>();
		Set<Map.Entry<Integer, Integer>> e1 = avl.entrySet();
		Set<Map.Entry<Integer, Integer>> e2 = tree.entrySet();
		// Some random input on both data structure
		Random rand = new Random();

		int  n = rand.nextInt(500) + 1;
		
		for (int i = 0; i < 10; i++) {
			n = (rand.nextInt(500) + 1);
			//System.out.println(i+ " " + n);
			
			avl.put(n, 0);
			tree.put(n, 0);
			
		}
		
		
		//structural equals
		boolean cond1 = (avl.equals(tree) && tree.equals(avl));
		//toString
		boolean cond2 = (avl.toString().equals(tree.toString()));
		//hashCode
		boolean cond3 = (avl.hashCode() == tree.hashCode());
		//entry set equals
		boolean cond4 = (e1.equals(e2) && e2.equals(e1));
		
		assertTrue(cond1);
		assertTrue(cond2);
		assertTrue(cond3);
		assertTrue(cond4);
	}
	
}
