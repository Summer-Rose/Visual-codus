/**
 * Course.java provides CRUD functionality for Course objects
 */

import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;


public class Course {

  private Integer course_id;
  private String course_name;

  public Course(String course_name) {
    this.course_name = course_name;
  }

  public Integer getCourseId() {
    return course_id;
  }

  public String getName() {
    return course_name;
  }

  @Override
  public boolean equals(Object otherCourse){
    if (!(otherCourse instanceof Course)) {
      return false;
    } else {
      Course newCourse = (Course) otherCourse;
      return this.getName().equals(newCourse.getName()) &&
        this.getCourseId() == newCourse.getCourseId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO courses (course_name) VALUES (:course_name)";
      this.course_id = (int) con.createQuery(sql, true)
        .addParameter("course_name", this.course_name)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Course> all() {
    String sql = "SELECT * FROM courses";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .executeAndFetch(Course.class);
    }
  }

  public static Course find(int course_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM courses where course_id=:course_id";
      Course course = con.createQuery(sql)
        .addParameter("course_id", course_id)
        .executeAndFetchFirst(Course.class);
      return course;
    }
  }

  public static String getCourseNameById(int course_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT course_name FROM courses where course_id=:course_id";
      String courseName = con.createQuery(sql)
        .addParameter("course_id", course_id)
        .executeAndFetchFirst(String.class);
      return courseName;
    }
  }

  // public static List<Student> studentsByCourse(int course_id){
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "SELECT * FROM students WHERE course_id=:course_id";
  //     List<Student> students = con.createQuery(sql)
  //       .addParameter("course_id", course_id)
  //       .executeAndFetchFirst(Student.class);
  //     return students;
  //   }
  // }

  public void update(String course_name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE courses SET course_name=:course_name WHERE course_id=:course_id";
      this.course_id = (int) con.createQuery(sql, true)
        .addParameter("course_name", course_name)
        .addParameter("course_id", this.course_id)
        .executeUpdate()
        .getKey();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM courses WHERE course_id=:course_id;";
        con.createQuery(deleteQuery)
          .addParameter("course_id", this.course_id)
          .executeUpdate();
    }
  }
}
