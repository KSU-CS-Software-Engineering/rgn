package ksu.rgn.scenario;

/**
 *
 */
public final class Truck {

    public final Node startingNode, endingNode;
    public final int capacity; // in kg

    // If starting/ending node is null, it means it can start/end where ever it wants
    public Truck(Node startingNode, Node endingNode, int capacity) {
        this.startingNode = startingNode;
        this.endingNode = endingNode;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return String.format("Truck { capacity = %d }", capacity);
    }
}
