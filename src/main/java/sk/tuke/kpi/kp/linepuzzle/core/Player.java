package sk.tuke.kpi.kp.linepuzzle.core;


import java.util.*;
import java.util.stream.Collectors;

public class Player {
    private final String name;
    private final Board board;
    private final LinkedList<Graph> historySteps = new LinkedList<>();

    public Player(String name, Board board) {
        if (board == null)
            throw new NullPointerException("Illegal setting Player");
        this.name = name;
        this.board = board;
        createBackupGraph();
    }

    public void back() {
        if (historySteps.isEmpty()) {
            createBackupGraph();
            return;
        }
        final Graph lastGraph = historySteps.pollLast();
        if (lastGraph.isEquals(board.getGraph())) {
            back();
            return;
        }

        board.replaceGraph(lastGraph);
        Arrays.stream(board.getNodes())
                .flatMap(Arrays::stream)
                .collect(Collectors.toList()).forEach((node) -> {
            final Optional<Node> nodeInGraphOptional = board.getGraph().getNodeByPosition(node.getPositionX(), node.getPositionY());
            if (!nodeInGraphOptional.isPresent()) {
                node.setNodeState(NodeState.INACTIV);
            }
        });
        createBackupGraph();
    }

    public Board getBoard() {
        return board;
    }

    public void splitNodesByPositions(int graphX, int graphY, int x, int y) {

        Optional<Node> graphNode = board.getGraph().getNodeByPosition(graphY, graphX);//graphY,graphX
        Node boardNode = board.getNode(y, x);

        if (!graphNode.isPresent()) {
            //vodit exception zadal zly uzol v graphe
            throw new NoSuchElementException("Graph Node is null");
        }
        LinkedList<Node> graphNodes = new LinkedList<>(board.getGraph().getNodes());
        LinkedList<Node> tmpNodes = new LinkedList<>();
        boolean wasFound = false;
        boolean wasAdded = false;
        int idx = 0;
        for (Node n : graphNodes) {
            if (wasFound) {
                tmpNodes.add(n);
                if (!wasAdded) {
                    board.getGraph().getNodes().set(idx, boardNode);
                    boardNode.setNodeState(NodeState.ACTIV);
                    wasAdded = true;
                } else {
                    board.getGraph().getNodes().set(idx, tmpNodes.poll());
                }
            }
            if (n == graphNode.get()) {
                wasFound = true;
            }
            idx++;
        }
        if (!wasAdded && wasFound) {
            board.getGraph().addNode(boardNode);
            boardNode.setNodeState(NodeState.ACTIV);
        } else {
            board.getGraph().addNode(tmpNodes.poll());
        }
        createBackupGraph();
    }

    private void createBackupGraph() {
        final Graph backupGraph = new Graph();
        board.getGraph().getNodes().forEach(node -> {
            final Node backupNode = new Node(node.getPositionX(), node.getPositionY());
            backupNode.setNodeState(NodeState.ACTIV);
            backupGraph.addNode(backupNode);
        });
        historySteps.add(backupGraph);
    }

    public String getName() {
        return name;
    }
}
