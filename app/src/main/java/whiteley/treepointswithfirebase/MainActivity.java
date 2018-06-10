package whiteley.treepointswithfirebase;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import whiteley.treepointswithfirebase.Login.LoginActivity;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 11;
    private Tree tree;
    private String treeId ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Context mContext = MainActivity.this;
    private static final String TAG = "MainActivity";
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
    AutoCompleteTextView acTreeId;
    AutoCompleteTextView acProjectName;
    Project project;

    FirebaseDatabase database;
    DatabaseReference databaseProjectsReference;
    DatabaseReference databaseReference;
    String projectId = "TestProject1";
    String projectName = "Test Project 1";
    ArrayList<String> treeNumberList = new ArrayList<>();
    ArrayAdapter<String> treeIdAdaptor;

    HashMap<String, Project> projectNameToProject = new HashMap<>();
    HashMap<String, Project> projectIdToProject = new HashMap<>();
    ArrayList<String> projectNames = new ArrayList<>();
    Integer treeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_editor);
        //Define the inputs
        acSpecies = (AutoCompleteTextView) (findViewById(R.id.species_spinner));
        acGrade = (AutoCompleteTextView) (findViewById(R.id.grade_spinner));
        acStatus = (AutoCompleteTextView) (findViewById(R.id.status_spinner));
        acHealth = (AutoCompleteTextView) (findViewById(R.id.health_spinner));
        acDBH = (AutoCompleteTextView) (findViewById(R.id.dbh_spinner));
        acNote = (AutoCompleteTextView) (findViewById(R.id.notes_spinner));
        etComments = (EditText) findViewById(R.id.comments);
        acTreeId = (AutoCompleteTextView) (findViewById(R.id.treeId)) ;
        acProjectName= (AutoCompleteTextView) (findViewById(R.id.projectName));
        treePic = (ImageView) (findViewById(R.id.treePic));

        /*Check to see if any project or tree info was passed to the activity*/
        Intent passedIntent = getIntent();
        if(passedIntent.hasExtra("treeId")){
            treeId=passedIntent.getStringExtra("treeId");
        }
        if(passedIntent.hasExtra("treeNumber")){
            treeNumber=passedIntent.getIntExtra("treeNumber",0);
            acTreeId.setText(treeNumber.toString());
        }
        if(passedIntent.hasExtra("projectId")){
            projectId=passedIntent.getStringExtra("projectId");
        }
        if(passedIntent.hasExtra("projectName")){
            projectName=passedIntent.getStringExtra("projectName");
            acProjectName.setText(projectName);
        }
        database = FirebaseDatabase.getInstance();
        hideSoftKeyboard();
        setupFirebaseAuth();
        mAuth.signOut();




        databaseProjectsReference = FirebaseDatabase.getInstance().getReference("Projects");
        databaseReference = FirebaseDatabase.getInstance().getReference();


        /*  Set Project drop down */
        final ValueEventListener getProjectNames = databaseProjectsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot project : dataSnapshot.getChildren()) {
                    projectNameToProject.put(project.child("projectName").getValue().toString(), project.getValue(Project.class));
                    projectIdToProject.put(project.getKey(), project.getValue(Project.class));
                    projectNames.add(project.child("projectName").getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter projectNameAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, projectNames);
        acProjectName.setAdapter(projectNameAdaptor);
        acProjectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(projectNameToProject.containsKey(editable.toString())){
                    project=projectNameToProject.get(editable.toString());
                    projectName=project.getProjectName();
                    projectId= project.getProjectId();
                    treeNumberList=new ArrayList<>();
                    treeNumberList.add("New");
                    for (int i=1; i <=project.getNumberOfTrees(); i++){
                        treeNumberList.add(Integer.toString(i));
                    }
                    treeIdAdaptor = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, treeNumberList);
                    acTreeId.setAdapter(treeIdAdaptor);
                } else{
                    Toast.makeText(MainActivity.this, "Please select a valid project name", Toast.LENGTH_SHORT).show();
                    projectId=null;
                    projectName=null;
                    treeNumberList=new ArrayList<>();
                }

            }
        });
        /********************  Set Tree Id drop down ************************/
        final ValueEventListener treeNameListener = databaseProjectsReference.child(projectId).child("numberOfTrees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer numberOfTrees = Integer.parseInt(dataSnapshot.getValue().toString()) ;
                treeNumberList=new ArrayList<>();
                treeNumberList.add("New");
                for (int i=1; i <=numberOfTrees; i++){
                    treeNumberList.add(Integer.toString(i));
                }
                treeIdAdaptor = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, treeNumberList);
                acTreeId.setAdapter(treeIdAdaptor);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        if (treeId == null) {
            tree = new Tree();
            tree.setProjectName(projectName);
            tree.setProjectId(projectId);
// If we need to revert to deeper structure this can get the number of trees in a project... not recomended
//            getChildCount(tree, databaseProjectsReference, new ChildCountInterface() {
//                @Override
//                public void onCallback(Integer value) {
//                    Log.d("TAG", value.toString());
//                    treeId = value;
//                    databaseProjectsReference.child("Trees").child(treeId.toString()).child("treeId").setValue(treeId);
//                }
//            });
        } else {
            getTree(databaseReference.child("Trees").child(treeId), new GetTreeInterface() {

                @Override
                public void onCallback(Tree treePulled) {
                    tree=treePulled;
                    acSpecies.setText(tree.getSpecies());
                    acGrade.setText(tree.getGrade());
                    acStatus.setText(tree.getStatus());
                    etComments.setText(tree.getComments());
                    acDBH.setText(tree.getDbh());
                    acNote.setText(tree.getNotes());
                    acHealth.setText(tree.getHealth());
                }
            });
        }
        //tree.setTreeId(databaseReference.push().getKey());




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
        treeIdAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, treeNumberList);
        acTreeId.setAdapter(treeIdAdaptor);


        //** "dumb" way to populate if an existing id is selected */
        acTreeId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if(projectId!=null && isInteger(editable.toString()) ){
                    databaseReference.child("Trees").child(projectId+"_"+editable.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                Bundle treeInfBundle = new Bundle();
                                treeInfBundle.putString("treeId", projectId+"_"+editable.toString());
                                treeInfBundle.putInt("treeNumber", Integer.parseInt(editable.toString()) );
                                treeInfBundle.putString("projectId", projectId);
                                treeInfBundle.putString("projectName", projectName);
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                intent.putExtras(treeInfBundle);
                                startActivity(intent);

                                } else {
                                Toast.makeText(MainActivity.this, "Will create new tree", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Will create new tree", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* add click listeners  to show dropdown on click*/
        /* TODO: Figure out a less repetitive method of doing this */
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
        acTreeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acTreeId.showDropDown();
            }
        });
        acProjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acProjectName.showDropDown();
            }
        });

        /* add Text listeners */
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
                tree.setDbh(editable.toString());
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
                tree.setNotes(editable.toString());
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
                tree.setNotes(editable.toString());
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

        /* sets a listener on the Take Picture button and launches the native camera app*/
        Button btnTakePicture = (Button) findViewById(R.id.take_picture);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
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
                Bundle treeInfBundle = new Bundle();
                if(treeId != null) {
                    treeInfBundle.putString("treeId", treeId);
                }
                if(treeNumber != null) {
                    treeInfBundle.putInt("treeNumber", treeNumber);
                }
                if(projectId != null) {
                    treeInfBundle.putString("projectId", projectId);
                }
                if(projectName != null) {
                    treeInfBundle.putString("projectName", projectName);
                }
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.putExtras(treeInfBundle);
                        startActivity(intent);

                        break;

                    case R.id.viewTree:
                        Intent intent1 = new Intent(MainActivity.this, TreeDBViewer.class);
                        intent1.putExtras(treeInfBundle);
                        startActivity(intent1);

                        break;

                    case R.id.viewMap:
                        Intent intent2 = new Intent(MainActivity.this, MapViewer.class);
                        intent2.putExtras(treeInfBundle);
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
        Boolean result = tree.pushToFirebase(databaseReference);



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
        databaseReference.child("Trees").addListenerForSingleValueEvent(new ValueEventListener() {
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tree = snapshot.getValue(Tree.class);
                callBack.onCallback(tree);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });
    }
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /**
     * checks to see if the @param 'user' is logged in
     * @param user: the user id string
     */
    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if(user == null){
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }
    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in
                checkCurrentUser(user);

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);

        }   }
    }

