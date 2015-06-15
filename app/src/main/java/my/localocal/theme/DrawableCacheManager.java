package my.localocal.theme;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;

/**
 * Class for managing caching of drawables. Modified from ImageDownloader.java
 * (http://developer.android.com/resources/samples/XmlAdapters/src/com/example/
 * android/xmladapters/ImageDownloader.html)
 * 
 * @author Fulbert KIV According to hearsay on the net, performance of
 *         SoftReference in Android is not satisfactory, LruCache is recommended
 *         for caching instead but the way this code is implemented, the
 *         hard-cache seems to already be implementing what LruCache does.
 */
public class DrawableCacheManager{
	private static final int	                                    HARD_CACHE_CAPACITY	= 30;
	private static final int	                                    DELAY_BEFORE_PURGE	= 30 * 1000;	                                                                   // in milliseconds

	// Hard cache, with a fixed maximum capacity and a life duration
	private final static HashMap<String, Drawable>	                sHardBitmapCache	= new LinkedHashMap<String, Drawable>(HARD_CACHE_CAPACITY / 2, 0.75f, true){
		                                                                                    private static final long	serialVersionUID	= -7190622541619388252L;

		                                                                                    @Override
		                                                                                    protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Drawable> eldest) {
			                                                                                    if (size() > HARD_CACHE_CAPACITY) {
				                                                                                    // Entries push-out of hard reference cache are transferred to
				                                                                                    // soft reference cache
				                                                                                    sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Drawable>(eldest.getValue()));
				                                                                                    return true;
			                                                                                    }

			                                                                                    return false;
		                                                                                    }
	                                                                                    };

	// Soft cache for bitmap kicked out of hard cache, package-private default
	// accessor
	final static ConcurrentHashMap<String, SoftReference<Drawable>>	sSoftBitmapCache	= new ConcurrentHashMap<String, SoftReference<Drawable>>(HARD_CACHE_CAPACITY / 2);

	private final static Handler	                                purgeHandler	    = new Handler();

	private final static Runnable	                                purger	            = new Runnable(){
		                                                                                    @Override
		                                                                                    public void run() {
			                                                                                    clearCache();
		                                                                                    }
	                                                                                    };

	// private constructor to avoid default public constructor from being
	// constructed for this static class
	private DrawableCacheManager() {}

	public static void addToCache(String key, Drawable imageDrawable) {
		resetPurgeTimer();

		// Add bitmap to cache
		if (imageDrawable != null) {
			synchronized (sHardBitmapCache) {
				sHardBitmapCache.put(key, imageDrawable);
			}
		}
	}

	public static void addToSoftCache(String key, Drawable imageDrawable) {
		// Add bitmap to soft cache
		if (imageDrawable != null) {
			synchronized (sSoftBitmapCache) {
				sSoftBitmapCache.put(key, new SoftReference<Drawable>(imageDrawable));
			}
		}
	}

	/**
	 * Clears the image cache used internally to improve performance. Note that
	 * for memory efficiency reasons, the cache will automatically be cleared
	 * after a certain inactivity delay.
	 */
	public static void clearCache() {
		sHardBitmapCache.clear();
		sSoftBitmapCache.clear();
		// System.gc();
	}

	private static void resetPurgeTimer() {
		purgeHandler.removeCallbacks(purger);
		purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
	}

	/**
	 * @param key
	 *            The key of the image that will be retrieved from the cache.
	 * @return The cached bitmap or null if it was not found.
	 */
	public static Drawable getDrawableFromCache(String key) {
		resetPurgeTimer();

		// First try the hard reference cache
		synchronized (sHardBitmapCache) {
			final Drawable imageDrawable = sHardBitmapCache.get(key);
			if (imageDrawable != null) {
				// Bitmap found in hard cache
				// Move element to first position, so that it is removed last
				sHardBitmapCache.remove(key);
				sHardBitmapCache.put(key, imageDrawable);
				return imageDrawable;
			}
		}

		// Then try the soft reference cache
		SoftReference<Drawable> bitmapReference = sSoftBitmapCache.get(key);
		if (bitmapReference != null) {
			final Drawable imageDrawable = bitmapReference.get();
			if (imageDrawable != null)
				// Bitmap found in soft cache
				return imageDrawable;
			// Soft reference has been Garbage Collected
			sSoftBitmapCache.remove(key);
		}
		return null;
	}
}
