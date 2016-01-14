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


import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import org.hibernate.validator.internal.util.logging.Log;
/**
 *
 * @author Vladimir
 */


public class game extends Applet implements KeyListener{
    
    public ArrayList<BaseClass> ALBaseClass; /*Коллекция всех объектов*/
    public CenterMass CM; /*Центр масс*/      
    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    public void init() {
        addKeyListener(this);
        ALBaseClass = new ArrayList<>();
        CM = new CenterMass(ALBaseClass);
        new BaseClass(50,50,70,5,0,2,ALBaseClass);
        new BaseClass(250,150,70,5,(float) Math.PI,2,ALBaseClass);
        new BaseClass(50,150,70,5, (float) Math.PI*3/2,2,ALBaseClass);
        new BaseClass(250,50,70,5,(float) Math.PI*2,2,ALBaseClass);
       
       
        
        CM.CalcCenterMass();
        
         for(int i=0;i<ALBaseClass.size();i++){
          ALBaseClass.get(i).calc_F_ravn(CM);
        }
      
        
    }
    
    public void CalcCenterMass(Graphics g){
        float Xc = ALBaseClass.get(0).X;
        float Yc = ALBaseClass.get(0).Y;
        float Mc = ALBaseClass.get(0).M;
        for(int i = 1; i < ALBaseClass.size(); i++){
            Xc = Xc + (Xc - ALBaseClass.get(i).X) * (ALBaseClass.get(i).M / Mc);
            Yc = Yc + (Yc - ALBaseClass.get(i).Y) * (ALBaseClass.get(i).M / Mc);
            Mc = Mc + ALBaseClass.get(i).M;
        }  
    }
    
    @Override
    public void paint(Graphics g){      
        for(int i=0;i<ALBaseClass.size();i++){
          ALBaseClass.get(i).draw(g);
        }
        
       
    }

    @Override
    public void keyTyped(KeyEvent ke) {
       
        CM.CalcCenterMass();
        for(int i=0;i<ALBaseClass.size();i++){
          ALBaseClass.get(i).calc_F_ravn(CM);
          ALBaseClass.get(i).move();
        }
        
     //  ALBaseClass.get(1).P.angle=(float) (ALBaseClass.get(1).P.angle+0.05);
     //  System.out.println(ALBaseClass.get(1).P.angle);
       repaint();
        
        
    }

    @Override
    public void keyPressed(KeyEvent ke) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent ke) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
