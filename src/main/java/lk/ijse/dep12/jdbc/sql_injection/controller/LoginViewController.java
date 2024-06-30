package lk.ijse.dep12.jdbc.sql_injection.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.dep12.jdbc.sql_injection.db.SingletonConnection;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.sql.*;
import java.util.Stack;

public class LoginViewController {

    public Button btnExit;

    public Button btnLogin;

    public PasswordField txtPassword;

    public TextField txtUserName;

    public void btnExitOnAction(ActionEvent event) {
        Platform.exit(); // close the app
    }

    public void btnLoginOnAction(ActionEvent event) throws IOException {
        // Login Logics
        String userName = txtUserName.getText();
        String password = txtPassword.getText();

        //username validation
        if (userName.isBlank() || userName.strip().contains(" ") || userName.strip().length() < 3) {
            txtUserName.requestFocus();
            txtUserName.selectAll();
            return;
        }

        //password validation
        if (password.isBlank() || password.strip().length() < 4 || !password.matches(".*\\d.*")
            || !password.matches(".*[A-Za-z].*")) {
            txtPassword.requestFocus();
            txtPassword.selectAll();
            return;
        }
        try {
            Connection connection = SingletonConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    SELECT FROM "user" WHERE username=?
                    """);
            preparedStatement.setString(1, userName.strip());
            // preparedStatement.setString(2, password.strip());
            ResultSet rst = preparedStatement.executeQuery();
            if (rst.next()) {

                PreparedStatement prepareStatement2 = connection.prepareStatement("""
                        SELECT * FROM "user" WHERE username=?
                        """);
                prepareStatement2.setString(1, userName.strip());
                rst = prepareStatement2.executeQuery();
                rst.next();
                String hashPassword = rst.getString("password");


                if (hashPassword.equals(DigestUtils.sha256Hex(password))) {
                    String fullName = rst.getString("full_name");
                    System.setProperty("app.principal.fullName", fullName);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainView.fxml"))));
                    stage.setOnCloseRequest(Event::consume);
                    stage.setTitle("SQL Injection: Main");
                    stage.setResizable(false);
                    stage.show();
                    stage.centerOnScreen();
                    ((Stage) (btnLogin.getScene().getWindow())).close();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Invalid username or password, try again!!").show();
                    txtUserName.requestFocus();
                    txtUserName.selectAll();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid username or password, try again!!").show();
                txtUserName.requestFocus();
                txtUserName.selectAll();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong, please try again").show();
        }


    }

}
