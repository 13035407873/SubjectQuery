package cn.xuemcu.myapplication1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 朱红晨 on 2017/8/14.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static String CREATE_PASSWORD = "create table TestWay ("
            + "ID integer primary key autoincrement, "
            + "Subject text, "
            + "Option text, "
            + "Answer text)";
    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PASSWORD);
        Toast.makeText(mContext,"Create TestWay Succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
