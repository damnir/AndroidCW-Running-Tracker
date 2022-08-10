package com.example.mdp_cw2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mdp_cw2.Entities.Run;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
data adapter that keeps all run entries used in the RecyclerView and sets on click listeners for them
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>{


    private List<Run> data;
    private Context context;
    private LayoutInflater layoutInflater;
    //item click listeners for specific entires/favourite button
    private DataAdapter.ItemClickListener clickListener;
    private DataAdapter.ItemClickListener favouriteListener;


    public DataAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public DataAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the itemView
        View itemView = layoutInflater.inflate(R.layout.runs_view, parent, false);
        return new DataAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.DataViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //set the data once observer has notified in the activity
    public void setData(List<Run> newData) {
        if (data != null) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            data = newData;
        }
    }

    //recycler viewholder
    class DataViewHolder extends RecyclerView.ViewHolder{

        TextView dateText;
        TextView distanceText;
        TextView durationText;
        ToggleButton favButton;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            //references to all fields in the view
            dateText = itemView.findViewById(R.id.rv_date);
            distanceText = itemView.findViewById(R.id.rv_distance);
            durationText = itemView.findViewById(R.id.rv_time);
            favButton = itemView.findViewById(R.id.toggleButton);

            //on click listener returns position of the item (run id)
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                clickListener.onItemClick(data.get(position));
            });
        }

        public void bind(Run run) {
            if(run != null) {
                //UPDATE the view UI elements if run != 0
                double time = run.getTime();
                String duration = String.format("%02d:%02d:%02d",
                        TimeUnit.SECONDS.toHours((long) time),
                        TimeUnit.SECONDS.toMinutes((long) time) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours((long) time)),
                        TimeUnit.SECONDS.toSeconds((long) time) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes((long) time)));

                durationText.setText(duration);
                distanceText.setText( Math.round((run.getDistance()/1000) * 100.00)/100.00 + "km" );
                dateText.setText(run.getDate());

                //activate the button if the run is marked as favourite
                if (run.getFavourite()){
                    favButton.setChecked(true);
                }

                //set the on click listener for the favourite button and update the run accordingly
                favButton.setOnClickListener(view -> {
                    int position = getAdapterPosition();
                    favouriteListener.onItemClick(data.get(position));

                    if(favButton.isChecked()){
                        run.setFavourite(true);
                    }
                    else
                        run.setFavourite(false);
                });
            }
        }
    }

    //entry click listener
    void setClickListener(DataAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    //favourite button click listener
    void setFavouriteListener(DataAdapter.ItemClickListener favClickListener) {
        this.favouriteListener = favClickListener;
    }

    //interface to be implemented in the main activity to register item clicks from the adapter list
    public interface ItemClickListener {
        void onItemClick(Run run);
    }

}
