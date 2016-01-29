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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author dns1
 */
public class Grenede extends BaseClass {
    
    Grenede(){
        super();
    }
    Grenede(  double x
            , double y
            , double m
            , int ro
            , double vangle
            , double vlength
            , ArrayList<BaseClass> AL){
        
        super(x, y, m, ro, vangle, vlength, AL);
        DeadSteps = 2;
    }
    
    
    
     
     void draw_in_scr(Graphics g
                     ,double x
                     ,double y
                     ,boolean v_F  
                     ,boolean v_P ){
         
         Graphics2D g2 = (Graphics2D)g;
         RenderingHints rh = new RenderingHints(
             RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setRenderingHints(rh);
         g2.setColor(Color.DARK_GRAY);
         g2.fill(new Ellipse2D.Double(x-RO, y-RO, RO*2, RO*2));
        // g2.drawOval((int)x-RO, (int)y-RO, RO*2, RO*2);

          /*Направление равнодействующей*/
         double r = 20;
        
         if((F.length!=0)&(v_F))
            {
                g2.setColor(Color.BLUE);
                g2.drawLine((int)x,(int) y, (int)x+(int)(Math.cos(F.angle)*r) ,(int) y+(int)(Math.sin(F.angle)*r));
            }
         
         /*Направление Импульса*/
          r = 20;
         if((P.length!=0)&(v_P))
            {
                g2.setColor(Color.GREEN);
                g2.drawLine((int)x, (int)y, (int)x+(int)(Math.cos(P.angle)*r) , (int)y+(int)(Math.sin(P.angle)*r));
            }
         
      
     }
     
     
     
    
}
