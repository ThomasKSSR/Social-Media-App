package com.example.lab08mapgui;

import Business.Network;
import Controller.MenuController;
import Domain.Friendship;
import Domain.User;
import Domain.Validators.ValidatorFriendship;
import Domain.Validators.ValidatorUser;
import Repository.DbFriendshipRepo;
import Repository.DbMsgRepo;
import Repository.DbUserRepo;
import UI.Ui;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiMain extends Application {
private Network network;
    public static void main(String[] args){launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        ValidatorUser<User> validUser = new ValidatorUser<User>();
        ValidatorFriendship<Friendship> validFriendship = new ValidatorFriendship<Friendship>();

        DbUserRepo dbUserRepo = new DbUserRepo("jdbc:postgresql://localhost:5432/LabMap",
                "postgres","admin",validUser);

        DbFriendshipRepo dbFriendshipRepo = new DbFriendshipRepo("jdbc:postgresql://localhost:5432/LabMap",
                "postgres","admin");
        DbMsgRepo dbMsgRepo = new DbMsgRepo("jdbc:postgresql://localhost:5432/LabMap","postgres","admin");
        network = new Network(dbUserRepo,dbFriendshipRepo,dbMsgRepo);
        initview(primaryStage);
        primaryStage.show();
    }




    private void initview(Stage MenuStage)throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/Menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),400,300);
        MenuStage.setTitle("Menu");
        MenuStage.setScene(scene);

        MenuController menuController = fxmlLoader.getController();
        menuController.setNetwork(network);
    }
}
