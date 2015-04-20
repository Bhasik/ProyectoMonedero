package com.proyecto.alberto.monedero;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alberto on 19/04/2015.
 */
public class BDMonedero extends SQLiteOpenHelper{

   private static final String DATABASE_NAME = "monedero";

        public BDMonedero(Context context, int version) {
            super(context, DATABASE_NAME, null, version);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE IF NOT EXISTS id (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT );");

            db.execSQL("INSERT INTO categoria " +
                    "VALUES " +
                    "(null, 'RPG y Aventuras Gráficas'), " +
                    "(null, 'Estrategia'), " +
                    "(null, 'Lucha'), " +
                    "(null, 'Plataformas y Aventuras'), " +
                    "(null, 'Shooter'), " +
                    "(null, 'Deportes'), " +
                    "(null, 'Otros');");

            db.execSQL("CREATE TABLE IF NOT EXISTS dificultad (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT );");

            db.execSQL("INSERT INTO dificultad VALUES " +
                    "(null, 'Fácil'), " +
                    "(null, 'Normal'), " +
                    "(null, 'Difícil'), " +
                    "(null, 'Muy difícil');");

            db.execSQL("CREATE TABLE IF NOT EXISTS franquicia (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT );");
        }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

}
