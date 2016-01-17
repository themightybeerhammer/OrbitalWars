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

import java.util.ArrayList;

/**
 *
 * @author Vladimir
 */
/* Игровой объект "Планета"
*  используется как для игрока, так и для целей
*  потом даже проще, наверное, будет ИИ реализовать
*/
public class Planet extends BaseClass {
    public boolean IsPlayer = false; /*метка планеты-игрока*/
    
    /*Конструкторы класса*/
    Planet(){
        super();
    }
    Planet(float x, 
            float y, 
            float m, 
            int ro, 
            float vangle, 
            float vlength, 
            ArrayList<BaseClass> AL, 
            boolean player){
        super(x, y, m, ro, vangle, vlength, AL);
        this.IsPlayer = player;
    }
    
    
    
    
}
