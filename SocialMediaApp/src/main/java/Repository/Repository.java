package Repository;

import Domain.HasID;
import Domain.Validators.ValidationException;

import java.util.List;

public interface Repository<ID,E> {
    E add(E e) throws ValidationException;
    E remove(ID id);
    E find(ID id);

    E update(E Entity) throws ValidationException;
    List<E> getAll();
}
