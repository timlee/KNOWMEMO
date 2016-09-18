package com.knowmemo.usermanagement;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import sqlite.Exp;
import sqlite.Meaning;
import sqlite.SqlHelper;
import sqlite.Words;
import sqlite.tableDao;

public class ShowWordActivity extends ActionBarActivity {

    int count = 0;
    String user_id = "user_test";
    TextToSpeech textToSpeech;
    SqlHelper sqlHelper;
    List<Words> wordsReturnList;
    List<Meaning> meaningReturnList;
    List<Exp> expReturnList;
    Exp expReturn;
    tableDao tabledao;
    Toolbar toolbar;
    TextView wordsText;
    TextView meaningText;
    TextView level1;
    TextView level2;
    TextView level3;
    TextView level4;
    TextView level5;
    Button rememberBtn;
    Button forgetBtn;
    ImageView imageSpeaker;

    private Spinner spinner;
    private ArrayAdapter<String> rootWord;
    private Context mContext;
    private String[] rootWordArr = {"顯示相同字根字首","pose","impose"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word);
        this.layoutInit();
        this.listenerInit();
        this.databaseInit();
        //顯示每一個箱子內單字數量
        setBoxCount();
        setSpinner();
    }



    //初始化所有的框架
    private void layoutInit() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        wordsText = (TextView) findViewById(R.id.wordsText);
        meaningText = (TextView) findViewById(R.id.meaningText);
        level1 = (TextView) findViewById(R.id.level1);
        level2 = (TextView) findViewById(R.id.level2);
        level3 = (TextView) findViewById(R.id.level3);
        level4 = (TextView) findViewById(R.id.level4);
        level5 = (TextView) findViewById(R.id.level5);
        rememberBtn = (Button) findViewById(R.id.rememberBtn);
        rememberBtn.setOnTouchListener(new ButtonTransparent());
        forgetBtn = (Button) findViewById(R.id.forgetBtn);
        forgetBtn.setOnTouchListener(new ButtonTransparent());
        imageSpeaker = (ImageView)findViewById(R.id.imageSpeaker);
        //初始化TextToSpeech 定義語言是US
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        spinner = (Spinner)findViewById(R.id.spinner);
        mContext = this.getApplicationContext();
    }

    private void databaseInit(){
        // 建立資料庫物件及資料
        tabledao = new tableDao(getApplicationContext());
        if (tabledao.getExpCount() == 0){
            tabledao.deleteWords();
            tabledao.sampleWord();
            tabledao.deleteMeaning();
            tabledao.sampleMeaning();
            tabledao.deleteExp();
        }
        //取10個單字加入levle1的箱子
        wordsReturnList = tabledao.top10Words(0);
        //顯示單字
        wordsText.setText(wordsReturnList.get(count).getWord());
    }

    //button click後的動作
    private void listenerInit() {

        //點擊記得
        rememberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取得目前單字的id
                int wordId = wordsReturnList.get(count).getId();
                //利用id去找該id的exp
                expReturn = tabledao.getExpById(wordId);
                int level = expReturn.getLevel();
                int learnedTimes = expReturn.getLearned();
                int levelCount = tabledao.getBoxCount(level + 1);

                //如果本來就在第五層，則level改成6後updateExp
                if (level == 5) {
                    Exp exp = new Exp(user_id, wordId, 6, 0, learnedTimes + 1, "");
                    tabledao.updateExp(exp);
                    count ++;
                } else { //如果在一到四層，要判斷要前往的箱子是否會滿
                    if (tabledao.checkBoxCount(level + 1)) { //要前往的箱子 加上這一個單字後還沒滿
                        Exp exp = new Exp(user_id, wordId, level + 1, levelCount + 1, learnedTimes + 1, "");
                        tabledao.updateExp(exp);
                        count++;
                    } else { //要前往的箱子 加上這一個單字後滿了
                        Exp exp = new Exp(user_id, wordId, level + 1, levelCount + 1, learnedTimes + 1, "");
                        tabledao.updateExp(exp);
                        expReturnList = tabledao.boxLevelData(level + 1);
                        wordsReturnList = tabledao.getWordsById(expReturnList);
                        count = 0;
                    }
                }
                //經過上述處理後判斷count是否等於目前的單字數
                // 等於的話，count歸零，再取得沒記過的10個單字
                if (count == wordsReturnList.size()) {
                    System.out.println("enter check");
                    int max = tabledao.getExpMaxWordId();
                    System.out.println(max);
                    wordsReturnList = tabledao.top10Words(max);
                    count = 0;
                }
                setBoxCount();
                wordsText.setText(wordsReturnList.get(count).getWord());
                System.out.println(tabledao.getExpCount());
                meaningText.setText("點擊以顯示解釋");
            }
        });

        //點擊忘記
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int wordId = wordsReturnList.get(count).getId();
                expReturn = tabledao.getExpById(wordId);
                int level = expReturn.getLevel();
                int learnedTimes = expReturn.getLearned();
                int levelCount = tabledao.getBoxCount(level - 1);

                if (level == 1) {
                    int level1Count = tabledao.getBoxCount(1);
                    Exp exp = new Exp(user_id, wordId, level, level1Count, learnedTimes + 1, "");
                    tabledao.updateExp(exp);
                    count++;

                } else {
                    if (tabledao.checkBoxCount(level - 1)) { //要前往的箱子 加上這一個單字後還沒滿
                        Exp exp = new Exp(user_id, wordId, level - 1, levelCount + 1, learnedTimes + 1, "");
                        tabledao.updateExp(exp);
                        count++;
                    } else { //要前往的箱子 加上這一個單字後滿了
                        Exp exp = new Exp(user_id, wordId, level - 1, levelCount + 1, learnedTimes + 1, "");
                        tabledao.updateExp(exp);
                        expReturnList = tabledao.boxLevelData(level - 1);
                        wordsReturnList = tabledao.getWordsById(expReturnList);
                        count = 0;
                    }
                }
                if (count == wordsReturnList.size()) {
                    if (tabledao.getBoxCount(1) == 0) {
                        int max = tabledao.getExpMaxWordId();
                        wordsReturnList = tabledao.top10Words(max);
                        count = 0;
                    } else {
                        expReturnList = tabledao.boxLevelData(1);
                        wordsReturnList = tabledao.getWordsById(expReturnList);
                        count = 0;
                    }

                }
                setBoxCount();
                wordsText.setText(wordsReturnList.get(count).getWord());
                meaningText.setText("點擊以顯示解釋");

            }
        });

        //點擊顯示解釋
        meaningText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取得現在那個單字的id
                int searchId = wordsReturnList.get(count).getId();
                //由id去找所有的單字
                meaningReturnList = tabledao.getMeaningById(searchId);
                String meaningResult = "";
                for (int i = 0; i < meaningReturnList.size(); i++) {
                    //目前只顯示詞性和中文解釋，之後可再新增其他meaning欄位
                    meaningResult += meaningReturnList.get(i).getPart_of_speech() + " "
                            + meaningReturnList.get(i).getEngChiTra() + "  ";
                }
                meaningText.setText(meaningResult);
            }
        });

        //點擊發聲
        imageSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = wordsText.getText().toString();
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    public void onPause(){
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    private void openDatabase() {
        sqlHelper = new SqlHelper(this);
    }

    //顯示各個箱子內的單字數
    private void setBoxCount() {
        level1.setText(Integer.toString(tabledao.getBoxCount(1)));
        level2.setText(Integer.toString(tabledao.getBoxCount(2)));
        level3.setText(Integer.toString(tabledao.getBoxCount(3)));
        level4.setText(Integer.toString(tabledao.getBoxCount(4)));
        level5.setText(Integer.toString(tabledao.getBoxCount(5)));
    }

    private void setSpinner(){
        rootWord = new ArrayAdapter<String>(ShowWordActivity.this, android.R.layout.simple_spinner_item, rootWordArr);
        spinner.setAdapter(rootWord);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}