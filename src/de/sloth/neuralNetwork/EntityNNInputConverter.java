package de.sloth.neuralNetwork;

import de.sloth.component.Position3DComp;
import de.sloth.entity.Entity;

public abstract class EntityNNInputConverter {
	static int canvasWidth = 640;
	static int canvasHeight = 480;
	
	public static double convertEntityToValue(Entity entity) {
		Position3DComp posComp = (Position3DComp) entity.getComponent(Position3DComp.class);
		return calcAreaCover(posComp.getX(), posComp.getY());
	}
	
	public static double calcAreaCover(int x, int y) {
		double posArea = x*(y-1) + x;
		double maxArea = canvasWidth*canvasHeight;
		return posArea / maxArea;
	}
}
