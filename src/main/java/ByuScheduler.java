import api.ByuApi;
import model.Semester;
import model.SemesterYear;

public class ByuScheduler
{
	public static void main(String[] args)
	{
		System.out.println("hello");
		Semester semester = ByuApi.getSemester(new SemesterYear(1, 2020));
		System.out.println("bye");
	}
}
