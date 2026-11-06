package com.easemytrip.constants;

import java.io.File;

/**
 * FrameworkConstants holds framework-wide constant values and file paths.
 * All constants are immutable and the class cannot be instantiated.
 */
public final class FrameworkConstants {

    private FrameworkConstants() {
        // Prevent instantiation
    }

    private static final String USER_DIR = System.getProperty("user.dir");

    public static final String CONFIG_FILE_PATH = USER_DIR + File.separator + "src" + File.separator 
            + "test" + File.separator + "resources" + File.separator + "config" + File.separator + "config.properties";

    public static final String TESTDATA_EXCEL_PATH = USER_DIR + File.separator + "src" + File.separator 
            + "test" + File.separator + "resources" + File.separator + "testdata" + File.separator + "easemytrip-testdata.xlsx";

    public static final String REPORTS_DIRECTORY = USER_DIR + File.separator + "reports";
    public static final String SCREENSHOTS_DIRECTORY = REPORTS_DIRECTORY + File.separator + "screenshots";

    public static final int DEFAULT_EXPLICIT_WAIT_SECONDS = 15;
    public static final int DEFAULT_PAGE_LOAD_TIMEOUT_SECONDS = 30;
    public static final int POLLING_INTERVAL_MILLIS = 500;

    public static final String DEFAULT_BROWSER = "chrome";
}
