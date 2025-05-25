package org.creche;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;

import spark.Session;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class App {
    static UserDao userDao;
    static ChildDao childDao;
    static AdmissionDao admissionDao;
    static Gson gson = new Gson();

    public static void main(String[] args) throws SQLException {
        port(4567);
        staticFiles.location("/public");

        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/creche_db?useSSL=false",
                "root",
                "12345678");

        userDao = new UserDao(conn);
        childDao = new ChildDao(conn);
        admissionDao = new AdmissionDao(conn);

        // Routes

        // Home redirect to login
        get("/", (req, res) -> {
            res.redirect("/login.html");
            return "";
        });

        // User Registration
        post("/register", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");
            if (userDao.findByUsername(username) != null) {
                res.status(400);
                return "User already exists";
            }
            User user = new User(username, User.hashPassword(password), "parent");
            userDao.save(user);
            res.redirect("/login.html");
            return "";
        });

        // User Login
        post("/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");
            User user = userDao.findByUsername(username);
            if (user != null && user.checkPassword(password)) {
                Session session = req.session(true);
                session.attribute("user", user);
                res.redirect("/dashboard.html");
                return "";
            } else {
                res.status(401);
                return "Invalid credentials";
            }
        });
        get("/api/my-children", (req, res) -> {
            User user = req.session().attribute("user");
            if (user == null) {
                res.status(401);
                return "Unauthorized";
            }
        
            List<Child> children = childDao.findByUserId(user.getId()); // or getChildrenByUserId
            res.type("application/json");
            return new Gson().toJson(children);
        });
        
        // Logout
        get("/logout", (req, res) -> {
            req.session().invalidate();
            res.redirect("/login.html");
            return "";
        });

        // Middleware - require login
        before("/dashboard.html", new AuthFilter());
        before("/add_child.html", new AuthFilter());
        before("/admission.html", new AuthFilter());

        // API to get current user info
        get("/api/user", (req, res) -> {
            User user = req.session().attribute("user");
            if (user == null) {
                res.status(401);
                return "Not logged in";
            }
            res.type("application/json");
            return gson.toJson(user);
        });

        // Add child form submission
        post("/api/children", (req, res) -> {
            User user = req.session().attribute("user");
            if (user == null) {
                res.status(401);
                return "Not logged in";
            }

            String name = req.queryParams("name");
            String birthdate = req.queryParams("birthdate");
            String allergies = req.queryParams("allergies");
            String specialNeeds = req.queryParams("special_needs");

            Child child = new Child(user.getId(), name, birthdate, allergies, specialNeeds);
            childDao.save(child);

            res.redirect("/dashboard.html");
            return "";
        });

        // List children for logged in user
        get("/api/children", (req, res) -> {
            User user = req.session().attribute("user");
            if (user == null) {
                res.status(401);
                return "Not logged in";
            }
            res.type("application/json");
            return gson.toJson(childDao.findByUserId(user.getId()));
        });

        // Submit admission request
        post("/api/admissions", (req, res) -> {
            User user = req.session().attribute("user");
            if (user == null) {
                res.status(401);
                return "Not logged in";
            }
            Admission admission = new Admission(user.getId(), "pending");
            admissionDao.save(admission);
            res.redirect("/dashboard.html");
            return "";
        });

        // Admin: List admissions
        get("/api/admissions", (req, res) -> {
            User user = req.session().attribute("user");
            if (user == null || !"admin".equals(user.getRole())) {
                res.status(403);
                return "Access denied";
            }
            res.type("application/json");
            return gson.toJson(admissionDao.findAll());
        });

        // Admin: Approve admission
        post("/api/admissions/:id/approve", (req, res) -> {
            User user = req.session().attribute("user");
            if (user == null || !"admin".equals(user.getRole())) {
                res.status(403);
                return "Access denied";
            }
            int admissionId = Integer.parseInt(req.params("id"));
            admissionDao.updateStatus(admissionId, "approved");
            res.redirect("/admin_dashboard.html");
            return "";
        });
    }
}
