package ru.quat63.coronainfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private Button bt_instr;
    private Button bt_back;
    private Button bt_test_know;

    private Button bt_call_emerg;
    private Button bt_call_corona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bt_instr = findViewById(R.id.bt_instruction);
        bt_back = findViewById(R.id.bt_back);
        bt_test_know = findViewById(R.id.bt_test_knowlege);

        bt_call_emerg = findViewById(R.id.bt_call_emerg);
        bt_call_corona = findViewById(R.id.bt_call_covid_line);

        bt_instr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.instructions_id).setVisibility(View.VISIBLE);
                findViewById(R.id.home_id).setVisibility(View.INVISIBLE);
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.instructions_id).setVisibility(View.INVISIBLE);
                findViewById(R.id.home_id).setVisibility(View.VISIBLE);
            }
        });

        bt_test_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(
                        HomeActivity.this,
                        "Тест в разработке",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        bt_call_emerg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = "103";
                String dial = "tel:" + phoneNo;
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
            }
        });

        bt_call_corona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = "88002000112";
                String dial = "tel:" + phoneNo;
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
            }
        });
    }
}