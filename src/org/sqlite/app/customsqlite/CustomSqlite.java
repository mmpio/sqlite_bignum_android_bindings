
package org.sqlite.app.customsqlite;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteStatement;

/*
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
*/

public class CustomSqlite extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void find_version(View view){

      System.loadLibrary("sqliteX");

      SQLiteDatabase db = null;
      SQLiteStatement st;
      String res;

      TextView tv = (TextView)findViewById(R.id.tv_widget);
      tv.setText("<attempting to access sqlite...>");

      try {
        db = SQLiteDatabase.openOrCreateDatabase(":memory:", null);
        st = db.compileStatement("SELECT sqlite_version()");
        res = st.simpleQueryForString();

        tv.setText(res);
            
      }catch(Exception e){
        Log.e("Error", "Error", e);
        tv.setText(e.toString());
      } finally {
        if (db != null)
          db.close();
      }
    }
}
