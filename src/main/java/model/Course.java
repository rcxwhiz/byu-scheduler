package model;

import model.id.CourseId;

import java.util.List;

public class Course
{
	private final CourseId courseId;
	private final String deptName;
	private final int catalogNumber;
	private final String catalogSuffix;
	private final String title;
	private final String fullTitle;
	private final double creditHours;
	private final String description;
	private final String effectiveDate;
	private final String expiredDate;
	private final String effectiveYearTerm;
	private final String expiredYearTerm;
	private final String honorsApproved;
	private final double labHours;
	private final double lectureHours;
	private final String note;
	private final String offered;
	private final String prerequisite;
	private final String recommended;
	private final String whenTaught;
	private final List<Section> sections;

	public Course(CourseId courseId,
	              String deptName,
	              int catalogNumber,
	              String catalogSuffix,
	              String title,
	              String fullTitle,
	              double creditHours,
	              String description,
	              String effectiveDate,
	              String expiredDate,
	              String effectiveYearTerm,
	              String expiredYearTerm,
	              String honorsApproved,
	              double labHours,
	              double lectureHours,
	              String note,
	              String offered,
	              String prerequisite,
	              String recommended,
	              String whenTaught,
	              List<Section> sections)
	{
		this.courseId = courseId;
		this.deptName = deptName;
		this.catalogNumber = catalogNumber;
		this.catalogSuffix = catalogSuffix;
		this.title = title;
		this.fullTitle = fullTitle;
		this.creditHours = creditHours;
		this.description = description;
		this.effectiveDate = effectiveDate;
		this.expiredDate = expiredDate;
		this.effectiveYearTerm = effectiveYearTerm;
		this.expiredYearTerm = expiredYearTerm;
		this.honorsApproved = honorsApproved;
		this.labHours = labHours;
		this.lectureHours = lectureHours;
		this.note = note;
		this.offered = offered;
		this.prerequisite = prerequisite;
		this.recommended = recommended;
		this.whenTaught = whenTaught;
		this.sections = sections;
	}

	public CourseId getCourseId()
	{
		return courseId;
	}

	public String getDeptName()
	{
		return deptName;
	}

	public int getCatalogNumber()
	{
		return catalogNumber;
	}

	public String getCatalogSuffix()
	{
		return catalogSuffix;
	}

	public String getTitle()
	{
		return title;
	}

	public String getFullTitle()
	{
		return fullTitle;
	}

	public double getCreditHours()
	{
		return creditHours;
	}

	public String getDescription()
	{
		return description;
	}

	public String getEffectiveDate()
	{
		return effectiveDate;
	}

	public String getExpiredDate()
	{
		return expiredDate;
	}

	public String getEffectiveYearTerm()
	{
		return effectiveYearTerm;
	}

	public String getExpiredYearTerm()
	{
		return expiredYearTerm;
	}

	public String getHonorsApproved()
	{
		return honorsApproved;
	}

	public double getLabHours()
	{
		return labHours;
	}

	public double getLectureHours()
	{
		return lectureHours;
	}

	public String getNote()
	{
		return note;
	}

	public String getOffered()
	{
		return offered;
	}

	public String getPrerequisite()
	{
		return prerequisite;
	}

	public String getRecommended()
	{
		return recommended;
	}

	public String getWhenTaught()
	{
		return whenTaught;
	}

	public List<Section> getSections()
	{
		return sections;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Course course = (Course) o;
		return courseId.equals(course.courseId);
	}
}
