import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

import java.util.HashMap;
import java.util.Iterator;
import java.time.LocalDateTime;

public class App {
  public static void main(String[] args) {
    ProcessBuilder process = new ProcessBuilder();
    Integer port;
    if (process.environment().get("PORT") != null) {
        port = Integer.parseInt(process.environment().get("PORT"));
    } else {
        port = 4567;
    }

   setPort(port);
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    /** [USER] Root: where graphics are displayed */
    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      // Distance traveled/origins graphic
      Integer wHeight = 200, wWidth = 800;
      model.put("wHeight", wHeight);
      model.put("wWidth", wWidth);
      model.put("stringDistancesSVG", Student.stringDistancesSVG(wHeight, wWidth, "black", "red", 5));
      model.put("stringOrigins", Student.stringOrigins());

      // Ages breakdown graphic
      List<Integer> uniqueAges = Student.getUniqueAges();
      model.put("students", Student.studentsByAge(uniqueAges));

      // Salary range breakdown graphic
      model.put("salaryRanges", Student.getSalaryRanges());

      // Gender breakdown graphic
      List<String> allGenders = Student.getAllGenders();
      model.put("genders", Student.displayGender(allGenders));

      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /** [ADMIN] Admin home page */
    get("/backend", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/backend-index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /** [ADMIN] Show individual course with edit fields */
    // FIXME: what does this even do?
    get("/backend/course/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Course showCourse = Course.find(id);
      model.put("showCourse", showCourse);
      model.put("template", "templates/backend-course.vtl");
      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    /** [ADMIN] Index for courses with edit fields */
      get("/backend/courses", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("allCourses", Course.all());
        model.put("template", "templates/backend-courses.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    /** [ADMIN] Index for students with edit fields */
      get("/backend/students", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("allStudents", Student.all());
        model.put("template", "templates/backend-students.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    /** [ADMIN] Show empty new course form */
    // FIXME: not working!
    get("/backend/course/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/course-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /** [ADMIN] Show empty new student form */
    get("/backend/student/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("allCourses", Course.all());
      model.put("template", "templates/student-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /** [ADMIN] Create course */
    post("/backend/courses", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      // Get course name, create new student, and save
      String courseName = request.queryParams("courseName");
      Course newCourse = new Course(courseName);
      newCourse.save();

      model.put("allCourses", Course.all());
      model.put("template", "templates/backend-courses.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /** [ADMIN] Create student */
    post("/backend/students", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      // Get student data (except for course_id)
      Integer studentAge = Integer.parseInt(request.queryParams("age"));
      String studentGender = request.queryParams("gender");
      String studentOrigin = request.queryParams("origin");
      String studentDistanceRaw = request.queryParams("distance");
      Integer studentDistanceClean  = Integer.parseInt(studentDistanceRaw.replaceAll("[^a-zA-Z0-9]",""));
      String studentSalaryRaw = request.queryParams("salary");
      Integer studentSalaryClean  = Integer.parseInt(studentSalaryRaw.replaceAll("[^a-zA-Z0-9]",""));

      // Get course_id from drop down menu, create new student, and save
      Integer courseId = Integer.parseInt(request.queryParams("studentCourses"));
      Student newStudent = new Student(courseId, studentAge, studentGender, studentOrigin, studentDistanceClean, studentSalaryClean);
      newStudent.save();

      // Need this for drop down menu
      model.put("allCourses", Course.all());

      model.put("allStudents", Student.all());
      model.put("template", "templates/backend-students.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /** [ADMIN] Edit course */
    get("/backend/courses/:id/edit", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      // Get specific course to edit
      int id = Integer.parseInt(request.params(":id"));
      Course editCourse = Course.find(id);

      model.put("editCourse", editCourse);
      model.put("template", "templates/course-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /** [ADMIN] Edit student */
    get("/backend/students/:id/edit", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      // Get specific student to edit
      int id = Integer.parseInt(request.params(":id"));
      Student editStudent = Student.find(id);

      // Need this for drop down menu
      model.put("allCourses",Course.all());

      model.put("editStudent", editStudent);
      model.put("template", "templates/student-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /** [ADMIN] Update course */
    post("/backend/courses/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      // Update specific course
      String newCourseName = request.queryParams("editCourseName");
      int id = Integer.parseInt(request.params(":id"));
      Course editCourse = Course.find(id);
      editCourse.update(newCourseName);

      model.put("allCourses", Course.all());
      model.put("template", "templates/backend-courses.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /** [ADMIN] Update student */
    post("/backend/students/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      // Update student data
      Integer studentAge = Integer.parseInt(request.queryParams("age"));
      String studentGender = request.queryParams("gender");
      String studentOrigin = request.queryParams("origin");
      String studentDistanceRaw = request.queryParams("distance");
      Integer studentDistanceClean  = Integer.parseInt(studentDistanceRaw.replaceAll("[^a-zA-Z0-9]",""));
      String studentSalaryRaw = request.queryParams("salary");
      Integer studentSalaryClean  = Integer.parseInt(studentSalaryRaw.replaceAll("[^a-zA-Z0-9]",""));
      Integer courseId = Integer.parseInt(request.queryParams("studentCourses"));
      Integer studentId = Integer.parseInt(request.params("id"));

      // FIXME: don't think I need this
      // Course myCourse = Course.find(courseId);
      Student editStudent = Student.find(studentId);
      editStudent.update(courseId, studentAge, studentGender, studentOrigin, studentDistanceClean, studentSalaryClean);

      model.put("allStudents", Student.all());
      model.put("template", "templates/backend-students.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /** [ADMIN] Delete course */
    get("/backend/courses/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      // Get specific course to delete
      int id = Integer.parseInt(request.params(":id"));
      Course deleteCourse = Course.find(id);
      deleteCourse.delete();

      model.put("allCourses", Course.all());
      model.put("template", "templates/backend-courses.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /** [ADMIN] Delete student */
    get("backend/students/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      // Get specific student to delete
      int id = Integer.parseInt(request.params(":id"));
      Student deleteStudent = Student.find(id);
      deleteStudent.delete();

      model.put("allStudents", Student.all());
      model.put("template", "templates/backend-students.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }  // Close main
}  // Close App
