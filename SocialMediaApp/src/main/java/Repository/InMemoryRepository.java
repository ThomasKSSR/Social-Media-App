package Repository;

import Domain.HasID;
import Domain.Validators.ValidationException;
import Domain.Validators.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryRepository<ID,E extends HasID<ID>> implements Repository<ID,E> {

    private Map<ID,E> entities;
    private Validator<E> validator;

    public InMemoryRepository(Validator<E> validator) {
        entities = new HashMap<ID,E>();
        this.validator = validator;
    }

    @Override
    public E add(E e) throws ValidationException {
        if(e == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if(entities.containsKey(e.getID())){
            return null;
        }
        validator.validate(e);

        entities.put(e.getID(),e);

        return e;

    }

    @Override
    public E remove(ID id) {


        if(entities.containsKey(id)){
            E entity = entities.remove(id);
            return entity;
        }
        else{
            return null;
        }
    }

    @Override
    public E find(ID id) {
        if(entities.containsKey(id)){
            return entities.get(id);
        }
        else{
            return null;
        }
    }

    @Override
    public E update(E entity) throws ValidationException{
        validator.validate(entity);
        if(entities.containsKey(entity.getID())){
            entities.put(entity.getID(), entity);
            return entity;
        }
        else{
            return null;
        }
    }

    @Override
    public List<E> getAll() {
        if (!entities.isEmpty())
            return entities.values().stream().collect(Collectors.toList());

        return null;
    }

    void emptyRepo(){
        entities.clear();
    }
}
