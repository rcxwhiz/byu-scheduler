package model.id;

import java.util.Objects;

public class CourseId implements Id
{
	private final CurriculumId curriculumId;
	private final TitleCode titleCode;

	public CourseId(CurriculumId curriculumId, TitleCode titleCode)
	{
		this.curriculumId = curriculumId;
		this.titleCode = titleCode;
	}

	public CurriculumId getCurriculumId()
	{
		return curriculumId;
	}

	public TitleCode getTitleCode()
	{
		return titleCode;
	}

	@Override
	public boolean idEquals(Object o)
	{
		return equals(o);
	}

	@Override
	public String toString()
	{
		return curriculumId.toString() + '-' + titleCode.toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CourseId courseId = (CourseId) o;
		return curriculumId.equals(courseId.getCurriculumId()) && titleCode.equals(courseId.getTitleCode());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(curriculumId, titleCode);
	}
}
