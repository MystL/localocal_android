package my.localocal.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

/**
 * Data manipulation utilities
 * @copyright RadioActive PTE Ltd, 2009
 * @author yohan.launay@gmail.com
 */
public class DataUtils{

	//------------
	// Serialization
	//------------

	/**
	 * Convert Object to Bytes
	 * @param obj
	 * @return bytes or null if error
	 */
	public static byte[] objectToBytes(Object obj) {
		if (obj == null)
			return new byte[0];

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			bos.close();
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	/**
	 * Convert Bytes to Object
	 * @param bytes
	 * @return Object or null if error
	 */
	public static Object bytesToObject(byte[] bytes) {
		if (bytes.length == 0)
			return null;
		Object object = null;
		try {
			object = new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}

	public static String serialize(Serializable obj) {
		if (obj == null)
			return "";
		byte[] b = objectToBytes(obj);
		return b == null ? "" : new String(Base64Coder.encode(b));
	}

	public static Object deserialize(String serializedObject) {
		if (serializedObject == null)
			return null;

		try {
			byte[] b = Base64Coder.decode(serializedObject);
			return b != null ? bytesToObject(b) : null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public static String UTF8UrlEncode(String value) {
		try {
			return URLEncoder.encode(value, "UTF8");
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String serializedObject, T valueIfNull) {
		T obj = (T) deserialize(serializedObject);
		return obj == null ? valueIfNull : null;
	}

	//------------
	// Primitive Data Types parsing and parameters extraction
	//------------

	/**
	 * Returns 0 in case of error
	 */
	public static long getLongValue(Object o) {
		try {
			return getLongValue(o, null);
		} catch (Exception e) {
			return 0;
		}
	}

	public static <T extends Exception> long getLongValue(Object o, T throwIfParsingError) throws T {
		long value = 0L;
		if (o == null) {
			if (throwIfParsingError != null)
				throw throwIfParsingError;
		} else {
			Class<?> c = o.getClass();
			if (Long.class.equals(c)) {
				value = ((Long) o).longValue();
			} else if (Integer.class.equals(c)) {
				value = ((Integer) o).intValue();
			} else if (String.class.equals(c)) {
				try {
					value = Long.parseLong((String) o);
				} catch (Exception e) {
					throw throwIfParsingError;
				}
			}
		}
		return value;
	}

	/**
	 * Returns 0 in case of error
	 */
	public static int getIntValue(Object o) {
		try {
			return getIntValue(o, null);
		} catch (Exception e) {
			return 0;
		}
	}

	public static <T extends Exception> int getIntValue(Object o, T throwIfParsingError) throws T {
		return (int) getLongValue(o, throwIfParsingError);
	}

	/**
	 * Returns 0 in case of error
	 */
	public static float getFloatValue(Object o) {
		try {
			return getFloatValue(o, null);
		} catch (Exception e) {
			return 0;
		}
	}

	public static <T extends Exception> float getFloatValue(Object o, T throwIfParsingError) throws T {
		float value = 0f;
		if (o == null) {
			if (throwIfParsingError != null)
				throw throwIfParsingError;
		} else {
			Class<?> c = o.getClass();
			if (Float.class.equals(c)) {
				value = ((Float) o).floatValue();
			} else if (Integer.class.equals(c)) {
				value = ((Integer) o).intValue();
			} else if (Long.class.equals(c)) {
				value = ((Long) o).longValue();
			} else if (Double.class.equals(c)) {
				value = (float) ((Double) o).doubleValue();
			} else if (String.class.equals(c)) {
				try {
					value = Float.parseFloat((String) o);
				} catch (Exception e) {
					throw throwIfParsingError;
				}
			}
		}
		return value;
	}

	/**
	 * Returns 0 in case of error
	 */
	public static double getDoubleValue(Object o) {
		try {
			return getDoubleValue(o, null);
		} catch (Exception e) {
			return 0;
		}
	}

	public static <T extends Exception> double getDoubleValue(Object o, T throwIfParsingError) throws T {
		double value = 0;
		if (o == null) {
			if (throwIfParsingError != null)
				throw throwIfParsingError;
		} else {
			Class<?> c = o.getClass();
			if (Double.class.equals(c)) {
				value = ((Double) o).doubleValue();
			} else if (Float.class.equals(c)) {
				value = ((Float) o).floatValue();
			} else if (Integer.class.equals(c)) {
				value = ((Integer) o).intValue();
			} else if (Long.class.equals(c)) {
				value = ((Long) o).longValue();
			} else if (String.class.equals(c)) {
				try {
					value = Double.parseDouble((String) o);
				} catch (Exception e) {
					throw throwIfParsingError;
				}
			}
		}
		return value;
	}

	/**
	 * Returns false in case of error
	 */
	public static boolean getBoolValue(Object o) {
		try {
			return getBoolValue(o, null);
		} catch (Exception e) {
			return false;
		}
	}

	public static <T extends Exception> boolean getBoolValue(Object o, T throwIfParsingError) throws T {
		boolean value = false;
		if (o != null) {
			Class<?> c = o.getClass();
			if (Boolean.class.equals(c)) {
				value = ((Boolean) o).booleanValue();
			} else if (Integer.class.equals(c)) {
				value = ((Integer) o).intValue() == 1;
			} else if (Long.class.equals(c)) {
				value = ((Long) o).longValue() == 1;
			} else if (String.class.equals(c)) {
				boolean isTrue = "1".equals(o) || "true".equalsIgnoreCase((String) o) || "yes".equalsIgnoreCase((String) o);
				boolean isFalse = "0".equals(o) || "false".equalsIgnoreCase((String) o) || "no".equalsIgnoreCase((String) o);

				if (!isTrue && !isFalse)
					throw throwIfParsingError;

				value = isTrue;
			}
		}
		return value;
	}

	/**
	 * @return string value of String, Text or object.toString
	 */
	public static String getStringValue(Object object) {
		if (object == null)
			return null;
		if (object instanceof String)
			return ((String) object);
		return object.toString();
	}

	@SuppressWarnings("unchecked")
	public static <T> T cast(T type, Object obj) {
		return obj != null && type.getClass().isAssignableFrom(obj.getClass()) ? (T) obj : null;
	}

	public static Random random() {
		return random.get();
	}

	public static int randomInt() {
		return random().nextInt();
	}

	public static int randomInt(int max) {
		return max <= 0 ? 0 : random().nextInt(max);
	}

	public static long randomLong() {
		return random().nextLong();
	}

	public static boolean randomBool() {
		return random().nextBoolean();
	}

	public static float randomFloat() {
		return random().nextFloat();
	}

	public static double randomDouble() {
		return random().nextDouble();
	}

	public static void randomBytes(byte[] bytes) {
		if (bytes != null) {
			random().nextBytes(bytes);
		}
	}

	public static String randomString() {
		return Long.toString(randomLong(), 36);
	}

	public static String randomString(int size) {
		String r = randomUID();
		while (r.length() < size) {
			r = r + "-" + randomUID();
		}
		if (r.length() > size) {
			r = r.substring(0, size);
		}
		return r;
	}

	public static String randomUID() {
		return Thread.currentThread().getId() + "-" + System.currentTimeMillis() + "-" + randomString();
	}

	public static String randomUID(String prefix) {
		return prefix + "-" + Thread.currentThread().getId() + "-" + System.currentTimeMillis() + "-" + randomInt();
	}

	/**
	 * Thread-specific random number generators. Each is seeded with the thread
	 * ID, so the sequence of pseudo-random numbers are unique between threads.
	 */
	private static ThreadLocal<Random>	random	= new ThreadLocal<Random>(){
		                                           @Override
		                                           protected Random initialValue() {
			                                           return new Random(System.currentTimeMillis() + Thread.currentThread().getId() * (24 * 60 * 60 * 1000));
		                                           }
	                                           };

}
