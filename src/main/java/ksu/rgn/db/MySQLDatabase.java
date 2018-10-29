package ksu.rgn.db;

import ksu.rgn.scenario.Node;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.Truck;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public class MySQLDatabase extends Thread implements DBQueries {

    private String url, dbName, user, password;
    private EntityManagerFactory emf = null;

    @Override
    public void open(String url, String dbName, String user, String password) {
        this.url = url;
        this.dbName = dbName;
        this.user = user;
        this.password = password;

        start();
    }

    private Runnable onOpenSuccess = null, onOpenFail = null;
    public void onOpenSuccess(Runnable onOpenSuccess) {
        this.onOpenSuccess = onOpenSuccess;
    }
    public void onOpenFail(Runnable onOpenFail) {
        this.onOpenFail= onOpenFail;
    }

    @Override
    public void run() {
        try {
            final HashMap<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.user", user);
            properties.put("javax.persistence.jdbc.password", password);
            properties.put("javax.persistence.jdbc.url", "jdbc:mysql://" + url + "/" + dbName);
            properties.put("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
            emf = Persistence.createEntityManagerFactory("RGN", properties);
            emf.createEntityManager();

            System.out.println(emf.isOpen());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            if (onOpenSuccess != null) onOpenSuccess.run();

        } catch (Exception e) {
            if (onOpenFail != null) onOpenFail.run();
        }
    }

    @Override
    public void close() {
        emf.close();
    }

    @Override
    public ArrayList<Scenario> getAllScenarios() {
        return new ArrayList<>();
    }

    @Override
    public void persistScenario(Scenario s) {

    }

    @Override
    public void persistNode(Node n) {

    }

    @Override
    public void persistTruck(Truck t) {

    }
}
