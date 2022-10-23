package sk.tuke.kpi.kp.linepuzzle.core;

import java.util.LinkedList;
import java.util.Optional;

public class Graph {
    LinkedList<Node> nodes = new LinkedList<>();

    public void addNode(Node node) {
        nodes.add(node);
    }

    public LinkedList<Node> getNodes() {
        return nodes;
    }

    public Optional<Node> getNodeByPosition(int x, int y) {
        return nodes.stream().filter((node -> node.getPositionX() == x && node.getPositionY() == y)).findFirst();

    }

    public boolean isEquals(Graph graph){
       return graph.getNodes().size() == this.getNodes().size() &&
                graph.getNodes().stream().allMatch((compareNode) -> {
                            final int idx = graph.getNodes().indexOf(compareNode);
                            final Node node = this.getNodes().get(idx);
                            return node.getPositionY() == compareNode.getPositionY() && node.getPositionX() == compareNode.getPositionX();
                        }
                );
    }
}
