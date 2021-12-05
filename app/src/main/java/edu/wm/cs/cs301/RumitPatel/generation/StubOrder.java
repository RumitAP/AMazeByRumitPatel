package edu.wm.cs.cs301.RumitPatel.generation;


public class StubOrder implements Order {
	private int skillLevel;
	private boolean isPerfect;
	private Builder builder;
	private Maze mazeContainer;
	//setters
	
	public void setSkill(int skillLevel) {
		this.skillLevel = skillLevel;
	}
	public void setPerfect(boolean isPerfect) {
		this.isPerfect = isPerfect;
	}
	public void setBuilder(Builder builder) {
		this.builder = builder;
	}
	
	public int getSkillLevel() {
		return this.skillLevel;
	}

	public Builder getBuilder() {
		return builder;
	}

	public boolean isPerfect() {
		return this.isPerfect;
	}
	public int getSeed() {
		return 13;
	}
	public void deliver(Maze mazeConfig) {
		mazeContainer = mazeConfig;
	}
	public Maze getMazeContainer() {
		return mazeContainer;
	}
	public void updateProgress(int percentage) {

	}
	
}

