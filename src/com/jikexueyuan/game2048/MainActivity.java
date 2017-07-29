package com.jikexueyuan.game2048;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static TextView tvScore;
	private static int score = 0;
	
	//�ṩһ���������ģʽ�������ȥ���ø����еĴ�������ķ���
	private static MainActivity mainActivity = null;
	public MainActivity(){
		mainActivity = this;
	}
	public static MainActivity getMainActivity(){
		return mainActivity;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvScore = (TextView) findViewById(R.id.tvScore);
    }

    //��������
	public void clearScore(){
		score = 0;
		showScore();
	}

	//�ڿؼ�����ʾ����
	public static int showScore(){
		tvScore.setText(score+"");
		return score;
	}

	//ʹ�÷�����ӷ���������ʾ����
	public void addScore(int s){
		score+=s;
		showScore();
	}
}
