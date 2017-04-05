package org.ethan.oss.api.interactive;

import org.ethan.oss.api.wrappers.GameObject;
import org.parabot.osscape.api.wrapper.Tile;
import org.ethan.oss.reflection.ReflWrapper;
import org.ethan.oss.utils.Random;
import org.ethan.oss.utils.Utilities;
import org.parabot.environment.api.utils.Filter;
import org.parabot.osscape.api.methods.Game;
import org.parabot.osscape.api.methods.Players;
import org.parabot.osscape.api.wrapper.Player;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GameObjects extends ReflWrapper {
    public static GameObject[] getAll() {
        return getAll(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return true;
            }
        });
    }

    public static GameObject[] getAll(final String... names) {
        return getAll(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.isValid() && gameObject.getName() != null
                        && Utilities.inArray(gameObject.getName(), names);
            }
        });
    }

    public static GameObject[] getAll(final int... ids) {
        return getAll(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.isValid() && Utilities.inArray(gameObject.getId(), ids);
            }
        });
    }

    public static GameObject[] getAll(Filter<GameObject> filter) {
        Set<GameObject> objects = new LinkedHashSet<GameObject>();
        Object          region  = getFieldValue("Region", null);
        Object[][][]    tiles   = (Object[][][]) getFieldValue("SceneTiles", region);
        if (tiles == null) {
            return objects.toArray(new GameObject[objects.size()]);
        }

        int z     = Game.getPlane();
        int baseX = Game.getBaseX(), baseY = Game.getBaseY();

        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                Object groundTile = tiles[z][x][y];
                if (groundTile != null) {

                    Object[] gameObjects = (Object[]) getFieldValue("GameObject", groundTile);
                    if (gameObjects != null) {
                        for (Object j : gameObjects) {
                            if (j != null) {
                                GameObject obj = new GameObject(j, GameObject.Type.INTERACTIVE, x + baseX, y + baseY,
                                        z);
                                if (obj != null && (filter == null || filter.accept(obj))) {
                                    objects.add(obj);
                                }

                            }
                        }
                    }

                    Object boundary = getFieldValue("BoundaryObject", groundTile);
                    if (boundary != null) {
                        GameObject obj = new GameObject(boundary, GameObject.Type.BOUNDARY, x + baseX, y + baseY, z);
                        if (obj != null && (filter == null || filter.accept(obj))) {
                            objects.add(obj);
                        }
                    }

                }

            }
        }
        return objects.toArray(new GameObject[objects.size()]);
    }

    public static GameObject getNearest(Filter<GameObject> filter) {
        return getNearest(Players.getMyPlayer().getLocation(), filter);
    }

    public static GameObject getNearest(Tile start, Filter<GameObject> filter) {
        GameObject closet   = new GameObject(null, GameObject.Type.INTERACTIVE, -1, -1, -1);
        int        distance = 255;
        for (GameObject gameObject : getAll(filter)) {
            if (gameObject.isValid() && distance > gameObject.distanceTo(start)) {
                closet = gameObject;
                distance = gameObject.distanceTo(start);
            }
        }
        return closet;
    }

    public static GameObject getNearest(final int... ids) {
        return getNearest(Players.getMyPlayer().getLocation(), new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.isValid() && Utilities.inArray(gameObject.getId(), ids);
            }
        });
    }

    public static GameObject getNearest(final String... names) {
        return getNearest(Players.getMyPlayer().getLocation(), new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject groundItem) {
                return groundItem.isValid() && Utilities.inArray(groundItem.getName(), names);
            }
        });
    }

    public static GameObject getAt(final Tile tile) {
        return getNearest(Players.getMyPlayer().getLocation(), new Filter<GameObject>() {

            @Override
            public boolean accept(GameObject obj) {
                return obj != null && tile.equals(obj.getLocation());
            }
        });
    }

    /**
     * @author Fryslan
     */
    public static GameObject[] rocksBeingMined() {
        List<GameObject> list = new ArrayList<GameObject>();
        GameObject[] rocks = GameObjects.getAll(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.isValid() && gameObject.getId() > 7450
                        && gameObject.getId() < 7500 && gameObject.isOnScreen();
            }
        });
        Player[] players = Players
                .getNearest(new Filter<Player>() {
                    @Override
                    public boolean accept(Player player) {
                        return !player.equals(Players.getMyPlayer()) && player.getAnimation() != -1;
                    }
                });
        for (Player player : players) {
            Tile loc  = player.getLocation();
            Tile rock = Players.getMyPlayer().getLocation();
            switch (player.getOrientation()) {
                case 0:
                    rock = new Tile(loc.getX(), loc.getY() - 1);
                    break;
                case 511:
                    rock = new Tile(loc.getX() - 1, loc.getY());
                    break;
                case 1023:
                    rock = new Tile(loc.getX(), loc.getY() + 1);
                    break;
                case 1537:
                    rock = new Tile(loc.getX() + 1, loc.getY());
                    break;
            }

            for (GameObject r : rocks) {
                if (r.getLocation().equals(rock)) {
                    list.add(r);

                }
            }
        }
        return list.toArray(new GameObject[list.size()]);
    }

    public static GameObject getNearbyBank() {
        GameObject current = null;
        for (GameObject n : GameObjects.getAll()) {
            if (Game.isLoggedIn() && n != null && n.isValid() && n.getLocation().distanceTo() < 15) {
                if (n.hasAction("Bank", n.getActions()) && (current == null || current.getLocation().distanceTo() > Players.getMyPlayer()
                        .distanceTo(n.getLocation()))) {
                    current = n;
                }
            }
        }
        return current;
    }

    public static GameObject getNext(Filter<GameObject> filter) {
        GameObject[] gameObjects = getAll(filter);
        if (gameObjects == null || gameObjects.length < 1) {
            return nil();
        }
        return gameObjects[Random.nextInt(0, gameObjects.length)];
    }

    public static GameObject getNext(final String... names) {
        return getNext(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.isValid() && gameObject.getName() != null
                        && Utilities.inArray(gameObject.getName(), names);
            }
        });
    }

    public static GameObject getNext(final int... ids) {
        return getNext(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.isValid() && Utilities.inArray(gameObject.getId(), ids);
            }
        });
    }

    public static GameObject nil() {
        return new GameObject(null, GameObject.Type.INTERACTIVE, -1, -1, -1);
    }
}