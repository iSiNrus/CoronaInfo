package ru.quat63.coronainfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "READ DB";

    private FirebaseAuth auth;
    private DatabaseReference reference;

    private EditText et_fam;
    private EditText et_name;
    private EditText et_place;
    private EditText et_email;
    private EditText et_phone;
    private TextView tv_res_cov;
    private TextView tv_chance_cov;
    private TextView tv_simptoms;

    private String fam  = "";
    private String name = "";
    private String place = "";
    private String email = "";
    private String phone = "";

    private String res_cov = "";
    private String chance_cov = "";
    private String simptoms = "";

    private Button bt_signout;
    private Button bt_save;
    private Button bt_friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        et_fam = findViewById(R.id.et_fam);
        et_name = findViewById(R.id.et_name);
        et_place = findViewById(R.id.et_place);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        tv_res_cov = findViewById(R.id.tv_res_cov);
        tv_chance_cov = findViewById(R.id.tv_chance_cov);
        tv_simptoms = findViewById(R.id.tv_simptoms);

        bt_signout = findViewById(R.id.bt_signout);
        bt_save = findViewById(R.id.bt_save);

        bt_friends = findViewById(R.id.bt_friends_info);
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();


        //Подгрузка данных о пользователе
        if(firebaseUser==null) {
            Toast.makeText(
                    ProfileActivity.this,
                    "Ошибка!",
                    Toast.LENGTH_LONG
            ).show();
            startActivity(new Intent(ProfileActivity.this, RegActivity.class));
            finish();
        }
        else {
            String userId = firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Object o_fam = snapshot.child("family_name").getValue();
                    Object o_name = snapshot.child("name").getValue();
                    Object o_place = snapshot.child("place").getValue();
                    Object o_email = snapshot.child("email").getValue();
                    Object o_phone = snapshot.child("phone").getValue();
                    Object o_res_cov = snapshot.child("res_cov").getValue();
                    Object o_chance = snapshot.child("chance").getValue();
                    Object o_simptoms = snapshot.child("simptoms").getValue();


                    if(o_fam!=null) fam = o_fam.toString();
                    if(o_name!=null) name = o_name.toString();
                    if(o_place!=null) place = o_place.toString();
                    if(o_email!=null) email = o_email.toString();
                    if(o_phone!=null) phone = o_phone.toString().replaceFirst("7", "");
                    if(o_res_cov!=null) res_cov = o_res_cov.toString();
                    if(o_chance!=null) chance_cov = o_chance.toString();
                    if(o_simptoms!=null) simptoms = o_simptoms.toString();

                    et_fam.setText(fam);
                    et_name.setText(name);
                    et_place.setText(place);
                    et_email.setText(email);
                    et_phone.setText(phone);
                    tv_res_cov.setText(res_cov);
                    tv_chance_cov.setText(chance_cov);
                    tv_simptoms.setText(simptoms);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //Кнопка сохранить
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_fam = et_fam.getText().toString();
                String txt_name = et_name.getText().toString();
                String txt_place = et_place.getText().toString();
                String txt_phone = et_phone.getText().toString();

                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userId= null;
                if(firebaseUser != null)
                    userId = firebaseUser.getUid();
                reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                reference.child("family_name").setValue(txt_fam);
                reference.child("name").setValue(txt_name);
                reference.child("place").setValue(txt_place);
                reference.child("phone").setValue(txt_phone);
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                finish();
                startActivity(intent);
                Toast.makeText(
                        ProfileActivity.this,
                        "Данные успешно сохранены",
                        Toast.LENGTH_LONG
                ).show();
            }

        });

        //Кнопка выход
        bt_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(ProfileActivity.this, RegActivity.class);
                Toast.makeText(
                        ProfileActivity.this,
                        "Выход выполнен",
                        Toast.LENGTH_SHORT
                ).show();
                startActivity(intent);
                finish();
            }
        });

        //Посмотреть друзей
        bt_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(
                        ProfileActivity.this,
                        "Просмотр друзей пока недоступен",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        //Скрытие клавитуры по нажатию на экран
        View bg = findViewById(R.id.profile_id);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_email.getWindowToken(), 0);
            }
        });

    }


}