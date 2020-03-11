package org.electromob.tracing;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder>
{
    ArrayList<createuser> namelist;
    Context c;
    createuser currentuserobj;

    MembersAdapter(ArrayList<createuser> namelist, Context c)
    {
        this.namelist = namelist;
        this.c = c;
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
        MembersViewHolder membersViewHolder = new MembersViewHolder(v,c,namelist);

        return membersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {

        currentuserobj = namelist.get(position);
        holder.name.setText(currentuserobj.name);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(c,currentuserobj.name

                        + "  Retriving Location ..... ",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(c,userlocation.class);
                intent.putExtra("name",currentuserobj.name);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                c.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }

    public static class MembersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name;
        ImageView status;
        View v;
        Context c;
        ArrayList<createuser> namearraylist;
        FirebaseAuth auth;
        FirebaseUser user;

        public MembersViewHolder(View itemView, Context c,ArrayList<createuser> namearraylist)
        {
            super(itemView);
            this.c = c;
            this.namearraylist = namearraylist;

            itemView.setOnClickListener(this);
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            name = itemView.findViewById(R.id.itemtittle);
            //status = itemView.findViewById(R.id.status);
        }


        @Override
        public void onClick(View view) {
            Toast.makeText(c,"click on the name to retrive user location",Toast.LENGTH_LONG).show();
        }
    }


}
