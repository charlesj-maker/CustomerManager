package com.example.customermanager;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private final TextField nameField = new TextField();
    private final ComboBox<String> provinceBox = new ComboBox<>();
    private final Label status = new Label();
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final TableView<Customer> table = new TableView<>();

    @Override
    public void start(Stage stage) {
        nameField.setPromptText("e.g., Mary Banda");
        Label nameLabel = new Label("Customer name");
        nameLabel.setLabelFor(nameField);

        provinceBox.getItems().addAll("Central", "Lusaka", "Copperbelt");
        provinceBox.setPromptText("Choose a province");

        Button saveButton = new Button("Save customer");
        saveButton.setDefaultButton(true);
        saveButton.setOnAction(event -> saveCustomer());

        Button deleteButton = new Button("Delete selected");
        deleteButton.setOnAction(event -> deleteSelected());

        HBox buttons = new HBox(10, saveButton, deleteButton);

        TableColumn<Customer, String> nameCol = new TableColumn<>("Customer name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));

        table.getColumns().add(nameCol);
        table.getColumns().add(provinceCol);
        table.setItems(customers);
        table.setPrefHeight(200);

        VBox formLayout = new VBox(10,
                nameLabel, nameField,
                provinceBox,
                buttons,
                status,
                new Label("Saved customers:"),
                table
        );
        formLayout.setPadding(new Insets(20));
        formLayout.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(formLayout, 500, 500);
        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.show();

        nameField.requestFocus();
    }

    private void saveCustomer() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            status.setText("Enter the customer name.");
            nameField.requestFocus();
            return;
        }

        String province = provinceBox.getValue();
        if (province == null) {
            status.setText("Choose a province.");
            provinceBox.requestFocus();
            return;
        }

        customers.add(new Customer(name, province));
        status.setText("Customer saved.");
        nameField.clear();
        provinceBox.setValue(null);
        nameField.requestFocus();
    }

    private void deleteSelected() {
        Customer selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            status.setText("Select a customer to delete.");
            return;
        }

        ButtonType delete = new ButtonType("Delete");
        Alert ask = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete the selected customer?",
                delete, ButtonType.CANCEL);
        ask.setHeaderText("Confirm deletion");

        if (ask.showAndWait().orElse(ButtonType.CANCEL) == delete) {
            customers.remove(selected);
            status.setText("Customer deleted.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}