package com.example.examfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.examfinal.models.Contacto;
import com.example.examfinal.services.ContactoService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearContactoActivity extends AppCompatActivity {

    public static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final int PICK_IMAGE_CAMERA = 0;
    public static final int PICK_IMAGE_GALLERY = 1;

    EditText Nombres, Correo, Telefono;

    private Bitmap bitmap;
    private File carpeta;
    private String RutaDeImagen;
    private ImageView nuevaImagen;
    private Drawable imagenActual;
    private String ImagenEnBase64;
    private byte[] ImagenEnBits;
    private Bitmap ImagenDecodificadaDe64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_contacto);
        GuardarContacto();
    }

    private void GuardarContacto() {
        Nombres = findViewById(R.id.etNombre);
        Correo = findViewById(R.id.etCorreo);
        Telefono = findViewById(R.id.etTelefono);
        nuevaImagen = findViewById(R.id.ivfotocontc);
        Button btnImangen = findViewById(R.id.btnImangen);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        btnImangen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clic: ", "Clic a imagen");
                RevisarPermisosCamara();
                EscogerMetodoFoto();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidarCampos()) {
                    MandarDatosParaGuardarContacto();
                } else {
                    Toast.makeText(CrearContactoActivity.this, "Todos los campos tienen que estar llenos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean ValidarCampos() {
        if (nuevaImagen.getDrawable() == imagenActual) {
            Toast.makeText(CrearContactoActivity.this, "Tiene que cambiar la imagen", Toast.LENGTH_LONG).show();
            return false;
        }
        if (Nombres.getText().toString().isEmpty()) {
            Toast.makeText(CrearContactoActivity.this, "El Nombre no puede estar vacio", Toast.LENGTH_LONG).show();
            return false;
        }
        if (Correo.getText().toString().isEmpty()) {
            Toast.makeText(CrearContactoActivity.this, "El Correo no puede estar vacio", Toast.LENGTH_LONG).show();
            return false;
        }
        if (Telefono.getText().toString().isEmpty()) {
            Toast.makeText(CrearContactoActivity.this, "El Telefono no puede estar vacio", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void MandarDatosParaGuardarContacto() {
        try {

            Contacto nuevoContacto = new Contacto();
            Log.d("Patch: ", RutaDeImagen);
            nuevoContacto.setImagen(ImagenEnBase64);
            nuevoContacto.setNombre(Nombres.getText().toString());
            nuevoContacto.setCorreo(Correo.getText().toString());
            nuevoContacto.setTelefono(Telefono.getText().toString());

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.sky4dev.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ContactoService service = retrofit.create(ContactoService.class);
            service.createContacto(nuevoContacto).enqueue(new Callback<Contacto>() {

                @Override
                public void onResponse(Call<Contacto> call, Response<Contacto> response) {

                }

                @Override
                public void onFailure(Call<Contacto> call, Throwable t) {

                }
            });

            Toast.makeText(CrearContactoActivity.this, "Se ha guardado el contacto", Toast.LENGTH_LONG).show();
            Log.d("Succes Guardar: ", "Se esta guardando");
            Log.d("Succes Guardar: ", "Guardado");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } catch (Exception error) {
            Log.e("Error: ", error.getMessage());
        }
    }

    private void RevisarPermisosCamara() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void EscogerMetodoFoto() {
        try {
            final CharSequence[] options = {"Camara", "Galeria", "Cancelar"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Escoja su opcion:");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Camara")) {
                        dialog.dismiss();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, PICK_IMAGE_CAMERA);
                    } else if (options[item].equals("Galeria")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                    } else if (options[item].equals("Cancelar")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Log.e("Actividad:", "Tomando desde la camara ");
                bitmap = (Bitmap) data.getExtras().get("data");
                CodificarBase64();

                String fechaActual = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                carpeta = new File(Environment.getExternalStorageDirectory() +
                        getString(R.string.app_name), "IMG_" + fechaActual + ".jpg");

                RutaDeImagen = carpeta.getAbsolutePath();
                Log.d("Path: ", RutaDeImagen);
                DecodificarBase64();
                nuevaImagen.setImageBitmap(ImagenDecodificadaDe64);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                Log.e("Actividad:", "Selecionar imagen de galeria ");
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                CodificarBase64();
                RutaDeImagen = getRutaRealDeUri(selectedImage);
                carpeta = new File(RutaDeImagen);
                Log.d("Ruta: ", carpeta.toString());
                Log.d("Path: ", RutaDeImagen);
                DecodificarBase64();
                nuevaImagen.setImageBitmap(ImagenDecodificadaDe64);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void DecodificarBase64() {
        ImagenEnBits = Base64.decode(ImagenEnBase64, Base64.DEFAULT);
        ImagenDecodificadaDe64 = BitmapFactory.decodeByteArray(ImagenEnBits, 0, ImagenEnBits.length);
        Log.e("Imagen en bits", ImagenDecodificadaDe64.toString());
    }

    private void CodificarBase64() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        ImagenEnBits = baos.toByteArray();
        ImagenEnBase64 = Base64.encodeToString(ImagenEnBits, Base64.DEFAULT);
        Log.d("Base 64: ", ImagenEnBase64);
    }

    public String getRutaRealDeUri(Uri contenidoUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contenidoUri, proj, null, null, null);
        int indiceDeColumna = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(indiceDeColumna);
    }
}