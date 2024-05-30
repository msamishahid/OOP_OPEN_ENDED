package com.labtasks.ooplab14;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.*;
import java.sql.Connection;

public class controller {

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private Button selectImageButton;

    @FXML
    private TilePane tilePane;

    private Connection connection;
    private File selectedImageFile;

    @FXML
    private void initialize() {
        addButton.setOnAction(e -> addProduct());
        deleteButton.setOnAction(e -> deleteProduct());
        selectImageButton.setOnAction(e -> selectImage());

        // Initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/openlab", "root", "12345678");

            // Fetch existing items from the database
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM openlab.items");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");

                // Create and display the product
                Image image = new Image(new File(resultSet.getString("path")).toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);

                Label nameLabel = new Label(name);
                Label priceLabel = new Label(String.valueOf(price));

                VBox productInfo = new VBox();
                productInfo.getChildren().addAll(imageView, nameLabel, priceLabel);
                productInfo.setSpacing(10); // Add spacing between items

                TilePane productTile = new TilePane();
                productTile.setPrefColumns(1);
                productTile.getChildren().add(productInfo);
                productTile.setPadding(new Insets(10)); // Add padding around each product
                tilePane.getChildren().add(productTile);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        selectedImageFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
    }

    @FXML
    private void addProduct() {
        try {
            if (selectedImageFile != null && !nameField.getText().isEmpty() && !priceField.getText().isEmpty()) {
                // Insert new product into the database
                PreparedStatement statement = connection.prepareStatement("INSERT INTO items (name, price, path) VALUES (?, ?, ?)");
                statement.setString(1, nameField.getText());
                statement.setDouble(2, Double.parseDouble(priceField.getText()));
                statement.setString(3, selectedImageFile.getAbsolutePath());
                statement.executeUpdate();

                // Display the added product
                Image image = new Image(selectedImageFile.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);

                Label nameLabel = new Label(nameField.getText());
                Label priceLabel = new Label(priceField.getText());

                VBox productInfo = new VBox();
                productInfo.getChildren().addAll(imageView, nameLabel, priceLabel);
                productInfo.setSpacing(10); // Add spacing between items

                TilePane productTile = new TilePane();
                productTile.setPrefColumns(1);
                productTile.getChildren().add(productInfo);
                productTile.setPadding(new Insets(20)); // Add padding around each product
                tilePane.getChildren().add(productTile);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void deleteProduct() {
        try {
            // Delete selected product from the database
            int productId = Integer.parseInt(idField.getText());
            PreparedStatement statement = connection.prepareStatement("DELETE FROM items WHERE id = ?");
            statement.setInt(1, productId);
            int rowsAffected = statement.executeUpdate();

            // Remove the product from the UI
            if (rowsAffected > 0) {
                for (int i = 0; i < tilePane.getChildren().size(); i++) {
                    TilePane productTile = (TilePane) tilePane.getChildren().get(i);
                    VBox productInfo = (VBox) productTile.getChildren().get(0);
                    Label idLabel = (Label) productInfo.getChildren().get(1); // Adjusted index here
                    int id = Integer.parseInt(idLabel.getText()); // Use getText() method of Label class
                    if (id == productId) {
                        tilePane.getChildren().remove(i);
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
