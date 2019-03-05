package application;

public interface UnionFind {

	public void union(int p, int q);

	public boolean connected(int p, int q);

	public int getNumberOfTrees(int noiseReduction);
}
