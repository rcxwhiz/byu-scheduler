package model;

import model.id.CourseId;

import java.util.HashMap;
import java.util.Map;

public class Semester
{
	// TODO could maybe add a map with depts and their courses?

	private final SemesterYear semesterYear;
	private final Map<CourseId, Course> courses;

	public Semester(SemesterYear semesterYear, Map<CourseId, Course> courses)
	{
		this.semesterYear = semesterYear;
		this.courses = courses;
	}

	public Semester(SemesterYear semesterYear)
	{
		this.semesterYear = semesterYear;
		this.courses = new HashMap<>();
	}

	public SemesterYear getSemesterYear()
	{
		return semesterYear;
	}

	public Map<CourseId, Course> getCourses()
	{
		return courses;
	}
}
