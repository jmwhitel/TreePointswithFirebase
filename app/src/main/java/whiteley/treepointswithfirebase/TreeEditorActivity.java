package whiteley.treepointswithfirebase;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
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
import java.util.List;

import whiteley.treepointswithfirebase.Login.LoginActivity;
import whiteley.treepointswithfirebase.Models.Project;
import whiteley.treepointswithfirebase.Models.Tree;


public class TreeEditorActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 11;
    private Tree tree;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Context mContext = TreeEditorActivity.this;
    private static final String TAG = "TreeEditorActivity";
    EditText etComments;
    Button btnadd;
    ArrayAdapter adapter;
    ImageView treePic;
    AutoCompleteTextView acDBH1;
    AutoCompleteTextView acDBH2;
    AutoCompleteTextView acDBH3;
    AutoCompleteTextView acDBH4;
    AutoCompleteTextView acDBH5;
    AutoCompleteTextView acDBH6;
    AutoCompleteTextView acDBH7;
    AutoCompleteTextView acDBH8;
    AutoCompleteTextView acDBH9;
    AutoCompleteTextView acDBH10;
    MultiAutoCompleteTextView acNote;
    AutoCompleteTextView acSpecies;
    AutoCompleteTextView acGrade;
    AutoCompleteTextView acStatus;
    AutoCompleteTextView acHealth;
    AutoCompleteTextView acTreeId;
    AutoCompleteTextView acProjectName;
    ArrayList<AutoCompleteTextView> dbhAutocompletes;
    ArrayList<AutoCompleteTextView> uiAutocompletes;
    LinearLayout imageContainer;

    Project project;

    FirebaseDatabase database;
    DatabaseReference databaseProjectsReference;
    DatabaseReference databaseReference;
    ArrayList<String> treeNumberList = new ArrayList<>();
    ArrayAdapter<String> treeIdAdaptor;

    HashMap<String, Project> projectNameToProject = new HashMap<>();
    HashMap<String, Project> projectIdToProject = new HashMap<>();
    ArrayList<String> projectNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tree = new Tree();

        setContentView(R.layout.activity_tree_editor);
        //Define the inputs
        acTreeId = findViewById(R.id.treeId);
        acProjectName = findViewById(R.id.projectName);
        acSpecies = findViewById(R.id.species_spinner);
        acGrade = findViewById(R.id.grade_spinner);
        acStatus = findViewById(R.id.status_spinner);
        acHealth = findViewById(R.id.health_spinner);
        acDBH1 = findViewById(R.id.dbh_spinner1);
        acDBH2 = findViewById(R.id.dbh_spinner2);
        acDBH3 = findViewById(R.id.dbh_spinner3);
        acDBH4 = findViewById(R.id.dbh_spinner4);
        acDBH5 = findViewById(R.id.dbh_spinner5);
        acDBH6 = findViewById(R.id.dbh_spinner6);
        acDBH7 = findViewById(R.id.dbh_spinner7);
        acDBH8 = findViewById(R.id.dbh_spinner8);
        acDBH9 = findViewById(R.id.dbh_spinner9);
        acDBH10 = findViewById(R.id.dbh_spinner10);
        acNote = findViewById(R.id.notes_spinner);
        etComments = findViewById(R.id.comments);
        imageContainer = findViewById(R.id.imageContainer);

        uiAutocompletes = new ArrayList<>();
        uiAutocompletes.add(acSpecies);
        uiAutocompletes.add(acGrade);
        uiAutocompletes.add(acStatus);
        uiAutocompletes.add(acHealth);
        uiAutocompletes.add(acHealth);
        uiAutocompletes.add(acDBH1);
        uiAutocompletes.add(acDBH2);
        uiAutocompletes.add(acDBH3);
        uiAutocompletes.add(acDBH4);
        uiAutocompletes.add(acDBH5);
        uiAutocompletes.add(acDBH6);
        uiAutocompletes.add(acDBH7);
        uiAutocompletes.add(acDBH8);
        uiAutocompletes.add(acDBH9);
        uiAutocompletes.add(acDBH10);
        uiAutocompletes.add(acNote);
        uiAutocompletes.add(acTreeId);
        uiAutocompletes.add(acProjectName);

        dbhAutocompletes = new ArrayList<AutoCompleteTextView>();
        dbhAutocompletes.add(acDBH1);
        dbhAutocompletes.add(acDBH2);
        dbhAutocompletes.add(acDBH3);
        dbhAutocompletes.add(acDBH4);
        dbhAutocompletes.add(acDBH5);
        dbhAutocompletes.add(acDBH6);
        dbhAutocompletes.add(acDBH7);
        dbhAutocompletes.add(acDBH8);
        dbhAutocompletes.add(acDBH9);
        dbhAutocompletes.add(acDBH10);

        /*Check to see if any project or tree info was passed to the activity and set values*/
        Intent passedIntent = getIntent();
        if (passedIntent.hasExtra("treeId")) {
            tree.setTreeId(passedIntent.getStringExtra("treeId"));
        }
        if (passedIntent.hasExtra("treeNumber")) {
            Integer treeNumber = passedIntent.getIntExtra("treeNumber", 0);
            tree.setTreeNumber(treeNumber);
            acTreeId.setText(treeNumber.toString());
        }
        if (passedIntent.hasExtra("projectId")) {
            tree.setProjectId(passedIntent.getStringExtra("projectId"));
        }
        if (passedIntent.hasExtra("projectName")) {
            String projectName = passedIntent.getStringExtra("projectName");
            tree.setProjectName(projectName);
            acProjectName.setText(projectName);
        }
        database = FirebaseDatabase.getInstance();
        hideSoftKeyboard();
        setupFirebaseAuth();
        //mAuth.signOut();


        databaseProjectsReference = FirebaseDatabase.getInstance().getReference("Projects");
        databaseReference = FirebaseDatabase.getInstance().getReference();


        /************  Set Project drop down ***********************/
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

        ArrayAdapter<String> projectNameAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, projectNames);
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
                if (projectNameToProject.containsKey(editable.toString())) {
                    project = projectNameToProject.get(editable.toString());
                    tree.setProjectName(project.getProjectName());
                    tree.setProjectId(project.getProjectId());
                    treeNumberList = new ArrayList<>();
                    treeNumberList.add("New");
                    for (int i = 1; i <= project.getNumberOfTrees(); i++) {
                        treeNumberList.add(Integer.toString(i));
                    }
                    treeIdAdaptor = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, treeNumberList);
                    acTreeId.setAdapter(treeIdAdaptor);
                } else {
                    Toast.makeText(TreeEditorActivity.this, "Please select a valid project name", Toast.LENGTH_SHORT).show();
                    tree.setProjectId(null);
                    tree.setProjectName(null);
                    treeNumberList = new ArrayList<>();
                }

            }
        });


        if (tree.getTreeId() != null) {
            getTree(databaseReference.child("Trees").child(tree.getTreeId()), new GetTreeInterface() {
                @Override
                public void onCallback(Tree treePulled) {
                    tree = treePulled;
                    acSpecies.setText(tree.getSpecies());
                    acGrade.setText(tree.getGrade());
                    acStatus.setText(tree.getStatus());
                    etComments.setText(tree.getComments());
                    addDBHToUi(tree.getDbhArray());
                    acNote.setText(tree.getNotes());
                    acHealth.setText(tree.getHealth());
                    tree.addTreeImagesToContainer(imageContainer);
                }
            });
        }




        /*Set dropdown input pre-set values*/
        adapter = ArrayAdapter.createFromResource(this, R.array.species_options, android.R.layout.simple_spinner_item);
        acSpecies.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.grade_options, android.R.layout.simple_spinner_item);
        acGrade.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.status_options, android.R.layout.simple_spinner_item);
        acStatus.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.health_options, android.R.layout.simple_spinner_item);
        acHealth.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.numeric_options, android.R.layout.simple_spinner_item);
        for (final AutoCompleteTextView dbhElement : dbhAutocompletes) {
            dbhElement.setAdapter(adapter);
        }
        adapter = ArrayAdapter.createFromResource(this, R.array.note_options, android.R.layout.simple_spinner_item);
        acNote.setAdapter(adapter);
        acNote.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        treeIdAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, treeNumberList);
        acTreeId.setAdapter(treeIdAdaptor);


        //** "dumb" way to populate if an existing id is selected i.e. reload entire activity*/
        acTreeId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if (tree.getProjectId() != null && isInteger(editable.toString())) {
                    databaseReference.child("Trees").child(tree.getProjectId() + "_" + editable.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Bundle treeInfBundle = new Bundle();
                                treeInfBundle.putString("treeId", tree.getProjectId() + "_" + editable.toString());
                                treeInfBundle.putInt("treeNumber", Integer.parseInt(editable.toString()));
                                treeInfBundle.putString("projectId", tree.getProjectId());
                                treeInfBundle.putString("projectName", tree.getProjectName());
                                Intent intent = new Intent(TreeEditorActivity.this, TreeEditorActivity.class);
                                intent.putExtras(treeInfBundle);
                                startActivity(intent);

                            } else {
                                Toast.makeText(TreeEditorActivity.this, "Will create new tree", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(TreeEditorActivity.this, "Will create new tree", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* add click listeners  to show dropdown on click*/
        for (final AutoCompleteTextView uiElement : uiAutocompletes) {
            uiElement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uiElement.showDropDown();
                }
            });
        }

        AutoCompleteTextView baseElement = null;
        AutoCompleteTextView nextElement = null;
        AutoCompleteTextView secondElement =null;
        /* Make DBH visible listeners  to show dropdown on click*/
        for ( Integer i=0; i<uiAutocompletes.size(); i++) {
            baseElement = uiAutocompletes.get(i);
            if (i+1 < uiAutocompletes.size()){
                nextElement = uiAutocompletes.get(i+1);
            }
            if (i+2 < uiAutocompletes.size()){
                secondElement = uiAutocompletes.get(i+2);
            }
            final AutoCompleteTextView finalNextElement = nextElement;
            final AutoCompleteTextView finalSecondElement = secondElement;
            baseElement.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(finalNextElement!=null){
                        finalNextElement.setVisibility(View.VISIBLE);
                    }
                    if(finalSecondElement!=null){
                        finalSecondElement.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }
        /* sets a listener on the Take Picture button and launches the native camera app*/
        Button btnTakePicture = (Button) findViewById(R.id.take_picture);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(TreeEditorActivity.this.getPackageManager()) != null) {
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
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle treeInfBundle = new Bundle();
                if (tree.getProjectId() != null) {
                    treeInfBundle.putString("projectId", tree.getProjectId());
                    if (tree.getTreeId() != null) {
                        treeInfBundle.putString("treeId", tree.getTreeId());
                    }
                    if (tree.getTreeNumber() != null) {
                        treeInfBundle.putInt("treeNumber", tree.getTreeNumber());
                    }
                    if (tree.getProjectName() != null) {
                        treeInfBundle.putString("projectName", tree.getProjectName());
                    }
                }
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(TreeEditorActivity.this, TreeEditorActivity.class);
                        intent.putExtras(treeInfBundle);
                        startActivity(intent);

                        break;

                    case R.id.viewTree:
                        Intent intent1 = new Intent(TreeEditorActivity.this, TreeDBViewer.class);
                        intent1.putExtras(treeInfBundle);
                        startActivity(intent1);

                        break;

                    case R.id.viewMap:
                        Intent intent2 = new Intent(TreeEditorActivity.this, MapViewer.class);
                        intent2.putExtras(treeInfBundle);
                        startActivity(intent2);

                        break;

                    case R.id.signOut:
                        mAuth.signOut();
                        break;


                }

                return false;
            }


        });

    }

    private void addDBHToUi(List dbhArray) {
        Integer i;
        //Set the value of the DBH input and change visibilty
        for (i = 0; i < dbhArray.size(); i++) {
            dbhAutocompletes.get(i).setText(dbhArray.get(i).toString());
            dbhAutocompletes.get(i).setVisibility(View.VISIBLE);
        }
        // Set the next input boxe to visibile incase more edits are necessary
        if (i < 8) {
            dbhAutocompletes.get(i + 1).setVisibility(View.VISIBLE);
        }

    }

    private List<Double> consolidateDBH() {
        List<Double> acDBHArray = new ArrayList<>();
        for (AutoCompleteTextView dbhElement : dbhAutocompletes) {
            if (canBeDouble(dbhElement.getText().toString())) {
                acDBHArray.add(Double.parseDouble(dbhElement.getText().toString()));
            }
        }
        return acDBHArray;
    }

    ;

    public void btnAddToDatabase(View view) {
        if (tree.getProjectId()==null){
            Toast.makeText(TreeEditorActivity.this, "Please select a valid Project", Toast.LENGTH_SHORT).show();
        } else {
            // Populate the tree from the UI set values
            tree.setSpecies(acSpecies.getText().toString());
            tree.setGrade(acGrade.getText().toString());
            tree.setStatus(acStatus.getText().toString());
            tree.setHealth(acHealth.getText().toString());
            tree.setDbhArray(consolidateDBH());
            tree.setNotes(acNote.getText().toString());
            tree.setComments(etComments.getText().toString());

            //If there is no id, sets the id to the 1+ the max key in the system.
            if (tree.getTreeId() == null) {
                Toast.makeText(TreeEditorActivity.this, "New tree created", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TreeEditorActivity.this, "Updat Tree #" + tree.getTreeNumber().toString(), Toast.LENGTH_SHORT).show();
            }
            Boolean result = tree.pushToFirebase(databaseReference);

            if (result) {
                Toast.makeText(TreeEditorActivity.this, "Tree Created Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TreeEditorActivity.this, "Error uploading tree, Please try again", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(TreeEditorActivity.this, TreeEditorActivity.class);
            tree = new Tree();
            startActivity(intent);
        }
    }

    private void configurebtnRequestLocation() {
    }

    public Boolean canBeDouble(String dbhString) {
        try {
            Double.parseDouble(dbhString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == TreeEditorActivity.RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = new ImageView(imageContainer.getContext());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setImageBitmap(imageBitmap);
            imageContainer.addView(imageView);
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
                tree.setTreeId(snapshot.getKey());
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
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /**
     * checks to see if the @param 'user' is logged in
     *
     * @param user: the user id string
     */
    private void checkCurrentUser(FirebaseUser user) {
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if (user == null) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth() {
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

        }
    }
}

