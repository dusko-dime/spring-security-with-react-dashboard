package com.example.polls.controller.generic;

import com.example.polls.common.RestException;
import com.example.polls.service.generic.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

public class GenericController<T, ID extends Serializable> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    protected GenericService<T,ID> service;

    @Transactional
    @GetMapping
    public ResponseEntity getAll() throws RestException {
        return new ResponseEntity(service.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable("id") ID id) throws RestException {
        return new ResponseEntity(service.findById(id), HttpStatus.OK);
    }

    @Transactional
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity insert(@RequestBody T object) throws RestException {
        T response = service.insert(object);
        if (response!=null){
            //logCreateAction(object);
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
        throw new RestException(HttpStatus.BAD_REQUEST, null);
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody T object) throws RestException {
       // T oldObject = cloner.deepClone(service.findById((ID) entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(object)));
        if ((object=service.update(object)) != null) {
            //logUpdateAction(object, oldObject);
            return new ResponseEntity(object, HttpStatus.OK);
        }
        throw new RestException(HttpStatus.BAD_REQUEST, null);
    }

    @DeleteMapping(value = {"/{id}"})
    @Transactional
    public ResponseEntity delete(@PathVariable ID id) throws RestException {
        T object = service.delete(id);
        if (object!=null){
           // logDeleteAction(object);
            return new ResponseEntity(null, HttpStatus.OK);
        }
        throw new RestException(HttpStatus.BAD_REQUEST, null);
    }

}
