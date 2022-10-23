package sk.tuke.kpi.kp.linepuzzle.core;


import java.util.Optional;

public class Board {
    private final int height;
    private final int width;
    private Graph graph;
    private Node[][] nodes;

    public Board(int height, int width, Graph graph) {
        if (graph == null)
            throw new NullPointerException("Illegal setting Board");
        if ((height == 0 || width == 0) || (height > 5 || width > 5))
            throw new ArrayIndexOutOfBoundsException("Illegal setting Board");
        this.height = height;
        this.width = width;
        this.graph = graph;
        this.nodes = new Node[width][height];
        generate();
    }

    private void generate() {
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                Optional<Node> nodeFromGraph = graph.getNodeByPosition(row, col);
                if (nodeFromGraph.isPresent()) {
                    nodes[row][col] = nodeFromGraph.get();
                    nodeFromGraph.get().setNodeState(NodeState.ACTIV);
                } else {
                    nodes[row][col] = new Node(row, col);
                }
            }
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public void replaceGraph(Graph graph) {
        this.graph=graph;
    }

    public Node getNode(int row, int col) {
        return nodes[row][col];
    }

    public Node[][] getNodes() {
        return nodes;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
