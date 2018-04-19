package c.mike.controldegastos;

import android.app.Activity;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

/**
 * The configuration screen for the {@link Widget Widget} AppWidget.
 */
public class WidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "c.mike.controldegastos.Widget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;

    private Cursor fila;
    final DBCreate DBClass = new DBCreate(this);



    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = mAppWidgetText.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Widget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public WidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.widget_configure);
        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mAppWidgetText.setText(loadTitlePref(WidgetConfigureActivity.this, mAppWidgetId));








//        final Dialog dialogLogin = new Dialog(WidgetConfigureActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
//        final Dialog dialogReg = new Dialog(WidgetConfigureActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
//        dialogLogin.setContentView(R.layout.login);
//
//        Button BtLogin = (Button) dialogLogin.findViewById(R.id.btnLogin);
//        BtLogin.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                EditText mLogin = (EditText) dialogLogin.findViewById(R.id.etLogin);
//                EditText mPass = (EditText) dialogLogin.findViewById(R.id.etPass);
//                SQLiteDatabase db = DBClass.getWritableDatabase();
//                String userid = mLogin.getText().toString();
//                String passid = mPass.getText().toString();
//                fila = db.rawQuery("select user,pass from Users where user='"+userid+"' and pass='"+passid+"'",null);
//                if(fila.moveToFirst()==true){
//                    String userdb = fila.getString(0);
//                    String passdb = fila.getString(1);
//                    if (userid.equals(userdb)&&passid.equals(passdb)){
//                        Toast.makeText(WidgetConfigureActivity.this,R.string.correct_login,Toast.LENGTH_SHORT).show();
//                        dialogLogin.dismiss();
//                        SetVar(userid);
//                    }
//                }else{
//                    Toast.makeText(WidgetConfigureActivity.this,R.string.fail_login,Toast.LENGTH_SHORT).show();
//                    mLogin.setText("");
//                    mPass.setText("");
//                }
//                db.close();
//            }
//        });
//        Button BtRegister = (Button) dialogLogin.findViewById(R.id.btnRegistrar);
//        BtRegister.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                EditText mLogin = (EditText) dialogLogin.findViewById(R.id.etLogin);
//                EditText mPass = (EditText) dialogLogin.findViewById(R.id.etPass);
//                mLogin.setText("");
//                mPass.setText("");
//                dialogReg.setContentView(R.layout.register);
//                Button BtReg = (Button) dialogReg.findViewById(R.id.btnReg);
//                BtReg.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        EditText mRegLogin = (EditText) dialogReg.findViewById(R.id.etRegLogin);
//                        EditText mRegPassOne = (EditText) dialogReg.findViewById(R.id.etRegPass);
//                        EditText mRegPassTwo = (EditText) dialogReg.findViewById(R.id.etRegPass2);
//                        String RegLogin = mRegLogin.getText().toString();
//                        String RegPassOne = mRegPassOne.getText().toString();
//                        String RegPassTwo = mRegPassTwo.getText().toString();
//                        boolean checkUser = DBClass.UserExists(RegLogin);
//
//                        if (RegLogin.matches("")) {
//                            Toast.makeText(WidgetConfigureActivity.this,R.string.reg_no_user,Toast.LENGTH_SHORT).show();
//                            mRegLogin.setText("");
//                            mRegPassOne.setText("");
//                            mRegPassTwo.setText("");
//                            return;
//                        }else if (RegPassOne.matches("") || RegPassTwo.matches("")){
//                            Toast.makeText(WidgetConfigureActivity.this,R.string.reg_no_pass,Toast.LENGTH_SHORT).show();
//                            mRegPassOne.setText("");
//                            mRegPassTwo.setText("");
//                            return;
//                        }else{
//                            if (!checkUser){
//                                if (RegPassOne.equals(RegPassTwo)) {
//                                    long insertValue = DBClass.CreateUser(RegLogin,RegPassOne);
//                                    if(insertValue >= 0){
//                                        Toast.makeText(WidgetConfigureActivity.this,R.string.db_insert,Toast.LENGTH_SHORT).show();
//                                        dialogReg.dismiss();
//                                        dialogLogin.show();
//                                    }else{
//                                        Toast.makeText(WidgetConfigureActivity.this,R.string.db_error,Toast.LENGTH_SHORT).show();
//                                        mRegLogin.setText("");
//                                        mRegPassOne.setText("");
//                                        mRegPassTwo.setText("");
//                                    }
//                                }else{
//                                    Toast.makeText(WidgetConfigureActivity.this,R.string.reg_pass_not_equal,Toast.LENGTH_SHORT).show();
//                                    mRegPassOne.setText("");
//                                    mRegPassTwo.setText("");
//                                }
//                            }else{
//                                Toast.makeText(WidgetConfigureActivity.this,R.string.reg_user_exists,Toast.LENGTH_SHORT).show();
//                                mRegLogin.setText("");
//                            }
//                        }
//                    }
//                });
//                Button BtCancel = (Button) dialogReg.findViewById(R.id.btnCancel);
//                BtCancel.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        dialogReg.dismiss();
//                        dialogLogin.show();
//                    }
//                });
//                dialogLogin.dismiss();
//                dialogReg.show();
//            }
//        });
//        dialogLogin.show();
    }


    public void SetVar(String username){
        int id_month,id_year;
        int Day,Month,Year;
        int Monto = 0;
        int userID = 0;
        boolean SueldoMes = false;
        Calendar instance = Calendar.getInstance();
        Day = instance.get(Calendar.DAY_OF_MONTH);
        Month = (instance.get(Calendar.MONTH))+1;
        Year = instance.get(Calendar.YEAR);
        SQLiteDatabase db = DBClass.getReadableDatabase();
        String Query1 = "select id_user from Users where user = '"+username+"'";
        Cursor file1 = db.rawQuery(Query1, null);
        if(file1.moveToFirst()==true){
            userID = file1.getInt(0);
        }
        file1.close();
        String Query2 = "select id_month,id_year,monto from Detalle where " +
                "id_user = "+userID+" and " +
                "id_month = "+Month+" and " +
                "id_year = "+Year;
        Cursor file2 = db.rawQuery(Query2, null);
        if(file2.moveToFirst()==true){
            id_month = file2.getInt(0);
            id_year = file2.getInt(1);
            Monto = file2.getInt(2);
            if ((Monto > 0) && (id_month == Month) && (id_year == Year) ) {
                SueldoMes = true;
            }
        }
        file2.close();
        Intent VarInfo = new Intent(WidgetConfigureActivity.this, Menu.class);
        VarInfo.putExtra("id", userID);
        VarInfo.putExtra("user", username);
        VarInfo.putExtra("day", Day);
        VarInfo.putExtra("month", Month);
        VarInfo.putExtra("year", Year);
        VarInfo.putExtra("monto", Monto);
        VarInfo.putExtra("sueldo", SueldoMes);
        finish();
        startActivity(VarInfo);
    }
}

