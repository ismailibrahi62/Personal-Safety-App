package com.ismail.personalsafety.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ismail.personalsafety.ModelClasses.Request;
import com.ismail.personalsafety.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<Request> requestList;

    public RequestAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_read, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requestList.get(position);
        holder.emergencyType.setText(request.getEmergencyType());
        holder.userName.setText(request.getUserName());
        holder.district.setText(request.getDistrict());
        holder.userPhone.setText(request.getUserPhoneNum());
        Picasso.get().load(request.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView emergencyType, userName, district, userPhone;
        ImageView imageView;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            emergencyType = itemView.findViewById(R.id.typeOfEm);
            userName = itemView.findViewById(R.id.name_user);
            district = itemView.findViewById(R.id.district_name);
            userPhone = itemView.findViewById(R.id.status);
            imageView = itemView.findViewById(R.id.emergencyImage);
        }
    }
}

