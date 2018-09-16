package whiteley.treepointswithfirebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import whiteley.treepointswithfirebase.Models.Tree;


//adapted from https://stackoverflow.com/questions/15297840/populate-listview-from-arraylist-of-objects
public class AdapterTree extends ArrayAdapter<Tree>{
    private Activity activity;
    private ArrayList<Tree> lTree;
    private static LayoutInflater inflater = null;

    public AdapterTree (Activity activity, int textViewResourceId, ArrayList<Tree> _lTree) {
        super(activity, textViewResourceId, _lTree);
        try {
            this.activity = activity;
            this.lTree = _lTree;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return lTree.size();
    }

    //This seems wrong... but rolling with it
    public Tree getItem(Tree position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_status;
        public TextView display_rating;
        public TextView display_grade;
        public TextView display_species;
        public TextView display_comments;
        public Button update_button;
        public LinearLayout tree_image_container;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.fragment_tree_item, null);
                holder = new ViewHolder();

                holder.display_status= (TextView) vi.findViewById(R.id.display_status);
                holder.display_rating= (TextView) vi.findViewById(R.id.display_rating);
                holder.display_grade = (TextView) vi.findViewById(R.id.display_grade);
                holder.display_species = (TextView) vi.findViewById(R.id.display_species);
                holder.display_comments= (TextView) vi.findViewById(R.id.display_comments);
                holder.update_button = (Button) vi.findViewById(R.id.btn_update_tree);
                holder.tree_image_container = (LinearLayout) vi.findViewById(R.id.tree_image_list);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }


            holder.display_status.setText(lTree.get(position).getStatus());
            holder.display_rating.setText(lTree.get(position).getHealth());
            holder.display_grade.setText(lTree.get(position).getGrade());
            holder.display_species.setText(lTree.get(position).getSpecies());
            holder.display_comments.setText("Comments: " + lTree.get(position).getComments().toString());
            holder.update_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), TreeEditorActivity.class);
                    Bundle treeInf=new Bundle();
                    treeInf.putInt("treeNumber",lTree.get(position).getTreeNumber());
                    treeInf.putString("treeId",lTree.get(position).getTreeId());
                    treeInf.putString("projectId",lTree.get(position).getProjectId());
                    treeInf.putString("projectName",lTree.get(position).getProjectName());
                    intent.putExtras(treeInf);
                    getContext().startActivity(intent);
                }
            });

            lTree.get(position).addTreeImagesToContainer(holder.tree_image_container);

        } catch (Exception e) {


        }
        return vi;
    }
}