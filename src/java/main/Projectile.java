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
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 *
 * @author Vladimir
 */
/** Снаряд для начала будет просто летающая болванка 
 * потом можно будет ввести типы снарядов 
 * и разветвить метод их поведения 
*/
public class Projectile extends BaseClass{
    
    /*Конструкторы класса*/
    Projectile(){
        super();
    }
    Projectile(float x, float y, float m, int ro, float vangle, float vlength, ArrayList<BaseClass> AL){
        super(x, y, m, ro, vangle, vlength, AL);
        DeadSteps = 2;
    }
    
    @Override
    void draw_in_scr(Graphics g
                    ,float x
                    ,float y
                    ,boolean v_F  
                    ,boolean v_P ){ 
        
        
        Graphics2D g2 = (Graphics2D)g;
        Point2D center = new Point2D.Float(x, y);
        g2.setPaint(Color.BLUE);
        g2.fill(new Ellipse2D.Float(x - RO, y - RO, RO * 2, RO * 2));
        
         /*Прорисовка гибели объекта*/
         if(DeadFlag){
         
             
             float radius = RO*2-DeadSteps+2;
             if(radius<=0) radius = 1;
            
             float[] dist = { 0.6f, 1.0f};
             Color[] colors = { Color.YELLOW, new Color(1,0,0,0) };
             RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
             g2.setPaint(p);
             g2.fill(new Ellipse2D.Float(x-radius, y-radius, radius*2, radius*2));
         }
        
    } 
   
}
