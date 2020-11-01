package model;

public class Meeting
{
	private final int beginTime;
	private final int endTime;
	private final String building;
	private final String room;
	private final boolean sun;
	private final boolean mon;
	private final boolean tue;
	private final boolean wed;
	private final boolean thu;
	private final boolean fri;
	private final boolean sat;
	private final int sequenceNum;

	public Meeting(int beginTime,
	               int endTime,
	               String building,
	               String room,
	               boolean sun,
	               boolean mon,
	               boolean tue,
	               boolean wed,
	               boolean thu,
	               boolean fri,
	               boolean sat,
	               int sequenceNum)
	{
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.building = building;
		this.room = room;
		this.sun = sun;
		this.mon = mon;
		this.tue = tue;
		this.wed = wed;
		this.thu = thu;
		this.fri = fri;
		this.sat = sat;
		this.sequenceNum = sequenceNum;
	}

	public int getBeginTime()
	{
		return beginTime;
	}

	public int getEndTime()
	{
		return endTime;
	}

	public String getBuilding()
	{
		return building;
	}

	public String getRoom()
	{
		return room;
	}

	public boolean isSun()
	{
		return sun;
	}

	public boolean isMon()
	{
		return mon;
	}

	public boolean isTue()
	{
		return tue;
	}

	public boolean isWed()
	{
		return wed;
	}

	public boolean isThu()
	{
		return thu;
	}

	public boolean isFri()
	{
		return fri;
	}

	public boolean isSat()
	{
		return sat;
	}

	public int getSequenceNum()
	{
		return sequenceNum;
	}
}
