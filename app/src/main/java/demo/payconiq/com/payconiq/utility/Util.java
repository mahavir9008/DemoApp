package demo.payconiq.com.payconiq.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

import demo.payconiq.com.payconiq.api.RepoApi;
import demo.payconiq.com.payconiq.api.RepositoryService;
import demo.payconiq.com.payconiq.model.Repository;
import demo.payconiq.com.payconiq.realmdao.DataItem;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;

public class Util {

    /**
     * @param context - use to access the current state of the application/object
     * @return - status of internet on your device
     */
    public static boolean isInterNetAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * @param results - delete the previous data and save the current data
     * @param realm   - instance of realm to save the data
     */
    public static void saveData(final List<Repository> results, Realm realm) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmList<DataItem> repoList = new RealmList<>();
                for (Repository rel : results) {
                    repoList.add(new DataItem(rel.getId(), rel.getFullName(), rel.getName(), rel.getForks(), rel.getDescription()));
                }
                realm.insertOrUpdate(repoList);
            }
        });
    }


    /**
     * Performs a Retrofit call to the JakeWhartonRepoApi API.
     * Same API call for Pagination.
     * As  will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    public static Call<List<Repository>> callJakeWhartonRepoApi(int currentPage) {
        RepositoryService repositoryService = RepoApi.getClient().create(RepositoryService.class);
        return repositoryService.getRepositoryAPI(
                currentPage,
                Constant.PER_PAGE_ITEM
        );
    }

    /**
     *
     * @param realm - reference of the database.
     * @param toDoItems = old data
     */
    public static void deleteOldData(Realm realm, RealmResults<DataItem> toDoItems) {
        realm.beginTransaction();
        toDoItems.deleteAllFromRealm();
        realm.commitTransaction();
    }

}
