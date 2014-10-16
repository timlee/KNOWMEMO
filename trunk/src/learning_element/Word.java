package learning_element;

public class Word
{
	private String ID;
	private String meanning;
	private String spelling;
	private String phonetic_alphabet;
	private String level;
	private String learned;
	private String remenber;

	public Word(String iD, String meanning, String spelling,
			String phonetic_alphabet, String level, String learned,
			String remenber)
	{
		super();
		ID = iD;
		this.meanning = meanning;
		this.spelling = spelling;
		this.phonetic_alphabet = phonetic_alphabet;
		this.level = level;
		this.learned = learned;
		this.remenber = remenber;
	}

	public Word()
	{
		
	}

	public String getID()
	{
		return ID;
	}

	public void setID(String iD)
	{
		ID = iD;
	}

	public String getMeanning()
	{
		return meanning;
	}

	public void setMeanning(String meanning)
	{
		this.meanning = meanning;
	}

	public String getSpelling()
	{
		return spelling;
	}

	public void setSpelling(String spelling)
	{
		this.spelling = spelling;
	}

	public String getPhonetic_alphabet()
	{
		return phonetic_alphabet;
	}

	public void setPhonetic_alphabet(String phonetic_alphabet)
	{
		this.phonetic_alphabet = phonetic_alphabet;
	}

	public String getLevel()
	{
		return level;
	}

	public void setLevel(String level)
	{
		this.level = level;
	}

	public String getLearned()
	{
		return learned;
	}

	public void setLearned(String learned)
	{
		this.learned = learned;
	}

	public String getRemenber()
	{
		return remenber;
	}

	public void setRemenber(String remenber)
	{
		this.remenber = remenber;
	}

}