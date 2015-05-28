package liquibase.ext.db2i.limits;

/**
 * The System i has various limits for lengths of names and so on. Some of them
 * apply to the database. This class helps to enforce them. 
 * 
 * @author Martin McCallion <martin@devilgate.org>
 * 
 */
public class LengthLimiter {
	
	private static final String ELLIPSIS = "...";

	public enum Length {
		TABLE_COMMENT(50),
		COLUMN_COMMENT(60)
		;
		
		private int length;
		
		private Length(int length) {
			this.length = length;
		}
		
		public int getLength() {
				return length;
		}
	}
	
	/**
	 * If the received String is longer than the limit length defined by the 
	 * received {@code Length} instance, the returned value is the original 
	 * String up to three characters less than the maximum length, with an 
	 * ellipsis (...) appended to indicate that truncation has taken place.
	 * 
	 * Otherwise the original String is returned.
	 * 
	 * @param input
	 * @param length
	 * @return
	 */
	public String truncate(String input, Length length) {
		if (input.length() > length.getLength()) {
			return input.substring(0, length.getLength() - 3) + ELLIPSIS;
		}
		else {
			return input;
		}
	}
}
