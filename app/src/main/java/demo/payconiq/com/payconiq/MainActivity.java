package demo.payconiq.com.payconiq;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import demo.payconiq.com.payconiq.model.Repository;
import demo.payconiq.com.payconiq.realmdao.DataItem;
import demo.payconiq.com.payconiq.realmdao.RealmBaseActivity;
import demo.payconiq.com.payconiq.utility.Constant;
import demo.payconiq.com.payconiq.utility.PaginationScrollListener;
import demo.payconiq.com.payconiq.utility.Util;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends RealmBaseActivity {

    private PaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rv;
    private ProgressBar progressBar;


    private boolean isLoading = false;
    private final boolean isLastPage = false;
    private int currentPage = Constant.PAGE_START;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializedView();
        addPagination();
        appStatus();
    }

    /**
     * initialized the view, adapter and realm
     */
    private void initializedView() {
        rv = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        Realm.init(getApplicationContext());
        realm = Realm.getInstance(getRealmConfig());
        adapter = new PaginationAdapter();
        rv.setAdapter(adapter);
    }

    /**
     * add pagination to the RecycleView
     */
    private void addPagination() {
        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                if (Util.isInterNetAvailable(MainActivity.this)) {
                    isLoading = true;
                    currentPage += 1;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addLoadingFooter();
                            loadPage();
                        }
                    }, Constant.MILLISECOND_DELAY);
                } else {
                    adapter.stopLoading();
                    Toast.makeText(MainActivity.this, Constant.INTERNET_UNAVAILABLE, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    /**
     * get the data from the server and add to the adapter
     */
    private void loadPage() {
        Util.callJakeWhartonRepoApi(currentPage).enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                if (currentPage == 1) {
                    // Delete all the old data
                    RealmResults<DataItem> realmResults = realm.where(DataItem.class).findAll();
                    Util.deleteOldData(realm,realmResults);
                }else{
                    adapter.removeLoadingFooter();
                    isLoading = false;
                }
                if (response.body().size() > 0) {
                    final List<Repository> results = response.body();
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);
                    adapter.addLoadingFooter();
                    Util.saveData(results, realm);
                } else {
                    isLoading = true;
                    Toast.makeText(MainActivity.this, Constant.DATA_UNAVAILABLE, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * get the status of the app -:
     * Internet available - get the data from the server
     * Internet Unavailable & offline data present - get the offline data and add to the adapter
     * No Internet & No Offline data - close the app and display the message
     */
    private void appStatus() {
        RealmResults<DataItem> realmResults = realm.where(DataItem.class).findAll();
        if (Util.isInterNetAvailable(this)) {
            loadPage();
        } else if (realmResults.size() > 0) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, Constant.OFFLINE_DATA, Toast.LENGTH_LONG).show();
            ArrayList<Repository> ob = new ArrayList<>();
            for (DataItem r : realmResults) {
                ob.add(new Repository(r.getId(), r.getFullName(),
                        r.getFullName(), r.getForks(), r.getDescription()));
            }
            adapter.addAll(ob);
        } else {
            Toast.makeText(this, Constant.NO_DATA_AND_INTERNET, Toast.LENGTH_LONG).show();
            finish();
        }

    }

    /**
     * close the realm database.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
            realm = null;
        }
    }
}
