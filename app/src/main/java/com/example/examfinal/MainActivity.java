package com.example.examfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.examfinal.adapters.ContactoAdapter;
import com.example.examfinal.models.Contacto;
import com.example.examfinal.services.ContactoService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ContactoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton botonCrear = findViewById(R.id.btnCrearContacto);
        mRecyclerView = findViewById(R.id.rvContactos);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.sky4dev.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ContactoService service = retrofit.create(ContactoService.class);
        Call<List<Contacto>> contactos = service.getAll();

        contactos.enqueue(new Callback<List<Contacto>>() {
            @Override
            public void onResponse(Call<List<Contacto>> call, Response<List<Contacto>> response) {
                List<Contacto> data = response.body();
                Log.i("LOG_SUCCES", String.valueOf(data.size()));
                mAdapter = new ContactoAdapter(data);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<Contacto>> call, Throwable t) {
                Log.i("LOG_ERROR", call.toString());
            }
        });

        botonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CrearContactoActivity.class);
                startActivity(intent);
            }
        });
    }
}
