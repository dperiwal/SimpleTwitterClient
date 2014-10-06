package com.codepath.apps.basictwitter;

import android.content.Context;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.codepath.apps.basictwitter.persistence.AppSpecificJDXSetup;
import com.codepath.apps.basictwitter.persistence.JDXPersistenceManagerImpl;
import com.codepath.apps.basictwitter.persistence.PersistenceManager;
import com.codepath.apps.basictwitter.rest.TwitterClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.softwaretree.jdxandroid.JDXSetup;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 * 
 *     RestClient client = RestClientApp.getRestClient();
 *     // use client to send requests to API
 *     
 */
public class TwitterApplication extends com.activeandroid.app.Application {
	public static final boolean USE_ACTIVE_ANDROID = false;
	private static Context context;
	private static PersistenceManager persistenceManager;

	@Override
	public void onCreate() {
		super.onCreate();
		TwitterApplication.context = this;

		// Create global configuration and initialize ImageLoader with this configuration
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc().build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(
				defaultOptions).build();
		ImageLoader.getInstance().init(config);
		if (USE_ACTIVE_ANDROID) {
			ActiveAndroid.initialize(this);
			// ActiveAndroid.setLoggingEnabled(true);
		}
		setupPersistenceManager();
	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterApplication.context);
	}
	
	public static PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}
	
	private void setupPersistenceManager() {	
		try {
			AppSpecificJDXSetup.initialize(); // must be done before calling getInstance()
			JDXSetup jdxSetup = AppSpecificJDXSetup.getInstance(this);
			persistenceManager = new JDXPersistenceManagerImpl(jdxSetup);
		} catch (Exception ex) {
			Toast.makeText(getBaseContext(), "Exception: " + ex.getMessage(),
					Toast.LENGTH_SHORT).show();
			persistenceManager = null;
		}
	}
	
    /**
     * Do the necessary cleanup.
     */
    @Override
    public void onTerminate() {
    	// TODO Auto-generated method stub
    	super.onTerminate();
    	cleanup();
    }
    
    private void cleanup() {
        AppSpecificJDXSetup.cleanup(); // Do this when the application is exiting.
    }
}