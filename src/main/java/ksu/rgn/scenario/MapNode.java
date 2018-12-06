package ksu.rgn.scenario;

import javax.persistence.*;

/**
 *
 */


@Entity
@Table(name="node")
public class MapNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int ID;

    @OneToOne(cascade = CascadeType.ALL)
    public MapLocation location;
    public String name;

    @Column(nullable = false)
    public int demand; // In kg
    @Column(nullable = false)
    public int supply; // In kg


    @Override
    public String toString() {
        return String.format("Node #%d { name = %s, demand = %d, location = %s }", ID, name, demand, location.toString());
    }
}

