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

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.ArrayList;
import main.Vector;
/**
 *
 * @author Vladimir
 */
public class BaseClass {
    int X,Y;   /*Координаты объекта*/
    float M;   /*Масса объекта */
    int RO;    /*Радиус сферы(объекта)*/
    Vector V;  /*Вектор импульса*/
    
     BaseClass(){
       this.X = 0;
       this.Y = 0;
       this.M = 1;
       this.RO = 1;
       V = new Vector(1,1);
     }
     
     
     BaseClass(
               int x
              ,int y
              ,float m
              ,int ro
              ,float vangle
              ,float vlength
              ,ArrayList<BaseClass> AL
              ){
       this.X = x;
       this.Y = y;
       this.M = m;
       this.RO = ro;
       this.V = new Vector(vangle,vlength);
       AL.add(this);
     }
     
     void draw(Graphics g){
         g.drawOval(X, Y, RO, RO);
     }
     
     
    
    
}
