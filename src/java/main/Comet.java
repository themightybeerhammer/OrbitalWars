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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.sqrt;
import java.util.ArrayList;

/**
 *
 * @author Vladimir
 */
public class Comet extends BaseClass{
    ArrayList<Point2D> tail; /*Хвост кометы*/
    
    Comet(){
        super();
        tail = new ArrayList<>();
        DeadSteps = 4;
        HealthMax = 10;
        dw_health = true;
    }
    Comet(double x, double y, double m, int ro, double vangle, double vlength, ArrayList<BaseClass> AL){
        super(x, y, m, ro, vangle, vlength, AL);
        tail = new ArrayList<>();
        DeadSteps = ro;
    }
    
    @Override
    void draw_in_scr(Graphics g, double x, double y, boolean v_F, boolean v_P ){
        RenderingHints rh = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        
        Graphics2D g2 = (Graphics2D)g;
        
        g2.setRenderingHints(rh);
        g2.setColor(Color.GRAY);
        if(DeadFlag == false){
            double _x = (x - RO);
            double _y = (y - RO);
            g2.fill(new Ellipse2D.Double(_x, _y, RO * 2, RO * 2));
        }
        
        float r = 20;
        /*Направление равнодействующей*/
        if((F.length != 0) & (v_F)){
            g2.setColor(Color.BLUE);
            g2.drawLine((int)x, (int)y, (int)x + (int)(Math.cos(F.angle) * r), (int)y + (int)(Math.sin(F.angle) * r));
        }
         
        /*Направление Импульса*/
        if((P.length != 0) & (v_P)){
            g2.setColor(Color.GREEN);
            g2.drawLine((int)x, (int)y, (int)x + (int)(Math.cos(P.angle) * r), (int)y + (int)(Math.sin(P.angle) * r));
        }
        
        /*Прорисовка гибели объекта*/
        if(DeadFlag){
            Point2D center = new Point2D.Double(x, y); 
            float radius = RO * (DeadSteps / 20) + 2;
            if(radius <= 0) radius = 1;
           
            float[] dist = { 0.6f, 1.0f};
            Color[] colors = { Color.YELLOW, new Color(1, 0, 0, 0) };
            RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
            g2.setPaint(p);
            g2.fill(new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2));
        }
     }
    
    /*Прорисовка хвоста кометы*/
    void draw_tail(Graphics g, double x, double y){
        Graphics2D g2 = (Graphics2D)g;
        RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setRenderingHints(rh);
        GeneralPath path = new GeneralPath();
        int nc;   /*Вычисление цвета на каждом шагу*/
        int ns;    /*Вычисление радиуса хвоста на каждом шагу*/
        if(tail.size() > 0)
        {
            for(int i = tail.size() - 1; i >= 0 ; i--){
                nc = (int)(255 * ((float)i / (float)(tail.size() - 1)));
                g2.setColor(new Color(nc, nc, max(0 , min(255, (int)(nc * 10)))));
                ns = RO * (tail.size() - i) / 2;
                g2.fillOval((int)(tail.get(i).getX() + x), (int)(tail.get(i).getY() + y), ns, ns);
            }
        }
     }

    @Override
    void move(double Mtplr){
        double xd;
        double yd;
        xd = (Math.cos(P.angle)*P.length / M);
        yd = (Math.sin(P.angle)*P.length / M);
        xd = xd / Mtplr;
        yd = yd / Mtplr;
        X = X + xd;
        Y = Y + yd;
        if(DeadFlag){
            DeadSteps--;  
            if(DeadSteps < 0)
                DeadSteps = 0;
        }
        Transparent = max(--Transparent, 0);
        tail.add(new Point2D.Double(X, Y));
        if(tail.size() > sqrt(pow(xd, 2) + pow(yd, 2)) * 2)
            tail.remove(0);
    }
    
    @Override
    void draw(Graphics g, Point2D p_display, boolean v_F, boolean v_P){
         if(dw_health)draw_health(g,(X+p_display.getX()),(Y+p_display.getY())); 
         /*Прорисовка хвоста*/
         draw_tail(g, p_display.getX(), p_display.getY());
         /*Отрисовка объекта*/
         draw_in_scr(g, X + p_display.getX(), Y + p_display.getY(), v_F, v_P);
     }
    
    @Override
    void Die(){
        DeadFlag = true;
        Disruption();
    }
    
}
