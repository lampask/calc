package com.myld.fornetti;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean focused = false;
    boolean mode = false;
    boolean MAIN_MODE = true;
    EditText input;
    EditText a;
    EditText b;
    ListView listView;
    List<Button> buttons = new ArrayList<>();
    List<Integer> values = new ArrayList<>();
    List<Integer> values_SortedClone = new ArrayList<>();
    int[] offset = {25, 50}; //default
    Activity act = this;
    CheckBox modeSwitch;

    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";

    public static final int NUM_OF_CHANGES = 10;
    public static final int MINIMALIZATOR = 6;

    public void Focus(EditText focusOn) {
        input = focusOn;
        focused = true;
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.switchbutton) {
            if (MAIN_MODE) {
                MAIN_MODE = false;
                ChangeMode_AB(false);
                if (!findViewById(R.id.inputB).isEnabled()) {
                    findViewById(R.id.checkBox).performClick();
                }
            } else {
                MAIN_MODE = true;
                ChangeMode_AB(true);
            }
            TextCalculation();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChangeMode_AB(true);
        listView = findViewById(R.id.content);

        // Keyboard

        buttons.add((Button) findViewById(R.id.buttonA));   values.add(23);
        buttons.add((Button) findViewById(R.id.buttonB));   values.add(202);
        buttons.add((Button) findViewById(R.id.buttonC));   values.add(19);
        buttons.add((Button) findViewById(R.id.buttonD));   values.add(15);
        buttons.add((Button) findViewById(R.id.buttonE));   values.add(1008);
        buttons.add((Button) findViewById(R.id.buttonF));   values.add(88);
        buttons.add((Button) findViewById(R.id.buttonG));   values.add(46);
        buttons.add((Button) findViewById(R.id.buttonH));   values.add(504);
        buttons.add((Button) findViewById(R.id.buttonI));   values.add(2);
        buttons.add((Button) findViewById(R.id.buttonJ));   values.add(7);
        buttons.add((Button) findViewById(R.id.buttonK));   values.add(144);
        buttons.add((Button) findViewById(R.id.buttonL));   values.add(24);
        buttons.add((Button) findViewById(R.id.buttonM));   values.add(3);
        buttons.add((Button) findViewById(R.id.buttonN));   values.add(0);
        buttons.add((Button) findViewById(R.id.buttonO));   values.add(2);
        buttons.add((Button) findViewById(R.id.buttonP));   values.add(37);

        values_SortedClone = new ArrayList<>(values);
        Collections.sort(values_SortedClone);
        Collections.reverse(values_SortedClone);
        values_SortedClone.remove((Integer)0);

        // Inputs
        a = findViewById(R.id.inputA);
        b = findViewById(R.id.inputB);

        View.OnTouchListener inputListener = new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg0.getId() == R.id.inputA) {
                    a.setRawInputType(Configuration.KEYBOARD_QWERTY);
                    Focus(a);
                } else if (arg0.getId() == R.id.inputB) {
                    b.setRawInputType(Configuration.KEYBOARD_QWERTY);
                    Focus(b);
                    /*InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(arg0.getWindowToken(), 0);
                    input.setInputType(InputType.TYPE_NULL); // disable soft input*/
                }
                int inType = input.getInputType(); // backup the input type
                input.onTouchEvent(arg1); // call native handler
                input.setInputType(inType); // restore input type
                return true; // consume touch even
            }
        };

        a.setOnTouchListener(inputListener);
        b.setOnTouchListener(inputListener);


        // Del buttons
        final Button del1 = findViewById(R.id.delbutton1);
        final Button del2 = findViewById(R.id.delbutton2);

        // Checkbox
        modeSwitch = findViewById(R.id.checkBox);
        modeSwitch.setChecked(mode);
        b.setEnabled(mode);
        del2.setEnabled(mode);
        modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mode = isChecked;
                modeSwitch.setChecked(mode);
                b.setEnabled(mode);
                del2.setEnabled(mode);
                b.setText(b.getText());
                if (mode) {
                    b.requestFocus();
                    View view = act.getCurrentFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    input = b;
                } else {
                    a.requestFocus();
                    input = a;
                    if (!MAIN_MODE) {
                        findViewById(R.id.switchbutton).performClick();
                    }
                }
            }
        });

        // Keyboard events
        View.OnClickListener buttonListener = new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Editable content = input != null ? input.getText() : a.getText();
                if (input == null || !input.isEnabled()) {
                    Focus(a);
                }
                // Default input
                if (input == a) {
                    input.requestFocus(View.FOCUS_DOWN);
                    input.setText(content + ((Button) v).getText().toString());
                } else {
                    input.setText((focused ? "" : content) + ((Button) v).getText().toString());
                }
                input.setSelection(input.getText().length());
                input.requestFocus();
                focused = false;
            }
        };
        for (Button button : buttons) {
            button.setOnClickListener(buttonListener);
        }

        a.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    input = b;
                    return true;
                }
                a.setSelection(a.getText().length());
                return false;
            }
        });

        // Del event
        View.OnClickListener deletion = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.delbutton1) {
                    a.setText("");
                } else if (v.getId() == R.id.delbutton2) {
                    b.setText("");
                }
            }
        };
        del1.setOnClickListener(deletion);
        del2.setOnClickListener(deletion);

        TextWatcher calc = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                TextCalculation();
            }
        };

        a.addTextChangedListener(calc);
        b.addTextChangedListener(calc);

        AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent krops = new Intent(MainActivity.this,Show.class);
                HashMap<String,String> a = (HashMap)(listView.getItemAtPosition(position));
                krops.putExtra("content",a.get("First"));
                startActivity(krops);


            }
        };
        listView.setOnItemClickListener(itemClick);
    }

    private void TextCalculation() {
        if ((!a.getText().toString().equals("") && !mode) || (mode && !b.getText().toString().equals("") && !a.getText().toString().equals(""))) {
            int value = 0;
            if (mode) {
                int value1 = 0;
                String text = a.getText().toString();
                String text1 = b.getText().toString();
                boolean con = false;
                int temp = 0;
                for (int i = 0; i < text.length(); i++) {
                    char component = text.charAt(i);
                    if (component <= 57) {
                        temp *= 10;
                        temp += component - '0';
                        con = true;
                    } else {
                        if (con) {
                            value += temp;
                            temp = 0;
                            con = false;
                        }
                        value += values.get(component - 'A');
                    }
                }
                if (con) {
                    value += temp;
                    temp = 0;
                    con = false;
                }
                for (int i = 0; i < text1.length(); i++) {
                    char component = text1.charAt(i);
                    if (component <= 57) {
                        temp *= 10;
                        temp += component - '0';
                        con = true;
                    } else {
                        if (con) {
                            value1 += temp;
                            temp = 0;
                            con = false;
                        }
                        value1 += values.get(component - 'A');
                    }
                }
                if (con) {
                    value += temp;
                }
                if (MAIN_MODE) {
                    value = value - value1;
                } else {
                    if (value1 != 0) {
                        value /= value1;
                    } else {
                        value = 0;
                    }
                }
            } else {
                String text = a.getText().toString();
                boolean con = false;
                int temp = 0;
                for (int i = 0; i < text.length(); i++) {
                    char component = text.charAt(i);
                    if (component <= 57) {
                        temp *= 10;
                        temp += component - '0';
                        con = true;
                    } else {
                        if (con) {
                            value += temp;
                            temp = 0;
                            con = false;
                        }
                        value += values.get(component - 'A');
                    }
                }
                if (con) {
                    value += temp;
                }
            }
            ListViewAdapter adapter = new ListViewAdapter(act, GetListContents(value));
            listView.setAdapter(adapter);
        } else {
            ListViewAdapter adapter = new ListViewAdapter(act, new ArrayList<HashMap<String, String>>());
            listView.setAdapter(adapter);
        }
    }

    private ArrayList<HashMap<String, String>> GetListContents(int target) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        if (MAIN_MODE) {
            for (int i = 0; i < NUM_OF_CHANGES; i++) {
            double off = mode ? new Random().nextInt((offset[1] - offset[0]) + 1) + offset[0] : 100;
            double tar = Math.ceil((double) target * (off / 100));
            String output = RandomChange((int) tar, 0, new ArrayList<Integer>());
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put(FIRST_COLUMN, output);
            hashmap.put(SECOND_COLUMN, "~" + (int) off);
            list.add(hashmap);
            }
        } else {
            String output = String.valueOf(Math.floor(target) > 5 ? 5 : (int) Math.floor(target));
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put(FIRST_COLUMN, output);
            hashmap.put(SECOND_COLUMN, "~" + 100);
            list.add(hashmap);
        }
        return list;
    }

    private String RandomChange(int target, int count, ArrayList<Integer> change) {
        if (target == 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < new Random().nextInt(MINIMALIZATOR)+1; i++) {
                sb.append("N ");
            }
            return sb.toString();
        }
        int comp = values.get(new Random().nextInt(values.size()));
        // Build rand list
        while (count + comp <= target) {
            change.add(comp);
            count += comp;
            if (count == target) {
                // Gen output
                StringBuilder sb = new StringBuilder();
                List<Character> temp = new ArrayList<>();
                for (Integer u : change) {
                    temp.add((char) (values.indexOf(u) + 'A'));
                }
                Collections.sort(temp);
                for (Character u : temp) {
                    sb.append(u);
                    sb.append(" ");
                }
                return sb.toString();
            }
            if (change.size() > MINIMALIZATOR) { break; }
            String s = RandomChange(target,count,change);
            if (!s.equals("N/A")) { return s; } else {
                change.remove(change.get(change.size()-1));
                count -= comp;
            }
        }
        // Adjust
        return MainChange(target, count, change);
    }

    private String MainChange(int target, int count, ArrayList<Integer> change) {
        for (int i = 0; i < values_SortedClone.size(); i++) {
            if (values_SortedClone.get(i) + count == target) {
                change.add(values_SortedClone.get(i));

                // Gen output
                StringBuilder sb = new StringBuilder();
                List<Character> temp = new ArrayList<>();
                for (Integer u : change) {
                    List<Integer> indexes = new ArrayList<>();
                    for(int j=0; j<values.size(); j++){
                        if(values.get(j).equals(u)){
                            indexes.add(j);
                        }
                    }
                    temp.add((char) (indexes.get(new Random().nextInt(indexes.size()))+ 'A'));
                }
                Collections.sort(temp);
                for (Character u : temp) {
                    sb.append(u);
                    sb.append(" ");
                }
                return sb.toString();

            } else if (values_SortedClone.get(i) + count < target) {
                change.add(values_SortedClone.get(i));
                count += values_SortedClone.get(i);
                String s = MainChange(target, count, change);
                if (!s.equals("N/A")) {
                    return s;
                } else {
                    change.remove(change.get(change.size()-1));
                    count -= values_SortedClone.get(i);
                }
            }
        }
        return "N/A";
    }

    private void ChangeMode_AB(boolean state) {
        if (state) {getSupportActionBar().setTitle("Mode: Babka");}
        else getSupportActionBar().setTitle("Mode: Stánok");
    }

}

