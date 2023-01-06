package model;

import model.Tiles.Tile;
import model.helper.AssetsManager;
import model.helper.SimplexNoise;

import java.util.ArrayList;

public class Level {
    private Tile[][] map;
    private AssetsManager assets;

    public ArrayList<GameObject> levelObjects = new ArrayList<>();
    public int score;

    private short permMod12[] = new short[512];
    private static final double F2 = 0.5*(Math.sqrt(3.0)-1.0);
    private static final double G2 = (3.0-Math.sqrt(3.0))/6.0;

    /**
     * Creates a level, a container for objects
     * @param width tiles across
     * @param height tiles down
     */
    public Level(AssetsManager assets, int width, int height) {
        map = new Tile[width][height];
        this.assets = assets;

        double[][] noiseData = generateNoiseMap(width, height, 5000);
        map = generateTileMap(width, height, noiseData);
    }

    public void update(double deltaTime) {
        ArrayList<GameObject> remove = new ArrayList<>();
        for (var fig: levelObjects) {
            if (fig.done) remove.add(fig);
            else fig.update(deltaTime);
        }
        levelObjects.removeAll(remove);
        levelObjects.trimToSize();

        //for (int x = 0; x < )
    }

    public void destroyAll() {
        levelObjects.clear();
    }

    public Tile[][] generateTileMap(int width, int height, double[][] noiseData) {
        Tile[][] tempMap = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tempMap[x][y] = new Tile(assets, x, y);
            }
        }
        return tempMap;
    }

    public double[][] generateNoiseMap(int width, int height, int seed) {
        SimplexNoise simplexNoise = new SimplexNoise(100, 0.1, seed);
        double[][] noiseData = new double[width][height];
        double xStart = 0;
        double XEnd = 500;
        double yStart = 0;
        double yEnd = 500;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int x =(int)(xStart + i * ((XEnd-xStart) / width));
                int y =(int)(yStart + j * ((yEnd-yStart) / height));
                noiseData[i][j] = 0.5 * (1 + simplexNoise.getNoise(x,y));
            }
        }
        return noiseData;
    }
}
