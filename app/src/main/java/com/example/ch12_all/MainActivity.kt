package com.example.ch12_all

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var edtName = findViewById<EditText>(R.id.edtName)
        var edtNumber = findViewById<EditText>(R.id.edtNumber)

        var edtNameResult = findViewById<EditText>(R.id.edtNameResult)
        var edtNumberResult = findViewById<EditText>(R.id.edtNumberResult)

        var btnInit = findViewById<Button>(R.id.btnInit)
        var btnInsert = findViewById<Button>(R.id.btnInsert)
        var btnSelect = findViewById<Button>(R.id.btnSelect)

        //1.database 생성 -> SQLiteOpenHelper:[생성자로 만듦] -> 상속받는 클래스 만들고(MyDBHelper) -> 객체 만들고
        //2.Table    생성 -> SQLiteOpenHelper:[onCreate()]
        //3.Insert 기능 만들기 -> SQLiteDatabase -> execSQL()메소드에 insert 쿼리문 작성
        //4.Select 기능 만들기 -> SQLiteDatabase -> rawQuery() 메소드에 select 쿼리문 작성 -> Cursor에서 반환값처리

        //1.MyDBHelper클래스 객체 생성만 해도, 1.DB와 2.테이블이 만들어짐
        var myHelper1 = MyDBHelper(applicationContext)

    }
    //내부 클래스로 만들겠다
    //1.database 생성 -> SQLiteOpenHelper:[생성자로 만듦] -> 상속받는 클래스 만들고(MyDBHelper)
    inner class MyDBHelper(context: Context) : SQLiteOpenHelper(context, "groupDB", null, 1)
    {
        //2.Table 생성 -> SQLiteOpenHelper:[onCreate()]
        override fun onCreate(p0: SQLiteDatabase?)//SQLiteDatabase -> execSQL() -> create table 쿼리 작성하여 수행
        {
            p0!!.execSQL("create table groupTBL (gName char(20), gNumber integer);")//execSQL()  -> create table 쿼리 작성하여 수행
        }

        //upgrade의 의미 -> 재생성 하겠다
        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("drop table IF EXISTS groupTBL;")//execSQL()  -> create table 쿼리 작성하여 수행
            onCreate(p0)
        }
    }
}