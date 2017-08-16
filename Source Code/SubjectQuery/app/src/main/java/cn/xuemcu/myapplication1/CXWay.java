package cn.xuemcu.myapplication1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 朱红晨 on 2017/8/14.
 */

public class CXWay {
    private MyDatabaseHelper myDatabaseHelper = null;
    private SQLiteDatabase sqLiteDatabase = null;
    private Context mContext;

    public CXWay(Context Context) {
        this.mContext = Context;
        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(mContext, "TestWay.db", null, 1);
        sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

    }

    public int GetDataLength() throws Exception {
        int num = 0;
        if(sqLiteDatabase == null)
            Toast.makeText(mContext,"sqLiteDatabase is null !!!",Toast.LENGTH_SHORT).show();
        else {
            Cursor cursor = sqLiteDatabase.rawQuery(
                    "select * from TestWay where 1 = 1"
                    , null);
            if (cursor.moveToFirst()) {
                do {
                    num++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return num;
    }

    private void SQLDeleteCXWay() {
        sqLiteDatabase.execSQL("delete from TestWay where 1 = 1");
        Toast.makeText(mContext,"删除所有数据！",Toast.LENGTH_SHORT).show();
    }
    /*
    private void CreatFolder() {
        File file = new File(mContext.getObbDir().getPath() + FolderStr);
        if (!file.exists())
        {
            file.mkdirs();
            Toast.makeText(mContext, "已创建文件夹:" + mContext.getObbDir().getPath() + FolderStr, Toast.LENGTH_SHORT).show();
        }
    }
    */
    public int SQLUpData() {
        SQLDeleteCXWay();
        /*
        File file = new File(mContext.getObbDir().getPath() + FolderStr + "/" + FileName);
        if(!file.exists()){
            //文件不存在
            Toast.makeText(mContext,FileName +"文件不存在",Toast.LENGTH_SHORT).show();
            return -1;
        } else {
            //文件存在
            Toast.makeText(mContext,FileName +"文件存在",Toast.LENGTH_SHORT).show();
        }
        */

        int Num = 0;
        String Subject = "";
        String Option  = "";
        String Answer  = "";
        try {
            InputStream read = mContext.getResources().openRawResource(R.raw.subject);
            InputStreamReader read1 = new InputStreamReader(read);
            BufferedReader bufferedReader = new BufferedReader(read1);
            String lineTxt = null;
            while((lineTxt = bufferedReader.readLine()) != null){
                if(lineTxt.length() >= 2) {
                    if((lineTxt.charAt(0) >= '0' && lineTxt.charAt(0) <= '9') ||
                            (lineTxt.charAt(0) >= 'A' && lineTxt.charAt(0) <= 'Z')) {
                        Num++;
                        String str[] = lineTxt.split("：（");
                        if (Num == 1 && str.length == 2) {
                            Subject = str[0];
                            //Toast.makeText(mContext,Subject,Toast.LENGTH_SHORT).show();
                            str[1] = str[1].replace("）", "");
                            str[1] = str[1].replace(" ", "");
                            Answer = str[1];
                        } else if (str.length == 1) {
                            Option += str[0];
                            Option += "#";
                            //if(Num == 2)
                            //    Toast.makeText(mContext,Option,Toast.LENGTH_SHORT).show();
                        } else if (str.length == 2) {
                            Option = Option.substring(0, Option.length() - 1);
                            ContentValues values = new ContentValues();
                            values.put("Subject", Subject);
                            values.put("Option", Option);
                            values.put("Answer", Answer);
                            sqLiteDatabase.insert("TestWay", null, values);

                            Subject = str[0];
                            Option = "";
                            str[1] = str[1].replace("）", "");
                            str[1] = str[1].replace(" ", "");
                            Answer = str[1];
                        }
                    }
                }
            }

            Option = Option.substring(0, Option.length() - 1);
            ContentValues values = new ContentValues();
            values.put("Subject", Subject);
            values.put("Option", Option);
            values.put("Answer", Answer);
            sqLiteDatabase.insert("TestWay", null, values);

            Toast.makeText(mContext, Subject, Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext, Answer, Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext, Option, Toast.LENGTH_SHORT).show();

            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return 0;
    }

    //KeyWord 格式   %关键字%
    public List<CXSubject> SQLFind(String[] KeyWord) {
        List<CXSubject> subjectList = new ArrayList<>();
        subjectList.clear();
        String str = "";
        str = "select * from TestWay where Subject like ? ";
        for(int i = 0; i < KeyWord.length - 1; i++) {
            str += "and Subject like ? ";
        }
        Cursor cursor = null;
        cursor = sqLiteDatabase.rawQuery(str, KeyWord);
        if(cursor.moveToFirst()) {
            do {
                CXSubject cxSubject = new CXSubject(cursor.getString(cursor.getColumnIndex("Subject")),
                        cursor.getString(cursor.getColumnIndex("Option")),
                        cursor.getString(cursor.getColumnIndex("Answer")));
                subjectList.add(cxSubject);
            } while(cursor.moveToNext());
        }

        return subjectList;
    }
}

