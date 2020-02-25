package com.example.polls.controller;

import com.example.polls.common.RestException;
import com.example.polls.controller.generic.GenericController;
import com.example.polls.model.Car;
import com.example.polls.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/car")
public class CarController extends GenericController<Car, Integer> {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity findById(@PathVariable("id") Integer id) throws RestException {
        return super.findById(id);
    }
}
