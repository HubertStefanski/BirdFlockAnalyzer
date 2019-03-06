package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuickUnionFind implements UnionFind {

	private final int N;
	private final int id[];
	private final int size[];

	public QuickUnionFind(int n) {
		N = n;
		id = new int[N];
		size = new int[N];

		for (int x = 0; x < N; x++) {
			id[x] = x;
			size[x] = 1;
		}
	}

	private int root(int x) {
		while (x != id[x]) {
			id[x] = id[id[x]]; // compression of the path
			x = id[x];
		}
		return x;
	}

	@Override
	public void union(int p, int q) {
		int x = root(p);
		int y = root(q);

		if (x != y) {
			if (size[x] < size[y]) {
				id[x] = y;
				size[y] += size[x];
			} else {
				id[y] = x;
				size[x] += size[y];
			}
		}
	}

	@Override
	public boolean connected(int p, int q) {
		return root(p) == root(q);
	}

	@Override
	public int getNumberOfTrees(final int noiseReduction) {
		Set<Integer> s = new HashSet<Integer>();
		for (int x = 0; x < N; x++) {
			if (size[x] > noiseReduction) {
				s.add(id[x]);
			}

		}
		return s.size();

	}

	public Set<Integer> getRoots(final int noiseReduction) {
		Set<Integer> s = new HashSet<Integer>();
		for (int x = 0; x < N; x++) {
			if (size[x] > noiseReduction) {
				s.add(root(x));
			}
		}
		return s;
	}

	public List<Integer> getElementsOfTree(final int node) {
		final int rootNode = root(node);
		if (rootNode == node && size[node] == 1) {
			return Arrays.asList(rootNode);
		}
		final List<Integer> treeElements = new ArrayList<Integer>();
		for (int x = 0; x < N; x++) {
			if (root(x) == rootNode) {
				treeElements.add(x);
			}
		}
		return treeElements;

	}

	

}
