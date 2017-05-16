package de.sloth.neuralNetwork.behavior;

import java.util.Random;

import de.sloth.core.EntityGenerator;
import de.sloth.entity.Entity;
import de.sloth.system.game.core.ConfigLoader;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.game.moveSystem.Direction;
import de.sloth.system.game.systemActivation.SystemActivationEvent;

public class BStartGameWithoutGUI implements IBehavior {

	@Override
	public void execute(GameSystem arg0) {}

	@Override
	public void execute(GameSystem system, GameEvent arg1) {
		System.out.println("[StartGameSys::StartGameWithoutGUI] Start game round...");
		system.getEntityManager().clear();
		Entity player = EntityGenerator.getInstance().generatePlayer();
		system.getEntityManager().addEntity(player);
		int enemyCount = Integer.parseInt(ConfigLoader.getInstance().getConfig("enemyCount", "1"));
		Random rand = new Random();
		for(int i = 0; i < enemyCount; i++) {
			Direction direct = Direction.CENTER;
			int nDirection = rand.nextInt(3);
			if(nDirection == 0) {
				direct = Direction.LEFT;
			} else if(nDirection == 1) {
				direct = Direction.RIGHT;
			}
			system.getEntityManager().addEntity(EntityGenerator.getInstance().generateEnemy(direct));
		}
		system.getEventQueue().add(new SystemActivationEvent("single", "endCondition", true));

	}

}
