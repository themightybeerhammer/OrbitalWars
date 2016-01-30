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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Random;
import static main.BaseClass.ALBaseClass;

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
         
         g2.setColor(Color.RED);
         g2.draw(new Line2D.Double(x-RO/2,y,x+RO/2,y));
         g2.draw(new Line2D.Double(x,y-RO/2,x,y+RO/2));
        
        
         if(DeadFlag){
             Point2D center = new Point2D.Double(x, y);
             
             float radius = RO*2-DeadSteps+2;
             if(radius<=0) radius = 1;
            
             float[] dist = { 0.6f, 1.0f};
             Color[] colors = { Color.YELLOW, new Color(1,0,0,0) };
             RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
             g2.setPaint(p);
             g2.fill(new Ellipse2D.Double(x-radius, y-radius, radius*2, radius*2));
         }
     }
     
     
     
      void Explode(){
          
        if(!DeadFlag){  
        DeadFlag = true;  
        int objCount = 20;   /*Кол-во осколков, на которые распадется планета*/
        int objSize = 1;     /*Размер осколков*/
        double objMass = 1; /*Масса осколков*/
        double nx, ny;       /*Координаты появления осколка*/
        Vector dirbuff;         /*Вектор полета осколка*/
        //Projectile projbuff;    /*Осколок*/
        /*Расстановка осколков внутри периметра планеты*/
        for(double i = 0; i < objCount; i++){
           
            
            Random rr = new Random();
            int r =rr.nextInt(2);
            if(r!=0){
                nx = X + (2 / 2 * Math.cos(i));
                ny = Y + (2 / 2 * Math.sin(i));
                dirbuff = (new Vector().SetAngle(X, Y, nx, ny));
                dirbuff.length=15;
                dirbuff.Plus(P);
                (new Projectile(nx, ny, objMass, objSize, dirbuff.angle,  dirbuff.length, ALBaseClass)).Transparent = 5;
            }
            
            r =rr.nextInt(2);
            if(r!=0){
                nx = X + (3 / 2 * Math.cos(i));
                ny = Y + (3 / 2 * Math.sin(i));
                dirbuff = (new Vector().SetAngle(X, Y, nx, ny));
                dirbuff.length=20;
                dirbuff.Plus(P);
               (new Projectile(nx, ny, objMass, objSize, dirbuff.angle, dirbuff.length, ALBaseClass)).Transparent = 5;
            }
            
            r =rr.nextInt(2);
            if(r!=0){
                nx = X + (4 / 2 * Math.cos(i));
                ny = Y + (4 / 2 * Math.sin(i));
                dirbuff = (new Vector().SetAngle(X, Y, nx, ny));
                dirbuff.length=25;
                dirbuff.Plus(P);
            
            (new Projectile(nx, ny, objMass, objSize, dirbuff.angle, dirbuff.length, ALBaseClass)).Transparent = 5;
            }
        }
       }
    }
    
     
     
    
}
