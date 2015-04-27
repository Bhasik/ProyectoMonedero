package com.proyecto.alberto.monedero;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

/**
 * Created by Alberto on 19/04/2015.
 */
public class BDMonedero extends SQLiteOpenHelper{

   private static final String DATABASE_NAME = "monedero";
   private ArrayList<Integer> ids;

        public BDMonedero(Context context, int version) {
            super(context, DATABASE_NAME, null, version);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE IF NOT EXISTS monederos (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT );"
            );


            db.execSQL("CREATE TABLE IF NOT EXISTS conceptos (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT );"
            );

            db.execSQL("INSERT INTO conceptos VALUES " +
                            "(null, 'Tarjeta Credito'), " +
                            "(null, 'Tarjeta Debito'), " +
                            "(null, 'Efectivo'), " +
                            "(null, 'Transferencia Bancaria');"
            );


            db.execSQL("CREATE TABLE IF NOT EXISTS pagos (" +
                            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "nombre TEXT, " +
                            "precio REAL, " +
                            "id_concepto INTEGER, " +
                            "id_monedero INTEGER, " +
                            "FOREIGN KEY(id_concepto) REFERENCES conceptos(id), " +
                            "FOREIGN KEY(id_monedero) REFERENCES monederos(id) );"
            );

        }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public void insertarDatos(String baseDatos, String nombre) {

        String sql = "INSERT INTO" + baseDatos +  "VALUES (?,?);";

        SQLiteDatabase db = this.getWritableDatabase();

        SQLiteStatement stmt = db.compileStatement(sql);

        stmt.bindNull(1);
        stmt.bindString(2, nombre);

        stmt.execute();

        db.close();
    }

    public void insertarDatos(String nombreMovimiento, double precio, int id_concepto) {

        String sql = "INSERT INTO monederos VALUES (?,?,?,?);";

        SQLiteDatabase db = this.getWritableDatabase();

        SQLiteStatement stmt = db.compileStatement(sql);

        stmt.bindNull(1);
        stmt.bindString(2, nombreMovimiento);
        stmt.bindDouble(3, precio);
        stmt.bindLong(4, id_concepto);

        stmt.execute();

        db.close();
    }



    public ArrayList<String> seleccionarConceptos(){

        ArrayList<String> conceptos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT  _id,nombre " +
                "FROM conceptos ", null);

        int id_concepto;
        while (cursor.moveToNext()) {

            conceptos.add(cursor.getString(1));


        }

        return conceptos;

    }


    public ArrayList<Integer> getIds() {
        return ids;
    }
}
