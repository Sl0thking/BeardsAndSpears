package de.sloth.spears.component;

import de.sloth.component.Component;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Component to save spears in a bag.
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 22.05.2017
 */
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