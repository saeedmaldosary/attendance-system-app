package com.example.StudentsAttendanceSystemFacialRecognition.NamesAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.example.StudentsAttendanceSystemFacialRecognition.DetectedNames;
import com.example.StudentsAttendanceSystemFacialRecognition.R;

import java.util.ArrayList;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.ViewHolder> {

    private ArrayList<String> mData;
    private ArrayList<String> mData2;
    private LayoutInflater mInflater;
    private Context mContext;

    // data is passed into the constructor


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lytnames, parent, false);
        return new ViewHolder(view);
    }

    public NameAdapter(Context context, ArrayList<String> data, ArrayList<String> data2) {
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mData = data;
        this.mData2 = data2;
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.checkBoxName.setText(mData.get(position));


        holder.checkBoxName.setChecked(true);

        holder.checkBoxName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {



                    String name = mData.get(position);
                //    mData.remove(name);
                    mData2.add(name);





                // DO YOU ACTION
            }
        });


    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxName;
        DetectedNames detectedNames = new DetectedNames();


        ViewHolder(View itemView) {
            super(itemView);
            checkBoxName = itemView.findViewById(R.id.checkboxName);


        }

    }

}