package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface MealService {

    Meal save(Meal meal);

    void delete(int id) throws NotFoundException;

    void update(Meal meal);

    Meal get(int id) throws NotFoundException;

    Collection<Meal> getAll();

    List<MealWithExceed> getWithExceeded(Collection<Meal> meals, int caloriesPerDay);

    List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay);

    List<MealWithExceed> getFilteredWithExceededInOneReturn(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay);
}