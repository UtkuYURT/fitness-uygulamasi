package com.example.fitness;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.fitness.databinding.FragmentOgunlerBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FragmentOgunler extends Fragment {

    private FragmentOgunlerBinding binding;
    private FirebaseFirestore mFirestore=FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String selectedMeal, selectedFood, selectedDate;
    private SimpleDateFormat dateFormatter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOgunlerBinding.inflate(getLayoutInflater(), container, false);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        meal();
        food();
        foods();

        return binding.getRoot();
    }

    public void meal(){
        ArrayList<String> ogun = new ArrayList<>();
        ogun.add("Seçiniz");
        ogun.add("Kahvaltı");
        ogun.add("Öğle Yemeği");
        ogun.add("Akşam Yemeği");
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, ogun);
        binding.spinnerOgunSecimi.setAdapter(arrayAdapter);
    }

    public void food(){
        ArrayList<String> food = new ArrayList<>();
        food.add("Seçiniz");
        food.add("Yumurta");
        food.add("Tavuk Pilav");
        food.add("Peynir");
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, food);
        binding.spinnerYemekSecim.setAdapter(arrayAdapter);
    }

    public void foods(){
        ArrayList<Food> foodListGlobal = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        binding.editTextTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, monthOfYear, dayOfMonth);
                        selectedDate = dateFormatter.format(selectedCalendar.getTime());
                        binding.editTextTarih.setText(selectedDate);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        binding.spinnerOgunSecimi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMeal = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Seçilemedi meal", Toast.LENGTH_SHORT).show();
            }
        });

        binding.spinnerYemekSecim.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFood = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Seçilemedi food", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonYemek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirestore.collection("Kullanıcılar").document(mAuth.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            List<Map<String, Object>> foodList = (List<Map<String, Object>>) documentSnapshot.get("foodList");
                            ArrayList<Food> foodList1 = new ArrayList<>();
                            if (foodList != null){
                                for (Map<String, Object> foodData : foodList) {
                                    Food food = new Food((String) foodData.get("Date"), (String) foodData.get("Meal"), (String) foodData.get("Food"));
                                    foodList1.add(food);
                                }
                            }
                            foodList1.add(new Food(selectedDate, selectedMeal, selectedFood));
                            foodListGlobal.addAll(foodList1);
                            mFirestore.collection("Kullanıcılar").document(mAuth.getUid()).
                                    set(new FoodList(foodListGlobal)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getActivity(), "Eklendi", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Document Bulunamadı", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}