package Controller;

import Business.Network;
import Domain.FriendReqDto;
import Domain.FriendshipDto;
import Domain.User;
import Utilis.Observer;
import com.example.lab08mapgui.GuiMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Observable;


public class UserController implements Observer {
    private Network network;
    private User user;

    //private ObservableList<User> usersModel = FXCollections.observableArrayList();

    private ObservableList<FriendshipDto> friendsModel= FXCollections.observableArrayList();

    private ObservableList<FriendReqDto> friendReqModel = FXCollections.observableArrayList();






    @FXML
    TableColumn<FriendshipDto,String> friendsFromColumn;

    @FXML
    TableView<FriendReqDto>  friendReqTableView;

    @FXML
    TableColumn<FriendReqDto,String> friendsReqColumn;

    @FXML
    TableView<FriendshipDto> friendsTableView;

    @FXML
    TableColumn<FriendshipDto,String> friendsColumn;

    @FXML
    TableColumn<FriendReqDto,String> receivedOnColumn;


    @FXML
    public void initialize(){
        /*userColumn.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        userTableView.setItems(usersModel);*/

        friendsColumn.setCellValueFactory(new PropertyValueFactory<FriendshipDto,String>("friendName"));
        friendsFromColumn.setCellValueFactory(new PropertyValueFactory<FriendshipDto, String>("friendsfrom"));
        friendsTableView.setItems(friendsModel);

        friendsReqColumn.setCellValueFactory(new PropertyValueFactory<FriendReqDto,String>("friendName"));
        receivedOnColumn.setCellValueFactory(new PropertyValueFactory<FriendReqDto,String>("reqSentDate"));
        friendReqTableView.setItems(friendReqModel);


    }

    public void setNetwork(Network network) {
        this.network = network;
        this.network.addObserver(this);
        initTables();
    }

    public void setUser(User user) {
        this.user = user;
    }



    /*public void addFriendHandler(){
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
                alert.setContentText("Friendship already exists");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText("FRIEND REQUEST SENT!");
                alert.setContentText(selectedUser.getName() + " received your friend request");
                alert.showAndWait();
            }

        }
    }*/

    /**
     * deletes a friend of an user
     */
    public void deleteFriendHandler(){
        FriendshipDto friendshipDto = friendsTableView.getSelectionModel().getSelectedItem();
        if(friendshipDto == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Oops!");
            alert.setContentText("You need to select a friend from the list to delete");
            alert.showAndWait();
        }
        else{
            User selectedUser= network.findUserByName(friendshipDto.getFriendName());
            network.remove_Friendship(user.getID(),selectedUser.getID());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("FRIEND DELETED!");
            alert.setContentText(selectedUser.getName()+" deleted from friendlist");
            alert.showAndWait();
        }
    }

    /**
     * accespt a friend request for a user
     */
    public void acceptFriendHandler(){
        FriendReqDto friendReq = friendReqTableView.getSelectionModel().getSelectedItem();
        String username = friendReq.getFriendName();
        User selected_user = network.findUserByName(username);
        network.acceptFriendReq(selected_user,user);

    }

    /**
     * logs out the user
     * @throws IOException
     */

    public void logOutHandler()throws IOException {
        Stage userStage = (Stage)friendsTableView.getScene().getWindow();
        userStage.close();

        Stage mainStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(GuiMain.class.getResource("views/Menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),400,300);
        mainStage.setTitle("Login Menu");
        mainStage.setScene(scene);

        MenuController menuController = fxmlLoader.getController();
        menuController.setNetwork(network);
        mainStage.show();
    }


    @Override
    public void update() {
        initTables();
    }
    public void initTables(){

        //usersModel.setAll(network.getAllSuggestions(user));
        friendsModel.setAll(network.getFriends(user));
        friendReqModel.setAll(network.getFriendReq(user));
    }

    public void addFriendMenuHandler()throws IOException{
        Stage userStage = (Stage)friendsTableView.getScene().getWindow();
        userStage.close();

        Stage addFriendMenu = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(GuiMain.class.getResource("views/AddFriendMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),700,500);
        addFriendMenu.setTitle("Add friend Menu");
        addFriendMenu.setScene(scene);
        AddFriendController  addFriendController= fxmlLoader.getController();
        addFriendController.setUser(user);
        addFriendController.setNetwork(network);

        addFriendMenu.show();

    }

    public void chatHandler() throws IOException{
        FriendshipDto friend = friendsTableView.getSelectionModel().getSelectedItem();
        if(friend ==null ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Oops!");
            alert.setContentText("You need to select a friend");
            alert.showAndWait();
        }
        else{
            User user_friend = network.findUserByName(friend.getFriendName());
            Stage chat = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(GuiMain.class.getResource("views/Chat.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),478,396);
            chat.setScene(scene);
            ChatController chatController = fxmlLoader.getController();
            chatController.setValues(network,user,user_friend);

            chat.show();
        }
    }


}
