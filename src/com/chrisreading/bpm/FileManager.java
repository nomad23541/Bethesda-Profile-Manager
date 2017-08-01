package com.chrisreading.bpm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.chrisreading.bpm.gui.model.Game;
import com.chrisreading.bpm.gui.model.Profile;
import com.chrisreading.bpm.utility.Vars;

/**
 * Manages program directory, transfering of saves
 */
public class FileManager {
	
	private List<Game> games;
	private File appDir;
	
	public FileManager(List<Game> games) {
		this.games = games;
		appDir = new File(Vars.APP_DIRECTORY);
		
		/* Create the main directory if it doesn't exist */
		if(!appDir.exists()) {
			System.out.println("App directory doesn't exist, creating...");
			appDir.mkdir();
		}
		
		/* Create folders for each game and their associated profiles */
		try {
			createGameFolders();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(getGameConfig(games.get(2)).toString());
	}
	
	/**
	 * Create game folders, scan for existing profiles and adds them to the game's
	 * profile list.
	 * @throws IOException
	 */
	private void createGameFolders() throws IOException {
		/* Create a folder for each detected game (if it doesn't exist already) */
		for(Game g : games) {
			File gameDir = new File(appDir + File.separator + g.getTitle());
			File gameConfig = new File(gameDir + File.separator + g.getTitle() + "Config");
			
			if(!gameDir.exists()) {
				gameDir.mkdir();
				System.out.println("Created folder for " + g.getTitle());
			}
			
			if(!gameConfig.exists()) {
				gameConfig.createNewFile();
				System.out.println("Created settings file for " + g.getTitle());
			}
			
			g.setBPMSaveDirectory(gameDir.toString());
			
			/* Add existing profile folders to the profiles list */
			File[] profileFolders = new File(g.getBPMSaveDirectory()).listFiles();
			for(File f : profileFolders) {
				if(!g.containsProfile(f.getName()) & f.isDirectory()) {
					g.getProfiles().add(new Profile(f.getName()));
				}
			}
			
			/* Then create a folder for each profile in that game (if it doesn't exist already) */
			for(Profile pm : g.getProfiles()) {
				File profileDir = new File(gameDir + File.separator + pm.getName());
				if(!profileDir.exists()) {
					profileDir.mkdir();
					System.out.println("Created profile folder " + pm.getName() + " in " + g.getTitle());
				}
			}
		}
	}
	
	/**
	 * Set the last profile used
	 * @param game
	 * @param profile
	 * @throws IOException
	 */
	public void setLastProfileUsed(Game game, Profile profile) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(getGameConfig(game)));
		writer.write(profile.getName());
		writer.close();
	}
	
	/**
	 * Get the last profile used
	 * @param game
	 * @return
	 * @throws IOException
	 */
	public Profile getLastProfileUsed(Game game) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(getGameConfig(game)));
		String name = reader.readLine();
		reader.close();
		return game.getProfile(name);
	}
	
	/**
	 * Create a profile under a selected game
	 * @param game
	 * @param name
	 * @throws IOException
	 */
	public void createProfile(Game game, String name) throws IOException {
		game.getProfiles().add(new Profile(name));
		/* create game folders again */
		createGameFolders();
	}
	
	/**
	 * Returns the game's config
	 * @param game
	 * @return
	 */
	private File getGameConfig(Game game) {
		return new File(game.getBPMSaveDirectory() + File.separator + game.getTitle() + "Config");
	}
	
	/**
	 * Transfer old saves to the BPM folder, then transfer new saves to the game folder
	 * @param game
	 * @param oldProfile
	 * @param newProfile
	 * @throws IOException
	 */
	public void switchSaves(Game game, Profile oldProfile, Profile newProfile) throws IOException {
		File gameFolder = new File(game.getGameSaveDirectory());
		File oldProfileFolder = null;
		File newProfileFolder = new File(game.getBPMSaveDirectory() + File.separator + newProfile.getName());
		
		// make sure the old profile exists (may be the first time using bpm)
		if(oldProfile != null) {
			 oldProfileFolder = new File(game.getBPMSaveDirectory() + File.separator + oldProfile.getName());
				FileUtils.cleanDirectory(oldProfileFolder); // delete old saves in oldprofile
				FileUtils.copyDirectory(gameFolder, oldProfileFolder); // move new saves to oldprofile
				FileUtils.cleanDirectory(gameFolder);
				System.out.println("Unloaded " + oldProfile.getName() + "'s saves to the BPM folder");	
		}
		
		FileUtils.copyDirectory(newProfileFolder, gameFolder); // move newprofile's saves to the game folder
		System.out.println("Loaded " + newProfile.getName() + "'s saves to the game folder");
		
	}

}
