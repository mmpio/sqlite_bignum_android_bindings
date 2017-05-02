package org.sqlite.database;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteOpenHelper;

import java.io.File;

import static org.junit.Assert.*;


class MyHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mydb.db";

    public MyHelper(Context ctx){
        super(ctx, ctx.getDatabasePath(DATABASE_NAME).getAbsolutePath(), null, 1);
    }
    public void onConfigure(SQLiteDatabase db){
        db.execSQL("PRAGMA key = 'secret'");

        db.enableWriteAheadLogging();

        final Cursor pragmaCursor = db.rawQuery("PRAGMA journal_mode = WAL", null);
        pragmaCursor.moveToFirst();
        pragmaCursor.close();
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE t1(x)");
    }
    public void onUpgrade(SQLiteDatabase db, int iOld, int iNew){
    }
}


/**
 * Created by dan on 5/3/17.
 */
@RunWith(AndroidJUnit4.class)
public class SeeTest1 {
        private Context mContext;

    @Before
    public void setup() throws Exception {

        System.loadLibrary("sqliteX");

        mContext = InstrumentationRegistry.getTargetContext();

        // delete any existing database
        File databaseFile = mContext.getDatabasePath(MyHelper.DATABASE_NAME);
        databaseFile.mkdirs();
        if (databaseFile.exists()) {
            databaseFile.delete();
        }
    }

    @Test
    public void testAndroidDefaultWalMode() throws Exception {
        // create database
        final MyHelper helper = new MyHelper(mContext);
        helper.getWritableDatabase();

        // verify that WAL journal mode is set
        final Cursor pragmaCursor = helper.getWritableDatabase().rawQuery("PRAGMA journal_mode", null);
        pragmaCursor.moveToFirst();
        Assert.assertEquals(pragmaCursor.getString(pragmaCursor.getColumnIndex("journal_mode")), "wal");
        pragmaCursor.close();

        // start long running transaction
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                helper.getWritableDatabase().beginTransactionNonExclusive();

                // simulate long insert
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                helper.getWritableDatabase().setTransactionSuccessful();
                helper.getWritableDatabase().endTransaction();
            }
        });

        // wait a short time until the long transaction starts
        Thread.sleep(300);

        long startTime = System.currentTimeMillis();

        //try to read something from the database while the slow transaction is running
        helper.getWritableDatabase().execSQL("SELECT * FROM t1");

        //verify that the operation didn't wait until the 3000ms long operation finished
        if (System.currentTimeMillis() - startTime > 3000) {
            throw new Exception("WAL mode isn't working corectly - read operation was blocked");
        }
    }
}
