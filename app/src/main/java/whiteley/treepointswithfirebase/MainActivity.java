package whiteley.treepointswithfirebase;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;


public class MainActivity extends AppCompatActivity {

    private Button btnRequestLocation;
    private Button btnTakePicture;
    public static int count = 0;
    static final int REQUEST_IMAGE_CAPTURE = 11;
    private Button btnPlus;
    private TextView textView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public static String DBid;
    FirebaseDatabase database;



    TextView TV1;
    EditText etNorthing, etEasting, etNotes;
    Button btnadd;
    ArrayAdapter adapter;
    Spinner spinnerDBH;
    Spinner spinnerNote;
    Spinner spinnerSpecies;
    Spinner spinnerGrade;
    Spinner spinnerStatus;
    Spinner spinnerHealth;
    Spinner m1;
    Spinner m2;
    Spinner m3;
    Spinner m4;
    Spinner m5;
    Spinner m6;
    Spinner m7;
    Spinner m8;
    DatabaseReference databaseReference;


//    public void onLaunchCamera() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);

                        break;

                    case R.id.viewTree:
                        Intent intent1 = new Intent(MainActivity.this, TreeDBViewer.class);
                        startActivity(intent1);

                        break;
                }

                return false;


            }


        });


        adapter = ArrayAdapter.createFromResource(this, R.array.tree_spinner_options, android.R.layout.simple_spinner_item);

        spinnerSpecies = (Spinner) (findViewById(R.id.species_spinner));
        spinnerGrade = (Spinner) (findViewById(R.id.grade_spinner));
        spinnerStatus = (Spinner) (findViewById(R.id.status_spinner));
        spinnerDBH = (Spinner) (findViewById(R.id.dbh_spinner));

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

        adapter = ArrayAdapter.createFromResource(this, R.array.dbh_options, android.R.layout.simple_spinner_item);

        spinnerDBH = (Spinner) (findViewById(R.id.dbh_spinner));

        spinnerDBH.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.note_options, android.R.layout.simple_spinner_item);

        spinnerNote = (Spinner) (findViewById(R.id.notes_spinner));

        spinnerNote.setAdapter(adapter);

        adapter=ArrayAdapter.createFromResource(this,R.array.m1_options,android.R.layout.simple_spinner_item);

        m1=(Spinner)(findViewById(R.id.m1));

        m1.setAdapter(adapter);

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();

        btnTakePicture = (Button) findViewById(R.id.take_picture);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

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


        databaseReference = FirebaseDatabase.getInstance().getReference("Tree Point");
        String id = databaseReference.push().getKey();
        String species = spinnerSpecies.getSelectedItem().toString();
        String latitude = etNorthing.getText().toString();
        String longitude = etEasting.getText().toString();
        String grade = spinnerGrade.getSelectedItem().toString();
        String status = spinnerStatus.getSelectedItem().toString();
        String health = spinnerHealth.getSelectedItem().toString();
        String notes = etNotes.getText().toString();

        databaseReference.child(id).child("Species").setValue(species);
        databaseReference.child(id).child("Latitude").setValue(latitude);
        databaseReference.child(id).child("Longitude").setValue(longitude);
        databaseReference.child(id).child("Grade").setValue(grade);
        databaseReference.child(id).child("Status").setValue(status);
        databaseReference.child(id).child("Rating").setValue(health);
        databaseReference.child(id).child("Notes").setValue(notes);

        Toast.makeText(MainActivity.this, "Tree Created Successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == MainActivity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageLabel.setImageBitmap(imageBitmap);
            encodeBitmapAndSaveToFirebase(imageBitmap);
        }
    }
}







