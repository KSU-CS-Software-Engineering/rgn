package ksu.rgn.scenario;

import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name="truck")
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int ID;

    public String name;

    @ManyToOne(cascade = CascadeType.ALL)
    public MapNode startingNode, endingNode;

    @Column(nullable = false)
    public int capacity; // in std palettes

    @Column(nullable = false)
    public boolean refrigerated;


    @Override
    public String toString() {
        return String.format("Truck #%d { capacity = %d }", ID, capacity);
    }
}
