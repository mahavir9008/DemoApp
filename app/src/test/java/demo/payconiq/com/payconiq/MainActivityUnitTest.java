package demo.payconiq.com.payconiq;

import org.junit.Test;

import java.util.List;

import demo.payconiq.com.payconiq.model.Repository;
import demo.payconiq.com.payconiq.utility.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;


public class MainActivityUnitTest {
    /**
     * check the retrofit API call, response size should be 15
     */
    @Test
    public void retrofitAPICall() {
        Util.callJakeWhartonRepoApi(1).enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                assertEquals(response.body().size(), 15);
            }
            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}