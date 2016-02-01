package ch.ethz.challenge;

import java.util.*;

/**
 * Created by julien on 01/02/16.
 */
public class BFS {

    private Tile src;
    private Tile dst;
    private Bot bot;

    public BFS(Tile s, Tile d, Bot b) {
        src = s;
        dst = d;
        bot = b;
    }

    public class Tuple<X, Y> {
        public final X x;
        public final Y y;
        public Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }
    }

    public Aim goTo(boolean avoid) {
        Ants ants = bot.getAnts();

        Set<Tile> enemies = ants.getEnemyAnts();

        if (!ants.getIlk(dst).isPassable()){
            System.err.println("ATTENTION");
            System.err.println(dst);
        }

        Set<Tile> visitedTiles = new HashSet<Tile>();
        Queue<Tuple<Tile,Aim>> toDo = new LinkedList<Tuple<Tile, Aim>>();

        visitedTiles.add(src);

        for (Aim a : Aim.values()){
            Tile next = ants.getTile(src, a);

            boolean isEnemyFree = true;
            for (Tile e : enemies){
                isEnemyFree &= ants.getDistance(next, e) > ants.getAttackRadius2() + 10;
            }
            if (avoid && !isEnemyFree) {
                continue;
            }

            if (next.equals(dst)) {
                //System.err.println("FOUND");
                return a;
            }
            if (ants.getIlk(next).isPassable() && !visitedTiles.contains(next)){
                toDo.add(new Tuple(next, a));
                visitedTiles.add(next);
            }
        }

        Tuple<Tile, Aim> current = toDo.poll();

        while (current != null && !current.x.equals(dst)){
            //System.out.println(current.x);
            //System.out.print("dest : ");
            //System.out.println(dst);
            for (Aim a : Aim.values()){
                Tile next = ants.getTile(current.x, a);
                if (next.equals(dst)) {
                    //System.err.println("FOUND");
                    return current.y;
                }
                if (ants.getIlk(next).isPassable() && !visitedTiles.contains(next)){
                    toDo.add(new Tuple(next, current.y));
                    visitedTiles.add(next);
                }
            }
            current = toDo.poll();
        }

        if (current == null) return null;
        else return current.y;
    }

}
