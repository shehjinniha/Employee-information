package employes;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLDocumentController {

    @FXML private TextField idField, nameField, deptField, emailField, gpaField;
    @FXML private TableView<officer> tableView;
    @FXML private TableColumn<officer, Integer> idCol;
    @FXML private TableColumn<officer, String> nameCol, deptCol, emailCol;
    @FXML private TableColumn<officer, Double> gpaCol;

    private final ObservableList<officer> studentList = FXCollections.observableArrayList();
    private Connection connection;

    public void initialize() {
        // Bind table columns to officer properties
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        deptCol.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        gpaCol.setCellValueFactory(cellData -> cellData.getValue().gpaProperty().asObject());

        // Connect to SQLite database
        connectDatabase();
        loadStudents();

        tableView.setItems(studentList);
    }

    private void connectDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:student_system.db");
            Statement statement = connection.createStatement();

            // Create the Students table if it doesn't exist
            statement.execute("CREATE TABLE IF NOT EXISTS Students (" +
                    "id INTEGER PRIMARY KEY, " +
                    "name TEXT NOT NULL, " +
                    "department TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "gpa REAL NOT NULL)");

            // Create the Users table if it doesn't exist
            statement.execute("CREATE TABLE IF NOT EXISTS Users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadStudents() {
        studentList.clear();
        String query = "SELECT * FROM Students";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                studentList.add(new officer(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("department"),
                        resultSet.getString("email"),
                        resultSet.getDouble("gpa")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        String query = "INSERT INTO Students (id, name, department, email, gpa) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, Integer.parseInt(idField.getText()));
            preparedStatement.setString(2, nameField.getText());
            preparedStatement.setString(3, deptField.getText());
            preparedStatement.setString(4, emailField.getText());
            preparedStatement.setDouble(5, Double.parseDouble(gpaField.getText()));
            preparedStatement.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            showAlert("Error", "Failed to add student. Please check your inputs.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        loadStudents();
        clearFields();
    }

    @FXML
    private void handleUpdate() {
        officer selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a student to update.", Alert.AlertType.WARNING);
            return;
        }

        String query = "UPDATE Students SET name = ?, department = ?, email = ?, gpa = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nameField.getText());
            preparedStatement.setString(2, deptField.getText());
            preparedStatement.setString(3, emailField.getText());
            preparedStatement.setDouble(4, Double.parseDouble(gpaField.getText()));
            preparedStatement.setInt(5, Integer.parseInt(idField.getText()));
            preparedStatement.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            showAlert("Error", "Failed to update student. Please check your inputs.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        loadStudents();
        clearFields();
    }

    @FXML
    private void handleDelete() {
        officer selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a student to delete.", Alert.AlertType.WARNING);
            return;
        }

        String query = "DELETE FROM Students WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, selected.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            showAlert("Error", "Failed to delete student.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        loadStudents();
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        deptField.clear();
        emailField.clear();
        gpaField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void logout(ActionEvent event) {
          try {
        // Load the LoginView.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Stage stage = (Stage) tableView.getScene().getWindow(); // Get the current window
        stage.setScene(new Scene(loader.load())); // Set the scene to the login screen
    } catch (IOException e) {
        showAlert("Error", "Failed to logout. Unable to load login screen.", Alert.AlertType.ERROR);
        e.printStackTrace();
    }
     
    }
}
