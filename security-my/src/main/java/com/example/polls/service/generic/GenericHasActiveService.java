package com.example.polls.service.generic;

import com.example.polls.common.HasActive;
import com.example.polls.common.RestException;
import com.example.polls.repository.genericRepository.HasActiveRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

public class GenericHasActiveService<T extends HasActive, ID extends Serializable> extends GenericService<T,ID> {

    @PersistenceContext
    private EntityManager entityManager;
    private HasActiveRepository<T, ID> repository;

    public GenericHasActiveService(JpaRepository<T, ID> repo) {
        super(repo);
        if (repo instanceof HasActiveRepository)
            repository = (HasActiveRepository) repo;
        else throw new RuntimeException("Repository must implement " + HasActiveRepository.class.getSimpleName());
    }

    @Override
    @Transactional
    public List<T> getAll() throws RestException {
        return repository.getAllByActiveIs((byte) 1);
    }

    @Override
    public T findById(ID id) throws RestException {
        return repository.getByIdAndActive(id, (byte) 1);
    }

    @Override
    public T update(@RequestBody T object) throws RestException {
        T objectDb = repository.getByIdAndActive((ID) entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(object), (byte) 1);
        if (objectDb != null) {
            if (object.getActive() == null)
                object.setActive(objectDb.getActive());
            return super.update(object);
        }
        return null;
    }

    @Override
    public T delete(@PathVariable ID id) throws RestException {
        T object = null;
        if ((object = repository.getByIdAndActive(id, (byte) 1)) != null) {
            object.setActive((byte) 0);
            object=repo.saveAndFlush(object);
        }
        return object;
    }

}
