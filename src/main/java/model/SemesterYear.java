package model;

import java.util.HashMap;
import java.util.Map;

public class SemesterYear
{
	enum Semesters
	{
		WINTER, SPRING, SUMMER, FALL;
	}

	private static final Map<Semesters, Integer> semesterKey = new HashMap<>()
	{{
		put(Semesters.WINTER, 1);
		put(Semesters.SPRING, 3);
		put(Semesters.SUMMER, 4);
		put(Semesters.FALL, 5);
	}};

	public static int getSemesterKey(String semester)
	{
		switch (semester.toLowerCase())
		{
			case "winter" -> {
				return semesterKey.get(Semesters.WINTER);
			}
			case "spring" -> {
				return semesterKey.get(Semesters.SPRING);
			}
			case "summer" -> {
				return semesterKey.get(Semesters.SUMMER);
			}
			case "fall" -> {
				return semesterKey.get(Semesters.FALL);
			}
			default -> throw new RuntimeException("Semester not found: " + semester);
		}
	}

	public static int getSemesterKey(Semesters semester)
	{
		return semesterKey.get(semester);
	}

	private final int semester;
	private final int year;

	public SemesterYear(int semester, int year)
	{
		this.semester = semester;
		this.year = year;
	}

	public SemesterYear(String semester, int year)
	{
		this.semester = getSemesterKey(semester);
		this.year = year;
	}

	public int getSemester()
	{
		return semester;
	}

	public int getYear()
	{
		return year;
	}

	public String getYearTerm()
	{
		return Integer.toString(year) + Integer.toString(semester);
	}
}
