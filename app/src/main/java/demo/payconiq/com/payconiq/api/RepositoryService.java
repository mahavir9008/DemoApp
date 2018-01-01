package demo.payconiq.com.payconiq.api;


import java.util.List;

import demo.payconiq.com.payconiq.model.Repository;
import demo.payconiq.com.payconiq.utility.Constant;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Prepare the URL to hit the server
 */
public interface RepositoryService {
    @GET(Constant.APPEND_URL)
    Call<List<Repository>> getRepositoryAPI(@Query(Constant.PAGE) int pageIndex, @Query(Constant.STRING_PER_PAGE) int perPage);
}
