package io.github.nfdz.findoordemoapp.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.nfdz.findoordemoapp.R;
import io.github.nfdz.findoordemoapp.common.dialog.AskLocationToExportDialogFragment;
import io.github.nfdz.findoordemoapp.common.dialog.SetAliasDialogFragment;
import io.github.nfdz.findoordemoapp.common.utils.ImportExportUtils;
import io.github.nfdz.findoordemoapp.common.utils.PreferencesUtils;
import io.github.nfdz.findoordemoapp.common.utils.RealmUtils;
import io.github.nfdz.findoordemoapp.compare.view.CompareActivity;
import io.github.nfdz.findoordemoapp.delete.view.DeleteActivity;
import io.github.nfdz.findoordemoapp.record.view.RecordActivity;
import io.github.nfdz.findoordemoapp.trial.view.TrialActivity;
import io.github.nfdz.findoordemoapp.view.view.ViewActivity;
import io.realm.Realm;

/**
 * This is the entry activity of the app.
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view) RecyclerView recycler_view;

    private Realm realm;
    private Integer locationToExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        realm = Realm.getInstance(RealmUtils.getConfiguration());
        locationToExport = null;
        setupRecyclerView();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (ImportExportUtils.onImportActivityResult(requestCode, resultCode, resultData, this, realm)) {
            return;
        }
        if (locationToExport != null) {
            if (ImportExportUtils.onExportActivityResult(requestCode, resultCode, resultData, this, locationToExport, realm)) {
                locationToExport = null;
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }

    private void setupRecyclerView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setHasFixedSize(true);
        recycler_view.setAdapter(new Adapter());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recycler_view.addItemDecoration(dividerItemDecoration);
    }

    private void listLocations() {
        RealmUtils.listLocations(realm, new RealmUtils.ListLocationsCallback() {
            @Override
            public void onFinish(List<Integer> locations) {
                StringBuilder bld = new StringBuilder("\n");
                Iterator<Integer> it = locations.iterator();
                while (it.hasNext()) {
                    int location = it.next();
                    String locationAlias = PreferencesUtils.getLocationAlias(MainActivity.this, location);
                    if (TextUtils.isEmpty(locationAlias)) {
                        bld.append(location);
                    } else {
                        bld.append(location).append(" (").append(locationAlias).append(")");;
                    }
                    bld.append(it.hasNext() ? ", " : ".");
                }
                bld.append("\n");
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.list_locations_title)
                        .setMessage(bld.toString())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
            @Override
            public void onError() {
                Toast.makeText(MainActivity.this, R.string.list_locations_msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLocationAlias() {
        SetAliasDialogFragment dialog = SetAliasDialogFragment.newInstance();
        dialog.setListener(new SetAliasDialogFragment.AliasListener() {
            @Override
            public void onSet(int location, String alias) {
                Toast.makeText(MainActivity.this, R.string.set_alias_location_success, Toast.LENGTH_LONG).show();
            }
        });
        dialog.show(getSupportFragmentManager(), "SetAliasDialogFragment");
    }

    private void importLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImportExportUtils.importLocation(MainActivity.this);
        } else {
            Toast.makeText(this, R.string.error_kitkat_version, Toast.LENGTH_LONG).show();
        }
    }

    private void exportLocation() {
        AskLocationToExportDialogFragment dialog = AskLocationToExportDialogFragment.newInstance();
        dialog.setListener(new AskLocationToExportDialogFragment.LocationListener() {
            @Override
            public void onExport(int location) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    locationToExport = location;
                    ImportExportUtils.exportLocation(MainActivity.this, locationToExport);
                } else {
                    Toast.makeText(MainActivity.this, R.string.error_kitkat_version, Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show(getSupportFragmentManager(), "AskLocationToExportDialogFragment");
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_text, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return 9;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.text_view) TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void onBind(int position) {
                switch (position) {
                    case 0:
                        textView.setText(R.string.record_location);
                        break;
                    case 1:
                        textView.setText(R.string.view_location);
                        break;
                    case 2:
                        textView.setText(R.string.compare_locations);
                        break;
                    case 3:
                        textView.setText(R.string.try_location);
                        break;
                    case 4:
                        textView.setText(R.string.list_locations);
                        break;
                    case 5:
                        textView.setText(R.string.set_alias_location);
                        break;
                    case 6:
                        textView.setText(R.string.remove_location);
                        break;
                    case 7:
                        textView.setText(R.string.export_location);
                        break;
                    case 8:
                        textView.setText(R.string.import_location);
                        break;
                    default:
                        textView.setText("");
                }
            }

            @OnClick(R.id.text_view)
            public void onClick() {
                int position = getAdapterPosition();
                switch (position) {
                    case 0:
                        RecordActivity.start(MainActivity.this);
                        break;
                    case 1:
                        ViewActivity.start(MainActivity.this);
                        break;
                    case 2:
                        CompareActivity.start(MainActivity.this);
                        break;
                    case 3:
                        TrialActivity.start(MainActivity.this);
                        break;
                    case 4:
                        listLocations();
                        break;
                    case 5:
                        setLocationAlias();
                        break;
                    case 6:
                        DeleteActivity.start(MainActivity.this);
                        break;
                    case 7:
                        exportLocation();
                        break;
                    case 8:
                        importLocation();
                        break;
                }
            }

        }
    }
}
