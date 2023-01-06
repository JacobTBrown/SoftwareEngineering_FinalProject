package model.helper;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AssetsManager {

	public static final int SPR_PLANE = 0;
	public static final int SPR_FIGHTER_PLANE = 1;
	public static final int SPR_BOMBER_PLANE = 2;
	public static final int SPR_AA_GUN = 3;
	public static final int SPR_FOREGROUND = 4;
	public static final int SPR_BACKGROUND = 5;

	public static final int SPRSHEET_A2 = 6;

	public static final int TILE_CARPET_UPLEFT = 7;
	public static final int TILE_CARPET_UPRIGHT = 8;
	public static final int TILE_CARPET_DOWNLEFT = 9;
	public static final int TILE_CARPET_DOWNRIGHT = 10;

	public ArrayList<BufferedImage> sprites;

	public AssetsManager() {
		sprites = new ArrayList<>();

		addSprite(Sprite.loadSprite("spr_plane"));
		addSprite(Sprite.loadSprite("spr_fighter_plane"));
		addSprite(Sprite.loadSprite("spr_bomber_plane"));
		addSprite(Sprite.loadSprite("ph_aa_gun"));
		addSprite(Sprite.loadSprite("grass_foreground"));
		addSprite(Sprite.loadSprite("ocean_background"));

		addSprite(Sprite.loadSprite("Tilesets/A2"));

		addSprite(Sprite.getSpriteSheet(getSprite(SPRSHEET_A2),8, 12));
		addSprite(Sprite.getSpriteSheet(getSprite(SPRSHEET_A2),8, 13));
		addSprite(Sprite.getSpriteSheet(getSprite(SPRSHEET_A2),9, 12));
		addSprite(Sprite.getSpriteSheet(getSprite(SPRSHEET_A2),9, 13));
	}

	/*
	Returns the position in the array.
	if(-1) then empty
	 */
	public void addSprite(BufferedImage sprite) {
		sprites.add(sprite);
	}

	public BufferedImage getSprite(int position) {
		return sprites.get(position);
	}
	//public static void playSound(Sound sound){
	//	if(Settings.getIsSoundEnabled())
	//		sound.play(1);
	//}
	
	public void dispose(){
		if(sprites.size() != 0) {
			for(int i = 0; i < sprites.size(); i++) {
				sprites.get(i).getGraphics().dispose();
			}
		}
	}
}