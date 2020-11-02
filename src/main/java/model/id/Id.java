package model.id;

abstract class Id
{
	abstract boolean idEquals(Object o);

	@Override
	public boolean equals(Object o)
	{
		return idEquals(o);
	}
}
