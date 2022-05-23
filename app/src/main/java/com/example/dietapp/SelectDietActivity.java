package com.example.dietapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class SelectDietActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecting_diet);
        Context context = getApplicationContext();
        String[] namesOfDiets = Diet.getNames(Diet.createListOfDiets(context));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, namesOfDiets);
        setListAdapter(adapter);
    }


    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        String name = Diet.getNames(Diet.createListOfDiets(getApplicationContext()))[position];
        String desc = Diet.getDescs(Diet.createListOfDiets(getApplicationContext()))[position];
        Intent intent = new Intent(this, MainActivity.class);
        new AlertDialog.Builder(SelectDietActivity.this)
                .setTitle(name)
                .setMessage(desc)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    String item = (String) getListAdapter().getItem(position);
                    Context context = getApplicationContext();
                    Diet.saveDiet(item, context);
                    startActivity(intent);
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

        /*String item = (String) getListAdapter().getItem(position);
        Context context = getApplicationContext();
        Diet.saveDiet(item, context);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);*/
    }
}

