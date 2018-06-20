package whiteley.treepointswithfirebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class TreeDBViewer extends AppCompatActivity {
    private static final String TAG = "Tree Viewer";
    private ListView listView;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private ArrayList<Tree> mTreeList = new ArrayList<>();
    private Context mContext = TreeDBViewer.this;
    DatabaseReference databaseTreeReference;
    DatabaseReference databaseProjectsReference;
    String projectId;
    String projectName;
    HashMap<String, String> projectNameToId = new HashMap<>();
    HashMap<String, String> projectIdToMap = new HashMap<>();
    ArrayList<String> projectNames = new ArrayList<>();
    AutoCompleteTextView acProjectNames;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_tree_viewer);
        acProjectNames = (AutoCompleteTextView) findViewById(R.id.projectName);
        Intent passedIntent = getIntent();
        if(passedIntent.hasExtra("projectId")){
            projectId=passedIntent.getStringExtra("projectId");
        }
        if(passedIntent.hasExtra("projectName")){
            projectName=passedIntent.getStringExtra("projectName");
            acProjectNames.setText(projectName);
        }

        database = FirebaseDatabase.getInstance();
        databaseTreeReference = FirebaseDatabase.getInstance().getReference("Trees");
        databaseProjectsReference = FirebaseDatabase.getInstance().getReference("Projects");
        /********************  Set Project drop down ************************/
        final ValueEventListener getProjectNames = databaseProjectsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot project : dataSnapshot.getChildren()) {
                    projectNameToId.put(project.child("projectName").getValue().toString(), project.getKey());
                    projectIdToMap.put(project.getKey(), project.child("projectName").getValue().toString());
                    projectNames.add(project.child("projectName").getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter projectNameAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, projectNames);
        acProjectNames.setAdapter(projectNameAdaptor);
        acProjectNames.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(projectNameToId.containsKey(editable.toString())){
                    projectName=editable.toString();
                    projectId=projectNameToId.get(editable.toString());
                    updateTreeList(projectId);
                } else{
                    Toast.makeText(TreeDBViewer.this, "Please select a valid project name", Toast.LENGTH_SHORT).show();
                    projectId=null;
                    projectName=null;
                    listView = (ListView) findViewById(R.id.listViewTrees);
                    listView.setAdapter(null);
                }

            }
        });

        if (projectId!=null){
            updateTreeList(projectId);
        }



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle projectInfBundle = new Bundle();
                if(projectId != null) {
                    projectInfBundle.putString("projectId", projectId);
                }
                if(projectName != null) {
                    projectInfBundle.putString("projectName", projectName);
                }
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(TreeDBViewer.this, TreeEditorActivity.class);
                        intent.putExtras(projectInfBundle);
                        startActivity(intent);

                        break;

                    case R.id.viewTree:

                        break;

                    case R.id.viewMap:
                        Intent intent2 = new Intent(TreeDBViewer.this, MapViewer.class);
                        intent2.putExtras(projectInfBundle);
                        startActivity(intent2);

                        break;

                    case R.id.signOut:
                        //mAuth.signOut();
                        break;
                }

                return false;
            }
        });
    }
    private void updateTreeList(String projectId){
        /***** Find all the trees for a given project  *******/
        // TODO: Sort by location using GeoFire
        Query projectTrees = databaseTreeReference.orderByChild("projectId").equalTo(projectId);
        final ArrayList<Tree> listOfTrees  = new ArrayList<Tree>();

        listView = (ListView) findViewById(R.id.listViewTrees);



        projectTrees.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG,"on child add data snapshot: "+dataSnapshot.getKey());


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Tree tree = snapshot.getValue(Tree.class);
                    tree.setTreeId(snapshot.getKey());
                    listOfTrees.add(tree);
                }

                AdapterTree adbTree= new AdapterTree (TreeDBViewer.this, 0, listOfTrees);
                listView.setAdapter(adbTree);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Viewer","ERORR");
            }
        });
    }

}

