package com.example.examfinal.services;

import com.example.examfinal.models.Contacto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ContactoService {

    @GET("N00034861/contacts")
    Call<List<Contacto>> getAll();

    @POST("N00034861/contacts")
    Call<Contacto> createContacto(@Body Contacto contacto);
}
