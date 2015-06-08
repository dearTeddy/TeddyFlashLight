package com.wxh.flashlight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

@SuppressLint({ "NewApi", "ResourceAsColor" })
public class MainActivity extends Activity {
	private Button statusButton = null;
	private Camera camera = null;
	private Parameters parameters = null;
	public static boolean statusFlag = true;
	private int back = 0;// �жϰ�����back
	
	private RelativeLayout mylayout;
	private Resources myColor;
	private int li;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		HideStatusBase();
		setContentView(R.layout.activity_main);

		statusButton = (Button) findViewById(R.id.statusButton);
		statusButton.setOnClickListener(new Mybutton());
		statusButton.performClick();
		
		mylayout = (RelativeLayout) findViewById(R.id.relativelayout);
		setColor(R.color.white);
		//�����ı䱳����ɫ
		//�ı���Ļ����
		li=0;
		SetBright(1.0f);
	}
	
	/**
	 * ��Ļ����¼���ʾ�˵�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		openOptionsMenu();
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu, menu);
		return true; 
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.setcolor:
			selectColor();
			break;
		case R.id.setbright:
			selectBright();
			break;
		case R.id.seteit:
			CloseApp();
			break;
		case R.id.about:
			about();
			break;

		default:
			break;
		}
		
		return false;
	}
	
	private void selectColor() {
		final String[] items = {"��ɫ", "��ɫ", "��ɫ","��ɫ","��ɫ"};
		new AlertDialog.Builder(this)
		.setTitle("ѡ����ɫ")
		.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					setColor(R.color.white);
					break;
				case 1:
					setColor(R.color.red);
					break;
				case 2:
					setColor(R.color.black);
					break;
				case 3:
					setColor(R.color.yellow);
					break;
				case 4:
					setColor(R.color.fs);
					break;

				default:
					setColor(R.color.white);
					break;
				}
			}
		}).show();
	}
	
	private void selectBright(){
		final String[] items = {"100%", "75%", "50%","25%","10%"};
		new AlertDialog.Builder(this)
		.setTitle("ѡ������")
		.setSingleChoiceItems(items, li, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				li = which;
				switch (which) {
				case 0:
					SetBright(1.0F);
					break;
				case 1:
					SetBright(0.75F);
					break;
				case 2:
					SetBright(0.5F);
					break;
				case 3:
					SetBright(0.25F);
					break;
				case 4:
					SetBright(0.1F);
					break;
				default:
					SetBright(1.0F);
					break;
				}
				dialog.cancel();
			}
		}).show();
	}
	
	private void about(){
		new AlertDialog.Builder(this)
		.setMessage("��ӭ��ʹ��Teddy wu�ֵ�Ͳ 1.0��\n�ҵ���վ��:\nhttp://blog.csdn.net/u012911862\n���ߣ���С�� \n��ϵ��ʽ��18822806825\n�ʼ���706324216@qq.com")
		.setIcon(R.drawable.icon)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).show();
	}

	class Mybutton implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (statusFlag) {
				statusButton.setBackgroundResource(R.drawable.switchon);
				camera = Camera.open();
				parameters = camera.getParameters();
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);// ����
				camera.setParameters(parameters);
				statusFlag = false;
			} else {
				statusButton.setBackgroundResource(R.drawable.switchoff);
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);// �ر�
				camera.setParameters(parameters);
				statusFlag = true;
				camera.release();
			}
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back++;

			CloseApp();

		}
		return false;// ���ó�false��backʧЧ ��true��ʾ ��ʧЧ

	}

	public void CloseApp() { // �رճ���
		if (statusFlag) {// ���عر�ʱ
			this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());// �رս���
		} else if (!statusFlag) {// ���ش�ʱ
			camera.release();
			this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());// �رս���
			statusFlag = true;// ���⣬�򿪿��غ��˳������ٴν��벻�򿪿���ֱ���˳�ʱ���������
		}
	}
	
	/**
	 * ȫ������
	 */
	private void HideStatusBase()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
		Window myWindow=this.getWindow();
		myWindow.setFlags(flag,flag);
	}
	
	/**
	 * ������Ļ��ɫ
	 * @param color_M
	 */
	private void setColor(int color){
		myColor = getBaseContext().getResources();
		Drawable color_M = myColor.getDrawable(color);
		mylayout.setBackground(color_M);
	}

	/**
	 * ������Ļ����
	 * @param light
	 */
	private void SetBright(float light)
	{
		WindowManager.LayoutParams lp=getWindow().getAttributes();
		lp.screenBrightness=light;
		getWindow().setAttributes(lp);
	}
}
