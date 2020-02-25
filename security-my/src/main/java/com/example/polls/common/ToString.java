package com.example.polls.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by drstjepanovic on 3/18/2019
 */
public class ToString {

    protected ObjectMapper objectMapper;

    public ToString() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
