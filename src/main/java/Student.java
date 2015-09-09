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

  public Student(int course_id, int age, String gender, String origin, Integer distance_traveled, int salary_before) {
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

  //still need getter method for course ID

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

  public static Student find(int student_id) {
  try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT * FROM students where student_id=:student_id";
    Student student = con.createQuery(sql)
      .addParameter("student_id", student_id)
      .executeAndFetchFirst(Student.class);
    return student;
    }
  }

  public void update(int course_id, int age, String gender, String origin, String distance_traveled, int salary_before, Course newCourse) {
  //  this.first_name = first_name; what does this need to be?
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE students SET course_id=:course_id, age=:age, origin=:origin, distance_traveled=:distance_traveled, salary_before:=salary_before WHERE student_id=:student_id";
      con.createQuery(sql)
      .addParameter("age", age)
      .addParameter("origin", origin)
      .addParameter("distance_traveled", distance_traveled)
      .addParameter("salary_before", salary_before)
      .executeUpdate();
    }
  }

  //
  // public void updateFirstName(String first_name) {
  //   this.first_name = first_name;
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "UPDATE students SET first_name=:first_name WHERE student_id=:student_id";
  //     con.createQuery(sql)
  //       .addParameter("first_name", first_name)
  //       .addParameter("student_id", student_id)
  //       .executeUpdate();
  //   }
  // }
  //
  // public void updateLastName(String last_name) {
  //   this.last_name = last_name;
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "UPDATE students SET last_name=:last_name WHERE student_id=:student_id";
  //     con.createQuery(sql)
  //       .addParameter("last_name", last_name)
  //       .addParameter("student_id", student_id)
  //       .executeUpdate();
  //   }
  // }
  //
  // public void updateDate(String date_of_enrollment) {
  //   this.date_of_enrollment = date_of_enrollment;
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "UPDATE students SET date_of_enrollment=:date_of_enrollment WHERE student_id=:student_id";
  //     con.createQuery(sql)
  //       .addParameter("date_of_enrollment", date_of_enrollment)
  //       .addParameter("student_id", student_id)
  //       .executeUpdate();
  //   }
  // }

  // public void updateCourse(Course newCourse) {
  //   try(Connection con = DB.sql2o.open()) {
  //     Integer newCourseId = newCourse.getCourseId();
  //     String sql = "UPDATE courses_students SET course_id = :course_id WHERE student_id = :student_id";
  //     con.createQuery(sql)
  //       .addParameter("course_id", newCourseId)
  //       .addParameter("student_id", student_id)
  //       .executeUpdate();
  //   }
  // }

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
