package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Arrays;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;

/**
 * Created by Vaisiliy Es'kin on 08/24/17.
 */

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
public class MealServiceTest {

    static{
        SLF4JBridgeHandler.install();}

    @Autowired
    private MealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception {
        Meal meal = service.get(ADMIN_MEAL_ID,ADMIN_ID);
        MATCHER_MEALS.assertEquals(ma1, meal);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        Meal meal = service.get(MEAL1_ID,ADMIN_ID);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        MATCHER_MEALS.assertCollectionEquals(Arrays.asList(m6, m5, m4, m3, m2), service.getAll(USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() throws Exception {
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        service.update(m2, ADMIN_ID);
    }
}