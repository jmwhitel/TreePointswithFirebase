package whiteley.treepointswithfirebase.CreateManageProject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import whiteley.treepointswithfirebase.Login.LoginActivity;
import whiteley.treepointswithfirebase.Models.UserProjects;
import whiteley.treepointswithfirebase.Models.Project;
import whiteley.treepointswithfirebase.R;
import whiteley.treepointswithfirebase.TreeEditorActivity;

public class CreateProject extends AppCompatActivity {

    Spinner spTCEstitmate;
    Spinner spAcrEstimate;
    ArrayAdapter<CharSequence> adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_project);

        hideSoftKeyboard();

        spTCEstitmate = (Spinner) findViewById(R.id.spTreeCountEst);
        adapter = ArrayAdapter.createFromResource(this, R.array.tree_count_estimate, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTCEstitmate.setAdapter(adapter);
        spTCEstitmate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position)+" selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spAcrEstimate = (Spinner) findViewById(R.id.spAcreEst);
        adapter = ArrayAdapter.createFromResource(this, R.array.acreage_estimate, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAcrEstimate.setAdapter(adapter);
        spAcrEstimate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position)+" selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    Button btntreeViewer = (Button) findViewById(R.id.btn_treeViewer);
            btntreeViewer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CreateProject.this, TreeEditorActivity.class);
            startActivity(intent);
        }
    }
            );


}
    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
