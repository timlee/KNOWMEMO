package com.knowmemo.usermanagement;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import sqlite.Words;

/**
 * Created by User on 2016/2/3.
 */
public class AddWordsActivity {

    Words addWords;
    Words[] addWordsData;
    int id = 0;
    String word = "";
    String phonetics = "";
    int GEPTlow = 0;
    int GEPTmiddle = 0;
    int GEPTmiddlehigh = 0;
    int GEPThigh = 0;
    int TOEFL = 0;
    int TOEIC = 0;
    int IELTS = 0;

    public Words[] readExcel() {
        String fileposition = "C:\\Users\\User\\wtlab\\Knowmemo\\knowMemo_newDB_words.xls";
        try {
            InputStream is = new FileInputStream(fileposition);
            Workbook book = Workbook.getWorkbook(is);
            Sheet sheet = book.getSheet(0);
            int Cols = sheet.getColumns();

            addWords = new Words(id,word,phonetics,GEPTlow,GEPTmiddle,GEPTmiddlehigh,GEPThigh,TOEFL,TOEIC,IELTS);;

            for (int i = 0; i < Cols; ++i) {

                addWords.setId(Integer.parseInt(sheet.getCell(i, 0).getContents()));
                addWords.setWord(sheet.getCell(i, 1).getContents());
                addWords.setPhonetics(sheet.getCell(i, 2).getContents());
                addWords.setGEPTlow(Integer.parseInt(sheet.getCell(i, 3).getContents()));
                addWords.setGEPTmiddle(Integer.parseInt(sheet.getCell(i, 4).getContents()));
                addWords.setGEPTmiddlehigh(Integer.parseInt(sheet.getCell(i, 5).getContents()));
                addWords.setGEPThigh(Integer.parseInt(sheet.getCell(i, 6).getContents()));
                addWords.setTOEFL(Integer.parseInt(sheet.getCell(i, 7).getContents()));
                addWords.setTOEIC(Integer.parseInt(sheet.getCell(i, 8).getContents()));
                addWords.setIELTS(Integer.parseInt(sheet.getCell(i, 9).getContents()));
                addWordsData[i] = addWords;

            }

            book.close();

        } catch (BiffException e) {
            System.out.print("BiffExeception : " );
            e.printStackTrace();
        } catch (IOException e) {
            System.out.print("IOException : " );
            e.printStackTrace();
        }

        return addWordsData;
    }

}


