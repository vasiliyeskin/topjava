package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> userMealWithExceeds = getFilteredWithExceeded(mealList, LocalTime.of(7, 0),
                LocalTime.of(13, 1), 2000);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {


        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        Map<LocalDate, Integer> map = new HashMap<>();
        LocalDate bufferDate;

        // fill map of callories per current date
        for (UserMeal userMeal : mealList) {
            bufferDate = userMeal.getDateTime().toLocalDate();
            if (!map.containsKey(bufferDate))
                map.put(bufferDate, userMeal.getCalories());
            else
                map.put(bufferDate, map.get(bufferDate) + userMeal.getCalories());
        }

        // fill userMealWithExceeds
        for (UserMeal userMeal : mealList) {
            if (userMeal.getDateTime().toLocalTime().isAfter(startTime) &&
                    userMeal.getDateTime().toLocalTime().isBefore(endTime)) {
                userMealWithExceeds.add(
                        new UserMealWithExceed(userMeal.getDateTime(),
                                userMeal.getDescription(),
                                userMeal.getCalories(),
                                map.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }

        return userMealWithExceeds;
    }
}
