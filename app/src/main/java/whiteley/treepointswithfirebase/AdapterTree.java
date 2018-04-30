package whiteley.treepointswithfirebase;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


//adapted from https://stackoverflow.com/questions/15297840/populate-listview-from-arraylist-of-objects
public class AdapterTree extends ArrayAdapter<Tree>{
    private Activity activity;
    private ArrayList<Tree> lTree;
    private static LayoutInflater inflater = null;

    public AdapterTree (Activity activity, int textViewResourceId,ArrayList<Tree> _lTree) {
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
        public TextView display_latitude;
        public TextView display_longitude;
        public TextView display_species;
        public TextView display_notes;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.fragment_tree_item, null);
                holder = new ViewHolder();

                holder.display_status= (TextView) vi.findViewById(R.id.display_status);
                holder.display_rating= (TextView) vi.findViewById(R.id.display_rating);
                holder.display_grade = (TextView) vi.findViewById(R.id.display_grade);
                holder.display_latitude = (TextView) vi.findViewById(R.id.display_latitude);
                holder.display_longitude = (TextView) vi.findViewById(R.id.display_longitude);
                holder.display_species = (TextView) vi.findViewById(R.id.display_species);
                holder.display_notes= (TextView) vi.findViewById(R.id.display_notes);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }


            holder.display_status.setText(lTree.get(position).getStatus());
            holder.display_rating.setText(lTree.get(position).getHealth());
            holder.display_grade.setText(lTree.get(position).getGrade());
            holder.display_latitude.setText(String.valueOf(lTree.get(position).getLatitude()).substring(0,6));
            holder.display_longitude.setText(String.valueOf(lTree.get(position).getLongitude()).substring(0,6));
            holder.display_species.setText(lTree.get(position).getSpecies());
            holder.display_notes.setText(lTree.get(position).getNotes().toString());

        } catch (Exception e) {


        }
        return vi;
    }
}