package com.jikexueyuan.game2048;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static TextView tvScore;
	private static int score = 0;
	
	//提供一个单例设计模式给别的类去调用该类中的处理分数的方法
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

    //分数清零
	public void clearScore(){
		score = 0;
		showScore();
	}

	//在控件上显示分数
	public static int showScore(){
		tvScore.setText(score+"");
		return score;
	}

	//使用方法添加分数，并显示出来
	public void addScore(int s){
		score+=s;
		showScore();
	}
}
