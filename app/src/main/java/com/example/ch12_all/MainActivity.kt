package com.example.ch12_all

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity()
{
    lateinit var myHelper1 : MyDBHelper
    lateinit var sqlDB : SQLiteDatabase

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
        var btnUpdate = findViewById<Button>(R.id.btnUpdate)
        var btnDelete = findViewById<Button>(R.id.btnDelete)

        //1.database 생성 -> SQLiteOpenHelper:[생성자로 만듦] -> 상속받는 클래스 만들고(MyDBHelper) -> 객체 만들고
        //2.Table    생성 -> SQLiteOpenHelper:[onCreate()]
        //3.Insert 기능 만들기 -> SQLiteDatabase -> execSQL()메소드에 insert 쿼리문 작성
        //4.Select 기능 만들기 -> SQLiteDatabase -> rawQuery() 메소드에 select 쿼리문 작성 -> Cursor에서 반환값처리

        //1.MyDBHelper클래스 객체 생성만 해도, 1.DB와 2.테이블이 만들어짐
        myHelper1 = MyDBHelper(this)
//        btnSelect.callOnClick() //여기는 안먹힘! 밑에 코드가 아직 초기화 안되었기 때문에
//
        //초기화 버튼
        btnInit.setOnClickListener {
            sqlDB  = myHelper1.writableDatabase
            myHelper1.onUpgrade(sqlDB,1,2)
            sqlDB.close()
        }

        //입력 insert
        btnInsert.setOnClickListener {
            sqlDB  = myHelper1.writableDatabase
            sqlDB.execSQL("insert into groupTBL values ('"+ edtName.text.toString() +"', "+edtNumber.text.toString()+");")
            sqlDB.close()
            Toast.makeText(applicationContext, "입력완료~!", Toast.LENGTH_SHORT).show()

            btnSelect.callOnClick()
        }

        //수정
        btnUpdate.setOnClickListener {
            sqlDB  = myHelper1.writableDatabase
//            var sql = "update groupTBL set gNumber= "+edtNumber.text.toString()+" where gName = '"+edtName.text.toString()+"'; "
            if(edtName.text.toString() != "") {
                var sql = " update "
                sql += "         groupTBL "
                sql += " set "
                sql += "         gNumber= " + edtNumber.text.toString()
                sql += " where "
                sql += "         gName = '" + edtName.text.toString() + "'; "

                sqlDB.execSQL(sql)
                sqlDB.close()
                Toast.makeText(applicationContext, "수정완료~!", Toast.LENGTH_SHORT).show()

                btnSelect.callOnClick()
            }
        }

        //삭제
        btnDelete.setOnClickListener {
            sqlDB  = myHelper1.writableDatabase
            if(edtName.text.toString() != "")
            {
                sqlDB.execSQL("delete from groupTBL where gName = '"+edtName.text.toString()+"';")
                sqlDB.close()
                Toast.makeText(applicationContext, "삭제 완료~!", Toast.LENGTH_SHORT).show()

                btnSelect.callOnClick()
            }
        }

        btnSelect.setOnClickListener {
            sqlDB  = myHelper1.readableDatabase

//            sqlDB.execSQL("select gName, gNumber from groupTBL;")//이게 아님!!!
            var c1 : Cursor = sqlDB.rawQuery("select gName, gNumber from groupTBL;", null)//이게 아님!!!

            var gName : String =""
            var gNumber : String =""

            while(c1.moveToNext())
            {
                gName   = gName   + c1.getString(0) + "\r\n"
                gNumber = gNumber + c1.getString(1) + "\r\n"
            }

            edtNameResult.setText(gName)
            edtNumberResult.setText(gNumber)

            c1.close()
            sqlDB.close()
        }

        btnSelect.callOnClick() //이거 안먹힘!@ 왠지 모름~!

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