package de.sloth.core;

import de.sloth.system.game.core.GameEvent;

public class CalcScoreEvent extends GameEvent {
	
	private ScoreType sType;
	
	public CalcScoreEvent(ScoreType sType) {
		super("CalcScore");
		this.sType = sType;
	}

	public ScoreType getsType() {
		return sType;
	}

	public void setsType(ScoreType sType) {
		this.sType = sType;
	}
}