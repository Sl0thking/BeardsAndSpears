package de.sloth.components;

import de.sloth.component.Component;
import javafx.beans.property.SimpleIntegerProperty;

public class SpearBagComp extends Component {
	private SimpleIntegerProperty spears;
	
	public SpearBagComp(int spears) {
		this.spears = new SimpleIntegerProperty(spears);
	}
	
	public SimpleIntegerProperty getSpearProperty() {
		return this.spears;
	}
	
	public int getSpears() {
		return spears.get();
	}

	public void setSpears(int spears) {
		this.spears.set(spears);
	}
}
