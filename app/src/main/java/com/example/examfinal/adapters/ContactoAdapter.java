package com.example.examfinal.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examfinal.DetalleContactoActivity;
import com.example.examfinal.R;
import com.example.examfinal.models.Contacto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder> {

    private List<Contacto> mData;

    public ContactoAdapter(List<Contacto> mData) {
        this.mData = mData;
    }

    public ContactoAdapter() {
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_contacto, parent,false);
        return new ContactoViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        ImageView ivAvatar = holder.mView.findViewById(R.id.ivAvatar);
        TextView tvNombre = holder.mView.findViewById(R.id.tvNombres);
        TextView tvTelefono = holder.mView.findViewById(R.id.tvTelefono);
        Contacto contacto = mData.get(position);

        tvNombre.setText(contacto.nombre);
        tvTelefono.setText(contacto.telefono);
        Picasso.get().load(contacto.imagen).into(ivAvatar);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.context, DetalleContactoActivity.class);
                intent.putExtra("ContactoPos", position);
                holder.context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ContactoViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public Context context;

        public ContactoViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            mView = itemView;
            this.context = context;
        }
    }
}
