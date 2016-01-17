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
    
    
    public void AssignList(ArrayList<BaseClass> ALBC)
    {
       
        ALBaseClass = new ArrayList<BaseClass>(ALBC);
  
    }
  

    public void AssignList(ArrayList<BaseClass> ALBC
                          ,boolean V_F
                          ,boolean V_P )
    {
        v_F = V_F;
        v_P = V_P; 
        ALBaseClass = new ArrayList<BaseClass>(ALBC);
  
    }
    
    public DrawPanel() {
       v_F = true;
       v_P = true;
       
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i = 0 ;i < ALBaseClass.size(); i++){
            ALBaseClass.get(i).draw(g,v_F,v_P);
                  
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DrawPanel timer = new DrawPanel();
                timer.AssignList(ALBaseClass,v_F,v_P);
            }
        });
    }
    
}
