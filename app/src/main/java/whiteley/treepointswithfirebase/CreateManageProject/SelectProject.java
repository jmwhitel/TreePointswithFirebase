package whiteley.treepointswithfirebase.CreateManageProject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import whiteley.treepointswithfirebase.Login.LoginActivity;
import whiteley.treepointswithfirebase.Models.Project;
import whiteley.treepointswithfirebase.R;
import whiteley.treepointswithfirebase.TreeEditorActivity;

public class SelectProject  extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context mContext = SelectProject.this;

    Spinner spProjectSelect;

    FirebaseDatabase database;
    DatabaseReference databaseProjectsReference;
    DatabaseReference databaseReference;
    ArrayList<String> projectNames = new ArrayList<>();
    HashMap<String, Project> projectNameToProject = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_dashboard);

        spProjectSelect = findViewById(R.id.projectSpinner);

        database = FirebaseDatabase.getInstance();
        setupFirebaseAuth();

        databaseProjectsReference = FirebaseDatabase.getInstance().getReference("Projects");
        databaseReference = FirebaseDatabase.getInstance().getReference();


        final ValueEventListener getProjectNames = databaseProjectsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot project : dataSnapshot.getChildren()) {
                    projectNameToProject.put(project.child("projectName").getValue().toString(), project.getValue(Project.class));
                    projectNames.add(project.child("projectName").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        final ArrayList<String> projectList = new ArrayList<>();
        databaseProjectsReference.child("projectName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Iterator<DataSnapshot> iterable = dataSnapshot.getChildren().iterator();
                    for (Iterator<DataSnapshot> it = iterable; it.hasNext(); ) {
                        DataSnapshot dataSnapshot1 = it.next();
                        projectList.add(dataSnapshot1.getValue(String.class));
                    }


                    ArrayAdapter<String> projectList = new ArrayAdapter<String>(SelectProject.this, android.R.layout.simple_spinner_item, projectNames);
                    projectList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spProjectSelect.setAdapter(projectList);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





            Button btntreeViewer = (Button) findViewById(R.id.btn_treeViewer);
            btntreeViewer.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent intent = new Intent(SelectProject.this, TreeEditorActivity.class);
                                                     startActivity(intent);
                                                 }
                                             }
            );

        Button btncreateProject = (Button) findViewById(R.id.btnCreateProject);
        btncreateProject.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                Intent intent = new Intent(SelectProject.this, CreateProject.class);
                                                 startActivity(intent);
                                             }
                                         }
        );
        }


    private void checkCurrentUser(FirebaseUser user) {

        if (user == null) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }


    /**
     * Setup the firebase auth object
     */
    public void setupFirebaseAuth() {

        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in
                checkCurrentUser(user);

                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
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


