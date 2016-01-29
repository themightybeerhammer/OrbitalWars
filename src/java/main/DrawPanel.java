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

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.*;


/**
 *
 * @author Vladimir
 */
public class DrawPanel extends JPanel{
    
    static boolean v_F;
    static boolean v_P; 
    static ArrayList<BaseClass> ALBaseClass;
    Point2D P_Display;
    public int DisplayW = 800;                  /*Ширина экрана*/
    public int DisplayH = 600;                  /*Высота экрана*/
    Planet Player;
    
    public DrawPanel(int displayw, int displayh){
        /*замена курсора на прицел*/
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("icons/crosshair.png");
        Cursor c = toolkit.createCustomCursor(image , new Point(getX(), 
        getY()), "img");
        setCursor (c);
        DisplayH = displayh;
        DisplayW = displayw;
        
        v_F = true;
        v_P = true;
        
        setBorder(BorderFactory.createEtchedBorder());
        
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        timer.start();
    }
    
    /*Передача данных в дисплей*/
    public void AssignList(ArrayList<BaseClass> ALBC){
        ALBaseClass = new ArrayList<>(ALBC);
    }
    public void AssignList(ArrayList<BaseClass> ALBC,Planet player,Point2D p_display, boolean V_F, boolean V_P ){
        v_F = V_F;
        v_P = V_P; 
        ALBaseClass = ALBC;//new ArrayList<>(ALBC);
        //P_Display = new Point2D(p_display);
        P_Display = p_display;
        //Player = new Planet();
        Player = player;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 600);
        if(ALBaseClass != null)
        for(int i = 0 ;i < ALBaseClass.size(); i++){
            if(i < ALBaseClass.size())
            if(ALBaseClass.get(i) != null){
               ALBaseClass.get(i).draw(g, P_Display, v_F, v_P);
            }      
        }
        
         Graphics2D g2 = (Graphics2D)g;
         RenderingHints rh = new RenderingHints(
         RenderingHints.KEY_ANTIALIASING,
         RenderingHints.VALUE_ANTIALIAS_ON
         );
     
        /*Отрисовка прогрессбара здоровья объекта*/
        /*Заливка бара*/
        if(Player != null){
            g2.setRenderingHints(rh);
            g2.setColor(Color.GRAY);
            g2.fillRect(20, 20, 200, 15);
            if(Player.HealthCur==Player.HealthMax){ g2.setColor(Color.GREEN); }
            if((Player.HealthCur!=Player.HealthMax)&&((Player.HealthCur>Player.HealthMax*0.4))){g2.setColor(Color.YELLOW);        }
            if((Player.HealthCur!=Player.HealthMax)&&((Player.HealthCur<=Player.HealthMax*0.4))){g2.setColor(Color.RED);        }
            g2.fillRect(20, 20, (int)(200 * (Player.HealthCur / Player.HealthMax)), (int)(15)); 

            rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING
                                   ,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHints(rh);
            g.setColor(Color.WHITE);
            if(Player.HealthCur==Player.HealthMax){ g2.setColor(Color.BLACK); }
            if((Player.HealthCur!=Player.HealthMax)&&((Player.HealthCur>Player.HealthMax*0.4))){g2.setColor(Color.BLACK);        }
            if((Player.HealthCur!=Player.HealthMax)&&((Player.HealthCur<=Player.HealthMax*0.4))){g2.setColor(Color.WHITE);        }
            g2.drawString((int)Player.HealthCur+"/"+(int)Player.HealthMax, 22, 32);

            /*Отрисовка прогрессбара заряда энергии*/
            /*Заливка бара*/
             rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING
                                   ,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            g2.setRenderingHints(rh);
            g2.setColor(Color.GRAY);
            g2.fillRect(20, 40, 200, 15);
            g2.setColor(Color.BLUE); 
            g2.fillRect(20, 40, (int)(200 * (Player.Energy / Player.MaxEnergy)), (int)(15)); 
            rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
            g2.setRenderingHints(rh);
            g2.setColor(Color.WHITE);
            g2.drawString((int)Player.Energy+"/"+(int)Player.MaxEnergy, 22, 52);
        }
        
        /*Прорисовка Иконок Оружия*/
        
        
        
        Point2D center = new Point2D.Double(40, Player.GunType*50+30);
        float radius = 25;
        float rr = (float)(Math.random()*4+20)/100;
        float[] dist = { 0.8f, 1.0f};
        Color[] colors = { Color.GREEN, new Color(1,0,0,0) };
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
        g2.setPaint(p);
      //  g2.setClip(new Ellipse2D.Double(20,Player.GunType*50-5,50,50));
        g2.fill(new Ellipse2D.Double(15, Player.GunType*50+5, 50, 50));
        
        
        
       for(int i=1;i<9;i++){
            rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING
                                ,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHints(rh);
            String str = "icons/weapon"+i+".png";
            Image img = Toolkit.getDefaultToolkit().getImage(str);
            g2.setStroke(new BasicStroke(1f));
            //g2.setClip(new Ellipse2D.Double(20,i*50+10,40,40));
            g2.drawImage(img, 20,i*50+10, this);
        }
        
     
       
        
        
      
        
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(DisplayW, DisplayH);
    }

}
