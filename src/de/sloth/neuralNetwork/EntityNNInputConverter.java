package de.sloth.neuralNetwork;

import de.sloth.component.Position3DComp;
import de.sloth.entity.Entity;

/**
 * Converts a entity with a position component
 * in a number between 0 and 1.
 * For example point (0|0) would be 0.0,
 * and in a canvas of dimension 640x480 the point
 * (640|480) would be 1.0.
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 18.05.2017
 *
 */
public abstract class EntityNNInputConverter {
	static int canvasWidth = 640;
	static int canvasHeight = 480;
	
	/**
	 * Convert entity with position component to a double value.
	 * A entity without position composition will always return -1.0.
	 * 
	 * @param entity target entity
	 * @return double between 0.0 and 1.0
	 */
	public static double convertEntityToValue(Entity entity) {
		Position3DComp posComp = (Position3DComp) entity.getComponent(Position3DComp.class);
		if(posComp != null) {
			return calcAreaCover(posComp.getX(), posComp.getY());
		} else {
			return -1.0;
		}
	}
	
	private static double calcAreaCover(int x, int y) {
		double posArea = canvasWidth*(y-1)+ x;
		double maxArea = canvasWidth*canvasHeight;
		return posArea / maxArea;
	}
}
