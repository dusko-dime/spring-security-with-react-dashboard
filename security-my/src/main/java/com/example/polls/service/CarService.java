package com.example.polls.service;

import com.example.polls.model.Car;
import com.example.polls.repository.CarRepository;
import com.example.polls.service.generic.GenericService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CarService extends GenericService<Car, Integer> {
    private final CarRepository carRepository;

    public CarService(JpaRepository<Car, Integer> repo) {
        super(repo);
        carRepository = (CarRepository) repo;
    }
}
