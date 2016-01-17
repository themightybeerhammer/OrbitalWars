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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
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
    
     
      void draw_in_scr(Graphics g
                     ,float x
                     ,float y
                     ,boolean v_F  
                     ,boolean v_P ){
         
         Graphics2D g2 = (Graphics2D)g;
          Point2D center = new Point2D.Float(X, Y);
          float radius = RO;
          float rr = (float)(Math.random()*4+20)/100;
          float[] dist = {rr, 0.6f, 1.0f};
          Color[] colors = {Color.RED, Color.YELLOW, new Color(1,0,0,0) };
          RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
          g2.setPaint(p);
          g2.fill(new Ellipse2D.Float(x-RO, y-RO, RO*2, RO*2));
          
         /*RenderingHints rh = new RenderingHints(
             RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setRenderingHints(rh);
         g2.setColor(Color.YELLOW);
         g2.draw(new Ellipse2D.Float(x-RO, y-RO, RO*2, RO*2));*/
         
        // g2.drawOval((int)x-RO, (int)y-RO, RO*2, RO*2);

          /*Направление равнодействующей*/
        
     } 
    
}
