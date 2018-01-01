package demo.payconiq.com.payconiq;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import demo.payconiq.com.payconiq.model.Repository;
import demo.payconiq.com.payconiq.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Check the package name of the application.
     */
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("demo.payconiq.com.payconiq", appContext.getPackageName());
    }

    /**
     * check the listView present
     */
    @Test
    public void ensureListViewIsPresent() {
        MainActivity activity = rule.getActivity();
        View viewById = activity.findViewById(R.id.main_recycler);
        Assert.assertNotNull(viewById);
    }

    // check the adapter data - get the data from server and fill-up in the adapter and check the size
    @Test
    @UiThreadTest
    public void checkAdapterData() {
        MainActivity activity = rule.getActivity();
        final RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.main_recycler);
        Assert.assertNotNull(recyclerView);
        // Initial data
        final PaginationAdapter paginationAdapter = new PaginationAdapter();
        Assert.assertSame(paginationAdapter.getItemCount(), 0);
        // Add data to adapter
        recyclerView.setAdapter(paginationAdapter);
        // get the data and fill to the adapter
        Util.callJakeWhartonRepoApi(1).enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                assertNotNull(response);
                assertEquals(response.body().size(), 15);
                paginationAdapter.addAll(response.body());
                assertEquals(15, paginationAdapter.getItemCount());
                // check the data from the listView adapter
                assertEquals(recyclerView.getAdapter().getItemCount(), 15);
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * check the internet connection
     */
    @Test
    public void checkInternet() {
        MainActivity activity = rule.getActivity();
        assertEquals(Util.isInterNetAvailable(activity), true);
    }

    /**
     * use the reflection to call the private method (LoadPage) and
     * check the data in adapter
     *
     * @throws Exception
     */

    @Test
    public void testLoadPage() throws Exception {
        MainActivity activity = rule.getActivity();
        PaginationAdapter paginationAdapter = new PaginationAdapter();
        java.lang.reflect.Field fieldAdapter =
                MainActivity.class.getDeclaredField("adapter");
        fieldAdapter.setAccessible(true);
        fieldAdapter.set(activity, paginationAdapter);
        try {
            java.lang.reflect.Method method;
            method = MainActivity.class.getDeclaredMethod("loadPage");
            method.setAccessible(true);
            method.invoke(activity);
            assertEquals(paginationAdapter.getItemCount(), 15);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * switch off the internet and test the method
     * message will be display  with offline;
     */
    @Test
    public void testAppStatusOffline() {
        try {
            MainActivity activity = rule.getActivity();
            java.lang.reflect.Method method;
            method = MainActivity.class.getDeclaredMethod("appStatus");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
