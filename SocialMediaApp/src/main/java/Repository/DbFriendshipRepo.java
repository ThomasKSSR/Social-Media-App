package Repository;

import Domain.Friendship;
import Domain.User;
import Domain.Validators.ValidationException;
import Domain.Validators.Validator;

import java.io.PipedReader;
import java.net.ConnectException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DbFriendshipRepo implements Repository<Set<Integer>, Friendship> {
    private String url;
    private String username;
    private String password;

    public DbFriendshipRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * adds a friendship to the data base
     * @param friendship -Friendship
     * @return friendship-Friendship, null if friendship already exists
     * @throws ValidationException if friendship is not valid
     */
    @Override
    public Friendship add(Friendship friendship) throws ValidationException {
        String command = "INSERT INTO friendships(id_user1,id_user2,friendsfrom,status) VALUES(?,?,?,?)";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement ps = connection.prepareStatement(command);

        ){
            ps.setInt(1,friendship.getUser1().getID());
            ps.setInt(2,friendship.getUser2().getID());
            DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            ps.setString(3,friendship.getFriendsfrom().format(formatter));
            ps.setString(4,friendship.getStatus());
            ps.executeUpdate();
        }catch(SQLException se){
            se.printStackTrace();
            return null;
        }
        return friendship;
    }

    /**
     * removes a friendship from the database
     * @param integers -Set<Integer>
     * @return fr-friendship if it was deleted, null if it doesn;t exist
     */
    @Override
    public Friendship remove(Set<Integer> integers) {
        Friendship fr = find(integers);
        if(fr !=null) {
            String command = "DELETE FROM friendships WHERE id_user1=? AND id_user2=? OR id_user1 =? AND id_user2=?";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(command);
            ) {
                Integer[] ids = new Integer[integers.size()];
                integers.toArray(ids);

                ps.setInt(1, ids[0]);
                ps.setInt(2, ids[1]);
                ps.setInt(3,ids[1]);
                ps.setInt(4,ids[0]);
                ps.executeUpdate();


            } catch (SQLException se) {
                se.printStackTrace();
                return null;
            }
        }
        return fr;

    }

    /**
     * finds a friendship in the database
     * @param integers -Set<Integer>
     * @return fr - if friendship found,null otherwise
     */
    @Override
    public Friendship find(Set<Integer> integers) {
        String command = """
        SELECT
        U1.id AS id_user1,
        U1.name AS name_user1,
        U1.password AS password_user1,
        U2.id AS id_user2,
        U2.name AS name_user2,
        U2.password AS password_user2,
        F.friendsfrom AS friends_from,
        F.status AS status
        
        FROM friendships F
        INNER JOIN users U1 ON F.id_user1 = U1.id
        INNER JOIN users U2 ON F.id_user2 = U2.id
        WHERE U1.id = ? AND U2.id = ? OR U1.id =? AND U2.id =? 
        """;

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement ps = connection.prepareStatement(command)
        ){
            Integer[] ids = new Integer[integers.size()];
            integers.toArray(ids);
            ps.setInt(1,ids[0]);
            ps.setInt(2,ids[1]);
            ps.setInt(3,ids[1]);
            ps.setInt(4,ids[0]);
            ResultSet rez = ps.executeQuery();

            if(!rez.next()){

                return null;
            }

            String sdate = rez.getString("friends_from");
            DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(sdate,formatter);
            Integer id_user1 = rez.getInt("id_user1");
            String name_user1 = rez.getString("name_user1");
            String password_user1 = rez.getString("password_user1");
            Integer id_user2 = rez.getInt("id_user2");
            String name_user2 = rez.getString("name_user2");
            String password_user2 = rez.getString("password_user2");
            String status = rez.getString("status");
            User user1 = new User(id_user1,name_user1,password_user1);
            User user2 = new User(id_user2,name_user2,password_user2);
            Friendship fr = new Friendship(user1,user2,dateTime,status);
            return fr;


        }catch(SQLException se){
            se.printStackTrace();
            return null;
        }
    }

    /**
     * updates a friendship in the database
     * @param friendship -Friendship
     * @return fr- if update worked,null otherwise
     * @throws ValidationException - throws if friendship not valid
     */
    @Override
    public Friendship update(Friendship friendship) throws ValidationException {
        String command = """
                UPDATE friendships SET status=?,friendsfrom=? WHERE id_user1=? AND id_user2=?
                """;
        try(Connection connection= DriverManager.getConnection(url,username,password);
            PreparedStatement ps = connection.prepareStatement(command);
        ){
            DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            ps.setString(1,friendship.getStatus());
            ps.setString(2,friendship.getFriendsfrom().format(formatter));
            ps.setInt(3,friendship.getUser1().getID());
            ps.setInt(4,friendship.getUser2().getID());
            ps.executeUpdate();

        }catch(SQLException se){
            se.printStackTrace();
        }
        return friendship;
    }

    /**
     * return all the friendships from the database
     * @return Friendships-List<Friendship>
     */
    @Override
    public List<Friendship> getAll() {
        List<Friendship> friends = new ArrayList<>();
        String command = """
        SELECT
        U1.id AS id_user1,
        U1.name AS name_user1,
        U1.password AS password_user1,
        U2.id AS id_user2,
        U2.name AS name_user2,
        U2.password AS password_user2,
        F.friendsfrom AS friends_from,
        F.status AS status
        FROM friendships F
        INNER JOIN users U1 ON F.id_user1 = U1.id
        INNER JOIN users U2 ON F.id_user2 = U2.id
                """;
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement ps = connection.prepareStatement(command);
        ){
            ResultSet rez = ps.executeQuery();
            while(rez.next()){
                String sdate = rez.getString("friends_from");
                DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                LocalDateTime dateTime = LocalDateTime.parse(sdate,formatter);
                Integer id_user1 = rez.getInt("id_user1");
                String name_user1 = rez.getString("name_user1");
                String password_user1 = rez.getString("password_user1");
                Integer id_user2 = rez.getInt("id_user2");
                String name_user2 = rez.getString("name_user2");
                String password_user2 = rez.getString("password_user2");
                String status = rez.getString("status");

                User user1 = new User(id_user1,name_user1,password_user1);
                User user2 = new User(id_user2,name_user2,password_user2);
                Friendship fr = new Friendship(user1,user2,dateTime,status);
                friends.add(fr);
            }
            return friends;
        }catch(SQLException se){
            se.printStackTrace();
            return null;
        }

    }
}
