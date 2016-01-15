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

public class DrawPanel extends JPanel {
    static ArrayList<BaseClass> ALBaseClass;

    public void AssignList(ArrayList<BaseClass> ALBC){
        ALBaseClass = new ArrayList<BaseClass>(ALBC);
    }
    
    public DrawPanel() {
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
            ALBaseClass.get(i).draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700, 500);
    }
    
}
