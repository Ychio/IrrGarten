/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

/**
 *
 * @author aulas
 */
public class Game {
    private final static int MAX_ROUNDS = 10;
    int currentPlayerIndex;
    String log;
    
    
    private int nRows =5;
    private int nCols = 7;
    private int exitCol = 2;
    private int exitRow = 2;
    private Monster[] monsters;
    private Player[] players;
    private Player currentPlayer;
    private int nMonsters = 4;
    Labyrinth labyrinth;
    
    public Game(int nplayers,boolean debug){
        labyrinth = new Labyrinth(nRows,nCols,exitRow,exitCol);
        players = new Player[nplayers];
        monsters = new Monster[nMonsters];
            for(int i=0;i<nplayers;i++){
                players[i] = new Player((char)('1'+ i),Dice.randomIntelligence(),Dice.randomStrength());                                                    
            }
            currentPlayerIndex = Dice.whoStarts(nplayers);
            currentPlayer = players[currentPlayerIndex-1];
            log = "";
            labyrinth.spreadPlayers(players);
        if(!debug){
            this.configureLabyrinth();
        }
    }
    
    public boolean finished(){
        return labyrinth.haveAWinner();
    }
    
    public boolean nextStep(Directions preferredDirection){
        log = "";
        boolean dead = currentPlayer.dead();
        if(!dead){
            Directions direction = actualDirection(preferredDirection);
            if(direction != preferredDirection){
                logPlayerNoOrders();
            }
            Monster monster = labyrinth.putPlayer(direction,currentPlayer);
            if(monster == null){
                logNoMonster();
            }else{
                GameChracter winner = combat(monster);
                manageReward(winner);
            }
        }else{
            manageResurrection();
        }
        
        boolean endGame = finished();
        
        if(!endGame){
            nextplayer();
        }
        
        return endGame;
    }
    
    public GameState getGameState(){
        String player="";
        String monster="";
        
        for(int i=0;i<players.length;i++){
            player += players[i].toString() + "\n";
        }
        
        for(int i=0;i<monsters.length;i++){
            monster += monsters[i].toString() + "\n";
        }
        
        GameState gameState = new GameState(labyrinth.toString(),player,monster,currentPlayerIndex,this.finished(),log);
        return gameState;
    }
    
    private void configureLabyrinth(){
        labyrinth.addBlock(Orientation.HORIZONTAL,1,0,2);
              
        for(int i=0;i<nMonsters;i++){
            String name = "Monster"+(i+1);
            float intelligence = Dice.randomIntelligence();
            float strength = Dice.randomStrength();
            monsters[i] = new Monster(name,intelligence,strength);
            labyrinth.addMonster(Dice.randomPos(nRows), Dice.randomPos(nCols), monsters[i]);
        } 
    }
    
    
    private void nextplayer(){
        
        currentPlayerIndex += 1;
        if(currentPlayerIndex > players.length ){
            currentPlayerIndex = 1;
        }
        currentPlayer = players[currentPlayerIndex-1];
        
    }
    
    private Directions actualDirection(Directions preferredDirection){
        int currentRow = currentPlayer.getRow();
        int currentCol = currentPlayer.getCol();
        Directions[] validMoves = labyrinth.validMoves(currentRow,currentCol);
        Directions output = currentPlayer.move(preferredDirection,validMoves);
        return output;
    }
    
    private GameChracter combat(Monster monster){
        int rounds = 0;
        GameChracter winner = GameChracter.PLAYER;
        
        float playerAttack = currentPlayer.attack();
        boolean lose = monster.defend(playerAttack);
        
        while(!lose && rounds < MAX_ROUNDS){
            winner = GameChracter.MONSTER;
            rounds++;
            float monsterAttack = monster.attack();
            lose = currentPlayer.defend(monsterAttack);
            if(!lose){
                playerAttack =currentPlayer.attack();
                winner = GameChracter.PLAYER;
                lose = monster.defend(playerAttack);
            }
        }
        
        logRounds(rounds,MAX_ROUNDS);
        return winner;
        
    }
    
    private void manageReward(GameChracter winner){
        if(winner == GameChracter.PLAYER){
            currentPlayer.receiveReward();
            logPlayerWon();
        }else{
            logMonsterWon();
        }
    }
    
    private void manageResurrection(){
        boolean resurrect = Dice.resurrectPlayer();
        if(resurrect){
            currentPlayer.resurrect();
            logResurrected();
        }else{
            logPlayerSkipTurn();
        }
    }
    
    private void logPlayerWon(){
       log += "Player won \n";
    }
    
    private void logMonsterWon(){
       log += "Monster won \n";
    }
    
    private void logResurrected(){
       log += "Resurrected \n";
    }
    
    private void logPlayerSkipTurn(){
       log += "Player skip because of death \n";
    }
    
    private void logPlayerNoOrders(){
       log += "Player order no posible \n";
    }
    
    private void logNoMonster(){
       log += "Move to a empty cell / cell unable to move\n";
    }
    
    private void logRounds(int rounds,int max){
       log += (rounds + "/" + max + "\n");
    }
}
