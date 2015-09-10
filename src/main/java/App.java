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
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    // Main page
    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Index for courses
    get("/courses", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("allCourses", Course.all());
      model.put("template", "templates/courses.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Index for students
    get("/students", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("allStudents", Student.all());
      model.put("template", "templates/students.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Show individual course
    get("/course/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Course showCourse = Course.find(id);
      model.put("showCourse", showCourse);
      model.put("template", "templates/course.vtl");
      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    // Show individual student
    get("/student/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Student showStudent = Student.find(id);
      model.put("showStudent", showStudent);
      model.put("template", "templates/student.vtl");
      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

//////////backend//////////

    //backend homepage
    get("/backend", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("template", "templates/backend-index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Show individual course with edit fields
    get("/course/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Course showCourse = Course.find(id);
      model.put("showCourse", showCourse);
      model.put("template", "templates/backend-course.vtl");
      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    // Index for courses with edit fields
      get("/backend/courses", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("allCourses", Course.all());
        model.put("template", "templates/backend-courses.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    // Show empty new course form
    get("/backend/course/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("template", "templates/course-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Show empty new student form
    get("/backend/student/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("allCourses", Course.all());
      model.put("template", "templates/student-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Create course
    post("/backend/courses", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String courseName = request.queryParams("courseName");
      Course newCourse = new Course(courseName);
      newCourse.save();

      model.put("allCourses", Course.all());
      model.put("template", "templates/courses.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
    //
    // Create student
    // post("/students", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //
    //   String studentFirstName = request.queryParams("studentFirstName");
    //   String studentLastName = request.queryParams("studentLastName");
    //   String studentDate= request.queryParams("studentDate");
    //   Integer studentCourseId = Integer.parseInt(request.queryParams("studentCourses"));
    //
    //   Course newCourse = Course.find(studentCourseId);
    //   Student newStudent = new Student();
    //
    //   newStudent.save();
    //   newStudent.addCourse(newCourse);
    //
    //   model.put("allStudents", Student.all());
    //   model.put("template", "templates/students.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
    //
    // Edit course
    get("/backend/courses/:id/edit", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Course editCourse = Course.find(id);

      model.put("editCourse", editCourse);
      model.put("template", "templates/course-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
    //
    // // Edit student
    // get("/students/:id/edit", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   int id = Integer.parseInt(request.params(":id"));
    //   Student editStudent = Student.find(id);
    //   model.put("allCourses", Course.all());
    //   model.put("editStudent", editStudent);
    //   model.put("template", "templates/student-form.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
    //
    // Update course
    post("/backend/courses/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String newCourseName = request.queryParams("editCourseName");

      int id = Integer.parseInt(request.params(":id"));
      Course editCourse = Course.find(id);

      editCourse.update(newCourseName);

      model.put("allCourses", Course.all());
      model.put("template", "templates/courses.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
    //
    // // Update student
    // post("/students/:id", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   String newStudentFirstName = request.queryParams("editStudentFirstName");
    //   String newStudentLastName = request.queryParams("editStudentLastName");
    //   String newStudentDate = request.queryParams("editStudentDate");
    //   Integer newCourseNumber = Integer.parseInt(request.queryParams("editCourses"));
    //   int id = Integer.parseInt(request.params(":id"));
    //
    //
    //   Course newCourse = Course.find(newCourseNumber);
    //   Student editStudent = Student.find(id);
    //
    //   editStudent.update(newStudentFirstName, newStudentLastName, newStudentDate, newCourse);
    //
    //   model.put("allStudents", Student.all());
    //   model.put("template", "templates/students.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
    //
    // Delete course
    get("/backend/courses/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Course deleteCourse = Course.find(id);
      deleteCourse.delete();
      model.put("allCourses", Course.all());
      model.put("template", "templates/courses.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
    //
    // // Delete student
    // get("/students/:id/delete", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   int id = Integer.parseInt(request.params(":id"));
    //   Student deleteStudent = Student.find(id);
    //   deleteStudent.delete();
    //   model.put("allStudents", Student.all());
    //   model.put("template", "templates/students.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());


  }//close
}//close
