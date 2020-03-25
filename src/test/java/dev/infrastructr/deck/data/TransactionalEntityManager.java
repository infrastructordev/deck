package dev.infrastructr.deck.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TransactionalEntityManager {

    @Autowired
    private TestEntityManager entityManager;

    public <E> E persist(E entity) {
        return entityManager.persist(entity);
    }
    public <E> E find(Class<E> entityClass, Object primaryKey) {
        return entityManager.find(entityClass, primaryKey);
    }
}
