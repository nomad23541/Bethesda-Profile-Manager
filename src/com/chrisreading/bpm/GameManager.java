package com.chrisreading.bpm;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.chrisreading.bpm.gui.model.Game;
import com.chrisreading.bpm.utility.Vars;
import com.chrisreading.bpm.utility.WinRegistry;

/**
 * Responsible for finding bethesda games and
 * then creating a gamemodel for each one.
 */
public class GameManager {
	
	private ArrayList<Game> games;
	
	public GameManager() {
		games = new ArrayList<Game>();
		
		try {
			findGames();
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Searches the Window's Registry for Bethesda game paths.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	private void findGames() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		/* Scan all games in the Bethesda Softworks registry key */
		List<String> list = WinRegistry.subKeysForPath(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\WOW6432Node\\Bethesda Softworks\\");
		for(String s : list) {
			if(StringUtils.equalsIgnoreCase(s, "skyrim")) {
				Game game = new Game("Skyrim");
				game.setGameExecutable(WinRegistry.valueForKey(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\WOW6432Node\\Bethesda Softworks\\" + s, "installed path") + "SkyrimLauncher.exe");
				game.setGameSaveDirectory(Vars.FOLDER_SKYRIM + File.separator + "Saves");
				games.add(game);
			}
			
			if(StringUtils.containsIgnoreCase(s, "skyrim") & StringUtils.containsIgnoreCase(s, "special edition")) {
				Game game = new Game("Skyrim SE");
				game.setGameExecutable(WinRegistry.valueForKey(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\WOW6432Node\\Bethesda Softworks\\" + s, "installed path") + "SkyrimSELauncher.exe");
				games.add(game);
			}
			
			if(StringUtils.containsIgnoreCase(s, "fallout") & StringUtils.contains(s, "4")) {
				Game game = new Game("Fallout 4");
				game.setGameExecutable(WinRegistry.valueForKey(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\WOW6432Node\\Bethesda Softworks\\" + s, "installed path") + "Fallout4Launcher.exe");
				games.add(game);
			}
			
			if(StringUtils.containsIgnoreCase(s, "fallout") & StringUtils.contains(s, "3")) {
				Game game = new Game("Fallout 3");
				game.setGameExecutable(WinRegistry.valueForKey(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\WOW6432Node\\Bethesda Softworks\\" + s, "installed path"));
				games.add(game);
			}
			
			if(StringUtils.containsIgnoreCase(s, "fallout") & StringUtils.contains(s, "new vegas")) {
				Game game = new Game("Fallout New Vegas");
				game.setGameExecutable(WinRegistry.valueForKey(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\WOW6432Node\\Bethesda Softworks\\" + s, "installed path"));
				games.add(game);
			}
			
		}
		
		/* Print detected games to console */
		for(Game g : games) {
			System.out.println("Found " + g.getTitle() + " in " + g.getGameExecutable());
		}
	}
	
	/**
	 * Get a game by it's title
	 * @param title
	 * @return
	 */
	public Game getGame(String title) {
		for(int i = 0; i < games.size(); i++) {
			Game game = games.get(i);
			if(game.getTitle().equals(title))
				return game;
		}
		
		return null;
	}
	
	public List<Game> getGames() {
		return games;
	}

}
