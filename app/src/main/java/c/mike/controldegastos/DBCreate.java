package c.mike.controldegastos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCreate  extends SQLiteOpenHelper{

    private static final String DB_NOMBRE="dbcontrol.sqlite";
    private static Integer DB_SCHEME_VERSION=1;

    public DBCreate(Context context) {
        super(context, DB_NOMBRE, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Users(" +
                "id_user integer primary key autoincrement," +
                "user text," +
                "pass text)"
        );
        db.execSQL("create table Categorias(" +
                "id_cat integer primary key," +
                "nombre text," +
                "resta integer)"
        );
        db.execSQL("insert into Categorias values(1,'Sueldo',0)");
        db.execSQL("insert into Categorias values(2,'Ingreso Extra',0)");
        db.execSQL("insert into Categorias values(3,'Transporte',1)");
        db.execSQL("insert into Categorias values(4,'Alimentos',1)");
        db.execSQL("insert into Categorias values(5,'Accesorios',1)");
        db.execSQL("insert into Categorias values(6,'Otros',1)");
        db.execSQL("create table Detalle(" +
                "id_detalle integer primary key autoincrement," +
                "id_user integer," +
                "monto integer," +
                "id_cat integer," +
                "id_day integer," +
                "id_month integer," +
                "id_year integer)"
        );
        db.execSQL("create table Resultado(" +
                "id_resultado integer primary key autoincrement," +
                "id_user integer," +
                "id_month integer," +
                "id_year integer," +
                "id_cat integer," +
                "total integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS "+"Users");
//        db.execSQL("DROP TABLE IF EXISTS "+"Categorias");
//        db.execSQL("DROP TABLE IF EXISTS "+"Detalle");
//        db.execSQL("DROP TABLE IF EXISTS "+"Resultado");
//        onCreate(db);
    }

    public boolean UserExists(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "select * from Users where user = '" + username + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public long CreateUser(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.putNull("id_user");
        values.put("pass",password);
        values.put("user",username);
        long rowID = db.insert("Users","",values);
        db.close();
     return rowID;
    }

    public long CreateDetalle(int id,int monto,int categ,int day,int month,int year){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.putNull("id_detalle");
        values.put("id_user",id);
        values.put("monto",monto);
        values.put("id_cat",categ);
        values.put("id_day",day);
        values.put("id_month",month);
        values.put("id_year",year);
        long rowID = db.insert("Detalle","",values);

        if(categ == 1 && rowID >= 0){
            db.execSQL("insert into Resultado values(null,"+id+","+month+","+year+",1,0)");
            db.execSQL("insert into Resultado values(null,"+id+","+month+","+year+",2,0)");
            db.execSQL("insert into Resultado values(null,"+id+","+month+","+year+",3,0)");
            db.execSQL("insert into Resultado values(null,"+id+","+month+","+year+",4,0)");
            db.execSQL("insert into Resultado values(null,"+id+","+month+","+year+",5,0)");
            db.execSQL("insert into Resultado values(null,"+id+","+month+","+year+",6,0)");
            db.execSQL("insert into Resultado values(null,"+id+","+month+","+year+",7,0)");
        }else{
            Cursor fila;
            fila = db.rawQuery("select id_resultado,total from Resultado where " +
                    "id_user = "+id+" and " +
                    "id_cat="+categ+" and " +
                    "id_month="+month+" and " +
                    "id_year="+year,null);
            if(fila.moveToFirst()==true) {
                int id_resultado = fila.getInt(0);
                int total = fila.getInt(1);
                total = (total + monto);
                db.execSQL("UPDATE Resultado SET total = "+ total +" WHERE id_resultado = "+id_resultado);
            }
        }
        db.close();
        return rowID;
    }
}