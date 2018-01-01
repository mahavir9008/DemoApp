package demo.payconiq.com.payconiq.realmdao;

import android.support.v7.app.AppCompatActivity;
import io.realm.RealmConfiguration;

/**
 * Prepare the realm database and create it instance
 */
public abstract class RealmBaseActivity extends AppCompatActivity {

    private RealmConfiguration realmConfiguration;
    protected RealmConfiguration getRealmConfig() {
        if (realmConfiguration == null) {
            realmConfiguration = new RealmConfiguration
                    .Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
        }
        return realmConfiguration;
    }
}