package de.sloth.core;

import de.sloth.component.AnimationComp;
import de.sloth.component.FocusComp;
import de.sloth.component.HealthComp;
import de.sloth.component.HitboxComp;
import de.sloth.component.MovableComp;
import de.sloth.component.Position3DComp;
import de.sloth.component.ScoreComp;
import de.sloth.component.SpriteComp;
import de.sloth.components.NeuralNetwork;
import de.sloth.components.NeuralNetworkComp;
import de.sloth.components.SlothEnemyComp;
import de.sloth.components.SpearBagComp;
import de.sloth.entity.Entity;
import de.sloth.system.game.core.ConfigLoader;
import de.sloth.system.game.flying.FlyingComp;
import de.sloth.system.game.moveSystem.Direction;

public class EntityGenerator {
	private static EntityGenerator instance;
	private static final int CANVAS_WIDTH = 640;
	private static final int CANVAS_HEIGTH = 480;
	private static final int SPRITE_WIDTH = 32;
	private static final int SPRITE_HEIGHT = 32;
	private static final double SCALING = 2.;
	private ConfigLoader cl;
	
	private EntityGenerator() {
		this.cl = ConfigLoader.getInstance("valHal.properties");
	}
	
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
		MovableComp mComp = new MovableComp(16, Direction.RIGHT);
		HitboxComp hitcomp = new HitboxComp(32, 32);
		//AnimationComp aniComp = new AnimationComp("idle", 0, 20);
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
		//enemy.addComponent(aniComp);
		return enemy;
	}
	
	public Entity generatePlayer() {
		int playerLife = Integer.parseInt(cl.getConfig("playerLife", "5"));
		int playerSpears = Integer.parseInt(cl.getConfig("playerSpears", "0"));
		int playerSpeed = Integer.parseInt(cl.getConfig("playerSpeed", "8"));
		Entity sloth = new Entity();
		sloth.setName("Sloth");
		sloth.setId(-1);
		Position3DComp posComp = new Position3DComp();
		posComp.setX(300);
		MovableComp mComp = new MovableComp(playerSpeed, Direction.LEFT);
		HitboxComp hbox = new HitboxComp(32, 32);
		HealthComp heComp = new HealthComp(playerLife);
		SpearBagComp spComp = new SpearBagComp(playerSpears);
		ScoreComp scoreComp = new ScoreComp(0);
		AnimationComp aniComp = new AnimationComp("idle", 0, 15);
		posComp.setY(CANVAS_HEIGTH-(int) (SPRITE_HEIGHT*SCALING));
		FocusComp fComp = new FocusComp();
		SpriteComp sComp = new SpriteComp("Viking_sheet");
		sloth.addComponent(posComp);
		sloth.addComponent(fComp);
		sloth.addComponent(sComp);
		sloth.addComponent(hbox);
		sloth.addComponent(mComp);
		sloth.addComponent(heComp);
		sloth.addComponent(spComp);
		sloth.addComponent(scoreComp);
		sloth.addComponent(aniComp);
		return sloth;
	}
	
	public Entity generateFlyingSpear(Entity thrower) {
		int spearSpeed = Integer.parseInt(cl.getConfig("spearSpeed", "4"));
		Entity spear = new Entity();
		spear.setId(-1);
		Position3DComp throwPosComp = (Position3DComp) thrower.getComponent(Position3DComp.class);
		Position3DComp spearPosComp = new Position3DComp();
		HitboxComp hitbox = new HitboxComp(14,32);
		spearPosComp.setX(throwPosComp.getX()+10);
		//AnimationComp aniComp = new AnimationComp("idle", 0, 20);
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
		MovableComp movComp = new MovableComp(spearSpeed, Direction.TOP);
		spear.addComponent(flyComp);
		spear.addComponent(movComp);
		spear.addComponent(spearPosComp);
		spear.addComponent(spComp);
		spear.addComponent(hitbox);
		return spear; 
		
	}

	public Entity generateNNEntity() {
		Entity nnEntity = new Entity();
		nnEntity.setId(-1);
		int population = 4; //get from prop in future
		NeuralNetworkComp nnComp = new NeuralNetworkComp(Integer.parseInt(ConfigLoader.getInstance().getConfig("nnGenerations", "5")), new NeuralNetwork(), population);
		nnEntity.addComponent(nnComp);
		return nnEntity;
	}
}