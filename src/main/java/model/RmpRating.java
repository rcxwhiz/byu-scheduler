package model;

public class RmpRating
{
	private final boolean valid;
	private final double avgRating;
	private final double avgHelpful;
	private final int numRatings;
	private final double avgDifficulty;
	private final double avgClarity;

	public RmpRating(double avgRating, double avgHelpful, int numRatings, double avgDifficulty, double avgClarity)
	{
		this.valid = true;
		this.avgRating = avgRating;
		this.avgHelpful = avgHelpful;
		this.numRatings = numRatings;
		this.avgDifficulty = avgDifficulty;
		this.avgClarity = avgClarity;
	}

	public RmpRating()
	{
		this.valid = false;
		this.avgRating = 0;
		this.avgHelpful = 0;
		this.numRatings = 0;
		this.avgDifficulty = 0;
		this.avgClarity = 0;
	}

	public boolean isValid()
	{
		return valid;
	}

	public double getAvgRating()
	{
		return avgRating;
	}

	public double getAvgHelpful()
	{
		return avgHelpful;
	}

	public int getNumRatings()
	{
		return numRatings;
	}

	public double getAvgDifficulty()
	{
		return avgDifficulty;
	}

	public double getAvgClarity()
	{
		return avgClarity;
	}
}
