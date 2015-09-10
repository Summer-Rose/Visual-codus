/**
 * Student.java provides CRUD functionality for Student objects
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

  public static Student find(Integer student_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM students where student_id=:student_id";
      Student student = con.createQuery(sql)
        .addParameter("student_id", student_id)
        .executeAndFetchFirst(Student.class);
      return student;
    }
  }

  public static List<Integer> getUniqueAges() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT DISTINCT age FROM students ORDER BY age";
      List<Integer> ages = con.createQuery(sql)
        .executeAndFetch(Integer.class);
      return ages;
    }
  }

  public static HashMap<String, String> getSalaryRanges() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT salary_before FROM students ORDER BY salary_before";
      List<Integer> salaries = con.createQuery(sql)
        .executeAndFetch(Integer.class);

      HashMap<String, String> salaryRanges = new HashMap<String, String>();

      Integer students010 = 0;
      Integer students1020 = 0;
      Integer students2030 = 0;
      Integer students3040 = 0;
      Integer students4050 = 0;
      Integer students5060 = 0;
      Integer students6070 = 0;
      Integer students7080 = 0;
      Integer students8090 = 0;
      Integer students90plus = 0;

      for(Integer salary : salaries) {
        if (salary < 10000) {
          students010++;
        } else if (salary >= 10000 && salary < 20000) {
          students1020++;
        } else if (salary >= 20000 && salary < 30000) {
          students2030++;
        } else if (salary >= 30000 && salary < 40000) {
          students3040++;
        } else if (salary >= 40000 && salary < 50000) {
          students4050++;
        } else if (salary >= 50000 && salary < 60000) {
          students5060++;
        } else if (salary >= 60000 && salary < 70000) {
          students6070++;
        } else if (salary >= 70000 && salary < 80000) {
          students7080++;
        } else if (salary >= 80000 && salary < 90000) {
          students8090++;
        } else {
          students90plus++;
        }
      }

      salaryRanges.put("salary010", String div010 = String.format("<div style=\"height: 20px; width: %d%%; background-color: red\"><p>$0-10,000</p></div>", students010 * 100 / Student.all().size()));
      salaryRanges.put("salary1020", String div1020 = String.format("<div style=\"height: 20px; width: %d%%; background-color: red\"><p>$10- 20,000</p></div>", students1020 * 100 / Student.all().size()));
      salaryRanges.put("salary2030", String div2030 = String.format("<div style=\"height: 20px; width: %d%%; background-color: red\"><p>$20-30,000</p></div>", students2030 * 100 / Student.all().size()));
      salaryRanges.put("salary3040", String div3040 = String.format("<div style=\"height: 20px; width: %d%%; background-color: red\"><p>$30-40,000</p></div>", students3040 * 100 / Student.all().size()));
      salaryRanges.put("salary4050", String div4050 = String.format("<div style=\"height: 20px; width: %d%%; background-color: red\"><p>$40-50,000</p></div>", students4050 * 100 / Student.all().size()));
      salaryRanges.put("salary5060", String div5060 = String.format("<div style=\"height: 20px; width: %d%%; background-color: red\"><p>$50-60,000</p></div>", students5060 * 100 / Student.all().size()));
      salaryRanges.put("salary6070", String div6070 = String.format("<div style=\"height: 20px; width: %d%%; background-color: red\"><p>$60-70,000</p></div>", students6070 * 100 / Student.all().size()));
      salaryRanges.put("salary7080", String div7080 = String.format("<div style=\"height: 20px; width: %d%%; background-color: red\"><p>$70-80,000</p></div>", students7080 * 100 / Student.all().size()));
      salaryRanges.put("salary8090", String div8090 = String.format("<div style=\"height: 20px; width: %d%%; background-color: red\"><p>$80-90,000</p></div>", students8090 * 100 / Student.all().size()));
      salaryRanges.put("salary90plus", String div90plus = String.format("<div style=\"height: 20px; width: %d%%; background-color: red\"><p>$90,000+</p></div>", students90plus * 100 / Student.all().size()));
      return salaryRanges;
    }
  }

  public Student youngest(){
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT age FROM students ORDER BY age";
      return con.createQuery(sql)
      .executeAndFetchFirst(Student.class);
    }
  }

  public Student oldest(){
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT age FROM students ORDER BY age DESC";
      return con.createQuery(sql)
      .executeAndFetchFirst(Student.class);
    }
  }

  // public String getPercentage(int age) {
  //   try (Connection con = DB.sql2o.open()) {
  //     String sql = "SELECT * FROM students WHERE age = :age";
  //     List<Student> students = con.createQuery(sql)
  //       .addParameter("age", age)
  //       .executeAndFetch(Student.class);
  //
  //       Integer percentage = students.size() * 100 / Student.all().size();
  //       String htmlString = String.format("<div style=\"height: 10px; width: %d%%; background-color: green\"></div>", percentage);
  //       return htmlString;
  //     }
  // }

  public static List<String> studentsByAge(List<Integer> ages){
    List<String> divStrings = new ArrayList<String>();
    for (Integer age : ages) {
      try (Connection con = DB.sql2o.open()) {
        String sql = "SELECT * FROM students WHERE age = :age";
        List<Student> students = con.createQuery(sql)
          .addParameter("age", age)
          .executeAndFetch(Student.class);

        Integer percentage = students.size() * 100 / Student.all().size();
        String htmlString = String.format("<div style=\"height: 20px; width: %d%%; background-color: green\"><p class=\"age\">%d</p></div>", percentage, age);
        divStrings.add(htmlString);
      }
    }
    return divStrings;
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

  public void addCourse(Course course) {
  try (Connection con = DB.sql2o.open()) {
    String sql = "UPDATE students SET course_id=:course_id WHERE student_id=:student_id";
    con.createQuery(sql)
      .addParameter("course_id", course.getCourseId())
      .addParameter("student_id", this.getStudentId())
      .executeUpdate();
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
}
