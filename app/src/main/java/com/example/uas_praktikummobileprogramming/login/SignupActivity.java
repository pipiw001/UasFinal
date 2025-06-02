package com.example.uas_praktikummobileprogramming.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uas_praktikummobileprogramming.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class SignupActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    Button buttonSignup, buttonToLogin;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Inisialisasi Firebase
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Inisialisasi komponen
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSignup = findViewById(R.id.buttonSignup);
        buttonToLogin = findViewById(R.id.buttonToLogin);

        // Aksi saat tombol SIGN UP ditekan
        buttonSignup.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString();
            String confirmPassword = editTextConfirmPassword.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, task -> {
                        if (task.isSuccessful()) {
                            String userId = auth.getCurrentUser().getUid();
                            User newUser = new User(name, email);
                            databaseReference.child(userId).setValue(newUser)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(SignupActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(SignupActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Aksi saat tombol Login ditekan
        buttonToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
