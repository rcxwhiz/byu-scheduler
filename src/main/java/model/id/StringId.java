package model.id;

import java.util.Objects;

public abstract class StringId implements Id
{
	private final String value;

	public StringId(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	@Override
	public boolean idEquals(Object o)
	{
		return equals(o);
	}

	@Override
	public String toString()
	{
		return value;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StringId stringId = (StringId) o;
		return value.equals(stringId.value);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(value);
	}
}
