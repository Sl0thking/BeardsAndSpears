package de.sloth.core.neuralNetwork;

import de.sloth.core.EntityGenerator;
import de.sloth.entity.Entity;
import de.sloth.system.game.core.ConfigLoader;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.game.moveSystem.Direction;
import de.sloth.system.game.systemActivation.SystemActivationEvent;

public class StartGameNN implements IBehavior {

	@Override
	public void execute(GameSystem arg0) {}

	@Override
	public void execute(GameSystem system, GameEvent arg1) {
		system.getEntityManager().clear();
		Entity player = EntityGenerator.getInstance().generatePlayer();
		system.getEntityManager().addEntity(player);
		System.out.println(system.getEntityManager().getActivePlayabaleEntity());
		int enemyCount = Integer.parseInt(ConfigLoader.getInstance().getConfig("enemyCount", "1"));
		for(int i = 0; i < enemyCount; i++) {
			system.getEntityManager().addEntity(EntityGenerator.getInstance().generateEnemy(Direction.LEFT));
		}
		system.getEventQueue().add(new SystemActivationEvent("single", "endCondition", true));
		System.out.println("Start Game");
	}

}
