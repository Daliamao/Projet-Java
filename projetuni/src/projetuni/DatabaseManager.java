package projetuni;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
	//connexion a notre base de données

	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/projet";
    private static final String USERNAME = "root"; 
    private static final String PASSWORD = ""; // Laissez vide pour une connexion de confiance

    public static void createTables() {
        createRoomTable();// Crée la table des salles.
        createReservationTable();// Crée la table des réservations.
    }
    public static void createRoomTable() {
    	//creation du tableau room
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS salles (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "batiment NVARCHAR(255)," +
                    "etage NVARCHAR(255)," +
                    "numero NVARCHAR(255)" +
                    ")";
            statement.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertRoom(Room room) {
    	//Insère une nouvelle salle dans la base de données.
    	createRoomTable();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO salles (batiment, etage, numero) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, room.getBuilding());
            preparedStatement.setString(2, room.getFloor());
            preparedStatement.setString(3, room.getNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRoom(Room room) {
    	//Supprime une salle
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM salles WHERE id = ?")) {
            preparedStatement.setInt(1, room.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Room> getAllRooms() {
    	//Récupère toutes les salles de la base de données
        List<Room> rooms = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM salles")) {
            while (resultSet.next()) {
                Room room = new Room(
                        resultSet.getInt("id"),
                        resultSet.getString("batiment"),
                        resultSet.getString("etage"),
                        resultSet.getString("numero")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public static void updateRoom(Room oldRoom, Room updatedRoom) {
    	//Met à jour les informations d'une salle
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE salles SET batiment = ?, etage = ?, numero = ? WHERE id = ?")) {
            preparedStatement.setString(1, updatedRoom.getBuilding());
            preparedStatement.setString(2, updatedRoom.getFloor());
            preparedStatement.setString(3, updatedRoom.getNumber());
            preparedStatement.setInt(4, oldRoom.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createReservationTable() {
    	//Crée la table des réservations
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS reservations (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "room_id INT," +
                    "date NVARCHAR(255)," +
                    "time NVARCHAR(255)," +
                    "promo NVARCHAR(255)," +
                    "FOREIGN KEY (room_id) REFERENCES salles(id)" +
                    ")";
            statement.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void insertReservation(Reservation reservation) {
    	//Insère une nouvelle réservation
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO reservations (room_id, date, time, promo, responsible) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, reservation.getRoom().getId());
            preparedStatement.setString(2, reservation.getDate());
            preparedStatement.setString(3, reservation.getTime());
            preparedStatement.setString(4, reservation.getPromo());
            preparedStatement.setString(5, reservation.getResponsible());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteReservation(Reservation reservation) {
    	//Supprime une réservation 
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM reservations WHERE id = ?")) {
            preparedStatement.setInt(1, reservation.getRoom().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateReservation(Reservation oldReservation, Reservation updatedReservation) {
    	//Met à jour les informations d'une réservation
    	try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE reservations SET room_id = ?, date = ?, time = ?, promo = ?, responsible = ? WHERE id = ?")) {
            preparedStatement.setInt(1, updatedReservation.getRoom().getId());
            preparedStatement.setString(2, updatedReservation.getDate());
            preparedStatement.setString(3, updatedReservation.getTime());
            preparedStatement.setString(4, updatedReservation.getPromo());
            preparedStatement.setString(5, updatedReservation.getResponsible());
            preparedStatement.setInt(6, oldReservation.getRoom().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Reservation> getAllReservations() {
    	//Affiche une liste des réservations 
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM reservations")) {
            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        getRoomById(resultSet.getInt("room_id")),
                        resultSet.getString("date"),
                        resultSet.getString("time"),
                        resultSet.getString("promo"),
                        resultSet.getString("responsible")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    private static Room getRoomById(int roomId) {
    	//Récupère une salle spécifique par son ID
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM salles WHERE id = ?")) {
            preparedStatement.setInt(1, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Room(
                        resultSet.getInt("id"),
                        resultSet.getString("batiment"),
                        resultSet.getString("etage"),
                        resultSet.getString("numero")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //Détermine les créneaux libres pour une salle donnée
    public static List<Reservation> getFreeSlots(Room room, String selectedDate) {
        List<Reservation> allReservations = getAllReservations();
        List<Reservation> occupiedSlots = getReservationsForDate(room, selectedDate, allReservations);

        // En supposant qu'on a des créneaux horaires prédéfinis
        List<String> allTimeSlots = getAllTimeSlots();
        List<String> occupiedTimeSlots = getOccupiedTimeSlots(occupiedSlots);

        //créneaux horaires libres
        List<String> freeTimeSlots = new ArrayList<>(allTimeSlots);
        freeTimeSlots.removeAll(occupiedTimeSlots);

        // Créer des réservations pour les créneaux disponibles
        List<Reservation> freeSlotReservations = new ArrayList<>();
        for (String freeTimeSlot : freeTimeSlots) {
            freeSlotReservations.add(new Reservation(room, selectedDate, freeTimeSlot, "", ""));
        }

        return freeSlotReservations;
    }

    private static List<Reservation> getReservationsForDate(Room room, String selectedDate, List<Reservation> allReservations) {
        //Récupère les réservations pour une salle et une date spécifiques
    	List<Reservation> reservationsForDate = new ArrayList<>();
        for (Reservation reservation : allReservations) {
            if (reservation.getRoom().equals(room) && reservation.getDate().equals(selectedDate)) {
                reservationsForDate.add(reservation);
            }
        }
        return reservationsForDate;
    }

    private static List<String> getAllTimeSlots() {
        //elle renverra une liste de trois créneaux horaires prédéfinis
        List<String> timeSlots = new ArrayList<>();
        timeSlots.add("08:00-09:00");
        timeSlots.add("09:00-10:00");
        timeSlots.add("10:00-11:00");
        return timeSlots;
    }

    private static List<String> getOccupiedTimeSlots(List<Reservation> reservations) {
    	//pour obtenir une liste des périodes pendant lesquelles les salles sont déjà réservées
        List<String> occupiedTimeSlots = new ArrayList<>();
        for (Reservation reservation : reservations) {
            occupiedTimeSlots.add(reservation.getTime());
        }
        return occupiedTimeSlots;
    }
}