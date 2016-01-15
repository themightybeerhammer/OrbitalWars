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
    
    private ArrayList<BaseClass> ALBaseClass;   /*Коллекция всех объектов*/
    private CenterMass CM;                      /*Центр масс*/ 
    private DrawPanel Display;                  /*Панель для отображения*/
    
    public void init() {
        addKeyListener(this);
        ALBaseClass = new ArrayList<>();
        CM = new CenterMass(ALBaseClass);
        
        /*создаем новый игровой экран*/
        Display = new DrawPanel();
        add(Display);
        
        /*тестовые болванки НАЧАЛО*/
        new BaseClass(100, 190, 5, 5, 20, 20, ALBaseClass);
        new BaseClass(122, 180, 5, 5, 20, 20, ALBaseClass);
        new BaseClass(55, 110, 5, 5, 20, 20, ALBaseClass);       
        new BaseClass(150, 150, 5, 5, 20, 20, ALBaseClass);
        new BaseClass(177, 160, 5, 5, 20, 20, ALBaseClass);
        new BaseClass(120, 100, 5, 5, 20, 20, ALBaseClass);
        /*тестовые болванки КОНЕЦ*/
        
        /*таймер обновления мира*/
        Timer oTimer = new Timer();
        TimerTask oTimerTask = new TimerTask(){
            /*в ране описываются периодические действия*/
            @Override 
            public void run(){
                CM.CalcCenterMass();                            /*пересчет центра масс*/
                Display.AssignList(ALBaseClass);                /*передача игровому экрану списка объектов для отрисовки*/  
                for(int i = 0 ;i < ALBaseClass.size(); i++){
                    ALBaseClass.get(i).calc_F_ravn(CM);         /*пересчет импульса объекта*/
                    ALBaseClass.get(i).move();                  /*движение объекта*/
                }
            }
        };
        oTimer.schedule(oTimerTask, 0, 1000);
    }
    
    /*расчет центра массы системы*/
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
    public void destroy(){
    }
    
    @Override 
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override 
    public void keyTyped(KeyEvent e) {
                CM.CalcCenterMass();                            /*пересчет центра масс*/
                Display.AssignList(ALBaseClass);                /*передача игровому экрану списка объектов для отрисовки*/  
                for(int i = 0 ;i < ALBaseClass.size(); i++){
                    ALBaseClass.get(i).calc_F_ravn(CM);         /*пересчет импульса объекта*/
                    ALBaseClass.get(i).move();                  /*движение объекта*/
                }
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
