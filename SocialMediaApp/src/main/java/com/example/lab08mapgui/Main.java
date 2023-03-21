package com.example.lab08mapgui;
import Business.Network;
import Domain.Friendship;
import Domain.User;
import Domain.Validators.ValidationException;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorFriendship;
import Domain.Validators.ValidatorUser;
import Repository.Repository;
import Repository.UserRepo;
import Repository.FriendshipRepo;
import Tests.Test1;
import UI.Ui;
import Repository.FileUserRepo;
import Repository.FileFriendshipRepo;
import Repository.DbUserRepo;
import Repository.DbFriendshipRepo;
import java.util.HashSet;
import java.util.List;
import Repository.DbMsgRepo;


public class Main {
    public static void main(String[] args) {

        //Test1.Network_tests();

        ValidatorUser<User> validUser = new ValidatorUser<User>();
        ValidatorFriendship<Friendship> validFriendship = new ValidatorFriendship<Friendship>();

        DbUserRepo dbUserRepo = new DbUserRepo("jdbc:postgresql://localhost:5432/LabMap",
                "postgres","admin",validUser);

        DbFriendshipRepo dbFriendshipRepo = new DbFriendshipRepo("jdbc:postgresql://localhost:5432/LabMap",
                "postgres","admin");

        DbMsgRepo dbMsgRepo = new DbMsgRepo("jdbc:postgresql://localhost:5432/LabMap","postgres","admin");
        Network network = new Network(dbUserRepo,dbFriendshipRepo,dbMsgRepo);
        Ui ui = new Ui(network);
        ui.run();




        //Test1.run();
        /*Test1.Network_tests();
        ValidatorUser<User> validator = new ValidatorUser<User>();
        UserRepo<User,Integer> userRepo = new UserRepo<User, Integer>(validator);
        FriendshipRepo<Friendship, HashSet<Integer>> friendshipUser = new FriendshipRepo<Friendship, HashSet<Integer>>();
        Network network = new Network(userRepo,friendshipUser);
        Ui ui = new Ui(network);
        ui.run();*/

        /*Test1.Network_tests();

        ValidatorUser<User> validUser = new ValidatorUser<User>();
        ValidatorFriendship<Friendship> validFriendship = new ValidatorFriendship<Friendship>();

        FileUserRepo fileUserRepo = new FileUserRepo("src/Users",validUser);
        FileFriendshipRepo fileFriendshipRepo = new FileFriendshipRepo("src/Friendships",validFriendship);
        Network network = new Network(fileUserRepo,fileFriendshipRepo);
        Ui ui = new Ui(network);
        ui.run();*/




    }


}