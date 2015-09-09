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
      return this.getName().equals(newCourse.getName());
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

  public void updateName(String course_name) {
    // update in memory
    this.course_name = course_name;

    // update in database
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE courses SET course_name=:course_name WHERE course_id=:course_id";
      this.course_id = (int) con.createQuery(sql, true)
        .addParameter("course_name", course_name)
        .addParameter("course_id", this.course_id)
        .executeUpdate()
        .getKey();
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

  // public void addStudent(Student student) {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "INSERT INTO courses_students (course_id, student_id) VALUES (:course_id, :student_id)";
  //     con.createQuery(sql)
  //       .addParameter("course_id", this.getCourseId())
  //       .addParameter("student_id", student.getStudentId())
  //       .executeUpdate();
  //   }
  // }

  // public List<Student> getStudents() {
  //   try(Connection con = DB.sql2o.open()){
  //     String sql = "SELECT students.* FROM courses JOIN courses_students ON (courses.course_id=courses_students.course_id) JOIN students ON (courses_students.student_id=students.student_id) WHERE courses.course_id=:course_id";
  //     List<Student> students = con.createQuery(sql)
  //       .addParameter("course_id", this.getCourseId())
  //       .executeAndFetch(Student.class);
  //     return students;
  //   }
  // }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM courses WHERE course_id=:course_id;";
        con.createQuery(deleteQuery)
          .addParameter("course_id", course_id)
          .executeUpdate();

      // String joinDeleteQuery = "DELETE FROM courses_students WHERE course_id = :course_id";
      // con.createQuery(joinDeleteQuery)
      //   .addParameter("course_id", this.getCourseId())
      //   .executeUpdate();
    }
  }

  public void clearStudents() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM students;";
      con.createQuery(sql)
        .executeUpdate();

    // String joinDeleteQuery = "DELETE FROM courses_students;";
    // con.createQuery(joinDeleteQuery)
    //   .executeUpdate();
     }
  }

  public static void removeCourse(int course_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM courses WHERE course_id=:course_id;";
      con.createQuery(sql).addParameter("course_id",course_id).executeUpdate();
    }
  }
}
