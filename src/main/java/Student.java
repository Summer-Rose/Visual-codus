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

  public static Student find(int student_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM students where student_id=:student_id";
      Student student = con.createQuery(sql)
        .addParameter("student_id", student_id)
        .executeAndFetchFirst(Student.class);
      return student;
    }
  }

  public void update(int course_id, int age, String gender, String origin, int distance_traveled, int salary_before) {
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

  /** getUniqueOriginsDistances returns a list of unique origins and distance_traveled values, sorted by distances_traveled in ascending order */
  public static List<Student> getUniqueOriginsDistances() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT DISTINCT origin, distance_traveled FROM students ORDER BY distance_traveled";
      List<Student> originsDistances = con.createQuery(sql)
        .executeAndFetch(Student.class);
      return originsDistances;
    }
  }

  /** stringDistancesSVG creates and returns the string format of svg elements needed to draw the distance map */
  public static List<String> stringDistancesSVG(Integer windowHeight, Integer windowWidth, String lineColor, String pointColor, Integer pointRadius) {
    // List of svg elements to draw in string format
    List<String> distanceSVGList = new ArrayList<String>();

    // Vertically center elements within svg window height; all elements share the same fixed y-coordinate
    Integer yFixed = windowHeight/2;

    // Draw a line that is 80% of the window width
    double maxLength = windowWidth * .8;
    double xShift = (windowWidth - maxLength) / 2;
    String line = String.format("<line x1=0 y1=%d x2=%f y2=%d style=\"stroke:%s\" transform=\"translate(%f)\"/>", yFixed, maxLength, yFixed, lineColor, xShift);
    distanceSVGList.add(line);

    // Save farthestDistance into a variable
    List<Student> sortedData = getUniqueOriginsDistances();
    Integer farthestDistance = sortedData.get(sortedData.size() - 1).getDistanceTraveled();
    
    // Draw points along the line corresponding to relative proportional distance from Portland (origin)
    for (int index=0; index<sortedData.size(); index++) {
      Integer distance = sortedData.get(index).getDistanceTraveled();
      double xCoordinate = (double) distance * maxLength / farthestDistance;
      String point = String.format("<circle cx=%f cy=%d r=%d fill=\"%s\" transform=\"translate(%f)\" onmouseover=\'document.getElementById(\"%d\").style.opacity=\"1\"\' onmouseout=\'document.getElementById(\"%d\").style.opacity=\"0.3\"\'/>", xCoordinate, yFixed, pointRadius, pointColor, xShift, index, index);
      distanceSVGList.add(point);
    }
    return distanceSVGList;
  }

  public static List<String> stringOrigins() {
    // List of origins in string format
    List<String> originList = new ArrayList<String>();

    List<Student> sortedData = getUniqueOriginsDistances();
    for (int index=0; index<sortedData.size(); index++) {
      String origin = String.format("<span id=\"%d\" style=\"opacity:0.3\">%s</span>", index, sortedData.get(index).getOrigin());
      originList.add(origin);
    }
    return originList;
  }




}
