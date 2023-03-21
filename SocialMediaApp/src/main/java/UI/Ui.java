package UI;

import Business.Network;
import Business.ServiceException;
import Domain.Friendship;
import Domain.User;
import Domain.Validators.ValidationException;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Ui {


    private Network network ;
    public Ui(Network network) {

        this.network = network;
    }
    private void startMenu(){
        //System.out.println("");
        System.out.println("Type Admin for admin menu ");
        System.out.println("Type User for user menu (COMING SOON!!!!)");
        System.out.println("Type Exit for exit");

    }
    private void adminMenu(){
        //System.out.println("");
        System.out.println("Type addusr for adding an user");
        System.out.println("Type delusr for deleting an user");
        System.out.println("Type addfr for adding a friendship");
        System.out.println("Type delfr for deleting a friendship");
        System.out.println("Type showusr for viewing all users");
        System.out.println("Type showfriends for viewing all friends");
        System.out.println("Type groups for viewing the number of friend groups");
    }
    private void groups(){
        int nr =network.ConexGroups();
        if(nr == 1){
            System.out.println("There is one group of friends");
        } else if (nr == 0) {
            System.out.println("There are no friend groups");
        } else {
            System.out.println("There are " + nr + " different friend groups");
        }
    }

    /**
     * adds a user to the network
     * @param scanner - scanner
     */
    private void addUser(Scanner scanner){
        System.out.println("Give an ID: ");
        Integer id = scanner.nextInt();
        System.out.println("Give an Username: ");
        String name = scanner.next();
        System.out.println("Give a password: ");
        String password = scanner.next();
        try {
            if(network.add_User(id, name,password) == false){
                System.out.println("The user already exists!  ");
                return;
            }
        }catch(ValidationException ve){
            System.out.println(ve.toString());
            return;
        }
        System.out.println("User added succesfully! ");

    }

    /**
     * deletes a user from the network
     * @param scanner - Scanner
     */
    private void delUser(Scanner scanner){
        System.out.println("Give an ID: ");
        Integer id = scanner.nextInt();
        try {
            if (network.remove_user(id) == false) {
                System.out.println("The user doesn't exist! ");
                return;
            }
        }catch(ServiceException se){
            System.out.println("The user still has friends,linked to it,try deleting the friendships first");
            return;
        }
        System.out.println("User deleted succesfully! ");

    }

    /**
     * adds a friendship to the network
     * @param scanner - Scanner
     */
    private void addFriendship(Scanner scanner){
        System.out.println("Give the ID of the first User: ");
        Integer id1 = scanner.nextInt();
        System.out.println("Give the ID of the second User: : ");
        Integer id2 = scanner.nextInt();
        if(network.add_Friendship(id1,id2) == false){
            System.out.println("The users do not exist");
            return;
        }
        System.out.println("Friendship added succesfully! ");
    }

    /**
     * deletes a friendship from the network
     * @param scanner - Scanner
     */
    private void delFrienship(Scanner scanner){
        System.out.println("Give the ID of the first User: ");
        Integer id1 = scanner.nextInt();
        System.out.println("Give the ID of the second User: : ");
        Integer id2 = scanner.nextInt();
        if(network.remove_Friendship(id1,id2) == false){
            System.out.println("The users or the friendship do not exist");
            return;
        }
        System.out.println("Friendship deleted succesfully! ");

    }

    /**
     * shows all the users in the network
     */
    private void showUsers(){

        List<User> users = network.getAllUsers();
        if(users == null){
            System.out.println("There are no users!");
            return;
        }
        for(User user :users){
            System.out.println(user);
        }
    }
    private void getNumberGroups(){
        System.out.println("Friend groups: " + network.ConexGroups());
    }

    private void showFriends(){
        List<Friendship> friends= network.get_all_friends();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for(Friendship fr:friends){
            System.out.println(fr.getUser1()+" and "+fr.getUser2()+" friends from "+fr.getFriendsfrom().format(formatter) +" status:"+fr.getStatus());
        }
    }

    /**
     * runs admin commands
     * @param scanner - Scanner
     */
    private void runAdmin(Scanner scanner){
       adminMenu();
        while (true) {
            //adminMenu();
            String command = scanner.nextLine();
            switch(command){
                case "addusr":
                    addUser(scanner);
                    break;
                case "delusr":
                    delUser(scanner);
                    break;
                case "addfr":
                    addFriendship(scanner);
                    break;
                case "delfr":
                    delFrienship(scanner);
                    break;
                case "showusr":
                    showUsers();
                    break;
                case "showfriends":
                    showFriends();
                    break;
                case "groups":
                    groups();

            }
        }
    }

    private void runUser(Scanner scanner){
        System.out.println("Sorry option not available right now");
    }

    /**
     * Console runner
     */
    public void run(){
        Scanner scanner = new Scanner(System.in);
        startMenu();
        while (true) {
            String command = scanner.nextLine();
            switch (command) {
                case "Admin":
                    runAdmin(scanner);
                    break;
                case "User":
                    runUser(scanner);
                    break;
                case "Exit":
                    System.out.println("Bye");
                    return;
                default:
                    System.out.println("Invalid command, try again!");
                    break;
            }
        }
    }

}
