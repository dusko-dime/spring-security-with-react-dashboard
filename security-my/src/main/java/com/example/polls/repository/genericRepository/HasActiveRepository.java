package com.example.polls.repository.genericRepository;


import com.example.polls.common.HasActive;

import java.io.Serializable;
import java.util.List;

public interface HasActiveRepository<T extends HasActive, ID extends Serializable> {
    List<T> getAllByActiveIs(Byte active);


    T getByIdAndActive(ID id, Byte active);

    Boolean existsByIdAndActive(ID id, Byte active);
}
