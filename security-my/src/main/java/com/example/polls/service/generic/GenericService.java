package com.example.polls.service.generic;

import com.example.polls.common.RestException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

public class GenericService<T, ID extends Serializable> {
    protected JpaRepository<T, ID> repo;

    @PersistenceContext
    private EntityManager entityManager;

    public GenericService(JpaRepository<T, ID> repo) {
        this.repo = repo;
    }


    @Transactional
    public List<T> getAll() throws RestException {
        return repo.findAll();
    }
    @Transactional
    public T findById( ID id) throws RestException {
        return repo.findById(id).orElse(null);

    }

    @Transactional
    public T insert( T object) throws RestException {
        T response = null;
        if ((response = repo.saveAndFlush(object)) != null) {
            entityManager.refresh(response);
            return response;
        }
        throw new RestException(HttpStatus.BAD_REQUEST, null);
    }

    public T update(T object) throws RestException {
        return repo.saveAndFlush(object);
    }

    @Transactional
    public T delete( ID id) throws RestException {
        T object = null;
        if ((object = repo.findById(id).orElse(null)) != null) {
            repo.deleteById(id);
        }
        return object;
    }
}
