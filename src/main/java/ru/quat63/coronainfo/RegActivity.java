package ru.quat63.coronainfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegActivity extends AppCompatActivity {

    public static final String TAG_FAM = "FAM";
    public static final String TAG_NAME = "NAME";
    public static final String TAG_PLACE = "PLACE";
    public static final String TAG_PHONE = "PHONE";

    private EditText et_Family;
    private EditText et_Name;
    private EditText et_Place;
    private EditText et_Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null)
        {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        et_Family = findViewById(R.id.et_fam);
        et_Name = findViewById(R.id.et_name);
        et_Place = findViewById(R.id.et_place);
        et_Phone = findViewById(R.id.et_phone);

        //Кнопка продолжить
        Button bt_continue = findViewById(R.id.bt_save);
        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(TextUtils.isEmpty(et_Name.getText().toString().trim()) ||
                        TextUtils.isEmpty(et_Family.getText().toString().trim()) ||
                        TextUtils.isEmpty(et_Place.getText().toString().trim()) ||
                        TextUtils.isEmpty(et_Phone.getText().toString().trim())
                )) {
                    Intent intent = new Intent(RegActivity.this, Reg2Activity.class);
                    intent.putExtra(TAG_FAM, et_Family.getText().toString().trim());
                    intent.putExtra(TAG_NAME, et_Name.getText().toString().trim());
                    intent.putExtra(TAG_PLACE, et_Place.getText().toString().trim());
                    intent.putExtra(TAG_PHONE, et_Phone.getText().toString().trim());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(RegActivity.this, "Нужно заполнить все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Кнопка входа
        Button bt_SignIn = findViewById(R.id.bt_signin);
        bt_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        //Кнопка ВК
        Button bt_vk = findViewById(R.id.bt_vk);
        bt_vk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO

                Toast.makeText(getApplicationContext(), "Авторизация через ВКонтакте пока невозможна", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(RegActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //Скрытие клавитуры по нажатию на экран
        View bg = findViewById(R.id.reg_activity_id);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_Family.getWindowToken(), 0);
            }
        });
    }
}