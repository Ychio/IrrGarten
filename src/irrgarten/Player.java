/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;
import java.util.ArrayList;
import java.util.Arrays;
/**
 *
 * @author aulas
 */
public class Player {
    private static int MAX_WEAPONS = 2;
    private static int MAX_SHIELDS = 3;
    private static int INITIAL_HEALTH = 10;
    private static int HITS2LOSE = 3;
    
    private String name;
    private char number;
    private float intelligence;
    private float strength;
    private float health;
    private int row;
    private int col;
    private int consecutiveHits = 0;
    
    ArrayList<Weapon> listWeapon = new ArrayList<>();
    ArrayList<Shield> listShield = new ArrayList<>();
    private int listWeapon_uses = 0;
    private int listShield_uses = 0;
    private int firstElement = 1;
    
    public Player(char number,float intelligence,float strength){
        this.name = "Player#" + number;
        this.intelligence = intelligence;
        this.strength = strength;
    }
    
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }
    
    public char getNumber(){
        return number;
    }
    
    public void resurrect(){
        for(int i=0;i<listWeapon_uses;i++){
            listWeapon.remove(firstElement);
        }
        
        for(int i=0;i<listShield_uses;i++){
            listShield.remove(firstElement);
        }
        
        listWeapon_uses = 0;
        listShield_uses = 0;
        health = INITIAL_HEALTH;
        consecutiveHits = 0;  
    }
    
    public void setPos(int row,int col){
        this.row = row;
        this.col = col;
    }
    
    public boolean dead(){
        boolean alive = true;
        if(health < 1){
            alive = false;
        }
        return alive;
    }
    
    public Directions move(Directions direction,Directions[] validMoves){
        int size = validMoves.length;
        boolean contained = Arrays.asList(validMoves).contains(direction);
        if(size > 0 && !contained){
            Directions firstElement = validMoves[0];
            return firstElement;
        }else{
            return direction;
        }
    }
    
    public float attack(){
        return strength + sumWeapons();
    }
    
    public boolean defend(float receivedAttack){
        return manageHit(receivedAttack);
    }
    
    public void receiveReward(){
        int wReward = Dice.weaponReward();
        int sReward = Dice.shieldsReward();
        
        for(int i = 1 ; i <= wReward;i++){
            Weapon wnew = newWeapon();
            receiveWeapon(wnew);
        }
        
        for(int i = 1 ; i <= sReward;i++){
            Shield snew = newShield();
            receiveShield(snew);
        }
        
        int extraHealth = Dice.healthReward();
        
        health += extraHealth;
    }
    @Override
    public String toString(){
        String devolver = "Name :" +name +" " +
               "Intelligence :" + intelligence + " " +
               "Strength : " + strength + " " +
               "Health :" + health + " " +
               "Position : [" + row + "," + col + "] \n" ;
        devolver += "Weapons:\n";
        for(int i=0;i<listWeapon.size();i++){
            devolver += listWeapon.get(i).toString() + "\n";
        }
        devolver += "Shields:\n";
        for(int i=0;i<listShield.size();i++){
            devolver += listShield.get(i).toString() + "\n";
        }
        
        
        return devolver;
    }
    
    private void receiveWeapon(Weapon w){
        for(int i=listWeapon.size() -1;i> 0;i--){
            Weapon wi = listWeapon.get(i);
            boolean discard = wi.discard();
            
            if(discard){
                listWeapon.remove(wi);
            }
            int size = listWeapon.size();
            if(size < MAX_WEAPONS){
                listWeapon.add(w);
            }
        }
    }
    
    private void receiveShield(Shield s){
        for(int i= listShield.size()-1; i>0;i--){
            Shield si = listShield.get(i);
            boolean discard = si.discard();
            
            if(discard){
                listShield.remove(si);
            }
            
        }
        int size = listShield.size();
        if(size < MAX_SHIELDS){
            listShield.add(s);
        }
    }
    
    private Weapon newWeapon(){
        Weapon armament = new Weapon(Dice.weaponPower(),Dice.usesLeft());
        return armament;
    }
    
    private Shield newShield(){
        Shield protector = new Shield(Dice.shieldPower(),Dice.usesLeft());
        return protector;
    }
    
    
    private float sumWeapons(){
        float sum = 0;
        
        for(int i=0;i<listWeapon_uses ;i++){
            sum += listWeapon.get(i).attack();
        }
        
        return sum;
    }
    
    private float sumShields(){
        float sum = 0;
        
        for(int i=0;i<listShield_uses ;i++){
            sum += listShield.get(i).protect();
        }
        
        return sum;
    }
    
    private float defensiveEnergy(){
        return intelligence + this.sumShields();
    }
    
    private boolean manageHit(float receivedAttack){
        float defense = defensiveEnergy();
        if(defense < receivedAttack){
            gotWounded();
            incConsecutiveHits();
        }else{
            resetHits();
        }
        boolean lose;
        if(consecutiveHits == HITS2LOSE || dead()){
            resetHits();
            lose = true;
        }else{
            lose = false;
        }
        return lose;
    }
    
    private void resetHits(){
        this.consecutiveHits = 0;
    }
    
    private void gotWounded(){
        this.health--;
    }
    
    private void  incConsecutiveHits(){
        this.consecutiveHits++;
    }
}
