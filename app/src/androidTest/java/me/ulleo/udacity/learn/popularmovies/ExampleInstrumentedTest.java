package me.ulleo.udacity.learn.popularmovies;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.ulleo.udacity.learn.popularmovies.model.SearchParam;
import me.ulleo.udacity.learn.popularmovies.utils.FetchMoviesTask;
import me.ulleo.udacity.learn.popularmovies.utils.NetworkUtils;

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
        //Context appContext = InstrumentationRegistry.getTargetContext();

        //assertEquals("me.ulleo.udacity.learn.popularmovies", appContext.getPackageName());
        new FetchMoviesTask().execute(new SearchParam(NetworkUtils.POPULAR,1));
    }
}
