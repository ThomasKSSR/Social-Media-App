package Repository;

import Domain.User;
import Domain.Validators.ValidationException;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class UserRepo<ID extends Integer,E extends  User> implements Repository<ID,E>{

    private Map<Integer,E> users;
    private Validator<E> validUser;

    public UserRepo( Validator<E> validUser) {
        this.users = new HashMap<Integer,E>();
        this.validUser = validUser;
    }

    /**
     * Adds a user to the repo
     * @param e - User
     * @return User user to be added, null if the user already exists in the repo
     * @throws ValidationException if the data of the user is not valid
     */
    @Override
    public E add(E e) throws ValidationException {
        if(e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if(users.containsKey(e.getID())){
            return null;
        }
        validUser.validate(e);

        users.put(e.getID(),e);

        return e;
    }

    /**
     * removes the user from the repo
     * @param id - ID id of the user to be removed
     * @return User user removed, null if the user doesn't exist in the repo
     */
    @Override
    public E remove(ID id) {
        if((int)id<0){
            throw new IllegalArgumentException("ID can't be negative");
        }

        if(users.containsKey(id)){
            E user = users.remove(id);
            return user;
        }
        else{
            return null;
        }



    }

    /**
     * Finds a user in the repo
     * @param id - ID,id of the user to be found
     * @return User user to be found, null if the user doesn't exist
     */
    @Override
    public E find(ID id) {
        /*if(id<0){
            throw new IllegalArgumentException("ID can't be negative");
        }*/

        if(users.containsKey(id)){
            return users.get(id);
        }
        else{
            return null;
        }



    }

    @Override
    public E update(E Entity) throws ValidationException {
        return null;
    }

    /**
     * gets all the users
     * @return List<E>  users in the repo, null if there are no users
     */
    @Override
    public List<E> getAll() {
        if(!users.isEmpty())
            return users.values().stream().collect(Collectors.toList());

        return null;


    }
}
