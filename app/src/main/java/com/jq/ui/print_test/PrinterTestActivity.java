package com.jq.ui.print_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.jq.R;
import com.jq.printer.JQPrinter;
import com.jq.printer.Printer_define;
import com.jq.ui.DemoApplication;
import com.jq.ui.MainActivity;
import com.jq.ui.expresstest.MainExpressTestActivity;
import com.jq.ui.print_test.jqtek_mode1.JQTEKMode1Activity;
import com.jq.ui.print_test.jqtek_mode2.JQTEKMode2Activity;
import com.jq.ui.print_test.table.TableExampleActivity;

public class PrinterTestActivity extends Activity {
    private Button mButtonTablePrintPre = null;
    private Button mButtonJQTEKMode1 = null;
    private Button mButtonJQTEKMode2 = null;
    private JQPrinter Printer = new JQPrinter(Printer_define.PRINTER_MODEL.JLP352Std);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_test);
        setTitle("自定义打印内容     ");
        mButtonTablePrintPre = (Button)findViewById(R.id.ButtonTableTestPrint);
        mButtonJQTEKMode1 = (Button)findViewById(R.id.ButtonJQTEKMode1);
        mButtonJQTEKMode2 = (Button)findViewById(R.id.ButtonJQTEKMode2);
        DemoApplication app = (DemoApplication)getApplication();
        if (app.printer != null)
        {
            Printer = app.printer;
        }
        else
        {
            Log.e("JQ", "app.printer null");
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.printer_test, menu);
        return true;
    }
    public void ButtonTableTestPrint_click(View view){
        // Cancel discovery because it will slow down the connection

        Intent myIntent = new Intent(PrinterTestActivity.this, TableExampleActivity.class);
        startActivity(myIntent);

    }

    public void ButtonJQTEKMode1_click(View view){
        // Cancel discovery because it will slow down the connection

        Intent myIntent = new Intent(PrinterTestActivity.this, JQTEKMode1Activity.class);
        startActivity(myIntent);

    }

    public void ButtonJQTEKMode2_click(View view){
        // Cancel discovery because it will slow down the connection

        Intent myIntent = new Intent(PrinterTestActivity.this, JQTEKMode2Activity.class);
        startActivity(myIntent);

    }
}
