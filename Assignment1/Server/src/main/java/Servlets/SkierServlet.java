package Servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {
    private static final String[] SKIER_POST_BODY = new String[]{"time", "liftID"};
    private final Gson gson = new Gson();

    private boolean isUrlValid(String[] urlPath) {
        if (urlPath.length == 8) {
            return urlPath[0].chars().allMatch(Character::isDigit) && urlPath[2].equals("seasons") &&
                    urlPath[3].chars().allMatch(Character::isDigit) && urlPath[4].equals("days") &&
                    urlPath[5].chars().allMatch(Character::isDigit) && urlPath[6].equals("skiers") &&
                    urlPath[7].chars().allMatch(Character::isDigit) && Integer.parseInt(urlPath[5]) >= 1 &&
                    Integer.parseInt(urlPath[5]) <= 365;
        }
        return false;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String[] parts = request.getPathInfo().split("/");

        if (parts.length != 8) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"Message\": \"URL is not correct!\"}");
            return;
        }
        JsonObject body = gson.fromJson(request.getReader(), JsonObject.class);

        for (String param : SKIER_POST_BODY) {
            if (body == null || body.get(param) == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"Message\": \"Body param is missing!\"}");
                return;
            }
        }
        response.getWriter().write("{\"Message\": \"URL is valid!\"}");
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}