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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity {

    private EditText et_email;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        ///Инициализация VIEW
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        //Кнопка Готово
        Button bt_submit = findViewById(R.id.bt_save);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txt_email = et_email.getText().toString().trim();
                String txt_pass = et_password.getText().toString().trim();

                if(TextUtils.isEmpty(txt_pass) || TextUtils.isEmpty(txt_email))
                {
                    Toast.makeText(
                            SigninActivity.this,
                            "Нужно заполнить все поля",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                else{
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(txt_email, txt_pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(SigninActivity.this, ProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                        Toast.makeText(
                                                SigninActivity.this,
                                                "Ошибка авторизации",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                }
                            });
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
        View bg = findViewById(R.id.signin_activity_id);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_email.getWindowToken(), 0);
            }
        });

    }
}