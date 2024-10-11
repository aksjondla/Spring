package com.example.springexample.SpringExample.services;

import java.util.Collection;

public interface IGRUDService <T, Y>{
    T getById(Y id);
    Collection<T> getAll();
    void create(T item);
    void update(T item , Y id);
    void delete(Y id);
}
