package com.example.chen.cs160finalproject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    List<String> presentationOptions;
    List<String> presentationDataList;
    HashMap<String, List<String>> listDataChild;
    final static String FILE_NAME_BASE = "presentation_";
    final static int EXTRA_LINES = 1;
    int NUMBER_OF_PRESENTATIONS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NUMBER_OF_PRESENTATIONS = getNumberPresentations();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Spotlight: Presentations");

        presentationOptions = new ArrayList<>();
        presentationOptions.add("Edit");
        presentationOptions.add("Practice");
        presentationOptions.add("Delete");
        presentationOptions.add("Statistics");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewPresentation();
            }
        });
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.parentListView);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                switch (childPosition) {
                    case 0:
                        Intent goToEdit = new Intent(MainActivity.this, EditPresentation.class);
                        packIntent(goToEdit, groupPosition);
                        startActivity(goToEdit);
                        break;
                    case 1:
                        load(groupPosition);
                        passDataToWatch();
                        Intent goToProgress = new Intent(MainActivity.this, PresentationInProgress.class);
                        packIntent(goToProgress, groupPosition);
                        startActivity(goToProgress);
                        break;
                    case 2: deletePresentationDialog(groupPosition);
                        break;
                    case 3:
                        Intent goToStats = new Intent(MainActivity.this, StatsActivity.class);
                        packIntent(goToStats, groupPosition);
                        startActivity(goToStats);
                        break;
                    default: break;
                }
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " : " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),
//                        Toast.LENGTH_LONG).show();

                return false;
            }
        });

    }

    private void passDataToWatch() {
        Intent goToWatch = new Intent(this, SignalWatch.class);

        String dataToPass = presentationDataList.get(0) + "\n";
        int totalSlides = Integer.parseInt(presentationDataList.get(1));
        int lineCounter = 2;
        int numKeywords;
        for(int i = 0; i < totalSlides; i++) {
            String nextLine = "";
            numKeywords = Integer.parseInt(presentationDataList.get(lineCounter));
            if(numKeywords != 0) {
                nextLine += presentationDataList.get(lineCounter + numKeywords + 1) + ",";
                for (int j = 0; j < numKeywords - 1; j++) {
                    nextLine += presentationDataList.get(lineCounter + j + 1) + ",";
                }
                nextLine += presentationDataList.get(lineCounter + numKeywords);
                dataToPass += nextLine + "\n";
            }
            lineCounter += numKeywords + MainActivity.EXTRA_LINES + 1;
        }
        dataToPass = dataToPass.trim();
        goToWatch.putExtra("data", dataToPass);
        startService(goToWatch);
    }

    private void load(int presentationNum) {
        try {
            String FILE_NAME = MainActivity.FILE_NAME_BASE + presentationNum;

            FileInputStream fis = openFileInput(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((fis)));
            String line = "";
            StringBuffer buffer = new StringBuffer();
            while((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            String[]presentationData = buffer.toString().split("\n");
            presentationDataList = new ArrayList<>();
            for(int i = 0; i < presentationData.length; i++) {
                presentationDataList.add(presentationData[i]);
            }
            fis.close();
        }catch(Exception e) {
//            Toast.makeText(getApplicationContext(), "Presentation data error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void packIntent(Intent intent, int presentationPosition) {
        intent.putExtra("presentation", presentationPosition);
    }

    private int getNumberPresentations() {
        int numPresentations = 0;
        try {
            int character;
            String FILE_NAME = "presentations";
            FileInputStream fis = openFileInput(FILE_NAME);
            String presentations = "";
            while ((character = fis.read()) != -1) {
                presentations = presentations + Character.toString((char) character);
            }
            fis.close();
            numPresentations = Integer.parseInt(presentations);
        } catch (Exception e) {
            return numPresentations;
        } finally {
//            Toast.makeText(getApplicationContext(), "Finished loading master presentation file with length " + numPresentations, Toast.LENGTH_LONG).show();
            return numPresentations;
        }
    }

    private void createNewPresentation() {
        final EditText textBox = new EditText(this);
        textBox.setHint("Title");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make a new presentation")
                .setView(textBox)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String presentationName = textBox.getText().toString();
                        if (presentationName == null || presentationName.length() == 0) {
                            textBox.setError("Presentation name is required!");
                        } else {
                            storeNewPresentation(presentationName);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void deletePresentationDialog(int presentation) {
        final int presentationNum = presentation;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete presentation")
                .setMessage("Are you sure you want to delete this presentation?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePresentation(presentationNum);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deletePresentation(int presentation) {
//        String name = listDataHeader.get(presentation);
//        listDataHeader.remove(presentation);
//        listDataChild.remove(name);
//
//
//        setNumberOfPresentations(NUMBER_OF_PRESENTATIONS - 1);
    }

    private void storeNewPresentation(String presentationName) {
        try {
            String FILE_NAME = FILE_NAME_BASE + NUMBER_OF_PRESENTATIONS;
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write((presentationName + "\n").getBytes());
            fos.write((1 + "\n").getBytes()); // one empty slide
            fos.write((0 + "\n").getBytes()); // placeholder keywords number
//            fos.write(("first\n").getBytes());
//            fos.write(("second\n").getBytes());
            fos.write((0 + "\n").getBytes()); // placeholder time
            fos.close();
        }catch(Exception e) {
        }
        listDataHeader.add(presentationName);
        listDataChild.put(presentationName, presentationOptions);

        setNumberOfPresentations(NUMBER_OF_PRESENTATIONS + 1);
    }

    private void setNumberOfPresentations(int newNumber) {
        NUMBER_OF_PRESENTATIONS = newNumber;
        try {
            String FILE_NAME = "presentations";
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            String numPresentations = NUMBER_OF_PRESENTATIONS + "";
            fos.write(numPresentations.getBytes());
            fos.close();
        }catch(Exception e) {
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        try {
//            for(int i = 0; i < 5; i++) {
//                FILE_NAME = FILE_NAME_BASE + i;
//                String currentString = "This is presentation " + i;
//                FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
//                fos.write(currentString.getBytes());
//                fos.close();
//                listDataHeader.add(FILE_NAME);
//            }
//            Toast.makeText(getApplicationContext(), "Finished creating presentations", Toast.LENGTH_LONG).show();
            for(int i = 0; i < NUMBER_OF_PRESENTATIONS; i++) {
                String FILE_NAME = FILE_NAME_BASE + i;
                FileInputStream fis = openFileInput(FILE_NAME);
                String title = new BufferedReader(new InputStreamReader(fis)).readLine();
                fis.close();
                listDataHeader.add(title);
                listDataChild.put(listDataHeader.get(i), presentationOptions);
            }
//            Toast.makeText(getApplicationContext(), "Finished loading presentations", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "errored out: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
