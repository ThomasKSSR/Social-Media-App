package Repository;

import Domain.Msg;
import Domain.Validators.ValidationException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbMsgRepo implements Repository<Integer, Msg>{

    String url;

    public DbMsgRepo(String url, String username, String password) {
        this.url = url;
        this.password = password;
        this.username = username;
    }

    String password;
    String username;


    @Override
    public Msg add(Msg msg) throws ValidationException {
        String sql = "INSERT INTO messages(sentby,sentto,contents) VALUES(?,?,?)";
        try(
                Connection connection = DriverManager.getConnection(url,username,password);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setString(1,msg.getSentBy());
            preparedStatement.setString(2,msg.getSentTo());
            preparedStatement.setString(3,msg.getContent());
            preparedStatement.executeUpdate();
        }catch(SQLException se){
            se.printStackTrace();
            return null;
        }
        return msg;
    }

    @Override
    public Msg remove(Integer integer) {
        return null;
    }

    @Override
    public Msg find(Integer integer) {
        return null;
    }

    @Override
    public Msg update(Msg Entity) throws ValidationException {
        return null;
    }

    @Override
    public List<Msg> getAll() {
        String sql = """
                SELECT * FROM messages
                """;
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement ps = connection.prepareStatement(sql);
        ){
            List<Msg> messages = new ArrayList<>();
            ResultSet rez = ps.executeQuery();
            while(rez.next()){
                Msg msg = new Msg(rez.getString("sentby"),
                        rez.getString("sentto"),
                        rez.getString("contents"));
                msg.setId(rez.getInt("id"));
                messages.add(msg);
            }
            return messages;

        }catch(SQLException se){
            se.printStackTrace();
            return null;
        }
    }
}
