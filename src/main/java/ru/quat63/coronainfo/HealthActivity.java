package ru.quat63.coronainfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HealthActivity extends AppCompatActivity {

    private Spinner sp_condition;
    private TextView tv_simptoms;
    private TextView tv_chance_cov;

    private Button bt_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        bt_start = findViewById(R.id.bt_start_test);

        tv_chance_cov = findViewById(R.id.tv_chance_cov);
        tv_simptoms = findViewById(R.id.tv_simptoms);
        sp_condition = findViewById(R.id.spinner);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        DatabaseReference reference;

        //Подгрузка данных о пользователе
        if(firebaseUser==null) {
            Toast.makeText(
                    HealthActivity.this,
                    "Ошибка!",
                    Toast.LENGTH_LONG
            ).show();
            startActivity(new Intent(HealthActivity.this, RegActivity.class));
            finish();
        }
        else {
            String userId = firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Object o_res_cov = snapshot.child("res_cov").getValue();
                    Object o_chance = snapshot.child("chance").getValue();
                    Object o_simptoms = snapshot.child("simptoms").getValue();

                    String res_cov = "";
                    String chance_cov = "0%";
                    String simptoms = "Нет";

                    if(o_res_cov!=null) res_cov = o_res_cov.toString();
                    if(o_chance!=null) chance_cov = o_chance.toString();
                    if(o_simptoms!=null) simptoms = o_simptoms.toString();

                    switch (res_cov){
                        case "Я не болею": {
                            sp_condition.setSelection(0);
                            break;
                        }
                        case "Я болею": {
                            sp_condition.setSelection(1);
                            break;
                        }
                        case "Был(а) в контакте с потенциальным больным": {
                            sp_condition.setSelection(2);
                            break;
                        }
                        case "Был(а) в контакте с подствержденным случаем": {
                            sp_condition.setSelection(3);
                            break;
                        }
                        case "Я уже переболел(а)": {
                            sp_condition.setSelection(4);
                            break;
                        }
                        case "Есть симптомы, схожие с вирусом": {
                            sp_condition.setSelection(5);
                            break;
                        }

                    }

                    tv_chance_cov.setText(chance_cov);
                    tv_simptoms.setText(simptoms);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        sp_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                DatabaseReference reference;

                if(firebaseUser==null) {
                    Toast.makeText(
                            HealthActivity.this,
                            "Ошибка!",
                            Toast.LENGTH_LONG
                    ).show();
                    startActivity(new Intent(HealthActivity.this, RegActivity.class));
                    finish();
                }
                else{
                    String userId = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    reference.child("res_cov").setValue(adapterView.getAdapter().getItem(i).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HealthActivity.this, TestActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}