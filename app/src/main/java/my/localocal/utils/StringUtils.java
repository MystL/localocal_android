package my.localocal.utils;

public class StringUtils{

	/**
	 * @param input
	 * @return true if input is null or empty
	 */
	public static boolean IsNullOrEmpty(String... input) {
		if (input.length == 0)
			return (input[0] == null || IsEmpty(input[0]));
		for (String in : input) {
			if (in == null || IsEmpty(in))
				return true;
		}
		return false;
	}

	/**
	 * @param input
	 * @return true if empty is not null and empty
	 */
	public static boolean IsEmpty(String input) {
		return input != null && ("".equals(input) || input.length() == 0);
	}

}
