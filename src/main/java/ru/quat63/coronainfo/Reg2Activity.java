package ru.quat63.coronainfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Reg2Activity extends AppCompatActivity {

    private EditText et_email;
    private EditText et_password;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg2);

        ///Инициализация VIEW
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        auth = FirebaseAuth.getInstance();
        final String fam = getIntent().getStringExtra(RegActivity.TAG_FAM);
        final String name = getIntent().getStringExtra(RegActivity.TAG_NAME);
        final String place = getIntent().getStringExtra(RegActivity.TAG_PLACE);
        final String phone = getIntent().getStringExtra(RegActivity.TAG_PHONE);

        //Кнопка Готово
        Button bt_submit = findViewById(R.id.bt_save);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = et_email.getText().toString().trim();
                String pass = et_password.getText().toString().trim();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Нужно заполнить все поля", Toast.LENGTH_SHORT).show();
                }
                else {
                    CheckBox cb = findViewById(R.id.cb_agree);
                    if(cb.isChecked())
                        register(fam, name, place, phone, email, pass);
                    else
                        Toast.makeText(
                                Reg2Activity.this,
                                "Для продолжения необходимо дать согласие на обработку данных",
                                Toast.LENGTH_SHORT
                        ).show();
                }
            }
        });

        //Кнопка вернуться
        Button bt_return = findViewById(R.id.bt_return);
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Скрытие клавитуры по нажатию на экран
        View bg = findViewById(R.id.reg2_activity_id);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_email.getWindowToken(), 0);
            }
        });
    }

    private void register(final String fam, final String name, final String place,
                          final String phone, final String email, final String pass)
    {
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId= null;
                            if(firebaseUser != null)
                                userId = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("family_name", fam);
                            hashMap.put("name", name);
                            hashMap.put("place", place);
                            hashMap.put("phone", phone);
                            hashMap.put("password", pass);
                            hashMap.put("email", email);
                            hashMap.put("res_cov", "Я не болен");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(Reg2Activity.this, ProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else
                            Toast.makeText(
                                    Reg2Activity.this,
                                    "Ошибка регистрации",
                                    Toast.LENGTH_SHORT
                            ).show();
                    }
                });
    }
}