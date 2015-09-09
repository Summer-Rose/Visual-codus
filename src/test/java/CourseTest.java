import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

public class CourseTest {
  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Course.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfOriginsAretheSame() {
    Course firstCourse = new Course("Java");
    Course secondCourse = new Course("Java");
    assertTrue(firstCourse.equals(secondCourse));
  }

  @Test
  public void all_savesIntoDatabase_true() {
    Course myCourse = new Course("Java");
    myCourse.save();
    assertEquals(Course.all().get(0).getName(), "Java");
  }

  @Test
  public void find_findsCoursesInDatabase_true() {
    Course myCourse = new Course("Java");
    myCourse.save();
    Course savedCourse = Course.find(myCourse.getCourseId());
    assertEquals(savedCourse.getName(), "Java");
  }

  @Test
  public void update_updatesCourseInfo_true() {
    Course myCourse = new Course("Java");
    myCourse.save();
    myCourse.update("Android");
    assertEquals(Course.all().get(0).getName(), "Android");
  }

  @Test
  public void delete_deletesACourse_true() {
    Course firstCourse = new Course("Ruby");
    Course secondCourse = new Course("Java");
    firstCourse.save();
    secondCourse.save();
    secondCourse.delete();
    assertEquals(Course.all().size(), 1);
  }
}