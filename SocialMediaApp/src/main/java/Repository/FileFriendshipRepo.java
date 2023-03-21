package Repository;

import Domain.Friendship;
import Domain.User;
import Domain.Validators.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileFriendshipRepo extends FileRepository<Set<Integer>,Friendship> {
    public FileFriendshipRepo(String Filename, Validator<Friendship> validator) {
        super(Filename, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        int id1 = Integer.parseInt(attributes.get(0));
        String name1 = attributes.get(1);
        int id2 = Integer.parseInt(attributes.get(2));
        String name2 = attributes.get(3);
        String date = attributes.get(4);

        User user1 = new User(id1,name1,"");
        User user2 = new User(id2,name2,"");
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date,formatter);
        Friendship fr = new Friendship(user1,user2,dateTime,"");
        return fr;
    }

    @Override
    protected String createEntityAsString(Friendship fr) {
        User user1 = fr.getUser1();
        User user2 = fr.getUser2();
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return user1.getID()+";"+user1.getName()+";"+ user2.getID()+";"+user2.getName()+";"+fr.getFriendsfrom().format(formatter);
    }
}