package Controller;

import Business.Network;
import Domain.FriendReqDto;
import Domain.User;
import Utilis.Observer;
import com.example.lab08mapgui.GuiMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class AddFriendController implements Observer {




    private Network network;
    private User user;

    public void setNetwork(Network network) {
        this.network = network;
        network.addObserver(this);
        initTables();
    }

    public void setUser(User user) {
        this.user = user;
    }

    private ObservableList<User> usersModel = FXCollections.observableArrayList();

    private ObservableList<FriendReqDto> friendReqModel = FXCollections.observableArrayList();

    @FXML
    private TextField textFieldSearchUser;

    @FXML
    TableView<FriendReqDto> friendReqTableView;

    @FXML
    TableColumn<FriendReqDto,String> friendReqUserColumn;

    @FXML
    TableColumn<FriendReqDto,String> friendReqDateColumn;

    @FXML
    TableView<User> userTableView;

    @FXML
    TableColumn<User,String> userColumn;

    @FXML
    public void initialize(){
        userColumn.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        userTableView.setItems(usersModel);

        friendReqUserColumn.setCellValueFactory( new PropertyValueFactory<FriendReqDto,String>("friendName"));
        friendReqDateColumn.setCellValueFactory(new PropertyValueFactory<FriendReqDto,String>("reqSentDate"));
        friendReqTableView.setItems(friendReqModel);
    }
    /**
     * adds a friend for a user
     */
    public void addFriendHandler(){
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();

        if(user == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Oops!");
            alert.setContentText("You need to select a user from the list");
            alert.showAndWait();
        }
        else{
            if(network.add_Friendship(user.getID(),selectedUser.getID())==false){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Oops!");
                alert.setContentText("This person already sent you a friend request");
                alert.showAndWait();
            }
            else{
                textFieldSearchUser.clear();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText("FRIEND REQUEST SENT!");
                alert.setContentText(selectedUser.getName() + " received your friend request");
                alert.showAndWait();
            }

        }
    }

    /**
     * searchers for a user that doesn't have a friendship relation with the user
     */
    public void searchAddFriendHandler(){
        String userName = textFieldSearchUser.getText();
        if(userName == ""){
            usersModel.setAll(network.getTop10Suggestions(user));
        }
        else{
            usersModel.setAll(network.getAllUsersMatchingName(user,userName));
        }
    }

    public void backToUserProfileHandler()throws IOException {
        Stage addFriendMenu = (Stage)userTableView.getScene().getWindow();
        addFriendMenu.close();

        Stage userProfile = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(GuiMain.class.getResource("views/UserMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),700,500);

        UserController userController = fxmlLoader.getController();
        userController.setUser(user);
        userController.setNetwork(network);
        userProfile.setTitle("User Profile");
        userProfile.setScene(scene);
        userProfile.show();


    }

    public void unsendHandler(){
        FriendReqDto friendReqDto = friendReqTableView.getSelectionModel().getSelectedItem();
        if(friendReqDto == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Oops!");
            alert.setContentText("You have to select a friend request in order to delete one");
            alert.showAndWait();
        }
        else{
            String friendReqName = friendReqDto.getFriendName();
            User userFriend = network.findUserByName(friendReqName);
            network.unsendFriendReq(user,userFriend);
        }
    }


    @Override
    public void update() {
        initTables();
    }

    public void initTables(){

        usersModel.setAll(network.getTop10Suggestions(user));
        friendReqModel.setAll(network.getFriendReqSent(user));
    }

}
