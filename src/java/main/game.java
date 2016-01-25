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
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import static java.lang.Math.PI;
import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.signum;
import static java.lang.Math.sqrt;
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
    public Point p_Delta                        /*Изменение координат при нажатии правой кнопки мыши  */
               , p_display;                     /*Координаты центра экрана*/
    public int DisplayW = 800;                  /*Ширина экрана*/
    public int DisplayH = 600;                  /*Высота экрана*/
    public boolean RMBPressed = false;          /*Нажата правая кнопка мыши*/
    boolean overChild;                          /*Буфер для перемещения по контейнерам*/
    double mx, my;                               /*Последние известные координаты мыши*/
    public boolean ScrFlwPlr=false;             /*Экран следует за планетой игрока*/     
    public int un_end_X=1000,un_end_Y=1000;     /*Края вселеной вылетая за которые объект погибает*/
    
    /*Параметры для дебугагинга начало*/
    public double Mltplr = 10f;                  /*Множитель замедления*/      
    public boolean v_F = false;                 /*Рисовать вектор равнодействующей*/
    public boolean v_P = false;                 /*Рисовать вектор импульса*/ 
    /*Параметры для дебугагинга конец*/
    
    @Override
    public void init() {
        ALBaseClass = new ArrayList<>();
        CM = new CenterMass(ALBaseClass);
        setSize(DisplayW, DisplayH);
        p_Delta = new Point(0,0);
        p_display = new Point(DisplayW / 2, DisplayH / 2);
       
        /*тестовые болванки НАЧАЛО*/
        
        /*Одна звезда и 3 планеты*/
        player = new Planet(0, -400, 30, 10, 0, 480, ALBaseClass, true, true);
        new Planet(400, 400, 30, 10, Math.PI *3/ 4, 395, ALBaseClass, false, false);
        new Planet(-350, -350, 30, 10, -Math.PI / 4, 425, ALBaseClass, false, false);
        new Planet(-325, 325, 30, 10, Math.PI*5/4, 440, ALBaseClass, false, false);
        new Planet(-250, -250, 30, 10, -Math.PI/4, 510, ALBaseClass, false, false);
        new Planet(-250, 0, 30, 10, Math.PI / 2, 600, ALBaseClass, false, false);
        
      
        new Star(0, 0, 100000, 150, 0, 0, ALBaseClass);
        
        /*Система сиськи*/
      /*  player = new Planet(375, 375, 10, 10, Math.PI*200/207,80, ALBaseClass, true, true);
        player.dw_orbit = true;
        new Star(250, 250, 8000, 40, 0, 0, ALBaseClass);
        new Star(500, 500, 8000, 40, 0, 0, ALBaseClass);
        */
      
      
         /*создаем новый игровой экран*/
        Display = new DrawPanel((int)(DisplayW * 0.9), (int)(DisplayH * 0.9));
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
                Display.AssignList(ALBaseClass,player,p_display,v_F,v_P);  /*передача игровому экрану списка объектов для отрисовки*/ 
                /*Перерасчет импульса объекта, движение объекта и удаление вылетивших за пределы*/
                for(int i = 0; i < ALBaseClass.size(); i++){
                    if(ALBaseClass.get(i) != null){
                          /*пересчет импульса объекта*/
                        ALBaseClass.get(i).calc_F_ravn(Mltplr);
                        /*движение объекта*/
                        ALBaseClass.get(i).move(Mltplr);
                        /*Проверка что объект в приделах системы иначе СМЕРТЬ!!! ХА-ХА-ХА!!!!!!!*/
                        if ((ALBaseClass.get(i).X>un_end_X)
                           |(ALBaseClass.get(i).X<-un_end_X)
                           |(ALBaseClass.get(i).Y<-un_end_Y)
                           |(ALBaseClass.get(i).Y>un_end_Y))
                        {ALBaseClass.get(i).DeadFlag=true;}
                        /*Подзарядка планеты*/
                        if(ALBaseClass.get(i).getClass().getName() == "main.Planet"){
                            ((Planet)ALBaseClass.get(i)).Charge();
                        }
                    }
                }
                /*Следование экрана за объектом*/
                if(ScrFlwPlr){
                    //p_display.x = (int)(DisplayW / 2 - player.X);    /*End - возрат камеры к планете игрока*/
                    //p_display.y = (int)(DisplayH / 2 - player.Y);
                    p_display.setLocation(DisplayW / 2 - player.X, DisplayH / 2 - player.Y);
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
                            double rr = (Math.sqrt(Math.pow((ALBaseClass.get(i).X-ALBaseClass.get(j).X),2)+ Math.pow((ALBaseClass.get(i).Y-ALBaseClass.get(j).Y),2)));
                            
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
               
                //System.out.println("---------------------------");
                /*for(int i = 0; i < ALBaseClass.size(); i++){
                    if((i < ALBaseClass.size()) && (ALBaseClass.get(i) != null)){
                        if((ALBaseClass.get(i).F !=null) && (ALBaseClass.get(i).P != null))
                            System.out.print(ALBaseClass.get(i).F.length + " " + ALBaseClass.get(i).P.length + " | ");
                        ALBaseClass.get(i).calc_orbit();  
                    }
                }*/
               
            }
        };
        oTimer.schedule(oTimerTask, 0,25);
        setFocusable(true);
        requestFocusInWindow();
    }
    
    void SendNewPlanet(){
        double MaxDist = 0; /*Расстояние до самой дальней планеты*/
        double NewM;
        int NewRO;
        double NewDist;
        Planet NewPlanet;
        for(BaseClass AL : ALBaseClass){
            if((AL != null) 
            && ("main.Planet".equals(AL.getClass().getName()))){
                MaxDist = max(MaxDist, sqrt(pow(AL.X, 2) + pow(AL.Y, 2)));
            }
        }
        NewM = random() * 50;
        NewRO = (int)(random() * 50);
        NewDist = -MaxDist - NewRO * 2;
        NewPlanet = new Planet(0, NewDist, NewM, NewRO, (PI * signum(random() - 0.5)), NewDist, ALBaseClass, false, false);
        //NewPlanet.P.length *= NewPlanet.M;
        for(BaseClass AL : ALBaseClass)if("main.Planet".equals(AL.getClass().getName()))AL.calc_orbit();
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
            case 35:    if(ScrFlwPlr){          /*END - слежение за планетой игрока*/
                            ScrFlwPlr = false;
                        }else{
                            ScrFlwPlr = true;
                        } 
                        break;
            case 36:    p_display.x = DisplayW / 2;    /*Home - возрат камеры к центру системы*/
                        p_display.y = DisplayH / 2;
                        break;
            /*клавиши 1-9 для переключения оружия*/
            case 49:    player.GunType = 1;
                        break;
            case 50:    player.GunType = 2;
                        break;
            case 112:   SendNewPlanet();
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
            mx = p.x - r.x + 20;
            my = p.y - r.y + 20;
        }
        else if(overChild){
            overChild = false;
        }
    }

}