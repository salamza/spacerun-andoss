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

public class Rock {
	
	float x;
	float y;
	boolean out;
	Bitmap pixmap;
	
	public Rock(){	
	}
	
	void fall(float deltaTime){
		if(y ==  600){
			return;
		}
		else{
			y += 2 * deltaTime;
		}
	}
}