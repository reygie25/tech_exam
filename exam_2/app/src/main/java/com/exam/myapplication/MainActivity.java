package com.exam.myapplication;

import android.os.Bundle;

import com.exam.myapplication.helper.Commons;
import com.exam.myapplication.model.DateAndGaps;
import com.exam.myapplication.model.DateAndTime;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private SimpleDateFormat oldFormat, newFormat, timeFormat, sdf;
    private String selectedDate;
    private String selectedDateTime;
    private int pickCount;

    private List<DateAndTime> listOfDates;
    private List<DateAndGaps> listOfDateGaps;
    private List<DateAndTime> finalListOfDate;

    private DateAndTime finalDateAndTime;
    private DateAndTime dateAndTime;

    private DateTimeAdapter adapter;
    private DateGapsAdapter dateAndGapsAdapter;
    private RecyclerView rvList;
    private Button btnCalculate, btnAdd, btnBack;

    private boolean isCalculate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        rvList = findViewById(R.id.rv_date_time);
        btnCalculate = findViewById(R.id.btn_calculate);
        btnAdd = findViewById(R.id.btn_add);
        btnBack = findViewById(R.id.btn_back);

        finalListOfDate = new ArrayList<>();
        listOfDates = new ArrayList<>();
        listOfDateGaps = new ArrayList<>();
        dateAndTime = new DateAndTime();

        sdf = new SimpleDateFormat("MM/dd/yyyy");
        oldFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        newFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        timeFormat = new SimpleDateFormat("hh:mm a");

        adapter = new DateTimeAdapter(listOfDates, position -> {
            Commons.showYesNoDialog(this, () -> {
                listOfDates.remove(position);
                adapter.notifyItemChanged(position);
            });
        });
        dateAndGapsAdapter = new DateGapsAdapter(listOfDateGaps);

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);

        btnCalculate.setOnClickListener(v -> calculate());
        btnAdd.setOnClickListener(v -> showDatePicker());
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDatePicker(){
        Commons.showDatePicker(getSupportFragmentManager(), (view, year, month, day) -> {
            selectedDate = (month+1)+"/"+day+"/"+year;
            showTimePicker();
        });
    }

    private void showTimePicker(){
        Commons.showTimePicker(getSupportFragmentManager(), (view, hour, minute, second) -> {
            String time = hour+":"+minute;
            try {
                Date date = oldFormat.parse(selectedDate + " " + time);
                selectedDateTime = newFormat.format(date);
                if(pickCount == 0) {
                    //select the time again
                    if(finalDateAndTime != null){
                        finalDateAndTime.setEndDateTime(selectedDateTime);
                        finalListOfDate.add(finalDateAndTime);
                        finalDateAndTime = null;
                    }

                    dateAndTime = new DateAndTime();
                    dateAndTime.setStartDateTime(selectedDateTime);
                    pickCount++;
                    showDatePicker();
                    Toast.makeText(MainActivity.this, "Please select another date and time", Toast.LENGTH_LONG).show();
                }else{
                    pickCount = 0;
                    dateAndTime.setEndDateTime(selectedDateTime);
                    listOfDates.add(dateAndTime);

                    adapter.notifyDataSetChanged();

                    finalDateAndTime = new DateAndTime();
                    finalDateAndTime.setStartDateTime(selectedDateTime);

                    btnCalculate.setVisibility(View.VISIBLE);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    private void calculate(){
        isCalculate = true;
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        List<String> listOfGaps;
        HashMap<String, List<String>> map = new HashMap<>();

        if(finalListOfDate.size() == 0){
            DateAndTime dd = listOfDates.get(0);
            finalListOfDate.add(dd);
        }

        for (DateAndTime d : finalListOfDate) {
            listOfGaps = new ArrayList<>();
            try {
                Date startDate = newFormat.parse(d.getStartDateTime());
                Date endDate = newFormat.parse(d.getEndDateTime());

                long timeDiff = endDate.getTime() - startDate.getTime();
                long diff = TimeUnit.MINUTES.convert(timeDiff, TimeUnit.MILLISECONDS);
                Date keyDate = sdf.parse(d.getEndDateTime());
                String key = sdf.format(keyDate);

                if (diff > 0) {
                    startCal.setTime(startDate);
                    startCal.add(Calendar.MINUTE, +1);

                    endCal.setTime(endDate);
                    endCal.add(Calendar.MINUTE, -1);

                    startDate = startCal.getTime();
                    endDate = endCal.getTime();


                    String startTime = timeFormat.format(startDate);
                    String endTime = timeFormat.format(endDate);

                    String gaps = startTime + " - " + endTime;

                    if (map.containsKey(key)) {
                        List<String> list = map.get(key);
                        if(list != null) {
                            list.add(gaps);
                            map.put(key, list);
                        }else{
                            listOfGaps.add(gaps);
                            map.put(key, listOfGaps);
                        }
                    } else {
                        listOfGaps.add(gaps);
                        map.put(key, listOfGaps);
                    }
                } else {
                    map.put(key, null);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        listOfDateGaps.clear();
        for(String k:map.keySet()){
            Log.d("abcdef",k);

            listOfDateGaps.add(new DateAndGaps(k, map.get(k)));
        }

        Collections.sort(listOfDateGaps, (d1, d2) -> d1.getDate().compareTo(d2.getDate()));
        btnAdd.setVisibility(View.GONE);
        btnCalculate.setVisibility(View.GONE);
        btnBack.setVisibility(View.VISIBLE);

        rvList.setAdapter(dateAndGapsAdapter);
        dateAndGapsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if(isCalculate)
            back();
        else
            super.onBackPressed();
    }

    private void back(){
        isCalculate = false;
        btnAdd.setVisibility(View.VISIBLE);
        btnCalculate.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.GONE);

        rvList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
