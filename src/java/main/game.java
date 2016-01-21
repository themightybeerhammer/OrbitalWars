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
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Object;

/**
 *
 * @author Vladimir
 */

public class game extends Applet implements KeyListener, MouseListener, MouseMotionListener {
    
    private ArrayList<BaseClass> ALBaseClass;   /*Коллекция всех объектов*/
    private CenterMass CM;                      /*Центр масс*/ 
    protected DrawPanel Display;                /*Панель для отображения*/
    public Planet player;
    public float DisplayX, DisplayY;            /*Координаты центра экрана*/
    public Point p_Delta, p_display;
    public boolean RMBPressed = false;          /*Нажата правая кнопка мыши*/
    boolean overChild;                          /*Буфер для перемещения по контейнерам*/
    float mx, my;                               /*Последние известные координаты мыши*/
    /*Параметры для дебугагинга начало*/
    public float Mltplr = 5f;                   /*Множитель замедления*/      
    public boolean v_F = false;                 /*Рисовать вектор равнодействующей*/
    public boolean v_P = false;                 /*Рисовать вектор импульса*/ 
    /*Параметры для дебугагинга конец*/
    
    @Override
    public void init() {
        ALBaseClass = new ArrayList<>();
        CM = new CenterMass(ALBaseClass);
        setSize(800, 600);
        p_Delta = new Point(0,0);
        p_display = new Point(0,0);
       
        /*тестовые болванки НАЧАЛО*/
        
        /*Одна звезда и 3 планеты*/
        player = new Planet(200, 30, 10, 10, 0, 78, ALBaseClass, true, true);
        new Planet(100, 200, 10, 10, (float)Math.PI * 2 / 4, 100, ALBaseClass, false, false);
        new Planet(200, 70, 10, 10, (float)Math.PI, 90, ALBaseClass, false, false);    
        new Star(200, 200, 10000, 40, 0, 0, ALBaseClass);
        
        /*Система сиськи*/
      /*  player = new Planet(375, 375, 10, 10, (float)Math.PI*200/207,80, ALBaseClass, true, true);
        player.dw_orbit = true;
        new Star(250, 250, 8000, 40, 0, 0, ALBaseClass);
        new Star(500, 500, 8000, 40, 0, 0, ALBaseClass);
        */
      
      
         /*создаем новый игровой экран*/
        Display = new DrawPanel();
        add(Display);
        //Display.addMouseMotionListener(new MotionSensor(this));
        Display.addMouseMotionListener(this);
        addKeyListener(this);
        Display.addMouseListener(this);
        Display.addMouseMotionListener(this);
        
        for(int i = 0 ;i < ALBaseClass.size(); i++){
            if(ALBaseClass.get(i).getClass().getName()=="main.Star"){
              ALBaseClass.get(i).dw_health = true;   
            }
           
            if(ALBaseClass.get(i).getClass().getName()=="main.Planet"){
              ALBaseClass.get(i).dw_orbit = true;
              ALBaseClass.get(i).dw_health = true;
            }
          
          
            
            ALBaseClass.get(i).calc_orbit();  /*Расчет орбит*/
            
        }
        /*тестовые болванки КОНЕЦ*/
        
        /*таймер обновления мира*/
        Timer oTimer = new Timer();
        TimerTask oTimerTask = new TimerTask(){
            /*в ране описываются периодические действия*/
            @Override 
            public void run(){
                player.Aim(mx - p_display.x, my - p_display.y);     /*постоянно нацеливаем орудие на последние координаты мыши*/
                CM.CalcCenterMass();                                /*пересчет центра масс*/
                Display.AssignList(ALBaseClass,p_display,v_F,v_P);  /*передача игровому экрану списка объектов для отрисовки*/ 
                for(int i = 0; i < ALBaseClass.size(); i++){
                    if(ALBaseClass.get(i) != null){
                        ALBaseClass.get(i).calc_F_ravn(Mltplr);     /*пересчет импульса объекта*/
                        ALBaseClass.get(i).move(Mltplr);            /*движение объекта*/
                        if(ALBaseClass.get(i).getClass().getName() == "main.Planet"){
                            ((Planet)ALBaseClass.get(i)).Charge();
                        }
                    }
                }
                
                /*Обработка столкновения*/
                for(int i = 0 ;i < ALBaseClass.size(); i++){
                    for(int j = 0 ;j < ALBaseClass.size(); j++){
                        if((i!=j)&&(ALBaseClass.size()>i)
                                 &&(ALBaseClass.size()>j)
                                 &&(ALBaseClass.get(i)!=null)
                                 &&(ALBaseClass.get(j)!=null)
                                 &&(ALBaseClass.get(j).getClass().getName()!="main.CenterMass")
                                 &&(ALBaseClass.get(i).getClass().getName()!="main.CenterMass")
                                 &&(ALBaseClass.get(i).DeadFlag==false)
                                 &&(ALBaseClass.get(j).DeadFlag==false)
                                &&(ALBaseClass.get(i).Transparent==0)
                                &&(ALBaseClass.get(j).Transparent==0)
                                ){
                            float rr = (float)(Math.sqrt(Math.pow((ALBaseClass.get(i).X-ALBaseClass.get(j).X),2)+ Math.pow((ALBaseClass.get(i).Y-ALBaseClass.get(j).Y),2)));
                            
                            if(rr<(ALBaseClass.get(i).RO+ALBaseClass.get(j).RO)){
                                
                                ALBaseClass.get(i).HealthCur=(int)(ALBaseClass.get(i).HealthCur -  ALBaseClass.get(j).M);
                                ALBaseClass.get(j).HealthCur=(int)(ALBaseClass.get(j).HealthCur -  ALBaseClass.get(i).M);
                                
                                if( ALBaseClass.get(i).HealthCur<=0) ALBaseClass.get(i).Die();//.DeadFlag=true;
                                if( ALBaseClass.get(j).HealthCur<=0) ALBaseClass.get(j).Die();//.DeadFlag=true;
                                /*if(ALBaseClass.get(i).getClass().getName()=="main.Projectile")
                                {ALBaseClass.get(i).DeadFlag=true;}
                                if(ALBaseClass.get(j).getClass().getName()=="main.Projectile")
                                {ALBaseClass.get(j).DeadFlag=true;}*/
                            
                            }
                        }
                    }  
                }
                
               /*Удаление погибших объектов*/ 
               int j=0; 
               do{
                  if((ALBaseClass.get(j).DeadFlag)&&(ALBaseClass.get(j).DeadSteps<=0)){
                    ALBaseClass.remove(j);
                  }else{
                   j++;
                  }
                   
               }while(j<ALBaseClass.size());
            }
        };
        oTimer.schedule(oTimerTask, 0, 50);
        setFocusable(true);
        requestFocusInWindow();
    }
    
    @Override
    public void destroy(){
    }
    
    @Override 
    public void keyTyped(KeyEvent e) {
    }
    
    @Override 
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case 32:    player.Shoot();    /*тест стрельбы по клавише ПРОБЕЛ*/
                        break;
            /*клавиши 1-9 для переключения оружия*/
            case 49:    player.GunType = 1;
                        break;
            case 50:    player.GunType = 2;
                        break;
            default: System.out.println(e.getKeyCode());
        }
    }
    
    @Override 
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch(e.getButton()){
            case 1: player.Shoot();
                    break;
            case 3: RMBPressed = true;
                    p_Delta  = e.getPoint();
                    break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
       if(RMBPressed){
         RMBPressed = false;
       }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
            if(RMBPressed){
              p_display.x = p_display.x + (e.getPoint().x - p_Delta.x);
              p_display.y = p_display.y + (e.getPoint().y - p_Delta.y);
              p_Delta = e.getPoint();
           }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        Rectangle r = Display.getBounds();
        if(r.contains(p)){
            if(!overChild)
                overChild = true;
            mx = p.x - r.x;
            my = p.y - r.y;
        }
        else if(overChild){
            overChild = false;
        }
    }

}