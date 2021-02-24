package ru.javawebinar.topjava.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealsRepository;
import ru.javawebinar.topjava.repository.MealsRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    private MealsRepository repository;

    @Override
    public void init() throws ServletException {
        repository = new InMemoryMealsRepository();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        LOG.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        repository.save(meal);
        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

//        if (action == null) {
//            LOG.info("getAll");
//            request.setAttribute("meals",
//                    MealsUtil.getWithExceeded(repository.getAll(), 2000));
//            request.getRequestDispatcher("meals.jsp").forward(request, response);
//        } else if (action.equals("delete")) {
//            int id = getId(request);
//            LOG.info("Delete {}", id);
//            repository.delete(id);
//            response.sendRedirect("meals");
//        } else {
//            Meal meal = action.equals("create") ?
//                    new Meal(LocalDateTime.now(), "", 1000) :
//                    repository.get(getId(request));
//            request.setAttribute("meal", meal);
//            request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
//        }
        switch (action == null ? "all" : action) {
            case "delete":
                String i = request.getParameter("id");
                System.out.println( "vivodim " + i);
//                int id = getId(request);
//                LOG.info("Delete {}", id);
                repository.delete(Integer.valueOf(request.getParameter("id")));
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = action.equals("create") ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        repository.get(1);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
                break;
            case "all":
            default:
                LOG.info("getAll");
                request.setAttribute( "meals",
                        MealsUtil.getWithExceeded(repository.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramID = request.getParameter("id");
        return Integer.parseInt(paramID);
    }
}
