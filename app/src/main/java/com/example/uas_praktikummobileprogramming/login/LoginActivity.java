package com.example.uas_praktikummobileprogramming.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uas_praktikummobileprogramming.MainActivity;
import com.example.uas_praktikummobileprogramming.R;
import com.example.uas_praktikummobileprogramming.dashboard.DashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonToSignup;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        // bind views
        editTextEmail    = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin      = findViewById(R.id.buttonLogin);
        buttonToSignup   = findViewById(R.id.buttonToSignup);

        // login button action
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String pass  = editTextPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError("Email tidak boleh kosong");
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                editTextPassword.setError("Password tidak boleh kosong");
                return;
            }

            auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Login sukses, bisa lanjut ke MainActivity atau halaman utama
                                Toast.makeText(LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                finish();
                            } else {
                                // Login gagal
                                Toast.makeText(LoginActivity.this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        });

        // switch to SignUpActivity
        buttonToSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });
    }
}
