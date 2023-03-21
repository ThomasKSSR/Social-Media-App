package Tests;

import Business.Network;
import Domain.Friendship;
import Domain.User;
import Domain.Validators.ValidationException;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorUser;
import Repository.Repository;
import Repository.UserRepo;
import Repository.FriendshipRepo;

import java.time.LocalDateTime;
import java.util.*;

public class Test1 {


//    public static void run(){
//        System.out.println("Starting tests...");
//        User u1 = new User(1,"Marcel");
//        System.out.println(u1);
//
//        Validator validUser = new ValidatorUser();
//        Repository RepoUser = new UserRepo(validUser);
//
//        User u2 = new User(-11,"");
//        User u3 = new User(3,"Dominitian");
//
//        try {
//            RepoUser.add(u2);
//        }catch(ValidationException ve){
//            System.out.println(ve);
//        }
//
//        try {
//            RepoUser.add(u1);
//        }catch(ValidationException ve){
//            assert false;
//        }
//        try {
//            RepoUser.add(u3);
//        }catch(ValidationException ve){
//            assert false;
//        }
//
//        Iterable<User> users = RepoUser.getAll();
//        for(User user:users){
//            System.out.println(user);
//        }
//        System.out.println("Found:" + RepoUser.find(1));
//
//        System.out.println("Removed: " + RepoUser.remove(1));
//
//        Iterable<User> users2 = RepoUser.getAll();
//        for(User user:users2){
//            System.out.println(user);
//        }
//        System.out.println(u2.getFriends());
//
//
//
//        System.out.println("Finished tests...");
//    }
//
//    public static void Network_tests(){
//        System.out.println("Starting network tests...");
//
//        Validator<User> validator = new ValidatorUser<User>();
//        UserRepo<Integer,User> userRepo = new UserRepo<Integer,User>(validator);
//        FriendshipRepo<Set<Integer>,Friendship> friendshipRepo = new FriendshipRepo<Set<Integer>,Friendship>();
//        Network network = new Network(userRepo,friendshipRepo);
//
//        User u1 = new User(1,"Marcel");
//        User u2 = new User(-11,"");
//        User u3 = new User(3,"Dominitian");
//        User u4 = new User(4,"Ovidiu");
//        User u5 = new User(5,"Odobasian");
//
//
//        try {
//            network.add_User(1, "Marcel");
//            network.add_User(3,"Dominitian");
//            network.add_User(4,"Ovidiu") ;
//            network.add_User(5,"Odobasian");
//
//        }catch(ValidationException ve){
//            System.out.println("N-am ajuns aici");
//        }
//        List<User> users = network.getAllUsers();
//        System.out.println(users.size() + "users");
//
//        //network.remove_user(5);
//
//        users = network.getAllUsers();
//        System.out.println(users.size() + "users");
//
//        try{
//            network.add_User(5,"Odobasian");
//        }catch(ValidationException ve){
//
//        }
//
//        network.add_Friendship(1,3);
//
//        System.out.println(network.add_Friendship(4,5));
//        User us = userRepo.find(4);
//        System.out.println(us.getFriends());
//
//        System.out.println("Groups:"+network.ConexGroups());
//        List<Friendship> friends = network.get_all_friends();
//        System.out.println(friends.size() + "friendships");
//
//        network.remove_Friendship(1,3);
//
//        friends = network.get_all_friends();
//        System.out.println(friends.size() + "friendships");
//
//        System.out.println(LocalDateTime.now());
//        System.out.println("Finished network tests...");
//    }


}
