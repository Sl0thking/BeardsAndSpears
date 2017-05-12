package de.sloth.core;

import de.sloth.component.FocusComp;
import de.sloth.component.Position3DComp;
import de.sloth.entity.Entity;

public abstract class EntityToInputConverter {
	static int canvasWidth = 640;
	static int canvasHeight = 480;
	
	public static double convertEntityToValue(Entity entity) {
		Position3DComp posComp = (Position3DComp) entity.getComponent(Position3DComp.class);
		if(entity.getComponent(FocusComp.class) != null) {
			System.out.println(calcAreaCover(posComp.getX(), posComp.getY()));
		}
		
		return calcAreaCover(posComp.getX(), posComp.getY());
	}
	
	public static double calcAreaCover(int x, int y) {
		double posArea = x*(y-1) + x;
		double maxArea = canvasWidth*canvasHeight;
		return posArea / maxArea;
	}
}
