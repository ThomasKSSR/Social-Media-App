package Controller;

import Business.Network;
import Business.ServiceException;
import Domain.User;
import com.example.lab08mapgui.GuiMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    private Network network;

    private User user;
    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldPassword;


    public void setNetwork(Network network) {
        this.network = network;
    }

    public void loginHandler()throws IOException{
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        user = network.loginSystem(username,password);
        if(user == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Oops!");
            alert.setContentText("Incorrect match between username and password");
            alert.showAndWait();

        }
        else {
            Stage mainStage = (Stage) textFieldUsername.getScene().getWindow();
            mainStage.close();

            Stage userStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GuiMain.class.getResource("views/UserMenu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            userStage.setTitle("UserProfile");
            userStage.setScene(scene);

            UserController userController = fxmlLoader.getController();
            userController.setUser(user);
            userController.setNetwork(network);
            userStage.show();
        }
    }


    public void signUpHandler()throws IOException{
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        try{
            network.signUpSystem(username,password);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("USER CREATED!");
            alert.setContentText("Welcome to the community "+username);
            alert.showAndWait();

        }catch (ServiceException se){
            if(se.getMessage().equals("Username taken")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Oops!");
                alert.setContentText("Username already taken, sorry :(");
                alert.showAndWait();
            }
            if(se.getMessage().equals("User data given is not valid")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Oops!");
                alert.setContentText("Data given can't form a valid user, sorry :(");
                alert.showAndWait();
            }
        }
    }

}
