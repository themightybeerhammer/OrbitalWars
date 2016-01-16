/*
 * Copyright (C) 2016 Vladimir
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package main;

import java.util.ArrayList;

/**
 *
 * @author Vladimir
 */
/* Центр солнечной системы
*  
*/

public class Star extends BaseClass {
    
    /*Конструкторы класса*/
    Star(){
        super();
    } 
    Star(float x, float y, float m, int ro, float vangle, float vlength, ArrayList<BaseClass> AL){
        super(x, y, m, ro, vangle, vlength, AL);
    }
    
     void move(float Mtplr)
     {
     
     }
     
    
}
