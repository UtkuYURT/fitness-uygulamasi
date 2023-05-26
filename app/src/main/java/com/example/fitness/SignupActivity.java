package com.example.fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import com.example.fitness.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private String email, sifre, sifreTekrar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        passwordVisible();
        signUpButton();
    }

    public void passwordVisible(){
        binding.btnPassVisible1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.editTextSifre.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    binding.editTextSifre.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.btnPassVisible1.setBackgroundResource(R.drawable.resim_visible_off);
                } else {
                    binding.editTextSifre.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.btnPassVisible1.setBackgroundResource(R.drawable.resim_visible);
                }
            }
        });

        binding.btnPassVisible2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.editTextSifreTekrar.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    binding.editTextSifreTekrar.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.btnPassVisible2.setBackgroundResource(R.drawable.resim_visible_off);
                } else {
                    binding.editTextSifreTekrar.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.btnPassVisible2.setBackgroundResource(R.drawable.resim_visible);
                }
            }
        });
    }

    public void signUpButton(){
        binding.buttonKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    public void signUp(){
        email = binding.editTextKullaniciAdi.getText().toString();
        sifre = binding.editTextSifre.getText().toString();
        sifreTekrar = binding.editTextSifreTekrar.getText().toString();

        if (sifre.equals(sifreTekrar)){
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(sifre)){
                mAuth.createUserWithEmailAndPassword(email,sifre)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignupActivity.this,"Kayıt Olma İşleminiz Gerçekleşmiştir",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(SignupActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(SignupActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else{
                Toast.makeText(SignupActivity.this,"Lütfen Gerekli Tüm Alanları Doldurunuz",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SignupActivity.this, "Şifreler Farklı Olamaz", Toast.LENGTH_SHORT).show();
        }
    }
}