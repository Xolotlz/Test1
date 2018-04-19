package c.mike.controldegastos;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Menu extends AppCompatActivity {
    final DBCreate DBClass = new DBCreate(this);
    int get_id,get_day,get_month,get_year,get_monto;
    String get_user;
    boolean get_new_sueldo = false;

    int[] ImgFiles = {
            R.drawable.btn_sueldo,
            R.drawable.btn_ingresoextra,
            R.drawable.btn_transporte,
            R.drawable.btn_alimentos,
            R.drawable.btn_accesorio,
            R.drawable.btn_otros,
            R.drawable.btn_grafica
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        TextView ettitle = (TextView) findViewById(R.id.menu_title);
        TextView etuser = (TextView) findViewById(R.id.et_user);
        TextView et1 = (TextView) findViewById(R.id.et1);
        TextView et2 = (TextView) findViewById(R.id.et2);
        TextView et3 = (TextView) findViewById(R.id.et3);
        TextView et4 = (TextView) findViewById(R.id.et4);
        TextView et5 = (TextView) findViewById(R.id.et5);
        TextView et6 = (TextView) findViewById(R.id.et6);
        TextView et7 = (TextView) findViewById(R.id.et7);

        Typeface myFont = Typeface.createFromAsset(getAssets(),"fonts/Exo2-ExtraBoldItalic.otf");
        ettitle.setTypeface(myFont);
        etuser.setTypeface(myFont);
        et1.setTypeface(myFont);
        et2.setTypeface(myFont);
        et3.setTypeface(myFont);
        et4.setTypeface(myFont);
        et5.setTypeface(myFont);
        et6.setTypeface(myFont);
        et7.setTypeface(myFont);

        GetInfo();

        ImageView imgSueldo = (ImageView)findViewById(R.id.ImgSueldo);
        ImageView imgIngreso = (ImageView)findViewById(R.id.IimgIngreso);
        ImageView imgTransporte = (ImageView)findViewById(R.id.ImgTransporte);
        ImageView imgAlimentos = (ImageView)findViewById(R.id.ImgAlimentos);
        ImageView imgAccesorios = (ImageView)findViewById(R.id.ImgAccesorios);
        ImageView imgOtros = (ImageView)findViewById(R.id.ImgOtros);
        ImageView imgGrafica = (ImageView)findViewById(R.id.ImgGrafica);
        etuser.setText(get_user);
        btmReload();

        imgSueldo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!get_new_sueldo){
                    GetMonto(0);
                }else{
                    Toast.makeText(Menu.this,R.string.img_block,Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgIngreso.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(get_new_sueldo){
                    GetMonto(1);
                }else{
                    Toast.makeText(Menu.this,R.string.img_block,Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgTransporte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(get_new_sueldo){
                    GetMonto(2);
                }else{
                    Toast.makeText(Menu.this,R.string.img_block,Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgAlimentos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(get_new_sueldo){
                    GetMonto(3);
                }else{
                    Toast.makeText(Menu.this,R.string.img_block,Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgAccesorios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(get_new_sueldo){
                    GetMonto(4);
                }else{
                    Toast.makeText(Menu.this,R.string.img_block,Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgOtros.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(get_new_sueldo){
                    GetMonto(5);
                }else{
                    Toast.makeText(Menu.this,R.string.img_block,Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgGrafica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(get_new_sueldo){
                    Intent VarInfo = new Intent(Menu.this, Grafica.class);
                    VarInfo.putExtra("id", get_id);
                    VarInfo.putExtra("month", get_month);
                    VarInfo.putExtra("year", get_year);
                    startActivity(VarInfo);
                }else{
                    Toast.makeText(Menu.this,R.string.img_block,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void GetMonto(int i){
        final Dialog dialogMonto = new Dialog(Menu.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialogMonto.setContentView(R.layout.ingreso);
        ImageView NewImg = (ImageView) dialogMonto.findViewById(R.id.Img_Ingreso_Monto);
        Button btIngreso = (Button) dialogMonto.findViewById(R.id.btnIngresoIngresar);
        Button btCancel = (Button) dialogMonto.findViewById(R.id.btnIngresoCancel);
        final EditText etMonto = (EditText) dialogMonto.findViewById(R.id.etIngresoMonto);
        NewImg.setImageResource(ImgFiles[i]);
        final int get_cat = i+1;

        btIngreso.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!etMonto.getText().toString().equals("")) {
                    int monto = Integer.parseInt(etMonto.getText().toString());
                    long insertValue = DBClass.CreateDetalle(get_id,monto,get_cat,get_day,get_month,get_year);
                    if(insertValue >=0) {
                        get_new_sueldo = true;
                        btmReload();
                    }
                    if(insertValue >= 0){
                        Toast.makeText(Menu.this,R.string.monto_db_insert,Toast.LENGTH_SHORT).show();
                        etMonto.setText("");
                        dialogMonto.dismiss();
                    }else{
                        Toast.makeText(Menu.this,R.string.db_error,Toast.LENGTH_SHORT).show();
                        etMonto.setText("");
                    }
                }else{
                    Toast.makeText(Menu.this,R.string.ingreso_et_monto,Toast.LENGTH_SHORT).show();
                }
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                etMonto.setText("");
                dialogMonto.dismiss();
            }
        });
        dialogMonto.show();
    }

    public void GetInfo(){
        Bundle extras = getIntent().getExtras();
        get_id = extras.getInt("id");
        get_user = extras.getString(("user"));
        get_user = extras.getString("user");
        get_day = extras.getInt("day");
        get_month = extras.getInt("month");
        get_year = extras.getInt("year");
    }

    public void btmReload() {
        getSueldo();

        ImageView imgSueldo = (ImageView)findViewById(R.id.ImgSueldo);
        ImageView imgIngreso = (ImageView)findViewById(R.id.IimgIngreso);
        ImageView imgTransporte = (ImageView)findViewById(R.id.ImgTransporte);
        ImageView imgAlimentos = (ImageView)findViewById(R.id.ImgAlimentos);
        ImageView imgAccesorios = (ImageView)findViewById(R.id.ImgAccesorios);
        ImageView imgOtros = (ImageView)findViewById(R.id.ImgOtros);
        ImageView imgGrafica = (ImageView)findViewById(R.id.ImgGrafica);

        if(get_new_sueldo) {
            imgSueldo.setImageResource(R.drawable.btn_sueldo_bloqueado);
            imgIngreso.setImageResource(R.drawable.btn_ingresoextra);
            imgTransporte.setImageResource(R.drawable.btn_transporte);
            imgAlimentos.setImageResource(R.drawable.btn_alimentos);
            imgAccesorios.setImageResource(R.drawable.btn_accesorio);
            imgOtros.setImageResource(R.drawable.btn_otros);
            imgGrafica.setImageResource(R.drawable.btn_grafica);
        }else{
            imgSueldo.setImageResource(R.drawable.btn_sueldo);
            imgIngreso.setImageResource(R.drawable.btn_ingresoextra_bloqueado);
            imgTransporte.setImageResource(R.drawable.btn_transporte_bloqueado);
            imgAlimentos.setImageResource(R.drawable.btn_alimentos_bloqueado);
            imgAccesorios.setImageResource(R.drawable.btn_accesorio_bloqueado);
            imgOtros.setImageResource(R.drawable.btn_otros_bloqueado);
            imgGrafica.setImageResource(R.drawable.btn_grafica_bloqueado);
        }
    }

    private int getSueldo(){
        DBCreate DBClass = new DBCreate(this);
        SQLiteDatabase db = DBClass.getReadableDatabase();
        int monto = 0;
        Cursor fila;
        fila = db.rawQuery("select monto from Detalle where " +
                "id_user = "+get_id+" and " +
                "id_cat="+1+" and " +
                "id_month="+get_month+" and " +
                "id_year="+get_year,null);
        if(fila.moveToFirst()==true) {
            monto = fila.getInt(0);
            if(monto == 0){
                get_new_sueldo = false;
            }else{
                get_new_sueldo = true;
            }
        }
        db.close();
        return monto;
    }
}