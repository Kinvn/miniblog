package com.kinvn.miniblog;

import android.content.Context;
import android.icu.util.Calendar;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.kinvn.miniblog", appContext.getPackageName());

        Log.i("tag1",new SimpleDateFormat("EE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH).format
                (java.util.Calendar.getInstance().getTime()));
    }
}
