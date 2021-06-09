package compressor;

public class EncodeType {
	private final String value;
	private final Integer frequency;
	private String encode;
	
	public EncodeType (String value, Integer frequency, String encode) {
		this.value = value;
		this.frequency = frequency;
		this.encode = encode;
	}
	
	public EncodeType (String value, Integer frequency) {
		this(value, frequency, null);
	}
	
	//Setter method
	public void setEncode(String encode) { this.encode = encode; }
	
	//Getter method
	public String getValue() { return value; }
	public Integer getFrequency() { return frequency; }
	public String getEncode() { return encode; }
	
	@Override
	public String toString() { return "\nvalue: " + value + " freq: " + frequency + " encode: " + encode; }
}
