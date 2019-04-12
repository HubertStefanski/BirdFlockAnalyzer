/*
 * @author Hubert Stefanski

 * @version 1.1 (Added functionality above bare-bones)
 * this class returns coordinates used to draw boxes around groups of pixels(birds)
 */
package application;

public class PixelGroups {

	public int x1;
	public int y1;
	public int x2;
	public int y2;

	public PixelGroups(int x1, int y1, int x2, int y2) {
		this.x1 = (x1 < x2 ? x1 : x2);
		this.x2 = (x1 < x2 ? x2 : x1);
		this.y1 = y1;
		this.y2 = y2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return "pixelGroups [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + ", getX1()=" + getX1()
				+ ", getY1()=" + getY1() + ", getX2()=" + getX2() + ", getY2()=" + getY2() + "]";
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

}
