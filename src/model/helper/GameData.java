package model.helper;

import controller.observer.*;
import model.GameObject;
import model.npo.enemies.AAGun;
import model.npo.enemies.BomberPlane;
import model.npo.enemies.FighterPlane;
import model.npo.weapons.Bomb;
import model.npo.weapons.Bullet;
import model.npo.weapons.Rocket;

import java.util.ArrayList;

public class GameData {
    public ArrayList<GameObject> gameObjects = new ArrayList<>();

    //public ArrayList<GameObject> fixedObject = new ArrayList<>();
   // public ArrayList<GameObject> friendObject = new ArrayList<>();
    //public ArrayList<GameObject> enemyObject = new ArrayList<>();

    public int score;

    public GameData() {
        score = 0;
    }

    public void update(double deltaTime) {
        ArrayList<GameObject> remove = new ArrayList<>();
        for (var fig: gameObjects) {
            if (fig.done) remove.add(fig);
            else fig.update(deltaTime);
        }
        gameObjects.removeAll(remove);
        gameObjects.trimToSize();

        /*for( var fig: fixedObject) {
            if (fig.done) remove.add(fig);
            else fig.update(deltaTime);
        }
        fixedObject.removeAll(remove);

        remove.clear();
        for(var fig: friendObject) {
            if (fig.done) remove.add(fig);
            else fig.update(deltaTime);
        }
        friendObject.removeAll(remove);

        remove.clear();
        for(var fig: enemyObject) {
            if (fig.done) remove.add(fig);
            else fig.update(deltaTime);
        }
        enemyObject.removeAll(remove);*/
    }

    public void destroyAll() {
        gameObjects.clear();

        //fixedObject.clear();
        //friendObject.clear();
       // enemyObject.clear();
    }

    public void addEnemyBullet(GameObject context) {
        var bullet = new Bullet(context);
        gameObjects.add(bullet);
        //bullet.attachListener(new Observer_);
    }

    public void addEnemyBomb(BomberPlane context) {
        var bomb = new Bomb(context);
        gameObjects.add(bomb);
    }

    public void addEnemyRocket(AAGun context, int tx, int ty) {
        var rocket = new Rocket(context);
        gameObjects.add(rocket);
    }

    public void addFighterWithListener(int x, int y) {
        var fighterPlane = new FighterPlane(x, y);
        fighterPlane.attachListener(new Observer_FPAddNew(fighterPlane));
        fighterPlane.attachListener(new Observer_FPAddPoint());
        gameObjects.add(fighterPlane);
    }

    public void addBomberWithListener(int x, int y) {
        var bomberPlane = new BomberPlane(x, y);
        bomberPlane.attachListener(new Observer_BPAddNew(bomberPlane));
        bomberPlane.attachListener(new Observer_BPAddPoint());
        gameObjects.add(bomberPlane);
    }

    public void addAAGunWithListener(int x, int y) {
        var aaGun = new AAGun(x, y);
        aaGun.attachListener(new Observer_AAGunAddNew(aaGun));
        aaGun.attachListener(new Observer_AAGunAddPoint());
        gameObjects.add(aaGun);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addToScore(int score) {
        this.score += score;
    }
}
