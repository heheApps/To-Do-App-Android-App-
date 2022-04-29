package com.heheapps.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FrontpageButtonHandler frontpageButtonHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frontpageButtonHandler = new FrontpageButtonHandler(this, (ListView) findViewById(R.id.mainListView), (TextView) findViewById(R.id.parent_name_text_view));
    }

    public void addItem(View view){
        frontpageButtonHandler.addItem(view);
    }

    public void addItemBtn(View view){
        frontpageButtonHandler.addItemBtn();
    }

    public void removeCheckedItems(View view){
        frontpageButtonHandler.removeCheckedItems();
    }

    public void onBackPressed(){
        frontpageButtonHandler.backPressed();
    }
    
    @Override
    protected void onPause() {
        frontpageButtonHandler.save();
        super.onPause();
    }

    public void editItemBtn(View view) {
        frontpageButtonHandler.saveEditButton();
    }
}