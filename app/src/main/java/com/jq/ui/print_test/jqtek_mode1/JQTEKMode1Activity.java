package com.jq.ui.print_test.jqtek_mode1;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Printer;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jq.R;
import com.jq.printer.JQPrinter;
import com.jq.printer.Printer_define;
import com.jq.printer.jpl.Barcode;
import com.jq.printer.jpl.Image;
import com.jq.printer.jpl.JPL;
import com.jq.printer.jpl.Page;
import com.jq.printer.jpl.Text;
import com.jq.ui.DemoApplication;

import java.text.SimpleDateFormat;

public class JQTEKMode1Activity extends Activity {
    private JQPrinter Printer = new JQPrinter(Printer_define.PRINTER_MODEL.JLP352Std);

    private Button mButtonJQTEKMode1Print = null;

    private EditText mModelNOEditText = null;
    private TextView mModelNOTextView = null;

    private EditText mSerialNOEditText = null;
    private TextView mSerialNOTextView = null;

    private EditText mVoltageEditText = null;
    private TextView mVoltageTextView = null;

    private EditText mCurrentEditText = null;
    private TextView mCurrentTextView = null;

    private EditText mPhioneNumEditText = null;
    private TextView mPhioneNumTextView = null;

    private EditText mCampanyChEditText = null;
    private TextView mCampanyChTextView = null;

    private EditText mCampanyEnEditText = null;
    private TextView mCampanyEnTextView = null;

    private EditText mAddressEditText = null;
    private TextView mAddressTextView = null;

    private EditText mCode2DEditText = null;
    private TextView mCode2DTextView = null;

    boolean rePrint = false;//是否需要重新打印
    int startIndex;//开始打印的序号
    int amount;//打印的总数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jqtekmode1);

        mButtonJQTEKMode1Print = (Button)findViewById(R.id.ButtonJQTEKMode1Print);

        mModelNOEditText = (EditText)findViewById(R.id.ModelNOEditText);
        mModelNOTextView = (TextView)findViewById(R.id.ModelNOTextView);

        mSerialNOEditText = (EditText)findViewById(R.id.SerialNOEditText);
        mSerialNOTextView = (TextView)findViewById(R.id.SerialNOTextView);

        mVoltageEditText = (EditText)findViewById(R.id.VoltageEditText);
        mVoltageTextView = (TextView)findViewById(R.id.VoltageTextView);

        mCurrentEditText = (EditText)findViewById(R.id.CurrentEditText);
        mCurrentTextView = (TextView)findViewById(R.id.CurrentTextView);

        mPhioneNumEditText = (EditText)findViewById(R.id.PhioneNumEditText);
        mPhioneNumTextView = (TextView)findViewById(R.id.PhioneNumTextView);

        mCampanyChEditText = (EditText)findViewById(R.id.CampanyChEditText);
        mCampanyChTextView = (TextView)findViewById(R.id.CampanyChTextView);

        mCampanyEnEditText = (EditText)findViewById(R.id.CampanyEnEditText);
        mCampanyEnTextView = (TextView)findViewById(R.id.CampanyEnTextView);

        mAddressEditText = (EditText)findViewById(R.id.AddressEditText);
        mAddressTextView = (TextView)findViewById(R.id.AddressTextView);

 //       mCode2DEditText = (EditText)findViewById(R.id.Code2DEditText);
 //       mCode2DTextView = (TextView)findViewById(R.id.Code2DTextView);

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
        getMenuInflater().inflate(R.menu.jqtekmodel1, menu);
        return true;
    }

    private boolean model1_Print(){
        String InputText;
        Paint paint= new Paint();
        Printer.jpl.intoGPL(1000);
        if (!Printer.getJPLsupport())
        {
            Toast.makeText(this, "不支持JPL，请设置正确的打印机型号！", Toast.LENGTH_SHORT).show();
            return false;
        }
        JPL jpl = Printer.jpl;

 //       SimpleDateFormat formatter   =   new   SimpleDateFormat("yyyy年MM月dd日");

        int x=10,y = 0,n=0,height=16;
        if (!jpl.page.start(0, 0, 576, 500, Page.PAGE_ROTATE.x0))//设置打印页面
            return false;

        y=25;//横线1


        jpl.text.drawOut(x , y ,"JQTEK 济强" ,48 ,
                true,false,false,false, Text.TEXT_ENLARGE.x1, Text.TEXT_ENLARGE.x1, JPL.ROTATE.x0);

        InputText=mModelNOEditText.getText().toString();
        y=y+48+16;
        jpl.text.drawOut(x , y ,"MODEL NO.（型号）：" +InputText,height ,
                false,false,false,false, Text.TEXT_ENLARGE.x1, Text.TEXT_ENLARGE.x1, JPL.ROTATE.x0);
        y=y+height+8;
        jpl.barcode.code93(x,y,56, Barcode.BAR_UNIT.x2, Barcode.BAR_ROTATE.ANGLE_0,InputText);

        InputText=mSerialNOEditText.getText().toString();
        y=y+56+16;
        jpl.text.drawOut(x , y ,"SERIAL NO.（序号）：" +InputText,height ,
                false,false,false,false, Text.TEXT_ENLARGE.x1, Text.TEXT_ENLARGE.x1, JPL.ROTATE.x0);
        y=y+height+8;
        jpl.barcode.code93(x,y,56, Barcode.BAR_UNIT.x2, Barcode.BAR_ROTATE.ANGLE_0,InputText);

        InputText=mVoltageEditText.getText().toString();
        String InputText1=mCurrentEditText.getText().toString();
        y=y+56+16;
        jpl.text.drawOut(x , y ,"INPUT（输入）: DC "  + InputText + "=" + InputText1 ,height ,
                false,false,false,false, Text.TEXT_ENLARGE.x1, Text.TEXT_ENLARGE.x1, JPL.ROTATE.x0);//⎓ ⎓

        InputText=mPhioneNumEditText.getText().toString();
        y=y+height+8;
        jpl.text.drawOut(x , y ,"热线：" + InputText,height ,
                false,false,false,false, Text.TEXT_ENLARGE.x1, Text.TEXT_ENLARGE.x1, JPL.ROTATE.x0);

        InputText=mCampanyChEditText.getText().toString();
        y=y+height+8;
        jpl.text.drawOut(x , y , InputText,height ,
                false,false,false,false, Text.TEXT_ENLARGE.x1, Text.TEXT_ENLARGE.x1, JPL.ROTATE.x0);

        InputText=mCampanyEnEditText.getText().toString();
        y=y+height+8;
        jpl.text.drawOut(x , y , InputText,height ,
                false,false,false,false, Text.TEXT_ENLARGE.x1, Text.TEXT_ENLARGE.x1, JPL.ROTATE.x0);

        InputText=mAddressEditText.getText().toString();
        y=y+height+8;
        jpl.text.drawOut(x , y , InputText,height ,
                false,false,false,false, Text.TEXT_ENLARGE.x1, Text.TEXT_ENLARGE.x1, JPL.ROTATE.x0);

        x=290;
        y=25;
        jpl.text.drawOut(x , y , "热敏条形码打印机",32 ,
                true,false,false,false, Text.TEXT_ENLARGE.x1, Text.TEXT_ENLARGE.x1, JPL.ROTATE.x0);
        y=y+40;
        jpl.text.drawOut(x , y , "Thermal Barcode Printer",16 ,
                false,false,false,false, Text.TEXT_ENLARGE.x1, Text.TEXT_ENLARGE.x1, JPL.ROTATE.x0);
        x=320;
        y=120;
        jpl.image.drawOut(x, y, this.getResources(), R.drawable.jqtekwechat2d , Image.IMAGE_ROTATE.x0);


        jpl.page.print();
        jpl.feedNextLabelBegin();
        return Printer.jpl.exitGPL(1000);
    }

    public void ButtonJQTEKMode1Print_click(View view){
        mButtonJQTEKMode1Print.setText("打印");
        mButtonJQTEKMode1Print.setVisibility(Button.INVISIBLE);

        if (!Printer.isOpen)
        {
        this.finish();
        return;
        }
//		if (!getPrinterState())
//		{
//			buttonPrint.setVisibility(Button.VISIBLE);
//			return;
//		}
        if (!rePrint)
        {
        amount = 1;
        startIndex = 1;
        }
        else
        {
        //软件无法判断当前打印的内容是否打印完好，所以需要重新打印当前张。你可以增加一个按钮来决定是打当前张还是打下一张。
        rePrint = false;
        }
        if (model1_Print())
            mButtonJQTEKMode1Print.setVisibility(Button.VISIBLE);

    }
}

