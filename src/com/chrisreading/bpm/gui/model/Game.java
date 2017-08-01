package com.chrisreading.bpm.gui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Each game has it's own model that stores information
 */
public class Game {
	
	private String title;
	private String gameExecutable;
	private String gameSaveDirectory;
	private String bpmSaveDirectory;
	
	private ArrayList<Profile> profiles;
	
	public Game() {}
	
	public Game(String title) {
		this.title = title;
		profiles = new ArrayList<Profile>();
	}
	
	public List<Profile> getProfiles() {
		return profiles;
	}
	
	public Profile getProfile(String name) {
		for(int i = 0; i < profiles.size(); i++) {
			Profile profile = profiles.get(i);
			if(profile.getName().equals(name))
				return profile;
		}
		
		return null;
	}
	
	/**
	 * Check to see if a profile already exists
	 */
	public boolean containsProfile(String name) {
		for(int i = 0; i < profiles.size(); i++) {
			Profile profile = profiles.get(i);
			if(profile.getName().equals(name))
				return true;
		}
		
		return false;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setGameExecutable(String dir) {
		this.gameExecutable = dir;
	}
	
	public void setBPMSaveDirectory(String dir) {
		this.bpmSaveDirectory = dir;
	}
	
	public void setGameSaveDirectory(String dir) {
		this.gameSaveDirectory = dir;
	}
	
	public String getGameExecutable() {
		return gameExecutable;
	}

	public String getBPMSaveDirectory() {
		return bpmSaveDirectory;
	}
	
	public String getGameSaveDirectory() {
		return gameSaveDirectory;
	}
	
	public String getTitle() {
		return title;
	}

}
