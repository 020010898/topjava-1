package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.util.MealsUtil;

import static org.slf4j.LoggerFactory.getLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("getAll");

//        request.getRequestDispatcher("/users.jsp").forward(request, response);
        request.setAttribute("mealsList",
                MealsUtil.getWithExceeded(MealsUtil.MEAL_LIST, 2000));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
}
