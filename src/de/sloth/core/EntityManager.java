package de.sloth.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.sloth.component.FocusComp;
import de.sloth.component.Position3DComp;
import de.sloth.entity.Entity;
import de.sloth.system.game.core.IEntityManagement;

/**
 * Class for managing internal entity-list
 *  
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 17.05.2017
 */
public class EntityManager implements IEntityManagement {
	private List<Entity> playableEntities;
	private Entity activeEntity;
	private Map<Integer, List<Entity>> chunkLists;
	private List<Entity> other;
	private int internalIdCounter;
	private static final int CHUNKS_PER_ROW = 1;
	private static final int CHUNKS_ROWS = 1;
	private static final int CHUNK_WIDTH = 640;
	private static final int CHUNK_HEIGTH = 480;
	
	/**
	 * 
	 */
	public EntityManager() {
		this.playableEntities = new LinkedList<Entity>();
		this.activeEntity = null;
		this.other = new LinkedList<Entity>();
		this.chunkLists = new HashMap<Integer, List<Entity>>();
		for(int x = 0; x < CHUNKS_PER_ROW * CHUNKS_ROWS; x++) {
			this.chunkLists.put(x, new LinkedList<Entity>());
		}
		internalIdCounter = 0;
	}
	
	@Override
	public List<Entity> getAllEntities() {
		List<Entity> allEntities = new LinkedList<Entity>();
		for(Entity pE:playableEntities) {
			if(!allEntities.contains(pE)) {
				allEntities.add(pE);
			}
		}
		for(Entity pE:other) {
			if(!allEntities.contains(pE)) {
				allEntities.add(pE);
			}
		}
		for(List<Entity> entities:this.chunkLists.values()) {
			for(Entity pE:entities) {
				if(!allEntities.contains(pE)) {
					allEntities.add(pE);
				}
			}
		}
		return allEntities;
	}
	
	/**
	 * (non-Javadoc)
	 * @see de.sloth.system.game.core.IEntityManagement#getPlayableEntities()
	 */
	@Override
	public List<Entity> getPlayableEntities() {
		return this.playableEntities;
	}

	/**
	 * @see de.sloth.system.game.core.IEntityManagement#getActivePlayabaleEntity()
	 */
	@Override
	public Entity getActivePlayabaleEntity() {
		return this.activeEntity;
	}

	@Override
	public List<Entity> getChunkEntities() {
		if(this.activeEntity != null) {
			Position3DComp pos = (Position3DComp) this.activeEntity.getComponent(Position3DComp.class);
			List<Entity> chunkRoom = new LinkedList<Entity>();
			int chunkNr = this.getChunkIndex(pos);
			chunkRoom.addAll(this.chunkLists.get(this.getChunkIndex(pos)));
			if(chunkNr % CHUNKS_PER_ROW > 0) {
				chunkRoom.addAll(this.chunkLists.get(chunkNr-1));
			}
			if(chunkNr % CHUNKS_PER_ROW < CHUNKS_PER_ROW ) {
				chunkRoom.addAll(this.chunkLists.get(chunkNr+1));
			}
			if(chunkNr % CHUNKS_ROWS > 0) {
				chunkRoom.addAll(this.chunkLists.get(chunkNr-CHUNKS_PER_ROW));
			}
			if(chunkNr % CHUNKS_ROWS < CHUNKS_ROWS ) {
				chunkRoom.addAll(this.chunkLists.get(chunkNr+CHUNKS_PER_ROW));
			}
			return chunkRoom;
		} else {
			return new LinkedList<Entity>();
		}
	}
	
	private int getChunkIndex(Position3DComp pos) {
		int xPos = pos.getX();
		int yPos = pos.getY();
		int chunkX = (int) xPos / CHUNK_WIDTH;
		int chunkY = (int) yPos / CHUNK_HEIGTH;
		return chunkX + (CHUNKS_PER_ROW*chunkY);
	}

	@Override
	public void addEntity(Entity entity) {
		FocusComp fComp = (FocusComp) entity.getComponent(FocusComp.class);
		Position3DComp pos = (Position3DComp) entity.getComponent(Position3DComp.class);
		entity.setId(internalIdCounter);
		internalIdCounter++;
		
		if(fComp != null) {
			this.activeEntity = entity;
			this.playableEntities.add(entity);
		} 
		
		if(pos != null) {
			List<Entity> chunkList = this.chunkLists.get(this.getChunkIndex(pos));
			if(chunkList != null) {
				chunkList.add(entity);
			}
		}
		
		if(pos == null && fComp == null) {
			this.other.add(entity);
		}
	}

	@Override
	public List<Entity> getOtherEntities() {
		return this.other;
	}

	@Override
	public void clear() {
		this.activeEntity = null;
		this.playableEntities.clear();
		this.other.clear();
		for(List<Entity> chunkEntities : this.chunkLists.values()) {
			chunkEntities.clear();
		}
	}

	@Override
	public void removeEntity(Entity entity) {
		if(this.activeEntity != null && this.activeEntity.equals(entity)) {
			this.activeEntity = null;
		}
		this.playableEntities.remove(entity);
		this.other.remove(entity);
		for(List<Entity> chunkEntities : this.chunkLists.values()) {
			chunkEntities.remove(entity);
		}
	}
}