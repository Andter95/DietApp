package com.example.dietapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button selectAnotherDiet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectAnotherDiet = findViewById(R.id.button);
        TextView name = findViewById(R.id.textView);
        TextView untilItEnds = findViewById(R.id.textView3);
        TextView toNextFood = findViewById(R.id.textView5);
        TextView litres = findViewById(R.id.textView7);
        TextView sportActivity = findViewById(R.id.textView9);
        TextView status = findViewById(R.id.textView10);
        TextView prod1 = findViewById(R.id.textView13);
        TextView val1 = findViewById(R.id.textView21);
        TextView prod2 = findViewById(R.id.textView16);
        TextView val2 = findViewById(R.id.textView22);
        TextView prod3 = findViewById(R.id.textView17);
        TextView val3 = findViewById(R.id.textView24);
        TextView prod4 = findViewById(R.id.textView18);
        TextView val4 = findViewById(R.id.textView25);
        TextView prod5 = findViewById(R.id.textView19);
        TextView val5 = findViewById(R.id.textView26);
        TextView prod6 = findViewById(R.id.textView20);
        TextView val6 = findViewById(R.id.textView27);
        if (Diet.checkSaves(getApplicationContext())) {
            name.setText(Diet.openText(getApplicationContext()).split("\n")[0]);
            untilItEnds.setText(Diet.getUntilItEnds(Diet.openText(getApplicationContext()).split("\n")[1],
                    Diet.getLength(Diet.openText(getApplicationContext()).split("\n")[0], getApplicationContext())));
            toNextFood.setText(Diet.getNextFoodTime(Diet.openText(getApplicationContext()).split("\n")[0], getApplicationContext()));
            litres.setText(Diet.getLitres(Diet.openText(getApplicationContext()).split("\n")[0], getApplicationContext()));
            sportActivity.setText(Diet.getSports(Diet.openText(getApplicationContext()).split("\n")[0], getApplicationContext()));
            if (toNextFood.getText() == "Сейчас") {
                status.setText("Прием пищи");
            }
            String[] schld = Diet.getSchldAndValue(Diet.openText(getApplicationContext()).split("\n")[1],
                    Diet.getLength(Diet.openText(getApplicationContext()).split("\n")[0], getApplicationContext()),
                    Diet.openText(getApplicationContext()).split("\n")[0], getApplicationContext());
            prod1.setText(schld[0]);
            val1.setText(schld[1]);
            prod2.setText(schld[2]);
            val2.setText(schld[3]);
            prod3.setText(schld[4]);
            val3.setText(schld[5]);
            prod4.setText(schld[6]);
            val4.setText(schld[7]);
            prod5.setText(schld[8]);

            val5.setText(schld[9]);
            prod6.setText(schld[10]);
            val6.setText(schld[11]);
        } else {
            name.setText("Диета не выбрана");
            untilItEnds.setText("");
            toNextFood.setText("");
            litres.setText("");
            sportActivity.setText("");
            status.setText("");
            prod1.setText("");
            val1.setText("");
            prod2.setText("");
            val2.setText("");
            prod3.setText("");
            val3.setText("");
            prod4.setText("");
            val4.setText("");
            prod5.setText("");
            val5.setText("");
            prod6.setText("");
            val6.setText("");
        }
    }

    public void onClickSelect(View view) {
        Intent intent = new Intent(this, SelectDietActivity.class);
        startActivity(intent);
    }

    static int co = 0;

    public void onClickAdv(View view) {
        TextView adv = findViewById(R.id.textView15);
        String[] advs = Diet.getAdv(Diet.openText(getApplicationContext()).split("\n")[0], getApplicationContext());
        adv.setText(advs[co]);
        co++;
        if (co == advs.length) {
            co = 0;
        }
    }


}