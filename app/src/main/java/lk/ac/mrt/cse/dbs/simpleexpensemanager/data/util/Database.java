package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper 
{

    public static final String DATABASE_NAME = "expensemgr150532E.db";
    public static final int DATABASE_VERSION = 1;
    

    public Database(Context context)
    {
        super  (context, DATABASE_NAME, null, DATABASE_VERSION);
        
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE account (accountNo TEXT PRIMARY KEY, bankName TEXT, accountHolderName TEXT, balance REAL)");
        sqLiteDatabase.execSQL("CREATE TABLE transaction (date TEXT, accountNo TEXT, expenceType TEXT, amount REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        return;
    }

}