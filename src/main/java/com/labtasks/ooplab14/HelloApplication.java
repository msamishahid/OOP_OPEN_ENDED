package com.labtasks.ooplab14;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HelloApplication extends Application {

    private static Connection databaseConnection;

    public static Connection getDatabaseConnection() {
        return databaseConnection;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            AnchorPane root = loader.load();

            // Set the desired size
            double width = 800;
            double height = 650;
            Scene scene = new Scene(root, width, height);
            primaryStage.setScene(scene);

            // Center the window on the screen
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((screenBounds.getWidth() - width) / 2);
            primaryStage.setY((screenBounds.getHeight() - height) / 2);

            primaryStage.setTitle("Electronic Items Management");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        try {
            databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/openlab", "root", "12345678");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            if (databaseConnection != null && !databaseConnection.isClosed()) {
                databaseConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
