package com.CIS642.Rural.GroceryImplementation;

import com.CIS642.Rural.GroceryImplementation.db.DBQueries;
import com.CIS642.Rural.GroceryImplementation.gui.Window;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GroceryImplementationApplication {

    public static void main(String[] args) {
        Application.launch(Window.class);
    }

    public static DBQueries db;
}
