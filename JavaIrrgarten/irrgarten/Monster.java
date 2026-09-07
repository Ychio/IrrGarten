/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

/**
 *
 * @author aulas
 */
public class Monster {
    private static int INITIAL_HEALTH = 5;
    private String name;
    private float intelligence;
    private float strength ;
    private float health;
    private int row;
    private int col;
    private static final int FUERA_TABLERO = -1;
    
    public Monster(String name,float intelligence,float strength){
        this.name = name;
        this.intelligence = intelligence;
        this.strength = strength;
        health = INITIAL_HEALTH;
        row = FUERA_TABLERO;
        col = FUERA_TABLERO;
    }
    public boolean dead(){
        boolean vivo = true;
        if(health < 1){
            vivo = false;
        }
        return vivo;
    }
    
    public float attack(){
        return Dice.intensity(this.strength);
    }
    
    public void setPos(int row,int col){
        this.row = row;
        this.col = col;
    }
    @Override
    public String toString(){
        return "Name: " + name + " Intelligence : " + intelligence + " " +
               " Strength:" + strength + " " + 
               "Health: " + health + " " +
               "Position:" + "[" + row + "," + col + "]";
    }
    
    private void gotWounded(){
        health--;
    }
    
    public boolean defend(float receiveAttack){
        boolean isDead =dead();
        if(!isDead){
            float defensiveEnergy = Dice.intensity(intelligence);
            if(defensiveEnergy < receiveAttack){
                gotWounded();
                isDead = dead();
            }
        }
        return isDead;
        
    }
    
    
}
