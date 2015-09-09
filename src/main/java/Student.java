import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Student {

  private int student_id;
  private String first_name;
  private String last_name;
  private String date_of_enrollment;

  public Student(String first_name, String last_name, String date_of_enrollment) {
    this.first_name = first_name;
    this.last_name = last_name;
    this.date_of_enrollment = date_of_enrollment;
  }

  public int getStudentId() {
    return student_id;
  }

  public String getName() {
    return first_name + " " + last_name;
  }

  public String getFirstName() {
    return first_name;
  }

  public String getLastName() {
    return last_name;
  }

  public String getDate() {
    return date_of_enrollment;
  }

  @Override
  public boolean equals(Object otherStudent){
    if (!(otherStudent instanceof Student)) {
      return false;
    } else {
      Student newStudent = (Student) otherStudent;
      return this.getName().equals(newStudent.getName()) &&
             this.getStudentId() == newStudent.getStudentId();
    }
  }

  public void save() {
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO students (first_name, last_name, date_of_enrollment) VALUES (:first_name, :last_name, :date_of_enrollment)";
    this.student_id = (int) con.createQuery(sql, true)
      .addParameter("first_name", first_name)
      .addParameter("last_name", last_name)
      .addParameter("date_of_enrollment", date_of_enrollment)
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

  public static Student find(int student_id) {
  try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT * FROM students where student_id=:student_id";
    Student student = con.createQuery(sql)
      .addParameter("student_id", student_id)
      .executeAndFetchFirst(Student.class);
    return student;
    }
  }

  public void update(String first_name, String last_name, String date_of_enrollment, Course newCourse) {
    updateFirstName(first_name);
    updateLastName(last_name);
    updateDate(date_of_enrollment);
    updateCourse(newCourse);
  }

  public void updateFirstName(String first_name) {
    this.first_name = first_name;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE students SET first_name=:first_name WHERE student_id=:student_id";
      con.createQuery(sql)
        .addParameter("first_name", first_name)
        .addParameter("student_id", student_id)
        .executeUpdate();
    }
  }

  public void updateLastName(String last_name) {
    this.last_name = last_name;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE students SET last_name=:last_name WHERE student_id=:student_id";
      con.createQuery(sql)
        .addParameter("last_name", last_name)
        .addParameter("student_id", student_id)
        .executeUpdate();
    }
  }

  public void updateDate(String date_of_enrollment) {
    this.date_of_enrollment = date_of_enrollment;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE students SET date_of_enrollment=:date_of_enrollment WHERE student_id=:student_id";
      con.createQuery(sql)
        .addParameter("date_of_enrollment", date_of_enrollment)
        .addParameter("student_id", student_id)
        .executeUpdate();
    }
  }

  public void updateCourse(Course newCourse) {
    try(Connection con = DB.sql2o.open()) {
      Integer newCourseId = newCourse.getCourseId();
      String sql = "UPDATE courses_students SET course_id = :course_id WHERE student_id = :student_id";
      con.createQuery(sql)
        .addParameter("course_id", newCourseId)
        .addParameter("student_id", student_id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM students WHERE student_id = :student_id;";
      con.createQuery(deleteQuery)
        .addParameter("student_id", student_id)
        .executeUpdate();

      String joinDeleteQuery = "DELETE FROM courses_students WHERE student_id = :student_id";
      con.createQuery(joinDeleteQuery)
        .addParameter("student_id", this.getStudentId())
        .executeUpdate();
    }
  }

  // public static void removeStudent(int student_id) {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "DELETE FROM students WHERE student_id=:student_id;";
  //     con.createQuery(sql)
  //       .addParameter("student_id",student_id)
  //       .executeUpdate();
  //   }
  // }

  public void addCourse(Course course) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO courses_students (course_id, student_id) VALUES (:course_id, :student_id)";
      con.createQuery(sql)
        .addParameter("course_id", course.getCourseId())
        .addParameter("student_id", this.getStudentId())
        .executeUpdate();
    }
  }

  public List<Course> getCourses() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT courses.* FROM students JOIN courses_students USING (student_id) JOIN courses USING (course_id) WHERE students.student_id=:student_id";
      List<Course> courses = con.createQuery(sql)
        .addParameter("student_id", this.getStudentId())
        .executeAndFetch(Course.class);
      return courses;
    }
  }

}
