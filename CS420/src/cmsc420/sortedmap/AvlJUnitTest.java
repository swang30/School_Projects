package cmsc420.sortedmap;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

public class AvlJUnitTest {

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
	public void testContainsSimple() {
		AvlGTree<String, Integer> avl = new AvlGTree();
		avl.put("q", 2);
		avl.put("t", 3);
		avl.put("q", 6);
//		avl.preOrder(avl.root);
		assertTrue(avl.containsKey("q"));
		
		AvlGTree<Integer, String> avl1 = new AvlGTree();
		avl1.put(2, "q");
		avl1.put(3, "r");
		avl1.put(3, "y");
//		avl1.preOrder(avl1.root);
		System.out.println(avl1.containsKey(3));
		assertTrue(avl1.containsKey(3));
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
		
		System.out.println(avl.entrySet());
		System.out.println(tree.entrySet());
		System.out.println(tree.equals(avl));
		assertTrue(cond1);
		assertTrue(cond2);
		assertTrue(cond3);
		assertTrue(cond4);
	}
	
	
	@Test
	public void testEntrySetSimple() {
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
		assertEquals(avlgtree.entrySet(), tm.entrySet());
	}
	
	
	@Test
	public void testEntrySet2() {
		AvlGTree<Integer, String> avlgtree = new AvlGTree();
		TreeMap<Integer, String> tm = new TreeMap();
		avlgtree.put(4, null);
		avlgtree.put(3, null);
		avlgtree.put(2, null);
		
		
		tm.put(4, null);
		tm.put(3, null);
		tm.put(2, null);
		
		
		System.out.println(tm.entrySet());
//		System.out.println(avlgtree.getRoot() + " " + avlgtree.getRoot().getLeft() + " "
//				+ avlgtree.getRoot().getRight());
		System.out.println(avlgtree.entrySet());
		assertEquals(avlgtree.entrySet(), tm.entrySet());
	}
	
	@Test
	public void testEntrySet() {
		AvlGTree<Integer, String> avlgtree = new AvlGTree();
		TreeMap<Integer, String> tm = new TreeMap();
		avlgtree.put(2, null);
		avlgtree.put(3, null);
		avlgtree.put(4, null);
		avlgtree.put(5, null);
		avlgtree.put(6, null);
		avlgtree.put(7, null);
		avlgtree.put(8, null);
		avlgtree.put(9, null);
		
		
		
		tm.put(2, null);
		tm.put(3, null);
		tm.put(4, null);
		tm.put(5, null);
		tm.put(6, null);
		tm.put(7, null);
		tm.put(8, null);
		tm.put(9, null);
		
		System.out.println(tm.entrySet());
		System.out.println(avlgtree);
		System.out.println(avlgtree.entrySet());
		assertEquals(avlgtree.entrySet(), tm.entrySet());
	}
	
	
	@Test
	public void testFirstKey() {
		AvlGTree<Integer, String> avlgtree = new AvlGTree();
		TreeMap<Integer, String> tm = new TreeMap();
		avlgtree.put(2, null);
		avlgtree.put(3, null);
		avlgtree.put(4, null);
		avlgtree.put(5, null);
		avlgtree.put(6, null);
		avlgtree.put(7, null);
		avlgtree.put(8, null);
		avlgtree.put(9, null);
		
		
		
		tm.put(2, null);
		tm.put(3, null);
		tm.put(4, null);
		tm.put(5, null);
		tm.put(6, null);
		tm.put(7, null);
		tm.put(8, null);
		tm.put(9, null);
		
		
		assertEquals(avlgtree.firstKey(), tm.firstKey());
	}
	
	@Test
	public void testLastKey() {
		AvlGTree<Integer, String> avlgtree = new AvlGTree();
		TreeMap<Integer, String> tm = new TreeMap();
		avlgtree.put(2, null);
		avlgtree.put(3, null);
		avlgtree.put(4, null);
		avlgtree.put(5, null);
		avlgtree.put(6, null);
		avlgtree.put(7, null);
		avlgtree.put(8, null);
		avlgtree.put(9, null);
		
		
		
		tm.put(2, null);
		tm.put(3, null);
		tm.put(4, null);
		tm.put(5, null);
		tm.put(6, null);
		tm.put(7, null);
		tm.put(8, null);
		tm.put(9, null);
		
		
		assertEquals(avlgtree.lastKey(), tm.lastKey());
	}
	
	@Test
	public void testSize() {
		AvlGTree<Integer, String> avlgtree = new AvlGTree();
		TreeMap<Integer, String> tm = new TreeMap();
		avlgtree.put(2, null);
		avlgtree.put(3, null);
		avlgtree.put(4, null);
		avlgtree.put(5, null);
		avlgtree.put(6, null);
		avlgtree.put(7, null);
		avlgtree.put(8, null);
		avlgtree.put(9, null);
		
		
		
		tm.put(2, null);
		tm.put(3, null);
		tm.put(4, null);
		tm.put(5, null);
		tm.put(6, null);
		tm.put(7, null);
		tm.put(8, null);
		tm.put(9, null);
		
		
		assertEquals(avlgtree.size(), tm.size());
	}
	
	
	
	
	@Test
	public void compareTrees(){
		SortedMap<String, Integer> avl = new AvlGTree<>(3);
		SortedMap<String, Integer> tree = new TreeMap<>();
		Set<Map.Entry<String, Integer>> e1 = avl.entrySet();
		Set<Map.Entry<String, Integer>> e2 = tree.entrySet();
		// Some random input on both data structure
		avl.put("Three", 3);
		tree.put("Three", 3);
		
		avl.put("Two", 2);
		tree.put("Two", 2);
		
		avl.put("Five", 5);
		tree.put("Five", 5);
		
		avl.put("Four", 4);
		tree.put("Four", 4);
		
		avl.put("Six", 6);
		tree.put("Six", 6);
		
		avl.put("Seven", 7);
		tree.put("Seven", 7);
		//structural equals
		boolean cond1 = (avl.equals(tree) && tree.equals(avl));
		//toString
		boolean cond2 = (avl.toString().equals(tree.toString()));
		//hashCode
		boolean cond3 = (avl.hashCode() == tree.hashCode());
		//entry set equals
		boolean cond4 = (e1.equals(e2) && e2.equals(e1));
		
		
//		// SUBMAPS
//		SortedMap<String, Integer>subAvl = avl.subMap("Five", "Two");
//		SortedMap<String, Integer>subTree = tree.subMap("Five", "Two");
//		//submap hashcode
//		assertEquals(subAvl.hashCode(),subTree.hashCode());
//		//submap size
//		assertEquals(subAvl.size(),subTree.size());
//		e1 = subAvl.entrySet();
//		e2 = subTree.entrySet();
//		//submap tostring
//		assertEquals(e1.toString(),e2.toString());
//		//submap contains
//		SortedMap<String, Integer> avl2 = new AvlGTree<>(3);
//		avl2.put("Three", 3);
//		avl2.put("Two", 2);
//		avl2.put("Five", 5);
//		avl2.put("Four", 4);
//		avl2.put("Six", 6);
//		avl2.put("Seven", 7);
//		Iterator<Map.Entry<String, Integer>> avl2iter = avl2.entrySet().iterator();
//		assertTrue(e1.contains(avl2iter.next()));
//		for (int i = 0; i < 4; i++) avl2iter.next();
//		//Shouldnt have Two
//		assertFalse(e1.contains(avl2iter.next()));
//		assertTrue(e1.equals(e2));
//		assertTrue(e2.equals(e1));
//		//submap equals
//		assertEquals(subAvl, subTree);
//		//submap of submap
//		SortedMap<String, Integer>subsubAvl = subAvl.subMap("Five", "Two");
//		SortedMap<String, Integer>subsubTree = subTree.subMap("Five", "Two");
//		assertEquals(subsubAvl, subsubTree);
//		//submap get
//		assertEquals(subAvl.get("Four"), subTree.get("Four"));
//		//submap put
//		subAvl.put("Sixty", 60);
//		subTree.put("Sixty", 60);
//		assertEquals(subAvl,subTree);
//		//submap firstkey
//		assertEquals(subAvl.firstKey(),subTree.firstKey());
//		//submap lastkey
//		assertEquals(subAvl.lastKey(),subTree.lastKey());
		
		
	}

}
