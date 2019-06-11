package main;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Ship {
	private int size;
	private boolean orientation; //true: vertical, false: horizontal
	private Map<Point, Boolean> points; //each point maps to a boolean representing if that spot has been hit
	
	public Ship(int size, boolean orientation, Point location) {
		this.size = size;
		this.orientation = orientation;
		points = new HashMap<Point, Boolean>();
		place(location);
	}
	
	public Ship(int size, boolean orientation) {
		this(size, orientation, new Point(-1, -1));
	}
	
	public void place(Point location) {
		points.clear();
		if (orientation) { //ship is vertical
			for (int i = 0; i < size; i++) {
				points.put(new Point(location.x, location.y + i), false);
			}
		} else { //ship is horizontal
			for (int i = 0; i < size; i++) {
				points.put(new Point(location.x + i, location.y), false);
			}
		}
	}

	public boolean isHit(Point guess) {
		return points.keySet().contains(guess);
	}
	
	public void guess(Point guess) {
		if (isHit(guess)) {
			points.replace(guess, true);
		}
	}
	
	public boolean isDestroyed() {
		for (boolean b: points.values()) {
			if (!b) {
				return false;
			}
		}
		return true;
	}

	public Set<Point> getPoints() {
		return points.keySet();
	}
	
	public boolean getOrientation() {
		return orientation;
	}
	
	public int size() {
		return size;
	}
	
	public String toString() {
		if (orientation) {
			return "[Ship: Length=" + size + ", V, Location=" + getPoints() + "]";
		} else {
			return "[Ship: Length=" + size + ", H, Location=" + getPoints() + "]";
		}
	}
}
