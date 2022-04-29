package com.heheapps.todolist;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class CheckboxAdapter extends BaseAdapter {

    private Context context;
    public static ArrayList<ListItem> itemList;
    public FrontpageButtonHandler frontpageButtonHandler;

    public CheckboxAdapter(Context context, ArrayList<ListItem> itemList, FrontpageButtonHandler frontpageButtonHandler) {
        this.context = context;
        this.itemList = itemList;
        this.frontpageButtonHandler = frontpageButtonHandler;
    }

    @Override
    //How many items are in the data set represented by the Adapter
    public int getCount() {
        if(itemList.size() == 0){
            return 1;
        }
        return itemList.size();
    }

    @Override
    //Get the data item associated with the specified position in the data set
    public Object getItem(int i) {
        if(i > itemList.size()-1){
            return null;
        }
        return itemList.get(i);
    }

    @Override
    //Get the row id associated with the specified position in the list
    public long getItemId(int i) {
        return 0;
    }


    @Override
    //The method called by the list view when using the setAdapter method, to display the list on the screen
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(itemList.size() < 2){
            TextView textView = new TextView(context);
            textView.setText("No tasks added jet \nPress the + button to add a new task");
            textView.setTextSize(25);
            textView.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
            textView.setPadding(50,50,50,50);
            return textView;
        }

        ViewHolder view = null;
        LayoutInflater inflator = ((Activity) context).getLayoutInflater();
        if (view == null) {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.list_item_view, null);

            view.label = (TextView) convertView.findViewById(R.id.lable);
            view.description = (TextView) convertView.findViewById(R.id.description);

            view.textLayout = (LinearLayout) convertView.findViewById(R.id.textLayout);
            view.textLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, itemList.get(position).getItemName() + " selected", Toast.LENGTH_SHORT).show();
                    frontpageButtonHandler.setCurrentItem(itemList.get(position));
                }
            });

            view.textLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    frontpageButtonHandler.editCurrentItem(itemList.get(position), view);
                    return true;
                }
            });

            view.checkBox=(CheckBox)convertView.findViewById(R.id.checkbox);
            view.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag(); // Here
                    // we get  the position that we have set for the checkbox using setTag.
                    itemList.get(getPosition).setChecked(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                }
            });
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }

        view.lockButton = convertView.findViewById(R.id.lock_button);
        view.lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemList.get(position).changeLock();
                if(itemList.get(position).isLocked()){
                    view.setBackgroundResource(R.mipmap.lock_icon_locked7);
                    Toast.makeText(context, itemList.get(position).getItemName() + " locked", Toast.LENGTH_SHORT).show();
                }else{
                    view.setBackgroundResource(R.mipmap.lock_icon_unlocked);
                    Toast.makeText(context, itemList.get(position).getItemName() + " unlocked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(itemList.get(position).isLocked()){
            view.lockButton.setBackgroundResource(R.mipmap.lock_icon_locked7);
        }

        view.checkBox.setTag(position);
        view.label.setText("" + itemList.get(position).getItemName());
        view.description.setText(""+ itemList.get(position).getDescription());

        view.checkBox.setChecked(itemList.get(position).isChecked());

        //Changes the view if the parent is null, as that is my filler view to allow scroll.
        //Don't want to see the checkbox and sets the textView to Invisible to suppress the onclick while still having normal height
        if(itemList.get(position).getParent() == null){
            view.checkBox.setVisibility(View.GONE);
            view.textLayout.setVisibility(View.INVISIBLE);
            view.lockButton.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        private CheckBox checkBox;
        private Button lockButton;
        private TextView label;
        private TextView description;
        private LinearLayout textLayout;
    }
}

