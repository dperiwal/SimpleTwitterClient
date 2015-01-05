package com.codepath.apps.basictwitter.persistence;

import android.content.ContextWrapper;

import com.codepath.apps.basictwitter.R;
import com.softwaretree.jdxandroid.BaseAppSpecificJDXSetup;
import com.softwaretree.jdxandroid.JDXSetup;

/**
 * This class is a shell to supply some basic application specific parameters 
 * for JDX ORM setup. The superclass does the bulk of the work.  After once calling 
 * {@link #initialize()} method, you may call {@link #getInstance(android.content.ContextWrapper)}
 * method to get and work with a {@link com.softwaretree.jdxandroid.JDXSetup} object. 
 * <p>
 * @author Damodar Periwal
 */
public class AppSpecificJDXSetup extends BaseAppSpecificJDXSetup {
	private static int ormId = R.raw.basic_twitter;
    
    /**
     * Initializes the ORMFileResourceId {@link #setORMFileResourceId(int)} of the file 
     * containing the JDX mapping specification.
     * <p>  
     * The license key for the JDX for Android ORM product must also be set in this 
     * method using the corresponding setter method {@link #setJdxForAndroidLicenseKey(String)} in the superclass.
     * <p> 
     * Any non-default values for the following parameters should be set in this 
     * method using the corresponding setter methods in the superclass:
     * <p>
     * forceCreateSchema {@link #setForceCreateSchema(boolean)} (default false)
     * <p>
     * debugLevel {@link #setDebugLevel(int)} (default 5)
     * <p>
     * This method should be called before calling the getInstance() method of this class.
     */
    public static void initialize() {
        setORMFileResourceId(ormId);
        setJdxForAndroidLicenseKey(ormId, "_PJ01.0cCsupNaU0ulc85zsudN3R9hzR331zbR1Ts9wT99N5JDXi7LYI3154");
        // setForceCreateSchema(ormId, true);
        // setDebugLevel(ormId, 3); // To see all the SQL statements in the log
    }  
    
	public static JDXSetup getInstance(ContextWrapper contextWrapper) throws Exception {
		return getInstance(ormId, contextWrapper);
	}

	public static void cleanup() {
		cleanup(ormId);
	}
}