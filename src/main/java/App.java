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
      List<Integer> uniqueAges = Student.getUniqueAges();
      model.put("students", Student.studentsByAge(uniqueAges));
      model.put("salaryRanges", Student.getSalaryRanges());
      List<String> allGenders = Student.getAllGenders();
      model.put("studentgenders", Student.displayGender(allGenders));
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // // Index for courses
    // get("/courses", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   model.put("allCourses", Course.all());
    //   model.put("template", "templates/courses.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());

    // // Index for students
    // get("/students", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   model.put("allStudents", Student.all());
    //   model.put("template", "templates/students.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());

    // // Show individual course
    // get("/course/:id", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   int id = Integer.parseInt(request.params(":id"));
    //   Course showCourse = Course.find(id);
    //   model.put("showCourse", showCourse);
    //   model.put("template", "templates/course.vtl");
    //   return new ModelAndView(model, layout);
    //   }, new VelocityTemplateEngine());

    // // Show individual student
    // get("/student/:id", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   int id = Integer.parseInt(request.params(":id"));
    //   Student showStudent = Student.find(id);
    //   model.put("showStudent", showStudent);
    //   model.put("template", "templates/student.vtl");
    //   return new ModelAndView(model, layout);
    //   }, new VelocityTemplateEngine());

<<<<<<< HEAD
    // // Show empty new course form
    // get("/courses/new", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
=======
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

    // Index for students with edit fields
      get("/backend/students", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("allStudents", Student.all());
        model.put("allCourses", Course.all());
        model.put("template", "templates/backend-students.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    // Show empty new course form
    get("/backend/course/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
>>>>>>> 8c9336d4d24e82cf0ceec16414cb33e8a5cc4e11

    //   model.put("template", "templates/course-form.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());

<<<<<<< HEAD
    // // Show empty new student form
    // get("/students/new", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   model.put("allCourses", Course.all());
    //   model.put("template", "templates/student-form.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
=======
    // Show empty new student form
    get("/backend/student/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("allCourses", Course.all());
      model.put("template", "templates/student-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
>>>>>>> 8c9336d4d24e82cf0ceec16414cb33e8a5cc4e11

    // Create course
    post("/backend/courses", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String courseName = request.queryParams("courseName");
      Course newCourse = new Course(courseName);
      newCourse.save();

      model.put("allCourses", Course.all());
      model.put("template", "templates/backend-courses.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //Create student
    post("/backend/students", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer studentAge = Integer.parseInt(request.queryParams("age"));
      String studentGender = request.queryParams("gender");
      String studentOrigin = request.queryParams("origin");
      String studentDistanceRaw = request.queryParams("distance");
      Integer studentDistanceClean  = Integer.parseInt(studentDistanceRaw.replaceAll("[^a-zA-Z0-9]",""));
      String studentSalaryRaw = request.queryParams("salary");
      Integer studentSalaryClean  = Integer.parseInt(studentSalaryRaw.replaceAll("[^a-zA-Z0-9]",""));
      Integer studentCourseId = Integer.parseInt(request.queryParams("studentCourses"));

      Course newCourse = Course.find(studentCourseId);
      Student newStudent = new Student(studentAge, studentGender, studentOrigin, studentDistanceClean, studentSalaryClean);

      newStudent.save();
      newStudent.addCourse(newCourse);

      //model.put("studentsByCourse", Student.allStudentsByCourse(studentCourseId));

      model.put("allStudents", Student.all());
      model.put("allCourses", Course.all());
      model.put("template", "templates/backend-students.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Edit course
    get("/backend/courses/:id/edit", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Course editCourse = Course.find(id);

      model.put("editCourse", editCourse);
      model.put("template", "templates/course-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Edit student
    get("/backend/students/:id/edit", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Student editStudent = Student.find(id);
      model.put("allCourses",Course.all());
      model.put("editStudent", editStudent);
      model.put("template", "templates/student-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Update course
    post("/backend/courses/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String newCourseName = request.queryParams("editCourseName");

      int id = Integer.parseInt(request.params(":id"));
      Course editCourse = Course.find(id);

      editCourse.update(newCourseName);

      model.put("allCourses", Course.all());
      model.put("template", "templates/backend-courses.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Update student
    post("/backend/students/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Integer studentAge = Integer.parseInt(request.queryParams("age"));
      String studentGender = request.queryParams("gender");
      String studentOrigin = request.queryParams("origin");
      String studentDistanceRaw = request.queryParams("distance");
      Integer studentDistanceClean  = Integer.parseInt(studentDistanceRaw.replaceAll("[^a-zA-Z0-9]",""));
      String studentSalaryRaw = request.queryParams("salary");
      Integer studentSalaryClean  = Integer.parseInt(studentSalaryRaw.replaceAll("[^a-zA-Z0-9]",""));
      Integer studentCourseId = Integer.parseInt(request.queryParams("studentCourses"));
      Integer id = Integer.parseInt(request.params("id"));

      Course newCourse = Course.find(studentCourseId);
      Student editStudent = Student.find(id);

      editStudent.update(studentAge, studentGender, studentOrigin, studentDistanceClean, studentSalaryClean);

      model.put("allStudents", Student.all());
      model.put("template", "templates/backend-students.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Delete course
    get("/backend/courses/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Course deleteCourse = Course.find(id);
      deleteCourse.delete();
      model.put("allCourses", Course.all());
      model.put("template", "templates/backend-courses.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // Delete student
    get("backend/students/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Student deleteStudent = Student.find(id);
      deleteStudent.delete();
      model.put("allStudents", Student.all());
      model.put("template", "templates/backend-students.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


  }//close
}//close
