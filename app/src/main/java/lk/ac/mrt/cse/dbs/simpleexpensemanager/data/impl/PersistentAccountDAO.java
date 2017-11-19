package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.util.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import java.util.ArrayList;
import java.util.List;

public class PersistentAccountDAO implements AccountDAO 
{
    Database database;

    public PersistentAccountDAO(Context context)
    {
        database = new Database(context);
    }

    @Override
    public List<String> getAccountNumbersList()
    {
        SQLiteDatabase databaseCon = database.getReadableDatabase();
        String[] column = {"accountNo"};
        Cursor cursor = databaseCon.query("account", column, null, null, null, null, null);
        List accountNoList = new ArrayList<>();

        while(cursor.moveToNext())
        {
            String itemID = cursor.getString(cursor.getColumnIndexOrThrow("accountNo"));
            accountNoList.add(itemID);
        }

        cursor.close();
        return accountNoList;
    }

    @Override
    public List<Account> getAccountsList()
    {
        SQLiteDatabase databaseCon = database.getReadableDatabase();
        String[] column = {"accountNo", "bankName", "accountHolderName", "balance"};
        Cursor cursor = databaseCon.query("account", column, null, null, null, null, null);
        List accountList = new ArrayList<>();

        while(cursor.moveToNext())
        {
            String accountNo = cursor.getString(cursor.getColumnIndexOrThrow("accountNo"));
            String bankName = cursor.getString(cursor.getColumnIndexOrThrow("bankName"));
            String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow("accountHolderName"));
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow("balance"));

            Account account = new Account(accountNo, bankName, accountHolderName, balance);

            accountList.add(account);
        }

        cursor.close();
        return accountList;
    }

    @Override
    public Account getAccount(String accNo) throws InvalidAccountException
    {
        SQLiteDatabase databaseCon = database.getReadableDatabase();
        String[] column = {"accountNo", "bankName", "accountHolderName", "balance"};
        String[] arg = {accNo};
        Cursor cursor = databaseCon.query("account", column, "accountNo = ?", arg, null, null, null);

        Account account = null;

        while(cursor.moveToNext())
        {
            String accountNo = cursor.getString(cursor.getColumnIndexOrThrow("accountNo"));
            String bankName = cursor.getString(cursor.getColumnIndexOrThrow("bankName"));
            String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow("accountHolderName"));
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow("balance"));

            account = new Account(accountNo, bankName, accountHolderName, balance);
        }

        cursor.close();
        return account;
    }

    @Override
    public void addAccount(Account account) 
    {

        SQLiteDatabase databaseCon = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("accountNo",account.getAccountNo());
        values.put("bankName",account.getBankName());
        values.put("accountHolderName",account.getAccountHolderName());
        values.put("balance",account.getBalance());

        long row = databaseCon.insert("account", null, values);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException 
    {

        SQLiteDatabase databaseCon = database.getWritableDatabase();
        String selection = "accountNo = ?";
        String[] args = { accountNo };
        databaseCon.delete("account", selection, args);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException 
    {

        SQLiteDatabase databaseCon = database.getWritableDatabase();
        Account tempAcc = getAccount(accountNo);

        double value = 0;

        switch (expenseType) 
        {
            case EXPENSE:
                value = tempAcc.getBalance() - amount;
                break;
            case INCOME:
                value = tempAcc.getBalance() + amount;
                break;
        }

        ContentValues values = new ContentValues();
        values.put("balance" , value );

        String selection = "accountNo = ?";
        String[] selectionArgs = { accountNo };

        int count = databaseCon.update("account", values, selection, selectionArgs);
    }
}