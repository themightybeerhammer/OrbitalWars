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
    Point P_Display;
    public int DisplayW = 800;                  /*Ширина экрана*/
    public int DisplayH = 600;                  /*Высота экрана*/
    
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
    public void AssignList(ArrayList<BaseClass> ALBC,Point p_display, boolean V_F, boolean V_P ){
        v_F = V_F;
        v_P = V_P; 
        ALBaseClass = new ArrayList<>(ALBC);
        P_Display = new Point(p_display);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 600);
        for(int i = 0 ;i < ALBaseClass.size(); i++){
            if(ALBaseClass.get(i) != null){
               ALBaseClass.get(i).draw(g, P_Display, v_F, v_P);
            }      
        }
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(DisplayW, DisplayH);
    }

}
