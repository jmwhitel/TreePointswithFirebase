package whiteley.treepointswithfirebase;

import android.Manifest;

import android.graphics.Bitmap;

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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


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
    private Tree tree;
    private Boolean result = false;
    private Integer treeId;

    TextView TV1;
    EditText etLatitude, etLongitude, etComments;
    Button btnadd;
    ArrayAdapter adapter;
    ImageView treePic;
    AutoCompleteTextView acDBH;
    AutoCompleteTextView acNote;
    AutoCompleteTextView acSpecies;
    AutoCompleteTextView acGrade;
    AutoCompleteTextView acStatus;
    AutoCompleteTextView acHealth;

    DatabaseReference databaseProjectReference;
    String projectId = "TestProject1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseProjectReference = FirebaseDatabase.getInstance().getReference(projectId);


        if (treeId == null) {
            tree = new Tree();
            getChildCount(tree, databaseProjectReference, new ChildCountInterface() {
                @Override
                public void onCallback(Integer value) {
                    Log.d("TAG", value.toString());
                    treeId = value;
                }
            });
            result = tree.pushToFirebase(databaseProjectReference);

        } else {
            getTree(databaseProjectReference.child("Trees").child(treeId.toString()), new GetTreeInterface() {

                @Override
                public void onCallback(Tree treePulled) {
                    tree=treePulled;
                }
            });
        }
        //tree.setTreeId(databaseReference.push().getKey());


        //Define the inputs
        acSpecies = (AutoCompleteTextView) (findViewById(R.id.species_spinner));
        acGrade = (AutoCompleteTextView) (findViewById(R.id.grade_spinner));
        acStatus = (AutoCompleteTextView) (findViewById(R.id.status_spinner));
        acHealth = (AutoCompleteTextView) (findViewById(R.id.health_spinner));
        acDBH = (AutoCompleteTextView) (findViewById(R.id.dbh_spinner));
        acNote = (AutoCompleteTextView) (findViewById(R.id.notes_spinner));
        etComments = (EditText) findViewById(R.id.comments);
        etLatitude = (EditText) findViewById(R.id.latitude);
        etLongitude = (EditText) findViewById(R.id.longitude);

        treePic = (ImageView) (findViewById(R.id.treePic));


        /*Set input pre-set values */
        adapter = ArrayAdapter.createFromResource(this, R.array.species_options, android.R.layout.simple_spinner_item);
        acSpecies.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.grade_options, android.R.layout.simple_spinner_item);
        acGrade.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.status_options, android.R.layout.simple_spinner_item);
        acStatus.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.health_options, android.R.layout.simple_spinner_item);
        acHealth.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.numeric_options, android.R.layout.simple_spinner_item);
        acDBH.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.note_options, android.R.layout.simple_spinner_item);
        acNote.setAdapter(adapter);



        /* add click listners  to show dropdowns on clic*/
        /* TODO: Figure out a less repetative method of doing this */
        acSpecies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acSpecies.showDropDown();
            }
        });
        acGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acGrade.showDropDown();
            }
        });
        acStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acStatus.showDropDown();
            }
        });
        acHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acHealth.showDropDown();
            }
        });
        acDBH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acDBH.showDropDown();
            }
        });
        acNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acNote.showDropDown();
            }
        });


        /* add Text listners */
        /*TODO: Add a validation step if desired */
        acSpecies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tree.setSpecies(editable.toString());
            }
        });
        acGrade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                tree.setGrade(editable.toString());
            }
        });
        acStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                tree.setStatus(editable.toString());
            }
        });
        acHealth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                tree.setHealth(editable.toString());
            }
        });
        acDBH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO: Make this so it doesn't just keep adding values
                tree.addDbh(Float.valueOf(editable.toString()));
            }
        });
        acNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO: Make this so it doesn't just keep adding values
                tree.addNote(editable.toString());
            }
        });
        acNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO: Make this so it doesn't just keep adding values
                tree.addNote(editable.toString());
            }
        });
        etComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tree.setComments(editable.toString());
            }
        });
        etLatitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tree.setLatitude(Float.parseFloat(editable.toString()));
            }
        });
        etLongitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tree.setLongitude(Float.parseFloat(editable.toString()));
            }
        });
        /*        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();*/

        /* sets a listener on the Take Picture button and launches the native camera app*/
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
        final TextView textview1 = (TextView) findViewById(R.id.latitude);
        final TextView textview2 = (TextView) findViewById(R.id.longitude);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                textview1.append("" + location.getLatitude());
                textview2.append("" + location.getLongitude());

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
        /* Checks access permissions for GPS location*/
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

        /*Sets bottom navigation functionality*/
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
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

                    case R.id.viewMap:
                        Intent intent2 = new Intent(MainActivity.this, MapViewer.class);
                        startActivity(intent2);

                        break;

                }

                return false;


            }


        });
    }

    private void configurebtnRequestLocation() {
    }

    public void btnAddToDatabase(View view) {
        //If there is no id, sets the id to the 1+ the max key in the system.
        if(tree.getTreeId()==null){
            if(treeId==null){
                Toast.makeText(MainActivity.this, "No Tree ID.  Dev Error", Toast.LENGTH_SHORT).show();
            } else {
                tree.setTreeId(treeId);
            }
        }
        result = tree.pushToFirebase(databaseProjectReference);

        if (result) {
            Toast.makeText(MainActivity.this, "Tree Created Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Error uploading tree, Please try again", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        tree = new Tree();
        startActivity(intent);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == MainActivity.RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            treePic.setImageBitmap(imageBitmap);
            savePicToTree(imageBitmap);
        }
    }

    public void savePicToTree(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        tree.addImage(imageEncoded);
    }

    public interface ChildCountInterface {
        void onCallback(Integer value);
    }

    public void getChildCount(Tree tree, DatabaseReference databaseReference, final ChildCountInterface callBack) {
        databaseReference.child("Trees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count of Children ", "" + snapshot.getChildrenCount());
                if (snapshot.getChildrenCount() == 0) {
                    callBack.onCallback(1);
                } else {
                    Integer maxKey = 0;
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        maxKey = Math.max(Integer.parseInt(postSnapshot.getKey().toString()), maxKey);
                    }
                    callBack.onCallback(maxKey + 1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });
    }

    public interface GetTreeInterface {
        void onCallback(Tree tree);
    }

    public void getTree(DatabaseReference databaseReference, final GetTreeInterface callBack) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Tree tree = snapshot.getValue(Tree.class);
                callBack.onCallback(tree);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });
    }
}



