package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public class InMemoryMealsRepository implements MealsRepository{
    @Override
    public Meal save(Meal meal) {
        return null;
    }

    @Override
    public void delete(Meal meal) {

    }

    @Override
    public Meal get(int id) {
        return null;
    }

    @Override
    public Collection<Meal> getAll() {
        return null;
    }
}
