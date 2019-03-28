package application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class testUnionFind {
	private static final int size = 20;
	private UnionFind uFind;

	@BeforeEach
	void setup() {
		this.uFind = new UnionFind(size);

	}

	// tests if two nodes are connected(Should be)
	@Test
	void testConnected() {
		assertTrue(!uFind.connected(0, 1));
		uFind.union(0, 1);
		assertTrue(uFind.connected(0, 1));
	}
	// Tests to see if the amount of "birds" give is returned correctly

	@Test
	void testGetNumberOfTrees() {
		assertEquals(uFind.getNumberOfTrees(0), size);
		uFind.union(0, 1);
		assertEquals(uFind.getNumberOfTrees(0), 19);
		assertEquals(uFind.getNumberOfTrees(0), 1);
	}

}
