package sk.tuke.kpi.kp.linepuzzle.core;

public class Node {
    private final int positionX;
    private final int positionY;
    private NodeState state = NodeState.INACTIV;

    public Node(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public NodeState getState() {
        return state;
    }

    public void setNodeState(NodeState state) {
        this.state = state;
    }

}
