/*
 * @author Hubert Stefanski
 * @version 1.1 (Added functionality above bare-bones)
 */

package application;

/*
 * Interface used by QuickUnionFind to implement these methods
 */
public interface UnionFind {

	public void union(int p, int q);

	public boolean connected(int p, int q);

	public int getNumberOfTrees(int noiseReduction);
}
