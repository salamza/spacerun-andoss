package com.samsals.space.runaway;
/*This file is part of SpaceRunaway Application

SpaceRunaway Application is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SpaceRunaway Application is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SpaceRunaway Application.  If not, see <http://www.gnu.org/licenses/>.
*/
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

public class SpaceRunaway extends Activity implements OnTouchListener{
	
	StringBuilder builder = new StringBuilder();
	FastRenderView renderView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		renderView = new FastRenderView(this);
		renderView.setOnTouchListener(this);
		setContentView(renderView);
    }
    
	protected void onResume() {
		super.onResume();
		renderView.resume();
	}

	protected void onPause() {
		super.onPause();
		renderView.pause();
	}
	
	enum Difficulty{
		 easy, 
		 normal,
		 hard,
		 veryHard 
		 }
	
	Fighter myfighter = new Fighter();
	Rock myrock = new Rock();
	int rocksSize = 105;
	ArrayList<Rock> rocks = new ArrayList<Rock>(rocksSize);
	Screen mainMenu = new Screen();
	Screen highScores = new Screen();
	Screen playGround = new Screen();
	Bitmap back;
	
	void waveInitalization(Difficulty lvl){
		Random rand = new Random();
		int[] xPlaces  = {0,80,160,240,320,400};//i deleted 320
		int[] yPlaces = new int[24];
		int sum=-2400;
		for (int k=0 ;k<yPlaces.length ; k+=2){
			
			yPlaces[k] = sum;
			yPlaces[k+1] = sum;
			
			sum+=200;
			//Log.d("yplaces" , "yplaces["+k+"] = " +yPlaces[k] +" .");
		}
		Rock Rocky;
		/*for(int i=0;i<70;i++){
			Log.d("rocksList", " the rocks["+i+"] = " +rocks.get(i).x+", " +rocks.get(i).y);
		}*/
		
		for(int i =0 ; i<rocksSize;i++){
			Rocky = new Rock();
			Rocky.pixmap = myrock.pixmap;
			Rocky.x = xPlaces[rand.nextInt(6)];
        	Rocky.y = yPlaces[rand.nextInt(24)];
        	rocks.add(Rocky);
		}
		
		// it gives infinite loop and i don't know why.
		
		/*if (lvl==Difficulty.easy){
			for (int i =0 ; i<rocksSize;i++){
				Rocky = new Rock();
				Rocky.pixmap = myrock.pixmap;
				Rocky.x = xPlaces[rand.nextInt(6)];
	        	Rocky.y = yPlaces[rand.nextInt(24)];
	        	
	        	
				
				rocks.add(Rocky);
	        	Log.d("rocksList", " the rocks["+i+"] = " +rocks.get(i).x+", " +rocks.get(i).y+".");
			}
			return;
		}
		else if (lvl==Difficulty.normal){
			
		}
		else if (lvl==Difficulty.hard){
			
		}
		else if (lvl==Difficulty.veryHard){
			
		}*/
		
	}
	
	void updateRocks(Canvas canvas, float deltaTime){
			//Log.d("rocksList", " the rocks["+i+"] = " +rocks.get(i).x+", " +rocks.get(i).y);
			for (int i=0 ;i<rocksSize;i++){
						rocks.get(i).fall(deltaTime);
						if(rocks.get(i).y >= 0){
							rocks.get(i).out=false;
						}
					}
					
		}
	
	void detectCollision(){
		
	}
	
    class FastRenderView extends SurfaceView implements Runnable {
		Thread renderThread = null;
		SurfaceHolder holder;
		volatile boolean running = false;
		
		InputStream inputStream;

		public FastRenderView(Context context) {
			super(context);
			holder = getHolder();
			try {
				AssetManager assetManager = context.getAssets();
				
				inputStream = assetManager.open("fighter.png");
				myfighter.pixmap = BitmapFactory.decodeStream(inputStream);
				Log.d("Bitmap", "fighter.png format: " + myfighter.pixmap.getConfig());
				
				inputStream = assetManager.open("Rock.png");
				myrock.pixmap = BitmapFactory.decodeStream(inputStream);
				Log.d("Bitmap", "Rock.png format: " + myrock.pixmap.getConfig());
				
				inputStream = assetManager.open("mainmenu.png");
				mainMenu.pixmap = BitmapFactory.decodeStream(inputStream);
				Log.d("Bitmap", "mainmenu.png format: " + mainMenu.pixmap.getConfig());
			
				inputStream = assetManager.open("playground.png");
			     playGround.pixmap = BitmapFactory.decodeStream(inputStream);
				Log.d("Bitmap", "playground.png format: " + playGround.pixmap.getConfig());
				
				inputStream = assetManager.open("scores.png");
			    highScores.pixmap = BitmapFactory.decodeStream(inputStream);
				Log.d("Bitmap", "scores.png format: " + highScores.pixmap.getConfig());
				
				inputStream = assetManager.open("back.png");
			    back = BitmapFactory.decodeStream(inputStream);
				Log.d("Bitmap", "back.png format: " + back.getConfig());
				
				inputStream.close();
			} catch (IOException e) {
				
				Log.d("Bitmap", " Couldn't read");
				//baaad!
			} finally {
				// close our input streams here.
			}
		}

		public void resume() {
			running = true;
			renderThread = new Thread(this);
			renderThread.start();
		}

		public void run() {
	        long startTime = System.nanoTime();
			mainMenu.live = false;
			playGround.live = true;
			highScores.live = false;
			float deltaTime;
			waveInitalization(Difficulty.easy);
			//int canvasWidth = canvas.getWidth();
			//int canvasHeight = canvas.getHeight();

			while (running) {
				if (!holder.getSurface().isValid())
					continue;
				
				deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
				Canvas canvas = holder.lockCanvas();
				
				if (mainMenu.live){
					canvas.drawBitmap(mainMenu.pixmap,mainMenu.x ,mainMenu.y , null);
				}
				else if (highScores.live){
					canvas.drawBitmap(highScores.pixmap, highScores.x, highScores.y,null);
					canvas.drawBitmap(back, 0, 650,null);
				}
				else if(playGround.live){
					
					canvas.drawBitmap(playGround.pixmap, playGround.x, playGround.y , null);
					//canvas.drawBitmap(myfighter.pixmap, myfighter.x, myfighter.y, null);
					for(int i =0 ; i<rocksSize;i++){
						if (!rocks.get(i).out){
							//Log.d("rocksList", " the rocks["+i+"] = " +rocks.get(i).x+", " +rocks.get(i).y);
							canvas.drawBitmap(rocks.get(i).pixmap, rocks.get(i).x,rocks.get(i).y,null);
						}
					}
					canvas.drawBitmap(myfighter.pixmap, myfighter.x, myfighter.y, null);
					updateRocks(canvas,deltaTime);
					
				}
				holder.unlockCanvasAndPost(canvas);
			}
		}

		public void pause() {
			running = false;
			while (true) {
				try {
					renderThread.join();
				} catch (InterruptedException e) {
					// retry
				}
			}
		}
	}
 
	public boolean onTouch(View d1, MotionEvent event) {
		
		float x,y;
		switch (event.getAction()){
		case MotionEvent.ACTION_DOWN:
			x = event.getX();
			y = event.getY();
			
			if(mainMenu.live){
				//pushed Start ( 20,200) -> (460 ,400 )
				if(x>20 && x<460 && y>200 && y<400){
					mainMenu.live=false;
					playGround.live=true;
				}
				//pushed highscores (20,400)->(460,600)
				else if(x>20 && x<460 && y>400 && y<600){
					mainMenu.live = false;
					highScores.live = true;
				}
				//pushed exit (0,700 ) -> (100 , 800)
				else if(x>0 && x<100 && y>700 && y<800){
					mainMenu.live = false;
					highScores.live = true;
				}	
			}
			
			else if(playGround.live){
				
				//pushed menu (151 ,700 ) -> (319 ,800)
				if(x>151 && x<319 && y>700 && y<800){
					playGround.live = false;
					mainMenu.live=true;
				}
				// pushed right  (320,700) -> (480 , 800)
				else if(x>320 && x<480 && y>700 && y<800){
					myfighter.move('r');
				}  
				//pushed left     (0,700) - > (150 , 800)
				else if(x>0 && x<150 && y>700 && y<800){
					myfighter.move('l');
				}
					
			}
			else if(highScores.live){
				
				if (x>0 && x<480 && y> 300 && y<800){
					highScores.live=false;
					mainMenu.live=true;
				}
			}
			
			builder.append("down, ");
			break;
		case MotionEvent.ACTION_MOVE:
			builder.append("move, ");
			break;
		case MotionEvent.ACTION_CANCEL:
			builder.append("cancel, ");
			break;
		case MotionEvent.ACTION_UP:
			builder.append("up, ");
			break;
		}
		builder.append(event.getX());
		builder.append(", ");
		builder.append(event.getY());
		String text = builder.toString();
		Log.d("TouchTest", text);
		return true;
	}
}