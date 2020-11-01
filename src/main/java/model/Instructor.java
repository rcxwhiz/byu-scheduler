package model;

import model.id.ByuId;
import model.id.NetId;
import model.id.PersonId;

public class Instructor
{
	private final PersonId personId;
	private final ByuId byuId;
	private final NetId netId;
	private final String firstName;
	private final String lastName;
	private final String sortName;
	private final String preferredFirstName;
	private final String restOfName;
	private final String surname;
	private final String phoneNumber;
	private final RmpRating rmpRating;

	public Instructor(PersonId personId,
	                  ByuId byuId,
	                  NetId netId,
	                  String firstName,
	                  String lastName,
	                  String sortName,
	                  String preferredFirstName,
	                  String restOfName,
	                  String surname,
	                  String phoneNumber,
	                  RmpRating rmpRating)
	{
		this.personId = personId;
		this.byuId = byuId;
		this.netId = netId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.sortName = sortName;
		this.preferredFirstName = preferredFirstName;
		this.restOfName = restOfName;
		this.surname = surname;
		this.phoneNumber = phoneNumber;
		this.rmpRating = rmpRating;
	}

	public PersonId getPersonId()
	{
		return personId;
	}

	public ByuId getByuId()
	{
		return byuId;
	}

	public NetId getNetId()
	{
		return netId;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getSortName()
	{
		return sortName;
	}

	public String getPreferredFirstName()
	{
		return preferredFirstName;
	}

	public String getRestOfName()
	{
		return restOfName;
	}

	public String getSurname()
	{
		return surname;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public boolean hasRmpRating()
	{
		return rmpRating.isValid();
	}

	public double getAvgRating()
	{
		return rmpRating.getAvgRating();
	}

	public double getAvgHelpful()
	{
		return rmpRating.getAvgHelpful();
	}

	public int getNumRatings()
	{
		return rmpRating.getNumRatings();
	}

	public double getAvgDifficulty()
	{
		return rmpRating.getAvgDifficulty();
	}

	public double getAvgClarity()
	{
		return rmpRating.getAvgClarity();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Instructor instructor = (Instructor) o;
		return personId.equals(instructor.personId);
	}
}
