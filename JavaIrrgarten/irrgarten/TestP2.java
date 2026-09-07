/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;
import irrgarten.UI.TextUI;
import irrgarten.controller.Controller;
/**
 *
 * @author aulas
 */
public class TestP2 {
    public static void main(String[] args){
        Game game = new Game(2,false);
        TextUI vista  = new TextUI();
        game.nextStep(Directions.DOWN);
        vista.showGame(game.getGameState());
        Controller c1 = new Controller(game,vista);
        c1.play();
    }
}
