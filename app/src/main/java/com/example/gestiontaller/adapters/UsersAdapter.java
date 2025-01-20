package com.example.gestiontaller.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gestiontaller.R;
import com.example.gestiontaller.data_classes.User;

import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<User> {
    private ArrayList<User> data;
    private boolean showCheck;
    public UsersAdapter(Context context, ArrayList<User> data) {
        super(context, R.layout.admin_user_list_item, data);
        this.data = data;
        showCheck = true;
    }
    public UsersAdapter(Context context, ArrayList<User> data, boolean showCheck) {
        super(context, R.layout.admin_user_list_item, data);
        this.data = data;
        this.showCheck = showCheck;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater mostrado = LayoutInflater.from(getContext());
            View element = mostrado.inflate(R.layout.admin_user_list_item, parent, false);
            TextView userFullName = element.findViewById(R.id.userFullName);
            userFullName.setText(data.get(position).getFullName());
            TextView userJobName = element.findViewById(R.id.userJobName);
            userJobName.setText(data.get(position).getJobName());
            ImageView userPicture = element.findViewById(R.id.userProfilePicture);
            Log.i("item", "item creado");

            CheckBox userSelected = element.findViewById(R.id.selectedCheckBox);
            userSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(b){
                        userPicture.setBackgroundTintList(ColorStateList.valueOf(element.getResources().getColor(R.color.softBlue)));
                    }else{
                        userPicture.setBackgroundTintList(ColorStateList.valueOf(element.getResources().getColor(R.color.softGreen)));
                    }
                }
            });

            if(!showCheck){
                userSelected.setVisibility(View.INVISIBLE);
            }
            element.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userSelected.setChecked(!userSelected.isChecked());
                }
            });
            return element;
    }

}
