package c.mike.controldegastos;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class Main extends AppCompatActivity {
    private Cursor fila;
    final DBCreate DBClass = new DBCreate(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Dialog dialogLogin = new Dialog(Main.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        final Dialog dialogReg = new Dialog(Main.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialogLogin.setContentView(R.layout.login);

        Button BtLogin = (Button) dialogLogin.findViewById(R.id.btnLogin);
        BtLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText mLogin = (EditText) dialogLogin.findViewById(R.id.etLogin);
                EditText mPass = (EditText) dialogLogin.findViewById(R.id.etPass);
                SQLiteDatabase db = DBClass.getWritableDatabase();
                String userid = mLogin.getText().toString();
                String passid = mPass.getText().toString();
                fila = db.rawQuery("select user,pass from Users where user='"+userid+"' and pass='"+passid+"'",null);
                if(fila.moveToFirst()==true){
                    String userdb = fila.getString(0);
                    String passdb = fila.getString(1);
                    if (userid.equals(userdb)&&passid.equals(passdb)){
                        Toast.makeText(Main.this,R.string.correct_login,Toast.LENGTH_SHORT).show();
                        dialogLogin.dismiss();
                        SetVar(userid);
                    }
                }else{
                    Toast.makeText(Main.this,R.string.fail_login,Toast.LENGTH_SHORT).show();
                    mLogin.setText("");
                    mPass.setText("");
                }
                db.close();
            }
        });
        Button BtRegister = (Button) dialogLogin.findViewById(R.id.btnRegistrar);
        BtRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText mLogin = (EditText) dialogLogin.findViewById(R.id.etLogin);
                EditText mPass = (EditText) dialogLogin.findViewById(R.id.etPass);
                mLogin.setText("");
                mPass.setText("");
                dialogReg.setContentView(R.layout.register);
                Button BtReg = (Button) dialogReg.findViewById(R.id.btnReg);
                BtReg.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        EditText mRegLogin = (EditText) dialogReg.findViewById(R.id.etRegLogin);
                        EditText mRegPassOne = (EditText) dialogReg.findViewById(R.id.etRegPass);
                        EditText mRegPassTwo = (EditText) dialogReg.findViewById(R.id.etRegPass2);
                        String RegLogin = mRegLogin.getText().toString();
                        String RegPassOne = mRegPassOne.getText().toString();
                        String RegPassTwo = mRegPassTwo.getText().toString();
                        boolean checkUser = DBClass.UserExists(RegLogin);

                        if (RegLogin.matches("")) {
                            Toast.makeText(Main.this,R.string.reg_no_user,Toast.LENGTH_SHORT).show();
                            mRegLogin.setText("");
                            mRegPassOne.setText("");
                            mRegPassTwo.setText("");
                            return;
                        }else if (RegPassOne.matches("") || RegPassTwo.matches("")){
                            Toast.makeText(Main.this,R.string.reg_no_pass,Toast.LENGTH_SHORT).show();
                            mRegPassOne.setText("");
                            mRegPassTwo.setText("");
                            return;
                        }else{
                            if (!checkUser){
                                if (RegPassOne.equals(RegPassTwo)) {
                                    long insertValue = DBClass.CreateUser(RegLogin,RegPassOne);
                                    if(insertValue >= 0){
                                        Toast.makeText(Main.this,R.string.db_insert,Toast.LENGTH_SHORT).show();
                                        dialogReg.dismiss();
                                        dialogLogin.show();
                                    }else{
                                        Toast.makeText(Main.this,R.string.db_error,Toast.LENGTH_SHORT).show();
                                        mRegLogin.setText("");
                                        mRegPassOne.setText("");
                                        mRegPassTwo.setText("");
                                    }
                                }else{
                                    Toast.makeText(Main.this,R.string.reg_pass_not_equal,Toast.LENGTH_SHORT).show();
                                    mRegPassOne.setText("");
                                    mRegPassTwo.setText("");
                                }
                            }else{
                                Toast.makeText(Main.this,R.string.reg_user_exists,Toast.LENGTH_SHORT).show();
                                mRegLogin.setText("");
                            }
                        }
                    }
                });
                Button BtCancel = (Button) dialogReg.findViewById(R.id.btnCancel);
                BtCancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialogReg.dismiss();
                        dialogLogin.show();
                    }
                });
                dialogLogin.dismiss();
                dialogReg.show();
            }
        });
        dialogLogin.show();
        dialogLogin.setCanceledOnTouchOutside(false);

//        dialogLogin.setOnDismissListener(new DialogInterface.OnDismissListener() {
////            @Override
////            public void onDismiss(DialogInterface dialogInterface) {
////                finish();
////            }
////        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }
        finish();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        return;
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

        Intent VarInfo = new Intent(Main.this, Menu.class);
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