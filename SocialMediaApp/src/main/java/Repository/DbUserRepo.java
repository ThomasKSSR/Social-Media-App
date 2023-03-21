package Repository;


import Domain.Validators.ValidationException;
import Domain.Validators.Validator;
import Domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbUserRepo implements Repository<Integer, User>{
    private String username;
    private String password;
    private String url;
    private Validator<User> validator;

    public DbUserRepo(String url,String username,String password, Validator<User> validator) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.validator = validator;
    }

    /**
     * Adds a user to database
     * @param user-User
     * @return user-User if doesn;t already exist, null otherwise
     * @throws ValidationException if user is not valid
     */
    @Override
    public User add(User user) throws ValidationException {
        validator.validate(user);
        String command = "INSERT INTO users(id,name,password) VALUES(?,?,?)";
        try(Connection connection = DriverManager.
                getConnection(url,username,password);
            PreparedStatement ps = connection.prepareStatement(command);
        ){
            ps.setInt(1,user.getID());
            ps.setString(2,user.getName());
            ps.setString(3,user.getPassword());
            ps.executeUpdate();
            return user;
        }catch(SQLException se){
            //System.out.println("O crapat ceva");
            return null;
        }


    }

    /**
     * Removes an user from database and returns it if it existed
     * @param integer -integer
     * @return -user-User if it exists,null otherwise
     */
    @Override
    public User remove(Integer integer) {
        User user = find(integer);
        if(user !=null) {
            String command = "DELETE FROM users WHERE id=?";

            try (Connection connection = DriverManager
                    .getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(command)
            ) {
                ps.setInt(1, integer);
                ps.executeUpdate();
            } catch (SQLException se) {
                se.printStackTrace();
                return null;
            }

        }
        return user;
    }

    /**
     * finds a user if it exists in database by id
     * @param integer -integer
     * @return user-User, null if it doesn;t exist
     */
    @Override
    public User find(Integer integer) {
        String command = "SELECT * FROM users WHERE id=?";
        try(Connection connection = DriverManager.
                getConnection(url,username,password);
            PreparedStatement ps = connection.prepareStatement(command)
        ){
            ps.setInt(1,integer);
            ResultSet rez = ps.executeQuery();
            rez.next();
            User user = new User(rez.getInt("id"),rez.getString("name"),
                    rez.getString("password"));
            return user;
        }catch(SQLException se){
            se.printStackTrace();
            return null;
        }

    }

    /**
     * updates a user in database
     * @param Entity -User
     * @return user-user
     * @throws ValidationException when user is not valid
     */
    @Override
    public User update(User Entity) throws ValidationException {
        String command ="UPDATE users SET name=? WHERE users.id=?";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement ps = connection.prepareStatement(command)
        ){
            ps.setString(1,Entity.getName());
            ps.setInt(2,Entity.getID());
            ps.executeUpdate();
            return Entity;
        }catch(SQLException se){
            se.printStackTrace();
            return null;
        }
    }

    /**
     * returns a list with all the users
     * @return users - List<User></User>, null if there are no users
     */
    @Override
    public List<User> getAll() {
        String command = "SELECT * FROM users";
        List<User> users =new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement ps = connection.prepareStatement(command);

        ){
            ResultSet rez = ps.executeQuery();
            while(rez.next()){
                User user = new User(rez.getInt("id"),rez.getString("name"),
                        rez.getString("password"));
                users.add(user);
            }
            return users;

        }catch(SQLException se){
            se.printStackTrace();
            return null;
        }


    }
}
