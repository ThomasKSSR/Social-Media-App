package Repository;

import Domain.Friendship;
import Domain.User;
import Domain.Validators.ValidationException;
import Domain.Validators.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FriendshipRepo<ID,E extends Friendship> implements Repository<ID,E>{
    private List<E> friends;



    public FriendshipRepo() {
        this.friends = new ArrayList<E>();

    }

    /**
     * adds a friendship to the repo
     * @param e - friendship to be added
     * @return fr -friendship
     */
    @Override
    public E add(E e)  {
        if(e.getUser1() == null || e.getUser2() == null){
            throw new IllegalArgumentException("Entity cannot be null");
        }

        friends.add(e);
        return e;


    }

    /**
     * removes a friendship from the network
     * @param id -ID the id of the frienship to be removed
     * @return fr - frienship if it exists in the network, null- if the friendship doesn't exist
     */
    @Override
    public E remove(ID id) {
        E fr1 = null;
       for(E fr :friends){
           if(Objects.equals(fr.getID(),id)){
               fr1 = fr;

           }

       }
       if(fr1 != null) {
           friends.remove(fr1);
           return fr1;
       }

       return null;
    }

    /**
     * finds a frienship in the repo
     * @param id - ID, the id of the friendship to be found
     * @return fr-frienship - if it exists, null - if the frienship doesn't exist
     */
    @Override
    public E find(ID id) {
        for(E fr : friends){
            if(fr.getID() == id){
                return fr;
            }
        }
        return null;
    }

    @Override
    public E update(E Entity) throws ValidationException {
        return null;
    }

    /**
     * gets all the friendships
     * @return friends- List<E>
     */
    @Override
    public List<E> getAll() {
        return friends;
    }
}
