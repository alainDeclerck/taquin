package be.umons.graph;

import be.umons.game.Action;

public class Node implements Cloneable {

    private final Integer PATH_COST = 1;

    private String state;
    private Action action;
    private Node parent;
    private Integer traveledPath;

    public Node(String initialState) {
        this.state = initialState;
        this.traveledPath = 0;
    }

    public void updateGraph(Node parent, Action performedAction) {
        this.action = performedAction;
        this.parent = parent;
        this.traveledPath = parent.getTraveledPath()+PATH_COST;
    }

    public Integer getTraveledPath() {
        return traveledPath;
    }

    public boolean isRoot() {
        return traveledPath==0;
    }

    public Node getParent() {
        return parent;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(state);
        result.append(" -> ");
        result.append("player as moved to "+action.label());
        return result.toString();
    }

    public Node clone() throws CloneNotSupportedException {
        return (Node)super.clone();
    }

}
