package com.jq.printer.esc;

import android.util.Log;

import com.jq.printer.PrinterParam;
import com.jq.printer.Printer_define.ALIGN;
import com.jq.printer.Printer_define.FONT_ID;
import com.jq.printer.Printer_define.PRINTER_MODEL;

public class Text extends BaseESC
{	
	/*
	 * 构造函数
	 */
	public Text(PrinterParam param) {
		super(param);
	}
	/*
	 * 设置文字的放大方式
	 */
	public boolean setFontEnlarge(ESC.TEXT_ENLARGE enlarge) {
		_cmd[0] = 0x1D;
		_cmd[1] = 0x21;
		_cmd[2] = (byte) enlarge.value();
		return _port.write(_cmd, 0, 3);
	}

	/*
	 * 设置文本字体ID
	 */
	public boolean setFontID(FONT_ID id) {
		switch (id) 
		{
			case ASCII_16x32:
			case ASCII_24x48:
			case ASCII_32x64:
			case GBK_32x32:
			case GB2312_48x48:
				if (_model == PRINTER_MODEL.VMP02
					|| _model == PRINTER_MODEL.ULT113x) {
					Log.e("JQ", "not support FONT_ID:" + id);
					return true;
				}
				break;
			default:
				break;
		}
		_cmd[0] = 0x1B;
		_cmd[1] = 0x4D;
		_cmd[2] = (byte) id.value();
		return _port.write(_cmd, 0, 3);
	}
	/*
	 * 设置文本字体高度
	 */
	public boolean setFontHeight(ESC.FONT_HEIGHT height)
	{
		switch(height)
		{
			case x24:
				setFontID(FONT_ID.ASCII_12x24);
				setFontID(FONT_ID.GBK_24x24);
				break;
			case x16:
				setFontID(FONT_ID.ASCII_8x16);
				setFontID(FONT_ID.GBK_16x16);
				break;
			case x32:
				setFontID(FONT_ID.ASCII_16x32);
				setFontID(FONT_ID.GBK_32x32);
				break;
			case x48:
				setFontID(FONT_ID.ASCII_24x48);
				setFontID(FONT_ID.GB2312_48x48);
				break;
			case x64:
				setFontID(FONT_ID.ASCII_32x64);
				break;
			default:
				return false;
		}	
		return true;
	}
	/*
	 * 设置文本加粗方式
	 */
	public boolean setBold(boolean bold) {
		_cmd[0] = 0x1B;
		_cmd[1] = 0x45;
		_cmd[2] = (byte) (bold ? 1 : 0);
		return _port.write(_cmd, 0, 3);
	}	
	/*
	 * 设置文本下划线方式
	 */
	public boolean setUnderline(boolean underline) {
		_cmd[0] = 0x1B;
		_cmd[1] = 0x2D;
		_cmd[2] = (byte) (underline ? 1 : 0);
		return _port.write(_cmd, 0, 3);
	}
	/*
	 * 打印输出文本
	 */
	public boolean drawOut(String text) {
		return _port.write(text);
	}
	/*
	 * 打印输出文本
	 * x: x坐标
	 * y：y坐标
	 * 注意：x，y坐标是有限制的，请参考setXY函数源代码
	 */
	public boolean drawOut(int x,int y,String text) {
		if (!setXY(x,y))
			return false;
		return _port.write(text);
	}	
	/*
	 * 打印输出文本
	 */
	public boolean drawOut(int x, int y, ESC.TEXT_ENLARGE enlarge,String text) {
		if (!setXY(x, y))
			return false;
		if (!setFontEnlarge(enlarge))
			return false;
		return _port.write(text);
	}	
	/*
	 * 打印输出文本
	 */
	public boolean drawOut(ESC.FONT_HEIGHT height, String text) {
		if (!setFontHeight(height))
			return false;
		return _port.write(text);
	}
	
	/*
	 * 打印输出文本
	 */
	public boolean drawOut(int x, int y, ESC.FONT_HEIGHT height, ESC.TEXT_ENLARGE enlarge, String text) {
		if (!setXY(x, y))
			return false;
		if (!setFontHeight(height))
			return false;
		if (!setFontEnlarge(enlarge))
			return false;
		return _port.write(text);
	}	
	/*
	 * 打印输出文本
	 */
	public boolean drawOut(int x, int y, ESC.FONT_HEIGHT height, boolean bold, ESC.TEXT_ENLARGE enlarge, String text) {
		if (!setXY(x, y))
			return false;
		if (!setFontHeight(height))
			return false;
		if (!setFontEnlarge(enlarge))
			return false;
		if (!setBold(bold))
			return false;
		return _port.write(text);
	}
	/*
	 * 打印输出文本
	 */
	public boolean drawOut(int x, int y, ESC.FONT_HEIGHT height, boolean bold, String text) {
		if (!setXY(x, y))
			return false;
		if (!setFontHeight(height))
			return false;
		if (!setBold(bold))
			return false;
		return _port.write(text);
	}
	/*
	 * 打印输出文本
	 */
	public boolean drawOut(ALIGN align, ESC.FONT_HEIGHT height, boolean bold, ESC.TEXT_ENLARGE enlarge, String text) {
		if (!setAlign(align))
			return false;
		if (!setFontHeight(height))
			return false;
		if (!setBold(bold))
			return false;
		if (!setFontEnlarge(enlarge))
			return false;
		return _port.write(text);
	}
	/*
	 * 打印输出文本
	 */
	public boolean drawOut(ALIGN align, boolean bold,String text) {
		if (!setAlign(align))
			return false;
		if (!setBold(bold))
			return false;
		return _port.write(text);
	}
	
	/// <summary>
    /// 打印输出文本
    /// 1)文件立即输出
    /// 2)会恢复字体效果到缺省值
	/// 注意：使用此方法时，请尽量保证内容不要超过一行。
	///      因为当y不等于0，如果打印内容超过一行，下一行出现位置会到page的结束位置。
    /// </summary>
    public boolean printOut(int x, int y, ESC.FONT_HEIGHT height,boolean bold, ESC.TEXT_ENLARGE enlarge, String text)
    {
        if (!setXY(x, y))   return false;
        if (bold) if (!setBold(true)) return false;
        if (!setFontHeight(height)) return false;
        if (!setFontEnlarge(enlarge)) return false;
        if (!_port.write(text)) return false;            
        enter();
        //恢复字体效果
        if (bold) if (!setBold(false)) return false;
        if (!setFontHeight(ESC.FONT_HEIGHT.x24)) return false;
        if (!setFontEnlarge(ESC.TEXT_ENLARGE.NORMAL)) return false;

        return true;
    }
    
  /// <summary>
    /// 打印输出文本
    /// 1)文件立即输出
    /// 2)会恢复字体效果到缺省值
	/// 注意：使用此方法时，请尽量保证内容不要超过一行。
	///      因为当y不等于0，如果打印内容超过一行，下一行出现位置会到page的结束位置。
    /// </summary>
    public boolean printOut(int x, int y, ESC.FONT_HEIGHT height,boolean bold,boolean underline, ESC.TEXT_ENLARGE enlarge, String text)
    {
        if (!setXY(x, y))   return false;
        if (bold) if (!setBold(true)) return false;
        if (underline) setUnderline(true);
        if (!setFontHeight(height)) return false;
        if (!setFontEnlarge(enlarge)) return false;
        if (!_port.write(text)) return false;            
        enter();
        //恢复字体效果
        if (bold) if (!setBold(false)) return false;
        if (underline) setUnderline(false);
        if (!setFontHeight(ESC.FONT_HEIGHT.x24)) return false;
        if (!setFontEnlarge(ESC.TEXT_ENLARGE.NORMAL)) return false;

        return true;
    }

    
    public boolean printOut(ALIGN align, ESC.FONT_HEIGHT height, boolean bold, ESC.TEXT_ENLARGE enlarge, String text)
    {
        if (!setAlign(align))   return false;
        if (!setFontHeight(height)) return false;
        if (bold) if (!setBold(true)) return false;
        if (!setFontEnlarge(enlarge))   return false;
        if (!_port.write(text))  return false;
        enter();
        //恢复        
        if (!setAlign(ALIGN.LEFT)) return false;
        if (!setFontHeight(ESC.FONT_HEIGHT.x24)) return false;
        if (bold) if (!setBold(false)) return false;            
        if (!setFontEnlarge(ESC.TEXT_ENLARGE.NORMAL)) return false;
        return true;
    }
    
    public boolean printOut(String text)
    {
        if (!_port.write(text))
            return false;
        return enter();            
    }

	/*
	 * 水墨屏开机
	 */
	public boolean p990DisplayOpen() {
		_cmd[0] = 0x10;//开机
		_cmd[1] = 0x45;
		_cmd[2] = 0x4F;
		return _port.write(_cmd, 0, 3);
	}

	/*
	 * 水墨屏关机
	 */
	public boolean p990DisplayClose() {
		_cmd[0] = 0x10;//关机
		_cmd[1] = 0x45;
		_cmd[2] = 0x6F;
		return _port.write(_cmd, 0, 3);
	}

	/*
	 * 水墨屏更新显示
	 */
	public boolean p990DisplayUpdate() {
		_cmd[0] = 0x10;//更新
		_cmd[1] = 0x45;
		_cmd[2] = 0x55;
		return _port.write(_cmd, 0, 3);
	}
	/*
	 * 水墨屏显示文本
	 */
	public boolean p990Display(int x, int y,int height,int remote,String text) {

		//if(!p990DisplayOpen()) return false;

		//设置文本
		_cmd[0] = 0x10;
		_cmd[1] = 0x45;
		_cmd[2] = 0x54;
		_cmd[3] = (byte) x;
		_cmd[4] = (byte) (x >> 8);
		_cmd[5] = (byte) y;
		_cmd[6] = (byte) (y >> 8);
		_cmd[7] = (byte) height;
		_cmd[8] = (byte) (height >> 8);
		_cmd[9] = (byte) remote;
		_cmd[10] = 0x00;

		if(!_port.write(_cmd, 0, 11))
			return false;

		return _port.write(text);

//		if(!p990DisplayUpdate()) return false;
//		if(!p990DisplayClose()) return false;

	}
}
