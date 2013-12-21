
package org.sqlite.app.customsqlite;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteStatement;

import android.database.Cursor;

/*
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
*/

public class CustomSqlite extends Activity
{
  private TextView myTV;          /* Text view widget */
  private int myNTest;            /* Number of tests attempted */
  private int myNErr;             /* Number of tests failed */

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    myTV = (TextView)findViewById(R.id.tv_widget);
  }

  public void report_version(){
    SQLiteDatabase db = null;
    SQLiteStatement st;
    String res;

    db = SQLiteDatabase.openOrCreateDatabase(":memory:", null);
    st = db.compileStatement("SELECT sqlite_version()");
    res = st.simpleQueryForString();

    myTV.append("SQLite version " + res + "\n\n");
  }

  public void test_warning(String name, String warning){
    myTV.append("WARNING:" + name + ": " + warning + "\n");
  }

  public void test_result(String name, String res, String expected){
    myTV.append(name + "... ");
    myNTest++;

    if( res.equals(expected) ){
      myTV.append("ok\n");
    } else {
      myNErr++;
      myTV.append("FAILED\n");
      myTV.append("   res=     \"" + res + "\"\n");
      myTV.append("   expected=\"" + expected + "\"\n");
    }
  }

  /*
  ** Use a Cursor to loop through the results of a SELECT query.
  */
  public void csr_test_1(){
    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(":memory:", null);
    String res = "";

    db.execSQL("CREATE TABLE t1(x)");
    db.execSQL("INSERT INTO t1 VALUES ('one'), ('two'), ('three')");
    
    Cursor c = db.rawQuery("SELECT x FROM t1", null);
    if( c!=null ){
      boolean bRes;
      for(bRes=c.moveToFirst(); bRes; bRes=c.moveToNext()){
        String x = c.getString(0);
        res = res + "." + x;
      }
    }else{
      test_warning("csr_test_1", "c==NULL");
    }

    test_result("csr_test_1", res, ".one.two.three");
  }

  public void run_the_tests(View view){
    System.loadLibrary("sqliteX");

    myTV.setText("");
    myNErr = 0;
    myNTest = 0;

    try {
      report_version();
      csr_test_1();

      myTV.append("\n" + myNErr + " errors from " + myNTest + " tests\n");
    } catch(Exception e) {
      myTV.append("Exception: " + e.toString() + "\n");
      myTV.append(android.util.Log.getStackTraceString(e) + "\n");
    }
  }
}


