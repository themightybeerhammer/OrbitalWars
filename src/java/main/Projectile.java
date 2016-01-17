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
/* Снаряд
*  для начала будет просто летающая болванка
*  потом можно будет ввести типы снярадов и 
*  разветвить метод их поведения 
*/
public class Projectile extends BaseClass{
    
    /*Конструкторы класса*/
    Projectile(){
        super();
    }
    Projectile(float x, float y, float m, int ro, float vangle, float vlength, ArrayList<BaseClass> AL){
        super(x, y, m, ro, vangle, vlength, AL);
    }
   
}
