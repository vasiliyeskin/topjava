package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal save(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public void delete(int id) throws NotFoundException {repository.delete(id);}

    @Override
    public Meal get(int id) throws NotFoundException {
        Meal meal = repository.get(id);
        if(meal == null || meal.getUserID() != AuthorizedUser.id())
            throw new NotFoundException("not found");
        else
            return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.getAll();
    }

    @Override
    public void update(Meal meal) {
        repository.save(meal);
    }


    public final int DEFAULT_CALORIES_PER_DAY = AuthorizedUser.getCaloriesPerDay();

    @Override
    public List<MealWithExceed> getWithExceeded(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredWithExceeded(meals, LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
    }

    @Override
    public List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime))
                .map(meal -> createWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public List<MealWithExceed> getFilteredWithExceededByCycle(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        meals.forEach(meal -> caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum));

        final List<MealWithExceed> mealsWithExceeded = new ArrayList<>();
        meals.forEach(meal -> {
            if (DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
                mealsWithExceeded.add(createWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay));
            }
        });
        return mealsWithExceeded;
    }

    /*
     *  Advanced solution in one return (listDayMeals can be inline).
     *  Streams are not multiplied, so complexity is still O(N)
     *  Execution time is increased as for every day we create 2 additional streams
     */
    @Override
    public List<MealWithExceed> getFilteredWithExceededInOneReturn(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Collection<List<Meal>> listDayMeals = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate)).values();

        return listDayMeals
                .stream().map(dayMeals -> {
                    boolean exceed = dayMeals.stream().mapToInt(Meal::getCalories).sum() > caloriesPerDay;
                    return dayMeals.stream().filter(meal ->
                            DateTimeUtil.isBetween(meal.getTime(), startTime, endTime))
                            .map(meal -> createWithExceed(meal, exceed));
                }).flatMap(identity())
                .collect(Collectors.toList());
    }

    public MealWithExceed createWithExceed(Meal meal, boolean exceeded) {
        return new MealWithExceed(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded, meal.getUserID());
    }
}