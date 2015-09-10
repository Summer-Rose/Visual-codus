/**
 * Student.java provides CRUD functionality for Student objects
 */

import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;


public class Student {

  private int student_id;
  private int course_id;
  private int age;
  private String gender;
  private String origin;
  private Integer distance_traveled;
  private int salary_before;

  public Student(int age, String gender, String origin, Integer distance_traveled, int salary_before) {
    this.course_id = course_id;
    this.age = age;
    this.gender = gender;
    this.origin = origin;
    this.distance_traveled = distance_traveled;
    this.salary_before = salary_before;
  }

  public int getStudentId() {
    return student_id;
  }

  public int getAge() {
    return age;
  }

  public String getGender() {
    return gender;
  }

  public String getOrigin() {
    return origin;
  }

  public Integer getDistanceTraveled() {
    return distance_traveled;
  }

  public Integer getSalaryBefore() {
    return salary_before;
  }

  @Override
  public boolean equals(Object otherStudent){
    if (!(otherStudent instanceof Student)) {
      return false;
    } else {
      Student newStudent = (Student) otherStudent;
      return this.getOrigin().equals(newStudent.getOrigin()) &&
             this.getStudentId() == newStudent.getStudentId();
    }
  }

  public void save() {
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO students (course_id, age, gender, origin, distance_traveled, salary_before) VALUES (:course_id, :age, :gender, :origin, :distance_traveled, :salary_before)";
    this.student_id = (int) con.createQuery(sql, true)
      .addParameter("course_id", course_id)
      .addParameter("age", age)
      .addParameter("gender", gender)
      .addParameter("origin", origin)
      .addParameter("distance_traveled", distance_traveled)
      .addParameter("salary_before", salary_before)
      .executeUpdate()
      .getKey();
    }
  }

  public static List<Student> all() {
    String sql = "SELECT * FROM students";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Student.class);
    }
  }

  // public static List<Student> allStudentsByCourse(Integer course_id) {
  //   String sql = "SELECT * FROM students WHERE course_id:=course_id";
  //   try(Connection con = DB.sql2o.open()) {
  //     return con.createQuery(sql)
  //     .addParameter("course_id", course_id)
  //     .executeAndFetch(Student.class);
  //   }
  // }


  public static String getCourseNameByStudentId(int student_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT course_name FROM courses JOIN students ON (students.course_id = courses.course_id) WHERE students.student_id = :student_id";
      String courseName = con.createQuery(sql)
        .addParameter("student_id", student_id)
        .executeAndFetchFirst(String.class);
      return courseName;
    }
  }

  public static Student find(int student_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM students where student_id=:student_id";
      Student student = con.createQuery(sql)
        .addParameter("student_id", student_id)
        .executeAndFetchFirst(Student.class);
      return student;
    }
  }

  public void addCourse(Course course) {
  try (Connection con = DB.sql2o.open()) {
    String sql = "UPDATE students SET course_id=:course_id WHERE student_id=:student_id";
    con.createQuery(sql)
      .addParameter("course_id", course.getCourseId())
      .addParameter("student_id", this.getStudentId())
      .executeUpdate();
  }
}

  public void update( Integer age, String gender, String origin, Integer distance_traveled, Integer salary_before) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE students SET course_id=:course_id, age=:age, gender=:gender, origin=:origin, distance_traveled=:distance_traveled, salary_before=:salary_before WHERE student_id=:student_id";
      con.createQuery(sql)
        .addParameter("course_id", course_id)
        .addParameter("age", age)
        .addParameter("gender", gender)
        .addParameter("origin", origin)
        .addParameter("distance_traveled", distance_traveled)
        .addParameter("salary_before", salary_before)
        .addParameter("student_id", this.student_id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM students WHERE student_id=:student_id;";
      con.createQuery(deleteQuery)
        .addParameter("student_id", this.student_id)
        .executeUpdate();
    }
  }
}
