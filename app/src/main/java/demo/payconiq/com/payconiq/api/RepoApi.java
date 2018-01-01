package demo.payconiq.com.payconiq.api;

import demo.payconiq.com.payconiq.utility.Constant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * use to construct the Retrofit API
 */
public class RepoApi {
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constant.BASE_URL)
                    .build();
        }
        return retrofit;
    }

}
