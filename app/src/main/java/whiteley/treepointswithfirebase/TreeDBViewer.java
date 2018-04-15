package whiteley.treepointswithfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TreeDBViewer extends AppCompatActivity {
    private static final String TAG = "Tree Viewer";
    private ListView listView;
    FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<Tree> mTreeList = new ArrayList<>();
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_viewer);
        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // TODO: Sort by location using GeoFire
        Query allTrees = databaseReference.child("Tree Point");
        final ArrayList<Tree> listOfTrees  = new ArrayList<Tree>();

        //then populate myListItems




          listView = (ListView) findViewById(R.id.listViewTrees);
//        final ArrayAdapter<Tree> arrayAdapter = new ArrayAdapter<Tree>(this, android.R.layout.simple_list_item_1, mTreeList);
//        listView.setAdapter(arrayAdapter);


        allTrees.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG,"on child add data snapshot: "+dataSnapshot.getKey());


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Tree tree = snapshot.getValue(Tree.class);
                    tree.setKey(snapshot.getKey());
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



//        allTrees.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.i(TAG,"on child add data snapshot: "+dataSnapshot.getKey());
//                Tree tree = dataSnapshot.getValue(Tree.class);
//                listOfTrees.add(tree);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });




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
                        Intent intent = new Intent(TreeDBViewer.this, MainActivity.class);
                        startActivity(intent);

                        break;

                    case R.id.viewTree:

                        break;
                }

                return false;
            }
        });
    }
}


