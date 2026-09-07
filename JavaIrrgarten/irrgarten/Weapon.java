/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

/**
 *
 * @author aulas
 */
public class Weapon {
    private float power;
    private int uses;
    
    public Weapon(float power,int uses){
        this.power = power;
        this.uses = uses;
    }
    
    public float attack(){
        if(uses > 0){
            uses--;
            return power;
        }else{
            return uses;
        }
    }
    @Override
    public String toString(){
        return "W[" + power+"," + uses + "]";
    }
    
    public boolean discard(){
        return Dice.discardElement(uses);
    }
}
