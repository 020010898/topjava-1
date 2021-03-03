package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository.USER_ID;

public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Map<Integer,Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal ->save(meal, USER_ID));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> meals = repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> meal = repository.get(userId);
        return meal != null && meal.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> meal = repository.get(userId);
        return meal == null ? null : meal.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        Map<Integer, Meal> meal = repository.get(userId);
        return CollectionUtils.isEmpty(meal) ? Collections.emptyList() :
                meal.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

