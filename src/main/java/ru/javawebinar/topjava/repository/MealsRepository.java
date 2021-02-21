package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealsRepository {
    Meal save(Meal meal);
    void delete(Meal meal);
    Meal get(int id);
    Collection<Meal> getAll();
}
