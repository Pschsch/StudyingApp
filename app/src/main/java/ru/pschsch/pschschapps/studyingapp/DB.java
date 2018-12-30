package ru.pschsch.pschschapps.studyingapp;

/*****************РАБОТА С SQLite напрямую - устраевший подход!!!!!!!************************/
/**********************ИСПОЛЬЗУЕТСЯ ROOM*****************************************************/


/*
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;  //конст на версию
    public static final String DB_NAME = "MyDB"; // конст на имя БД
    public static final String INDEX = "1"; //конст на имя таблицы
    public static final String KEY_ID = "_id"; //идентификатор для работы в с SQLite в Android
    // начинается именно с нижнего подчеркивания, работа с курсорами
    public static final String KEY_MAIL = "mail";
    public static final String KEY_NAME = "name";
    DB(Context context, String name, int version){
        super(context,DB_NAME,null,DB_VERSION);  //к фактори позже
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table" + INDEX + "(" + KEY_ID + "integer primary key,"
                + KEY_NAME + "text," + KEY_MAIL + "text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
*/