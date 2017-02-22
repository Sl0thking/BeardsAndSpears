package de.sloth.core;

import de.sloth.component.FocusComp;
import de.sloth.component.HitboxComp;
import de.sloth.component.MovableComp;
import de.sloth.component.Position3DComp;
import de.sloth.component.ScoreComp;
import de.sloth.component.SpriteComp;
import de.sloth.components.SlothComp;
import de.sloth.components.SlothEnemyComp;
import de.sloth.entity.Entity;
import de.sloth.system.game.flying.FlyingComp;
import de.sloth.system.game.moveSystem.Direction;

public class EntityGenerator {
	
	private static EntityGenerator instance;
	private static final int CANVAS_WIDTH = 640;
	private static final int CANVAS_HEIGTH = 480;
	private static final int SPRITE_WIDTH = 32;
	private static final int SPRITE_HEIGHT = 32;
	private static final double SCALING = 2.;
	
	
	private EntityGenerator() {}
	
	public static EntityGenerator getInstance() {
		if(instance == null) {
			instance = new EntityGenerator();
		}
		return instance;
	}
	
	
	public Entity generateEnemy(Direction direction) {
		Entity enemy = new Entity();
		enemy.setId(-1);
		enemy.setName("Viking");
		Position3DComp posComp = new Position3DComp();
		MovableComp mComp = new MovableComp(16);
		HitboxComp hitcomp = new HitboxComp(32, 32);
		if(direction.equals(Direction.LEFT)) {
			posComp.setX((int) (SPRITE_WIDTH*SCALING));
			posComp.setY((int) (SPRITE_HEIGHT*SCALING));
		} else {
			posComp.setX(CANVAS_WIDTH-((int) (SPRITE_WIDTH*SCALING)));
			posComp.setY((int) (SPRITE_HEIGHT*SCALING));
		}
		SpriteComp s2Comp = new SpriteComp("Viking_left.png");
		enemy.addComponent(posComp);
		enemy.addComponent(s2Comp);
		enemy.addComponent(new SlothEnemyComp());
		enemy.addComponent(mComp);
		enemy.addComponent(hitcomp);
		return enemy;
	}
	
	public Entity generatePlayer() {
		Entity sloth = new Entity();
		sloth.setName("Sloth");
		sloth.setId(-1);
		Position3DComp posComp = new Position3DComp();
		MovableComp mComp = new MovableComp(8);
		HitboxComp hbox = new HitboxComp(32, 32);
		SlothComp scomp = new SlothComp(0,5);
		ScoreComp scoreComp = new ScoreComp(0);
		posComp.setY(CANVAS_HEIGTH-(int) (SPRITE_HEIGHT*SCALING));
		FocusComp fComp = new FocusComp();
		SpriteComp sComp = new SpriteComp("Viking_right.png");
		sloth.addComponent(posComp);
		sloth.addComponent(fComp);
		sloth.addComponent(sComp);
		sloth.addComponent(hbox);
		sloth.addComponent(mComp);
		sloth.addComponent(scomp);
		sloth.addComponent(scoreComp);
		return sloth;
	}
	
	public Entity generateFlyingSpear(Entity thrower) {
		Entity spear = new Entity();
		spear.setId(-1);
		Position3DComp throwPosComp = (Position3DComp) thrower.getComponent(Position3DComp.class);
		Position3DComp spearPosComp = new Position3DComp();
		HitboxComp hitbox = new HitboxComp(16,32);
		spearPosComp.setX(throwPosComp.getX());
		Direction direct = Direction.TOP;
		SpriteComp spComp = new SpriteComp("spear.png");
		if(thrower.getComponent(FocusComp.class) == null) {
			spearPosComp.setY(throwPosComp.getY() + (int) (SPRITE_HEIGHT*SCALING));
		} else {
			spearPosComp.setY(throwPosComp.getY() - (int) (SPRITE_HEIGHT*SCALING));
			direct = Direction.BOTTOM;
			spComp = new SpriteComp("spear_backward.png");
		}
		FlyingComp flyComp = new FlyingComp(500, direct);
		MovableComp movComp = new MovableComp(2);
		spear.addComponent(flyComp);
		spear.addComponent(movComp);
		spear.addComponent(spearPosComp);
		spear.addComponent(spComp);
		spear.addComponent(hitbox);
		return spear; 
		
	}
}