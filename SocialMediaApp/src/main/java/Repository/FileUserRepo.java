package Repository;

import Domain.User;
import Domain.Validators.Validator;

import java.util.List;

public class FileUserRepo extends FileRepository<Integer,User>{

    public FileUserRepo(String Filename, Validator<User> validator) {
        super(Filename, validator);
    }

    @Override
    public User extractEntity(List<String> attributes) {
        int id = Integer.parseInt(attributes.get(0));
        User user = new User(id,attributes.get(1),"");
        return user;
    }

    @Override
    protected String createEntityAsString(User user) {
        return user.getID()+";"+user.getName();
    }
}
