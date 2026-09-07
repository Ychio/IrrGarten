/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

import java.util.Random;

/**
 *
 * @author aulas
 */
public class Dice {

    private final static int MAX_USES = 5;
    private final static float MAX_INTELLIGENCE = 10.0f;
    private final static float MAX_STRENGTH = 10.0f;
    private final static float RESURRECT_PROB = 0.3f;
    private final static int WEAPONS_REWARDS = 2;
    private final static int HEALTH_REWARDS = 5;
    private final static int SHIELDS_REWARDS = 3;
    private final static int MAX_ATTACK = 3;
    private final static int MAX_SHIELD = 2;
    private static Random generator = new Random();

    public static int randomPos(int max) {
        return generator.nextInt(max);
    }
    
    public static int whoStarts(int nplayers){
        return generator.nextInt(1,nplayers+1);
    }
    
    public static float randomIntelligence(){
        return generator.nextFloat() * MAX_INTELLIGENCE;
    }
    
    public static float randomStrength(){
        return generator.nextFloat() * MAX_STRENGTH;
    }
    
    public static boolean resurrectPlayer(){
        boolean resucitar = false;
        if(generator.nextFloat() < RESURRECT_PROB){
            resucitar = true;
        }
        return resucitar;
    }
    
    public static int weaponReward(){
        return generator.nextInt(WEAPONS_REWARDS+1);
    }
    
    public static int shieldsReward(){
        return generator.nextInt(SHIELDS_REWARDS+1);
    }
    
    public static int healthReward(){
        return generator.nextInt(HEALTH_REWARDS+1);
    }
    
    public static float weaponPower(){
        return generator.nextFloat(MAX_ATTACK);
    }
    
    public static float shieldPower(){
        return generator.nextFloat(MAX_SHIELD);
    }
    
    public static int usesLeft(){
        return generator.nextInt(MAX_USES+1);
    }
    
    public static float intensity(float competence){
        return generator.nextFloat(competence);
    }
    
    public static boolean discardElement(int usesLeft){
        boolean returnar = false;
       if(generator.nextFloat() > (float)usesLeft/MAX_USES){
           returnar = true;
       }
       return returnar;
    }
    
}
