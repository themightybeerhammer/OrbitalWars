/*
 * Copyright (C) 2016 dns1
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import static java.time.Clock.system;
import java.util.ArrayList;

/**
 *
 * @author dns1
 */
public class CenterMass extends BaseClass {
    
    ArrayList<BaseClass> ALBaseClass;
     CenterMass(
               ArrayList<BaseClass> al
              ){
       this.X = 0;
       this.Y = 0;
       this.M = 0;
       this.RO = 0;
       this.P = new Vector(0,0);
       ALBaseClass = al;
       ALBaseClass.add(this);
     }
     
     
      public void CalcCenterMass(){
        /*Вычисление центра масс*/
       float Xc=0;
       float Yc=0;
       float Mc=0; 
       
       for(int i=0;i<ALBaseClass.size();i++){
           if(ALBaseClass.get(i)!=this)
           {
                Xc =  Xc+(ALBaseClass.get(i).X-Xc)* ((ALBaseClass.get(i).M)/(Mc+ALBaseClass.get(i).M));
                Yc =  Yc+(ALBaseClass.get(i).Y-Yc)* ((ALBaseClass.get(i).M)/(Mc+ALBaseClass.get(i).M));
                Mc=Mc+ALBaseClass.get(i).M;
                
              
           }
           
        }
       
       this.X = Xc;
       this.Y = Yc;
       this.M = Mc;
    }
      
     void draw_in_scr(Graphics g
                     ,float x
                     ,float y
                     ,boolean v_F  
                     ,boolean v_P){
         
         Graphics2D g2 = (Graphics2D)g;
         RenderingHints rh = new RenderingHints(
             RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setRenderingHints(rh);
         g2.setColor(Color.ORANGE);
         g2.draw(new Ellipse2D.Float(x-5, y-5, 10, 10));
         
     }
     
     void move(float Mtplr){
     
     }
        
      
      
      
    
    
}
