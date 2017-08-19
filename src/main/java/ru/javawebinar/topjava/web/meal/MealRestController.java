package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;


    public Collection<Meal> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.save(meal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        checkIdConsistent(meal, id);
        service.update(meal);
    }

    public List<MealWithExceed> getWithExceeded(int caloriesPerDay)
    {
        return  service.getWithExceeded(getAll(), caloriesPerDay);
    }

    public List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay)
    {
        return service.getFilteredWithExceeded(meals, startTime, endTime, caloriesPerDay);
    }

    public List<MealWithExceed> getFilteredWithExceededInOneReturn(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay)
    {
        return service.getFilteredWithExceededInOneReturn(meals, startTime, endTime, caloriesPerDay);
    }


}