package com.example.mdp_cw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mdp_cw2.ViewModels.AllRunsViewModel;

/*
this activity displays all of Run logs in the database and let's the user pick a specific run
to view. once chosen, the map activity is started using the specific run ID.
 */
public class AllRunsActivity extends AppCompatActivity {

    private AllRunsViewModel viewModel;
    DataAdapter adapter;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_runs);

        //find the recycler view - all entries displayed in the recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //create a new data adapter and set it on the recycler view
        adapter = new DataAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AllRunsViewModel.class);

        //observe the allRuns data from the viewModel, once notified, set the adapter data
        viewModel.getAllRuns().observe(this, runs ->
                adapter.setData(runs));

        //set an on click listener from the adapter for all entires, start the map activity
        //with the chosen Run ID.
        adapter.setClickListener(run -> {
            Intent intent = new Intent(AllRunsActivity.this, MapActivity.class);
            intent.putExtra("runID", run.get_id()); //put the id in intent as extra
            //startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
            startActivity(intent);
        });

        //set another listener for the favourite button within the adapter,
        //if button clicked, update the run accordingly; makr as favourite/un favourite
        adapter.setFavouriteListener(run -> {
            viewModel.update(run);
        });

    }

    public void onBackClicked(View v) {
        //close the activity on back button click
        finish();
    }
}