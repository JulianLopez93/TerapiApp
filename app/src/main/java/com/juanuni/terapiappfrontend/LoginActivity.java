package com.juanuni.terapiappfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewForgotPassword;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Obtener referencias a los elementos del formulario
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Agregar listener para el botón de inicio de sesión
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                // Aquí puedes agregar la lógica de validación de inicio de sesión
                // por ejemplo, puedes verificar si los campos de correo electrónico
                // y contraseña no están vacíos, y luego hacer una solicitud de inicio de
                // sesión a un servidor o base de datos.

                // Si las credenciales son válidas, puedes iniciar la actividad principal
                Intent intent = new Intent(LoginActivity.this, ServicesListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Agregar listener para la opción de olvidar contraseña
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí puedes agregar la lógica para la opción de olvidar contraseña
                // por ejemplo, puedes abrir una actividad para restablecer la contraseña.
            }
        });

        // Agregar listener para el botón de registro
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí puedes agregar la lógica para el botón de registro
                // por ejemplo, puedes abrir una actividad de registro.
            }
        });
    }
}