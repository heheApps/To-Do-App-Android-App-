package com.heheapps.todolist;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class DataStorageHandler {
    private Context context;

    private String fileName;

    public DataStorageHandler(Context context) {
        this.context = context;

        fileName = "TopItem";
    }

    public void saveData(ListItem data){
        try {
            //IMPORTANT cannot use new FileOutputStream like in normal java
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
        }catch (Exception ignored){}
    }

    public ListItem loadData(){
        ListItem data = null;
       try {
           FileInputStream fin = context.openFileInput(fileName);
           ObjectInputStream ois = new ObjectInputStream(fin);
           data = (ListItem) ois.readObject();
           ois.close();
       }catch (Exception ignored){}
       
       return data;
    }
}

