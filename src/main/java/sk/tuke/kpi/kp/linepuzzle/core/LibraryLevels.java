package sk.tuke.kpi.kp.linepuzzle.core;

import java.util.LinkedList;

public class LibraryLevels extends LinkedList<Level> {

    public LibraryLevels() {
        super();
        this.add(getL1());
        this.add(getL2());
        this.add(getL3());
    }

    private Level getL1() {
        final Graph gameGraph = new Graph();
        gameGraph.addNode(new Node(2, 1));
        gameGraph.addNode(new Node(1, 1));
        gameGraph.addNode(new Node(1, 3));
        gameGraph.addNode(new Node(2, 3));

        final Graph playerGraph = new Graph();
        playerGraph.addNode(new Node(2, 1));
        playerGraph.addNode(new Node(1, 1));
        playerGraph.addNode(new Node(1, 3));
        return new Level(5, 5,1, "First", gameGraph, playerGraph);
    }

    private Level getL2() {
        final Graph gameGraph = new Graph();
        gameGraph.addNode(new Node(2, 0));
        gameGraph.addNode(new Node(1, 1));
        gameGraph.addNode(new Node(1, 3));
        gameGraph.addNode(new Node(2, 4));

        final Graph playerGraph = new Graph();
        playerGraph.addNode(new Node(2, 0));
        playerGraph.addNode(new Node(1, 1));
        playerGraph.addNode(new Node(1, 3));
        return new Level(5, 5,0.5, "Second", gameGraph, playerGraph);
    }

    private Level getL3() {
        final Graph gameGraph = new Graph();
        gameGraph.addNode(new Node(2, 0));
        gameGraph.addNode(new Node(1, 1));
        gameGraph.addNode(new Node(2, 2));
        gameGraph.addNode(new Node(2, 4));
        gameGraph.addNode(new Node(1, 3));

        final Graph playerGraph = new Graph();
        playerGraph.addNode(new Node(2, 0));
        playerGraph.addNode(new Node(2, 2));
        playerGraph.addNode(new Node(2, 4));
        return new Level(5, 5,2, "Third", gameGraph, playerGraph);
    }

}
