package be.umons.game;

public enum Action {
    LEFT("left"),
    RIGHT("right"),
    UP("up"),
    DOWN("down");

    private String label;

    Action(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
