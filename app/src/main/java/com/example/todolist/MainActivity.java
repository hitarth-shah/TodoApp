package com.example.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listview = findViewById(R.id.listview);
        final TextAdapter adapter = new TextAdapter();

        readInfo();

        adapter.setData(list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete This Task?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                adapter.setData(list);

                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                        dialog.show();

            }
        });

        final Button addnewtask = findViewById(R.id.addnewtask);
        addnewtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskInput = new EditText(MainActivity.this);
                taskInput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add new Task")
                        .setMessage("What is your new task?")
                        .setView(taskInput)
                        .setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.add(taskInput.getText().toString());
                                adapter.setData(list);

                            }
                        })
                        .setNegativeButton("cancel", null)
                        .create();
                    dialog.show();

            }
        });


    }
    @Override
    protected void onPause(){
        super.onPause();
        saveInfo();
    }

    private void saveInfo(){
        try{
            File file = new File(this.getFilesDir(),"Saved");
            FileOutputStream Fout = new FileOutputStream(file);
            BufferedWriter buwriter = new BufferedWriter(new OutputStreamWriter(Fout));
            for (int i = 0; i < list.size(); i++){
                buwriter.write(list.get(i));
                buwriter.newLine();


            }
            buwriter.close();
            Fout.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void readInfo(){
        File file = new File(this.getFilesDir(),"saved");
        if (!file.exists()){
            return;
        }
        try{
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = reader.readLine();
            while (line!=null){
                list.add(line);
                line = reader.readLine();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    class TextAdapter extends BaseAdapter {
        List <String> list = new ArrayList<>();
        void setData(List<String> mlist){
            list.clear();
            list.addAll(mlist);
            notifyDataSetChanged();
        }

        @Override
                public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position){
            return null;
        }
        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(int position,View Convertview, ViewGroup parent){
            final LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.item, parent, false);
            final TextView textView = rowView.findViewById(R.id.task);
            textView.setText(list.get(position));
            return rowView;

        }


    }
}
