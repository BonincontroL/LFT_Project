public class NumberTok extends Token {
	public static final int tag=256;
	int value;
	public NumberTok(int v) {
		super(tag);
		value=v;
	}
	public String toString() {
		return "<" + tag + ", " + value + ">";
	}
}
