package projetuni;
import javafx.beans.property.SimpleStringProperty;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private Stage primaryStage;
    private TableView<Room> roomTable;// Table pour afficher les informations des salles

    public static void main(String[] args) {
        launch(args);// Lance l'application JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;// Initialise la fenêtre principale
        primaryStage.setTitle("Gestion des Salles");

        // Connection a DatabaseManager
        DatabaseManager.createTables();  

        // Création des composants de l'interface utilisateur
        Button addRoomButton = new Button("Ajouter une salle");
        addRoomButton.setOnAction(e -> showAddRoomPage());

        Button deleteRoomButton = new Button("Supprimer une salle");
        deleteRoomButton.setOnAction(e -> showDeleteRoomPage());

        Button editRoomButton = new Button("Modifier une salle");
        editRoomButton.setOnAction(e -> showEditRoomPage());

        Button reserveRoomButton = new Button("Réserver une salle");
        reserveRoomButton.setOnAction(e -> showReservationPage());

        //Button freeSlotsButton = new Button("Créneaux Libres");
        //freeSlotsButton.setOnAction(e -> showFreeSlotsPage());

        //Button reservationsButton = new Button("Liste des Réservations");
        //reservationsButton.setOnAction(e -> showReservationsPage());


        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(addRoomButton, deleteRoomButton, editRoomButton);

        // Création et affichage de la scène principale.
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

 // Méthode pour afficher l'interface d'ajout de salle.
    private void showAddRoomPage() {
        Stage addRoomStage = new Stage();
        addRoomStage.setTitle("Ajouter une salle");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        
        TextField idField = new TextField();
        idField.setPromptText("Id");
        GridPane.setConstraints(idField, 0, 2);

        TextField buildingField = new TextField();
        buildingField.setPromptText("Bâtiment");
        GridPane.setConstraints(buildingField, 0, 0);

        TextField floorField = new TextField();
        floorField.setPromptText("Étage");
        GridPane.setConstraints(floorField, 0, 1);

        TextField numberField = new TextField();
        numberField.setPromptText("Numéro");
        GridPane.setConstraints(numberField, 0, 2);

        Button addButton = new Button("Ajouter");
        GridPane.setConstraints(addButton, 1, 2);
        addButton.setOnAction(e -> {
        	Room room = new Room( 0, buildingField.getText(), floorField.getText(), numberField.getText());
        	DatabaseManager.insertRoom(room);
            addRoomStage.close();
        });

        grid.getChildren().addAll(buildingField, floorField, numberField, addButton);

        Scene scene = new Scene(grid, 300, 150);
        addRoomStage.setScene(scene);
        addRoomStage.showAndWait();
    }

    
    // Méthode pour afficher l'interface de suppression de salle.
    private void showDeleteRoomPage() {
        Stage deleteRoomStage = new Stage();
        deleteRoomStage.setTitle("Supprimer une salle");

        // Create room table
        roomTable = new TableView<>();
        TableColumn<Room, String> buildingCol = new TableColumn<>("Bâtiment");
        buildingCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBuilding()));

        TableColumn<Room, String> floorCol = new TableColumn<>("Étage");
        floorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFloor()));

        TableColumn<Room, String> numberCol = new TableColumn<>("Numéro");
        numberCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumber()));

        TableColumn<Room, Void> deleteCol = new TableColumn<>("Supprimer");
        deleteCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Room room = getTableView().getItems().get(getIndex());
                    deleteRoom(room);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        roomTable.getColumns().addAll(buildingCol, floorCol, numberCol, deleteCol);

        refreshRoomTable();

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().add(roomTable);

        Scene scene = new Scene(layout, 400, 300);
        deleteRoomStage.setScene(scene);
        deleteRoomStage.show();
    }


    private void deleteRoom(Room room) {
        DatabaseManager.deleteRoom(room);
        refreshRoomTable();
    }

    //Affiche l'interface de modification d'une salle
    private void showEditRoomPage() {
        Stage editRoomStage = new Stage();
        editRoomStage.setTitle("Modifier une salle");

        // Create room table with edit buttons
        roomTable = new TableView<>();
        TableColumn<Room, String> buildingCol = new TableColumn<>("Bâtiment");
        buildingCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBuilding()));

        TableColumn<Room, String> floorCol = new TableColumn<>("Étage");
        floorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFloor()));

        TableColumn<Room, String> numberCol = new TableColumn<>("Numéro");
        numberCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumber()));

        TableColumn<Room, Void> editCol = new TableColumn<>("Modifier");
        editCol.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");

            {
                editButton.setOnAction(event -> {
                    Room room = getTableView().getItems().get(getIndex());
                    showEditRoomDialog(room);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });

        roomTable.getColumns().addAll(buildingCol, floorCol, numberCol, editCol);

        refreshRoomTable();

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().add(roomTable);

        Scene scene = new Scene(layout, 400, 300);
        editRoomStage.setScene(scene);
        editRoomStage.show();
    }


    //Affiche l'interface de réservation de salle
    private void showEditRoomDialog(Room room) {
        Stage editRoomDialog = new Stage();
        editRoomDialog.setTitle("Modifier la salle");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        TextField buildingField = new TextField(room.getBuilding());
        buildingField.setPromptText("Bâtiment");
        GridPane.setConstraints(buildingField, 0, 0);

        TextField floorField = new TextField(room.getFloor());
        floorField.setPromptText("Étage");
        GridPane.setConstraints(floorField, 0, 1);

        TextField numberField = new TextField(room.getNumber());
        numberField.setPromptText("Numéro");
        GridPane.setConstraints(numberField, 0, 2);

        Button saveButton = new Button("Enregistrer");
        GridPane.setConstraints(saveButton, 1, 2);
        saveButton.setOnAction(e -> {
            Room updatedRoom = new Room(0,buildingField.getText(), floorField.getText(), numberField.getText());
            editRoom(room, updatedRoom);
            editRoomDialog.close();
        });

        grid.getChildren().addAll(buildingField, floorField, numberField, saveButton);

        Scene scene = new Scene(grid, 300, 150);
        editRoomDialog.setScene(scene);
        editRoomDialog.showAndWait();
    }

    private void editRoom(Room oldRoom, Room updatedRoom) {
        DatabaseManager.updateRoom(oldRoom, updatedRoom);
        refreshRoomTable();
    }

    private void refreshRoomTable() {
        roomTable.getItems().clear();
        roomTable.getItems().addAll(DatabaseManager.getAllRooms());
    }
    private void showReservationPage() {
        Stage reservationStage = new Stage();
        reservationStage.setTitle("Réserver une salle");

        // Créer la liste déroulante (ComboBox) pour la sélection des salles
        ComboBox<Room> roomComboBox = new ComboBox<>();
        roomComboBox.getItems().addAll(DatabaseManager.getAllRooms());
        roomComboBox.setPromptText("Sélectionner une salle");

        //input pour reservation (date, time, promo, responsible)
        DatePicker datePicker = new DatePicker();
        TextField timeField = new TextField();
        TextField promoField = new TextField();
        TextField responsibleField = new TextField();

        Button reserveButton = new Button("Réserver");
        reserveButton.setOnAction(e -> {
            Room selectedRoom = roomComboBox.getValue();
            String date = datePicker.getValue().toString();
            String time = timeField.getText();
            String promo = promoField.getText();
            String responsible = responsibleField.getText();

            if (selectedRoom != null && !date.isEmpty() && !time.isEmpty() && !promo.isEmpty() && !responsible.isEmpty()) {
                Reservation reservation = new Reservation(selectedRoom, date, time, promo, responsible);
                DatabaseManager.insertReservation(reservation);
                reservationStage.close();
            } else {
                //validation ou si non affiche erreur
            }
        });

        // Configuration du layout pour la page de réservation
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        GridPane.setConstraints(roomComboBox, 0, 0);
        GridPane.setConstraints(datePicker, 0, 1);
        GridPane.setConstraints(timeField, 0, 2);
        GridPane.setConstraints(promoField, 0, 3);
        GridPane.setConstraints(responsibleField, 0, 4);
        GridPane.setConstraints(reserveButton, 1, 4);

        grid.getChildren().addAll(roomComboBox, datePicker, timeField, promoField, responsibleField, reserveButton);

        Scene scene = new Scene(grid, 300, 200);
        reservationStage.setScene(scene);
        reservationStage.showAndWait();
    }

    private void showFreeSlotsPage(Room room) {
        Stage freeSlotsStage = new Stage();
        freeSlotsStage.setTitle("Créneaux Libres - " + room.getNumber());

        // Créer un sélecteur de date (DatePicker) pour la sélection de la date
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Sélectionner une date");

        // Créer un bouton pour récupérer et afficher les créneaux libres
        Button showFreeSlotsButton = new Button("Afficher les Créneaux Libres");
        showFreeSlotsButton.setOnAction(e -> {
            String selectedDate = datePicker.getValue().toString();
            if (!selectedDate.isEmpty()) {
                List<Reservation> freeSlots = DatabaseManager.getFreeSlots(room, selectedDate);
                showFreeSlotsDialog(freeSlots);
            } else {
                // validation ou si non affiche erreur
            }
        });

        // Configuration du layout pour la page des créneaux libres
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(datePicker, showFreeSlotsButton);

        Scene scene = new Scene(layout, 300, 150);
        freeSlotsStage.setScene(scene);
        freeSlotsStage.show();
    }

    private void showFreeSlotsDialog(List<Reservation> freeSlots) {
        Stage freeSlotsDialog = new Stage();
        freeSlotsDialog.setTitle("Créneaux Libres");

        // Créer une ListView pour afficher les créneaux libres
        ListView<String> freeSlotsListView = new ListView<>();
        for (Reservation freeSlot : freeSlots) {
            freeSlotsListView.getItems().add(freeSlot.getTime());
        }

        // Configuration du layout pour la boîte de dialogue des créneaux libres
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().add(freeSlotsListView);

        Scene scene = new Scene(layout, 200, 200);
        freeSlotsDialog.setScene(scene);
        freeSlotsDialog.showAndWait();
    }


}