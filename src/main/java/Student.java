/**
 * Student.java provides CRUD functionality for student objects
 *
 * @since 09-09-2015
 */

import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;
import java.util.HashMap;


public class Student {

  private Integer student_id;
  private Integer course_id;
  private Integer age;
  private String gender;
  private String origin;
  private Integer distance_traveled;
  private Integer salary_before;


  /**
   * CONSTRUCTOR AND GETTERS FOR EACH INSTANCE VARIABLE
   */

  public Student(Integer course_id, Integer age, String gender, String origin, Integer distance_traveled, Integer salary_before) {
    this.course_id = course_id;
    this.age = age;
    this.gender = gender;
    this.origin = origin;
    this.distance_traveled = distance_traveled;
    this.salary_before = salary_before;
  }

  public Integer getStudentId() {
    return student_id;
  }

  public Integer getCourseId() {
    return course_id;
  }

  public Integer getAge() {
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
  public boolean equals(Object otherStudent) {
    if (!(otherStudent instanceof Student)) {
      return false;
    } else {
      Student newStudent = (Student) otherStudent;
      return this.getOrigin().equals(newStudent.getOrigin()) &&
             this.getStudentId() == newStudent.getStudentId();
    }
  }


  /**
   * CRUD OPERATIONS FOR ADMIN
   */

  public void save() {
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO students (course_id, age, gender, origin, distance_traveled, salary_before) VALUES (:course_id, :age, :gender, :origin, :distance_traveled, :salary_before)";
    this.student_id = (Integer) con.createQuery(sql, true)
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

  public static Student find(Integer student_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM students where student_id=:student_id";
      Student student = con.createQuery(sql)
        .addParameter("student_id", student_id)
        .executeAndFetchFirst(Student.class);
      return student;
    }
  }

  public void update(Integer course_id, Integer age, String gender, String origin, Integer distance_traveled, Integer salary_before) {
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


  /**
   * HELPER METHOD FOR GRAPHICS: showing students by course
   */

  public static List<Student> allStudentsByCourse(Integer course_id) {
    String sql = "SELECT * FROM students WHERE course_id:=course_id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .addParameter("course_id", course_id)
      .executeAndFetch(Student.class);
    }
  }

  public static String getCourseNameByStudentId(int student_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT course_name FROM courses JOIN students USING (course_id) WHERE students.student_id=:student_id";
      String courseName = con.createQuery(sql)
        .addParameter("student_id", student_id)
        .executeAndFetchFirst(String.class);
      return courseName;
    }
  }


  /**
   * HELPER METHOD FOR GRAPHICS: distance_traveled graphic
   */

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


  /**
   * HELPER METHOD FOR GRAPHICS: age graphic
   */

   // FIXME: Make ArrayList similar to salary range
   public static List<String> studentsByAge(List<Integer> ages) {
     List<String> divStrings = new ArrayList<String>();
     for (Integer age : ages) {
       try (Connection con = DB.sql2o.open()) {
         String sql = "SELECT * FROM students WHERE age=:age";
         List<Student> students = con.createQuery(sql)
           .addParameter("age", age)
           .executeAndFetch(Student.class);

         Integer percentage = students.size() * 100 / Student.all().size();
         String htmlString = String.format("<div style=\"width: %d%%\" class=\"bar-style age-bar\"><p class=\"age\">%d</p></div>", percentage, age);
         divStrings.add(htmlString);
       }
     }
     return divStrings;
   }

  public static List<Integer> getUniqueAges() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT DISTINCT age FROM students ORDER BY age";
      List<Integer> ages = con.createQuery(sql)
        .executeAndFetch(Integer.class);
      return ages;
    }
  }

  public Student youngest() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT age FROM students ORDER BY age";
      return con.createQuery(sql)
      .executeAndFetchFirst(Student.class);
    }
  }

  public Student oldest() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT age FROM students ORDER BY age DESC";
      return con.createQuery(sql)
      .executeAndFetchFirst(Student.class);
    }
  }


  /**
   * HELPER METHOD FOR GRAPHICS: salary range graphic
   */

  public static HashMap<Integer, String> getSalaryRanges() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT salary_before FROM students ORDER BY salary_before";
      List<Integer> salaries = con.createQuery(sql)
        .executeAndFetch(Integer.class);

      HashMap<Integer, Integer> salaryRange = new HashMap<Integer, Integer>();
        //Initializing salaryRange keys with values set to 0
        for (Integer i = 0; i <= 9; i++) {
          salaryRange.put(i, 0);
        }
        for (Integer salary : salaries) {
          Integer key = salary/10000;
          salaryRange.put(key, salaryRange.get(key)+1);
          }
      HashMap<Integer, String> salaryDivs = new HashMap<Integer, String>();
        //Initializing salaryDiv keys and adding String div
        for (Integer i = 0; i <= 9; i++) {
          String div = String.format("<div style=\"width: %d%%;\" class=\"bar-style salary-bar\"></div>", salaryRange.get(i) * 100 / Student.all().size());
          salaryDivs.put(i, div);
        }
      return salaryDivs;
    }
  }


  /**
   * HELPER METHOD FOR GRAPHICS: gender graphic
   */

  public static List<String> getAllGenders() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT gender FROM students";
      List<String> allGenders = con.createQuery(sql)
        .executeAndFetch(String.class);
        return allGenders;
    }
  }

  public static List<String> displayGender(List<String> listGenders) {
    List<String> divStrings = new ArrayList<String>();
    Integer female = 0;
    Integer male = 0;
    Integer nonbinarytrans = 0;

    for (String gender : listGenders){
      if (gender.equals("F")) {
        female++;
      } else if (gender.equals("M")){
        male++;
      } else {
        nonbinarytrans++;
      }
    }
    String htmlStringF = String.format("<div class=\"innerdiv\" style=\"height: 20px; width: %d%%; background-color: #009688\"></div>", female * 100 / Student.all().size());
    divStrings.add(htmlStringF);
    String htmlStringM = String.format("<div class=\"innerdiv\" style=\"height: 20px; width: %d%%; background-color: #FF9800\"></div>", male * 100 / Student.all().size());
    divStrings.add(htmlStringM);
    String htmlStringNBT = String.format("<div class=\"innerdiv\" style=\"height: 20px; width: %d%%; background-color: #2196F3\"></div>", nonbinarytrans * 100 / Student.all().size());
    divStrings.add(htmlStringNBT);
    return divStrings;
  }
}
