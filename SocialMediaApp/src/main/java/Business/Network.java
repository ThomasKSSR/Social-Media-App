package Business;

import Domain.*;
import Domain.Validators.ValidationException;
import Repository.FriendshipRepo;
import Repository.Repository;
import Repository.UserRepo;
import Utilis.Observable;
import Utilis.Observer;

import java.time.LocalDateTime;
import java.util.*;

public class Network implements Observable {

    List<Observer> observers;
    private Repository<Integer,User> userRepo;
    private Repository<Set<Integer>,Friendship> friendshipRepo;

    private Repository<Integer,Msg> msgRepo;

    public Network(Repository<Integer,User> userRepo, Repository<Set<Integer>,Friendship> friendshipRepo,
    Repository<Integer,Msg> msgRepo
        ) {
        this.msgRepo=msgRepo;
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        observers = new ArrayList<>();
    }

    /**
     * adds a user to the network
     * @param ID - id of the user to be added in the network
     * @param Name - name of the user to be added in the network
     * @return true if adding was succesful, false if the user already exists in the network
     * @throws ValidationException if the data of the user is invalid
     */

    public boolean add_User(Integer ID,String Name,String password) throws ValidationException {
        User user = new User(ID,Name,password);
        if(userRepo.add(user) == null){
            return false;
        }
        return true;

    }

    /**
     * Removes a user from the network
     * @param ID -int id of the user to be deleted
     * @return true if deleteing si succesful, false if user doesn't exist in the network
     * @throws ServiceException if the user still has frienships related
     */
    public boolean remove_user(Integer ID) throws ServiceException{
        User user = userRepo.find(ID);
        if(user == null){
            return false;
        }
        boolean ok=true;
        List<Friendship> friends = friendshipRepo.getAll();
        for(Friendship friend:friends){
            if(friend.getID().contains(ID)){
                ok=false;
            }
        }

        if(ok == false ){
            throw new ServiceException("User still has friendships and cannot be deleted!");
        }
        userRepo.remove(ID);

        return true;
    }

    public List<User> getAllUsers(){
        return userRepo.getAll();
    }

    /**
     * Adds a friendship between 2 members
      * @param id_user1 - id of the first user
     * @param id_user2 - id fo the second user
     * @return returns true if users exist and adding is succesful, false if users dont exist and add fails
     * @throws IllegalArgumentException
     */
    public boolean add_Friendship(int id_user1,int id_user2) throws IllegalArgumentException{
        User user1 = userRepo.find(id_user1);
        User user2 = userRepo.find(id_user2);

        if(user1 != null && user2 != null){
            Friendship fr = new Friendship(user1,user2, LocalDateTime.now(),"sent");
            Set ids = new HashSet<Integer>();
            ids.add(id_user1);
            ids.add(id_user2);
            Friendship fr_found = friendshipRepo.find(ids);
            if(fr_found != null && fr_found.getStatus().equals("unsent")){
                try{
                    friendshipRepo.update(fr);
                }catch(ValidationException ve){
                    ve.printStackTrace();
                }

            }
            else {
                try {
                    friendshipRepo.add(fr);


                } catch (ValidationException ve) {
                    ve.printStackTrace();
                }
            }
            //user1.addFriend(user2);
            //user2.addFriend(user1);
            notifyAllObs();
            return true;
        }
        return false;

    }

    /**
     * Removes a friendship from the network
     * @param id_user1 - id of the first user
     * @param id_user2 - if of the seconf user
     * @return true if the users exist and friendship has been deleted, false if users don't exist and remove fails
     * @throws IllegalArgumentException if ids are invalid
     */
    public boolean remove_Friendship(int id_user1,int id_user2) throws IllegalArgumentException{
        User user1 = userRepo.find(id_user1);
        User user2 = userRepo.find(id_user2);
        if(user1 != null && user2 != null) {
            //Friendship fr = new Friendship(user1, user2,LocalDateTime.now());
            Set<Integer> ids = new HashSet<Integer>();
            ids.add(id_user1);
            ids.add(id_user2);
            if(friendshipRepo.remove(ids) == null){
                return false;
            }
            //user1.removeFriend(user2);
            //user2.removeFriend((user1));
            notifyAllObs();
            return true;
        }
        return false;
    }

    /**
     * gets all the friendships in the network
     * @return list of friendships
     */
    public List<Friendship> get_all_friends(){
        return friendshipRepo.getAll();
    }

    /**
     * initializez the matrix of the graph with the friendships
     * @param mat - graph matrix
     */
    private void initGraph(int[][] mat){
        List<User> users = userRepo.getAll();
        int n = users.size();
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                User user1 = users.get(i);
                User user2 = users.get(j);
                //HashSet<Integer> set = new HashSet<>(user1.getID(),user2.getID());
                Friendship fr = new Friendship(user1,user2,LocalDateTime.now(),"pending");
                if(friendshipRepo.getAll().contains(fr)){
                    mat[i][j] = 1;
                    mat[j][i] = 1;
                }
            }
        }
    }

    /**
     * DFS on a graph
     * @param mat - the graph matrix
     * @param visit - the visited vertices list
     * @param n  - number of vertices
     * @param i - visiting vertex
     */
    private void dfs(int[][] mat,boolean[] visit, int n, int i){
        visit[i] = true;
        for(int j=0;j<n;j++){
            if(mat[i][j] >0 && visit[j] == false ){
                dfs(mat,visit,n,j);
            }
        }
    }

    /**
     * finds the number of unrelated groups of friends in the network
     * @return int groups - the number of different groups in the network
     */
    public int ConexGroups(){
        if(friendshipRepo.getAll().size() ==0){
            return 0;
        }
        int n = userRepo.getAll().size();
        int[][] mat = new int[n][n];
        boolean[] visit = new boolean[n];
        initGraph(mat);
        int groups =0;

        for(int i=0;i<n;i++){
            if(visit[i] == false) {
                dfs(mat, visit, n, i);
                groups++;
            }
        }


        return groups;
    }

    /**
     * verifies if the login username and password match an existing user
     * @param username - String
     * @param password - String
     * @return user if it matches an user, null otherwise
     */
    public User loginSystem(String username,String password){
        List<User> users = userRepo.getAll();
        for(User user:users){
            if(user.getName().equals(username) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    /**
     * searches for a user by its username
     * @param name - String
     * @return user-User if it exists and null otherwise
     */
    public User findUserByName(String name){
        List<User> users = getAllUsers();
        for(User user:users){
            if(user.getName().equals(name)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObs() {
        for(Observer obs:observers){
            obs.update();
        }
    }

    /**
     * gets the friends of a user
     * @param user - User
     * @return friends -List<FriendshipDto>
     */
    public List<FriendshipDto> getFriends(User user){
        List<Friendship> friendships = friendshipRepo.getAll();
        List<FriendshipDto> friends = new ArrayList<FriendshipDto>();

        for(Friendship friendship :friendships){
            if(friendship.getStatus().equals("accepted")) {
                User user1 = friendship.getUser1();
                User user2 = friendship.getUser2();
                FriendshipDto friendshipDto;
                if (user1.equals(user)) {
                    friendshipDto = new FriendshipDto(user2.getName(), friendship.getFriendsfrom(), friendship.getStatus());
                    friends.add(friendshipDto);
                }
                if (user2.equals(user)) {
                    friendshipDto = new FriendshipDto(user1.getName(), friendship.getFriendsfrom(), friendship.getStatus());
                    friends.add(friendshipDto);
                }
            }
        }
        /*if(friends.size() ==0){
            return null;
        }*/
        return friends;
    }

    /**
     * gets the friend requests of an user
     * @param user -User
     * @return friendreqs -List<FriendReqDto>
     */
    public List<FriendReqDto> getFriendReq(User user){
        List<Friendship> friendships = friendshipRepo.getAll();
        List<FriendReqDto> friendreqs = new ArrayList<FriendReqDto>();

        for(Friendship friendship:friendships){
            if(friendship.getStatus().equals("sent") && friendship.getUser2().equals(user)){
                FriendReqDto friendReqDto = new FriendReqDto(friendship.getUser1().getName(),friendship.getFriendsfrom());
                friendreqs.add(friendReqDto);
            }
        }
        /*if(friendreqs.size()==0){
            return null;
        }*/
        return friendreqs;
    }

    public List<FriendReqDto> getFriendReqSent(User user){
        List<Friendship> friendships = friendshipRepo.getAll();
        List<FriendReqDto> friendreqs = new ArrayList<FriendReqDto>();

        for(Friendship friendship:friendships){
            if(friendship.getStatus().equals("sent")  && friendship.getUser1().equals(user)){
                FriendReqDto friendReqDto = new FriendReqDto(friendship.getUser2().getName(),friendship.getFriendsfrom());
                friendreqs.add(friendReqDto);
            }
        }

        return friendreqs;
    }

    public void unsendFriendReq(User user1,User user2){
        Friendship fr = new Friendship(user1,user2,LocalDateTime.now(),"unsent");
        try{
            friendshipRepo.update(fr);
            notifyAllObs();
        }
        catch(ValidationException ve){
            ve.printStackTrace();
        }

    }


    /**
     * accepts a friend request
     * @param user1 -User
     * @param user2 -User
     */
    public void acceptFriendReq(User user1,User user2){
        Friendship fr= new Friendship(user1,user2,LocalDateTime.now(),"accepted");
        try {
            friendshipRepo.update(fr);
        }catch(ValidationException ve){

        }
        notifyAllObs();
    }

    /**
     * finds the max id of all the users
     * @return id_max-Integer
     */

    public Integer findMaxID(){
        List<User> users = userRepo.getAll();
        Integer id_max;
        if(users.size() ==0){
            id_max=1;
        }
        id_max= users.get(0).getID();
        for(User user :users){
            if(user.getID()>id_max){
                id_max=user.getID();
            }
        }
        return id_max;
    }

    /**
     *  signs up an user
     * @param username -String
     * @param password -String
     * @throws ServiceException if the username is taken
     */
    public void signUpSystem(String username,String password)throws ServiceException{
        if(findUserByName(username) !=null){
            throw new ServiceException("Username taken");
        }

        Integer id_max = findMaxID();
        try{
        add_User(id_max+1,username,password);

        }catch(ValidationException ve){
            throw new ServiceException("User data given is not valid");
        }

    }

    /**
     * gets a the friends names of a user
     * @param currentuser -User
     * @return friendsNames - List<String>
     */
    public List<String> getFriendsNames(User currentuser){
        List<FriendshipDto> friends = getFriends(currentuser);
        List<String> friendsNames = new ArrayList<String>();
        for(FriendshipDto friend:friends){
            friendsNames.add(friend.getFriendName());
        }
        return friendsNames;
    }

    public List<String> getFriendsAndFriendReqNames(User currentuser){
        List<FriendshipDto> friends = getFriendsAndFriendReq(currentuser);
        List<String> friendsNames = new ArrayList<String>();
        for(FriendshipDto friend:friends){
            friendsNames.add(friend.getFriendName());
        }
        return friendsNames;
    }


    /**
     * gets all the users that are not friends of an user
     * @param currentUser-User
     * @return suggestionUsers - List<User>
     */
    public List<User> getAllSuggestions(User currentUser){
        List<User> users =userRepo.getAll();
        List<User> suggestionUsers = new ArrayList<User>();
        List<String> friendsnames = getFriendsAndFriendReqNames(currentUser);
        for(User user :users){
            if(!friendsnames.contains(user.getName() )){
                suggestionUsers.add(user);
            }
        }


        suggestionUsers.remove(currentUser);
        return suggestionUsers;

    }

    /**
     * gets all the users that are friends or have friend requests pending with a ceratin user
     * @param user -User
     * @return all the friendships of a users
     */
    public List<FriendshipDto> getFriendsAndFriendReq(User user){
        List<Friendship> friendships = friendshipRepo.getAll();
        List<FriendshipDto> friends = new ArrayList<FriendshipDto>();

        for(Friendship friendship :friendships){
            if(friendship.getStatus().equals("accepted") || friendship.getStatus().equals("sent")) {
                User user1 = friendship.getUser1();
                User user2 = friendship.getUser2();
                FriendshipDto friendshipDto;
                if (user1.equals(user)) {
                    friendshipDto = new FriendshipDto(user2.getName(), friendship.getFriendsfrom(), friendship.getStatus());
                    friends.add(friendshipDto);
                }
                if (user2.equals(user)) {
                    friendshipDto = new FriendshipDto(user1.getName(), friendship.getFriendsfrom(), friendship.getStatus());
                    friends.add(friendshipDto);
                }
            }
        }
        /*if(friends.size() ==0){
            return null;
        }*/
        return friends;
    }

    /**
     * gets all the users who match an username and are not friends with the current user and
     * @param currentUser -User
     * @param userName -String
     * @return List<User> matchingUsers
     */

    public List<User> getAllUsersMatchingName(User currentUser,String userName){
        List<User> suggestions = getAllSuggestions(currentUser);
        List<User> matchingUsers = new ArrayList<>();
        int nr=0;
        for(User user: suggestions){
            if(nr==10){
                break;
            }
            if(user.getName().contains(userName)){
                matchingUsers.add(user);
                nr++;
            }
        }
        return matchingUsers;
    }

    /**
     * gets the first 10 users that are not friends with a user
     * @param currentUser -User
     * @return List<User> top of first 10 users
     */
    public List<User> getTop10Suggestions(User currentUser){
        List<User> suggestions = getAllSuggestions(currentUser);
        List<User> top10Suggestion = new ArrayList<>();
        int nr=0;
        for(User user:suggestions){
            if(nr == 10){
                break;
            }
            top10Suggestion.add(user);
            nr++;
        }
        return top10Suggestion;
    }

    public List<Msg> getMsgOf2Users(User user1,User user2){
        List<Msg> allMessages = msgRepo.getAll();
        List<Msg> neededMessages = new ArrayList<>();
        for(Msg msg:allMessages){
            if(msg.getSentBy().equals(user1.getName()) &&
                    msg.getSentTo().equals(user2.getName())
            || msg.getSentBy().equals(user2.getName()) &&
                    msg.getSentTo().equals(user1.getName())
            ){
                neededMessages.add(msg);
            }
        }
        neededMessages.sort(Comparator.comparing(Msg::getId));
        return neededMessages;
    }

    public void add_msg(Msg msg){
        try{
            msgRepo.add(msg);
        }catch(ValidationException ve){
            ve.printStackTrace();
        }
        notifyAllObs();
    }


}
