package server.repository;
import common.domain.Entity;

import java.util.Optional;

public interface Repository<ID, E extends Entity<ID>> {

    Optional<E> findOne(ID id);

    Iterable<E> getAll();

    Long save (E entity);

    void delete(ID id);

    void update(E entity);
}
