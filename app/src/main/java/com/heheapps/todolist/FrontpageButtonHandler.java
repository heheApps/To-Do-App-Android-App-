package com.heheapps.todolist;

import android.content.Context;
import android.speech.SpeechRecognizer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


class FrontpageButtonHandler {
    private PopupWindow popupWindowAddItem;

    private Context context;
    private ListView listView;

    private ArrayList<ListItem> itemArrayList;
    private ListItem topItem;
    private DataStorageHandler dataStorageHandler;
    private ListItem currentItem;
    private ListItem selectedItem;

    private TextView parentNameTextView;


    public FrontpageButtonHandler(Context context, ListView listView, TextView parentNameTextView) {
        this.context = context;
        this.listView = listView;
        this.parentNameTextView = parentNameTextView;

        itemArrayList = new ArrayList<>();
        dataStorageHandler = new DataStorageHandler(context);

        topItem = dataStorageHandler.loadData();
        if(topItem == null) {
            topItem = new ListItem("To do list", "description", null);
        }

        currentItem = topItem;
        makeList();
    }

    public void addItem(View view){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_window_add_item, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindowAddItem = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindowAddItem.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void addItemBtn(){
        EditText titleEt = popupWindowAddItem.getContentView().findViewById(R.id.popup_window_add_item_title);
        String title = titleEt.getText().toString();

        EditText descriptionEt = popupWindowAddItem.getContentView().findViewById(R.id.popup_window_add_item_description);
        String description = descriptionEt.getText().toString();

        ListItem newListItem = new ListItem(title, description, currentItem);
        currentItem.addChild(newListItem);

        popupWindowAddItem.dismiss();
        makeList();
    }

    public void removeCheckedItems(){
        ArrayList<ListItem> rmList = new ArrayList();
        for(ListItem listItem: currentItem.getChildren()){
            if(listItem.isChecked()) {
                rmList.add(listItem);
            }
            if(listItem.getParent() == null) {
                rmList.add(listItem);
            }
        }

        String lockedItems = "";
        for (ListItem listItem: rmList){
            if(!listItem.isLocked()) {
                currentItem.removeChild(listItem);
            }else{
                if(lockedItems.equals("")){
                    lockedItems += listItem.getItemName();
                }else {
                    lockedItems += ", " + listItem.getItemName();
                }
            }
        }

        if(!lockedItems.equals("")) {
            Toast.makeText(context, lockedItems + " was not removed, as they where locked", Toast.LENGTH_LONG).show();
        }
        makeList();
    }

    public void makeList(){
        dataStorageHandler.saveData(topItem);

        //Adding one extra empty element for scrolling
        ArrayList<ListItem> outArray  = new ArrayList<>();
        outArray.addAll(currentItem.getChildren());
        outArray.add(new ListItem("", "", null));

        CheckboxAdapter checkboxAdapter = new CheckboxAdapter(context, outArray, this);
        listView.setAdapter(checkboxAdapter);
    }

    public void setCurrentItem(ListItem currentItem) {
        this.currentItem = currentItem;
        parentNameTextView.setText(currentItem.getItemName());
        makeList();
    }

    public void editCurrentItem(ListItem selectedItem, View view){
        this.selectedItem = selectedItem;

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_window_edit_item, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindowAddItem = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindowAddItem.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Sets title text
        TextView titleText = popupWindowAddItem.getContentView().findViewById(R.id.popup_window_add_item_tv);
        titleText.setText("Editing " + selectedItem.getItemName());

        //Sets the editTexts to their current values
        EditText titleEt = popupWindowAddItem.getContentView().findViewById(R.id.popup_window_add_item_title);
        titleEt.setText(selectedItem.getItemName());

        EditText descriptionEt = popupWindowAddItem.getContentView().findViewById(R.id.popup_window_add_item_description);
        descriptionEt.setText(selectedItem.getDescription());
    }

    public void saveEditButton(){
        //Changes title from editText in popup
        EditText titleEt = popupWindowAddItem.getContentView().findViewById(R.id.popup_window_add_item_title);
        selectedItem.setItemName(titleEt.getText().toString());

        //Change description from editText in popup
        EditText descriptionEt = popupWindowAddItem.getContentView().findViewById(R.id.popup_window_add_item_description);
        selectedItem.setDescription(descriptionEt.getText().toString());

        popupWindowAddItem.dismiss();

        makeList();
    }

    public void backPressed(){
        if(currentItem.getParent() == null){
            Toast.makeText(context, "You are already in the top level of your list", Toast.LENGTH_LONG).show();
            return;
        }
        currentItem = currentItem.getParent();
        parentNameTextView.setText(currentItem.getItemName());
        makeList();
    }

    public void save(){
        dataStorageHandler.saveData(topItem);
    }
}
