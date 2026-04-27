package com.example.androidpractice13_2;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // 必须与应用1的AUTHORITY完全一致
    private static final String AUTHORITY = "com.example.androidpractice13_1.studentprovider";
    private static final String TABLE_NAME = "student";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    // 字段名必须与应用1相同
    public static final String STU_ID = "stu_id";
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String MAJOR = "major";

    private Button btnQueryAll, btnQueryById;
    private EditText etQueryId;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnQueryAll = findViewById(R.id.btn_query_all);
        btnQueryById = findViewById(R.id.btn_query_by_id);
        etQueryId = findViewById(R.id.et_query_id);
        tvResult = findViewById(R.id.tv_result);

        // 查询所有
        btnQueryAll.setOnClickListener(v -> queryAllStudents());

        // 按学号查询
        btnQueryById.setOnClickListener(v -> {
            String stuId = etQueryId.getText().toString().trim();
            if (!stuId.isEmpty()) {
                queryStudentById(stuId);
            } else {
                Toast.makeText(MainActivity.this, "请输入学号", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 跨应用查询所有学生
    private void queryAllStudents() {
        StringBuilder sb = new StringBuilder();
        try {
            Cursor cursor = getContentResolver().query(CONTENT_URI,
                    null, null, null, null);

            if (cursor == null) {
                sb.append("访问失败：Provider 不存在或未授权\n");
            } else {
                while (cursor.moveToNext()) {
                    String stuId = cursor.getString(cursor.getColumnIndexOrThrow(STU_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
                    int age = cursor.getInt(cursor.getColumnIndexOrThrow(AGE));
                    String major = cursor.getString(cursor.getColumnIndexOrThrow(MAJOR));

                    sb.append("学号：").append(stuId).append("\n");
                    sb.append("姓名：").append(name).append("\n");
                    sb.append("年龄：").append(age).append("\n");
                    sb.append("专业：").append(major).append("\n\n");
                }
                cursor.close();
            }
        } catch (Exception e) {
            sb.append("访问失败：").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        tvResult.setText(sb);
    }

    // 按学号精确查询
    private void queryStudentById(String stuId) {
        StringBuilder sb = new StringBuilder();
        try {
            Cursor cursor = getContentResolver().query(CONTENT_URI,
                    null, STU_ID + "=?", new String[]{stuId}, null);

            if (cursor == null) {
                sb.append("访问失败：Provider 不存在");
            } else {
                if (cursor.moveToFirst()) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
                    int age = cursor.getInt(cursor.getColumnIndexOrThrow(AGE));
                    String major = cursor.getString(cursor.getColumnIndexOrThrow(MAJOR));

                    sb.append("查询结果：\n");
                    sb.append("学号：").append(stuId).append("\n");
                    sb.append("姓名：").append(name).append("\n");
                    sb.append("年龄：").append(age).append("\n");
                    sb.append("专业：").append(major).append("\n");
                } else {
                    sb.append("未找到该学号学生");
                }
                cursor.close();
            }
        } catch (Exception e) {
            sb.append("访问失败：").append(e.getMessage());
            e.printStackTrace();
        }
        tvResult.setText(sb);
    }
}