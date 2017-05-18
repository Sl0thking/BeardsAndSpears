package de.sloth.core.component;

import de.sloth.component.EnemyComp;

/**
 * Component for enemy data.
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 18.05.2017
 *
 */
public class VikingEnemyComp extends EnemyComp {
	private int tickDelay;
	private int currTickDelay;
	
	public VikingEnemyComp() {
		tickDelay = 8;
		currTickDelay = 8;
	}

	public int getTickDelay() {
		return tickDelay;
	}

	public void setTickDelay(int tickDelay) {
		this.tickDelay = tickDelay;
	}

	public int getCurrTickDelay() {
		return currTickDelay;
	}

	public void setCurrTickDelay(int currTickDelay) {
		this.currTickDelay = currTickDelay;
	}
}
