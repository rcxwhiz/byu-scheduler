package model.id;

import java.util.Objects;

public  abstract class NumericId extends Id
{
	private final int value;

	public NumericId(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	@Override
	public boolean idEquals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NumericId numericId = (NumericId) o;
		return value == numericId.value;
	}

	@Override
	public String toString()
	{
		return Integer.toString(value);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(value);
	}
}
