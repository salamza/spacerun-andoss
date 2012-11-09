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
import android.graphics.Bitmap;

public class Fighter {
	
	int x;
	int y;
	boolean alive;
	Bitmap pixmap;
	
	public Fighter(){
		x = 160;
		y = 600;
	}
	
	void move(char dir){
		if (dir == 'l'){
			if(x==0){return;}
			x = x - 30;
		}
		else if (dir == 'r'){
			if(x==400){return;}
			x = x + 30;
		}
		else{
			return;
		}
	}
	

}