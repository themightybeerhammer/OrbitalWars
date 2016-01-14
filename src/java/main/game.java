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
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Vladimir
 */

public class game extends Applet implements ActionListener, KeyListener {
    
    private ArrayList<BaseClass> ALBaseClass;
    private DrawPanel Display;
    
    public void init() {
        ALBaseClass = new ArrayList<>();
        
        new BaseClass(100, 90, 5, 5, 20, 20, ALBaseClass);
        new BaseClass(22, 80, 5, 5, 20, 20, ALBaseClass);
        new BaseClass(5, 110, 5, 5, 20, 20, ALBaseClass);       
        new BaseClass(50, 50, 5, 5, 20, 20, ALBaseClass);
        new BaseClass(77, 60, 5, 5, 20, 20, ALBaseClass);
        new BaseClass(120, 100, 5, 5, 20, 20, ALBaseClass);
        
        Display = new DrawPanel();
        add(Display);
        
        Timer oTimer = new Timer();
        TimerTask oTimerTask = new TimerTask(){
            @Override 
            public void run(){  
                for(int i = 0 ;i < ALBaseClass.size(); i++){
                    ALBaseClass.get(i).X++;
                }
                Display.AssignList(ALBaseClass);
            }
        };
        oTimer.schedule(oTimerTask, 0, 100);
    }
    
    public void CalcCenterMass(Graphics g){
        int   Xc = ALBaseClass.get(0).X;
        int   Yc = ALBaseClass.get(0).Y;
        float Mc = ALBaseClass.get(0).M;
        for(int i = 1; i < ALBaseClass.size(); i++){
            Xc = Xc + (Xc - ALBaseClass.get(i).X) * (int)(ALBaseClass.get(i).M / Mc);
            Yc = Yc + (Yc - ALBaseClass.get(i).Y) * (int)(ALBaseClass.get(i).M / Mc);
            Mc = Mc + ALBaseClass.get(i).M;
        }  
    }
    
    @Override
    public void destroy(){
    }
    
    @Override 
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override 
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override 
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override 
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
