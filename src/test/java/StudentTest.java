import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

public class StudentTest {
  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Student.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfOriginsAretheSame() {
    Student firstStudent = new Student(1, 28, "Female", "Portland", 0, 20000);
    Student secondStudent = new Student(1, 28, "Female", "Portland", 0, 20000);
    assertTrue(firstStudent.equals(secondStudent));
  }

  @Test
  public void all_savesIntoDatabase_true() {
    Student myStudent = new Student(1, 28, "Female", "Portland", 0, 20000);
    myStudent.save();
    assertEquals(Student.all().get(0).getOrigin(), "Portland");
  }

  @Test
  public void find_findsStudentsInDatabase_true() {
    Student myStudent = new Student(1, 28, "Female", "Portland", 0, 20000);
    myStudent.save();
    Student savedStudent = Student.find(myStudent.getStudentId());
    assertEquals(savedStudent.getOrigin(), "Portland");
  }

  @Test
  public void update_updatesStudentInfo_true() {
    Student myStudent = new Student(1, 28, "Female", "Portland", 0, 20000);
    myStudent.save();
    myStudent.update(1, 28, "Female", "Sao Paolo", 0, 20000);
    assertEquals(Student.all().get(0).getOrigin(), "Sao Paolo");
  }

  @Test
  public void delete_deletesAStudent_true() {
    Student firstStudent = new Student(1, 20, "Male", "Eugene", 0, 10000);
    Student secondStudent = new Student(1, 28, "Female", "Portland", 0, 20000);
    firstStudent.save();
    secondStudent.save();
    secondStudent.delete();
    assertEquals(Student.all().size(), 1);
  }
}