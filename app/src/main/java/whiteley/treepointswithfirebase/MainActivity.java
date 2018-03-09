package whiteley.treepointswithfirebase;

import android.Manifest;
import android.graphics.Point;
import android.support.design.widget.BottomNavigationView;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private Button btnRequestLocation;
    private TextView textView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public static String DBid;
    FirebaseDatabase database;
    DatabaseReference myRef;


    EditText etNorthing, etEasting, etNotes;
    Button btnadd;
    ArrayAdapter adapter;
    Spinner spinnerSpecies;
    Spinner spinnerGrade;
    Spinner spinnerStatus;
    Spinner spinnerHealth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);

                        break;

                    //case R.id.viewTree:
                    //  Intent intent1 = new Intent(MainActivity.this, ViewListContents.class);
                    // startActivity(intent1);

                    //break;
                }

                return false;


            }


        });


        adapter = ArrayAdapter.createFromResource(this, R.array.tree_spinner_options, android.R.layout.simple_spinner_item);

        spinnerSpecies = (Spinner) (findViewById(R.id.species_spinner));
        spinnerGrade = (Spinner) (findViewById(R.id.grade_spinner));
        spinnerStatus = (Spinner) (findViewById(R.id.status_spinner));

        spinnerSpecies.setAdapter(adapter);


        adapter = ArrayAdapter.createFromResource(this, R.array.grade_options, android.R.layout.simple_spinner_item);

        spinnerGrade = (Spinner) (findViewById(R.id.grade_spinner));

        spinnerGrade.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.status_options, android.R.layout.simple_spinner_item);

        spinnerStatus = (Spinner) (findViewById(R.id.status_spinner));

        spinnerStatus.setAdapter(adapter);


        adapter = ArrayAdapter.createFromResource(this, R.array.healthrating_options, android.R.layout.simple_spinner_item);

        spinnerHealth = (Spinner) (findViewById(R.id.health_spinner));

        spinnerHealth.setAdapter(adapter);

        btnRequestLocation = (Button) findViewById(R.id.btnRequestLocation);
        final TextView textview1 = (TextView) findViewById(R.id.etNorthing);
        final TextView textview2 = (TextView) findViewById(R.id.etEasting);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                textview1.append(""+location.getLatitude());
                textview2.append(""+location.getLongitude());

            }


            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            } else {
                configurebtnRequestLocation();
            }
        }

    }

    public void btnAdd(View view){
        Spinner spinnerSpecies = (Spinner) findViewById(R.id.species_spinner);
        EditText etNorthing = (EditText) findViewById(R.id.etNorthing);
        EditText etEasting = (EditText) findViewById(R.id.etEasting);
        Spinner spinnerGrade = (Spinner) findViewById(R.id.grade_spinner);
        Spinner spinnerStatus = (Spinner) findViewById(R.id.status_spinner);
        Spinner spinnerHealth = (Spinner) findViewById(R.id.health_spinner);
        EditText etNotes = (EditText) findViewById(R.id.etNotes);


        //String child = spinnerSpecies.getSelectedItem().toString();
        //myRef = database.getReference("Species").child(child);
        databaseReference = FirebaseDatabase.getInstance().getReference("Tree Point");
        String id = databaseReference.push().getKey();
        String species = spinnerSpecies.getSelectedItem().toString();
        String latitude = etNorthing.getText().toString();
        String longitude = etEasting.getText().toString();
        String grade = spinnerGrade.getSelectedItem().toString();
        String status = spinnerStatus.getSelectedItem().toString();
        String health = spinnerHealth.getSelectedItem().toString();
        String notes = etNotes.getText().toString();

        databaseReference.child(id).child(species).child("Latitude").setValue(latitude);
        databaseReference.child(id).child(species).child("Longitude").setValue(longitude);
        databaseReference.child(id).child(species).child("Grade").setValue(grade);
        databaseReference.child(id).child(species).child("Status").setValue(status);
        databaseReference.child(id).child(species).child("Rating").setValue(health);
        databaseReference.child(id).child(species).child("Notes").setValue(notes);

        //myRef.child("Species").setValue(spinnerSpecies.getSelectedItem().toString());
        //myRef.child("Latitude").setValue(etNorthing.getText().toString());

        //myRef.child("Species").setValue(spinnerSpecies.getSelectedItem().toString());
        //myRef.child("Longitude").setValue(etEasting.getText().toString());

        //myRef.child("Species").setValue(spinnerSpecies.getSelectedItem().toString());
        //myRef.child("Grade").setValue(spinnerGrade.getSelectedItem().toString());

        //myRef.child("Species").setValue(spinnerSpecies.getSelectedItem().toString());
        //myRef.child("Status").setValue(spinnerStatus.getSelectedItem().toString());

        //myRef.child("Species").setValue(spinnerSpecies.getSelectedItem().toString());
        //myRef.child("Health").setValue(spinnerHealth.getSelectedItem().toString());

        //myRef.child("Species").setValue(spinnerSpecies.getSelectedItem().toString());
        //myRef.child("Notes").setValue(etNotes.getText().toString());

        Toast.makeText(MainActivity.this, "Tree Created Successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);

    }

    private void configurebtnRequestLocation() {
        btnRequestLocation.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager.requestLocationUpdates("gps", 0, 50, locationListener);
            }
        }));
    }
}







