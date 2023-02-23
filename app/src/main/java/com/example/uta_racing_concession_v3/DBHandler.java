package com.example.uta_racing_concession_v3;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "concession";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "accounts";

    // below variable is for our primary key column.
    private static final String KEY_COL = "key_id";

    // below variable is our persons id column.
    private static final String ID_COL = "id";

    // below variable is for our persons name column
    private static final String NAME_COL = "name";

    // below variable id for our account balance column.
    private static final String BALANCE_COL = "balance";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " ("
            + KEY_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ID_COL + " INTEGER,"
            + NAME_COL + " TEXT,"
            + BALANCE_COL + " DOUBLE)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types
        // at last we are calling a exec sql
        // method to execute sql query
        db.execSQL(SQL_CREATE_ENTRIES);
    }


    /**
     *  Create a new account to add to the database
     * @param name
     * @param id
     * @param startingBalance
     * @return void
     */
    public void addAccount(String name, Integer id , double startingBalance) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(ID_COL, id);
        values.put(NAME_COL, name);
        values.put(BALANCE_COL, startingBalance);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     *
     * @param id IF null, return full list
     * @return NULL if no account found AccountModal if given id, ArrayList<AccountModal> if no id given
     */
    @Nullable
    public Object getAccount(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query;
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor;

        if(id != null) {
            query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COL + " = ?";
            cursor = db.rawQuery(query, selectionArgs);
        }
        else{
            query = "SELECT * FROM " + TABLE_NAME;
            cursor = db.rawQuery(query,null);
        }


        //Made in case multiple accounts pop up with the same ID or ID not given
        ArrayList<AccountModal> accountModalArrayList = new ArrayList<>();

        if(cursor.moveToFirst()){
            do {
                //cursor.get(0) is the primary key
                accountModalArrayList.add(new AccountModal(cursor.getInt(1),
                                                            cursor.getString(2),
                                                            cursor.getDouble(3)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        Object mstate;
        if(id !=null){
            try {
                mstate = accountModalArrayList.get(0);
            }catch(Exception e){
                return null;
            }
            if(mstate==null){
                return null;
            }
            return mstate;
        }
        else{
            mstate =accountModalArrayList;
            if (mstate==null){
                return null;
            }
            else {
                return accountModalArrayList;
            }
        }

    }

    /**
     * @param id ID of account to delete
     * @return True if successful
     */
    public boolean delAccount(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();

        //Refuse to delete account with negative balance remaining
        AccountModal accountModal = (AccountModal) getAccount(id);
        if(accountModal==null){
            return false;
        }
        if (accountModal.getBalance()<0.00){
            return false;
        }
        else{
            return db.delete(TABLE_NAME, ID_COL + "="+id, null) > 0;
        }
    }


    /**
     * DO NOT UPDATE THE ID, this will cause the account to not be found.
     * @param accountModal: Pre-updated accountModal passed in to be stored in Database.
     * @return
     */
    public boolean updateAccount(AccountModal accountModal){
        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if(accountModal == null) {
            return false;
        }
        else{
            values.put(ID_COL, accountModal.getId());
            values.put(NAME_COL,accountModal.getName());
            values.put(BALANCE_COL,accountModal.getBalance());

            db.update(TABLE_NAME, values, "id=?",new String[]{accountModal.getId().toString()});
        }
        return true;
    }

}

