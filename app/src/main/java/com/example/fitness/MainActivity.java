package com.example.fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.fitness.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private String ka, s;
    private boolean isRememberMeChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        signInButton();
        signUp();
        passwordVisible();
        remember();
        runRemember();
    }

    public void signInButton(){
        binding.buttonGY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ka = binding.editTextKA.getText().toString();
                s = binding.editTextPassword.getText().toString();
                signIn(ka, s);
            }
        });
    }

    public void signIn(String kullaniciAdi, String sifre){
        kullaniciAdi = binding.editTextKA.getText().toString();
        sifre = binding.editTextPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(kullaniciAdi, sifre)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                SharedPreferences preferences = MainActivity.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();

                                Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Hatalı kullanıcı adı veya şifre", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signUp(){
        binding.textViewKO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public void passwordVisible(){
        binding.btnPassVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.editTextPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    binding.editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.btnPassVisible.setBackgroundResource(R.drawable.resim_visible_off);
                } else {
                    binding.editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.btnPassVisible.setBackgroundResource(R.drawable.resim_visible);
                }
            }
        });
    }

    public void remember(){
        ka = binding.editTextKA.getText().toString();
        s = binding.editTextPassword.getText().toString();

        binding.checkBoxRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = MainActivity.this.getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if (isChecked) {
                    editor.putString("remember", "true");
                    editor.putString("email", ka);
                    editor.putString("password", s);
                    isRememberMeChecked = true;
                } else {
                    editor.putString("remember", "false");
                    editor.remove("email");
                    editor.remove("password");
                    isRememberMeChecked = false;
                }
                editor.apply();
            }
        });
    }

    public void runRemember() {
        SharedPreferences preferences = MainActivity.this.getSharedPreferences("checkbox", Context.MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");

        if (checkbox.equals("true")) {
            if (isLoggedIn()) {
                Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                startActivity(intent);
            } else if (isRememberMeChecked) {
                String savedEmail = preferences.getString("email", "");
                String savedPassword = preferences.getString("password", "");

                if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
                    signIn(savedEmail, savedPassword);
                }
            }
        }
    }

    private boolean isLoggedIn() {
        SharedPreferences preferences = MainActivity.this.getSharedPreferences("session", Context.MODE_PRIVATE);
        return preferences.getBoolean("isLoggedIn", false);
    }
}