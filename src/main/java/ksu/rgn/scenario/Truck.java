package ksu.rgn.scenario;

/**
 *
 */
public final class Truck {

    private static int counter = 0; // TODO not thread safe

    public final Node startingNode, endingNode;
    public final int capacity; // in kg
    public final int id;

    // If starting/ending node is null, it means it can start/end where ever it wants
    public Truck(Node startingNode, Node endingNode, int capacity) {
        this.startingNode = startingNode;
        this.endingNode = endingNode;
        this.capacity = capacity;
        this.id = counter++;
    }

    @Override
    public String toString() {
        return String.format("Truck #%d { capacity = %d }", id, capacity);
    }
}
