/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package irrgarten;




/**
 *
 * @author aulas
 */
public class Labyrinth {
    private static char BLOCK_CHAR = 'X';
    private static char EMPTY_CHAR = '-';
    private static char MONSTER_CHAR = 'M';
    private static char COMBAT_CHAR = 'C';
    private static char EXIT_CHAR = 'E';
    private static final int ROW = 0;
    private static final int COL = 1;
    
    private int nRows ;
    private int nCols;
    private int exitRow;
    private int exitCol;
    private Player[][] players;
    private char[][] labyrinth;
    private Monster[][] monsters;
    
    private static final int NUM_VECTOR = 2;
    
    public Labyrinth(int nRows,int nCols,int exitRow,int exitCol){
        players = new Player[nRows][nCols];
        monsters = new Monster[nRows][nCols];
        labyrinth = new char[nRows][nCols];
        
        for(int i=0;i<nRows;i++){
            for(int j=0;j<nCols;j++){
                labyrinth[i][j] = EMPTY_CHAR;
            }
        }
        this.nRows = nRows;
        this.nCols = nCols;
        this.exitCol = exitCol;
        this.exitRow = exitRow;
        labyrinth[exitRow][exitCol] = EXIT_CHAR;
    }
    
    public void spreadPlayers(Player[] players){
        for (Player p : players) {          
            int[] pos = randomEmptyPos();
            putPlayer2D(-1,-1,pos[ROW],pos[COL],p);
        }
    }
    
    public boolean haveAWinner(){
        return players[exitRow][exitCol] != null;
    }
    
    @Override
    public String toString(){
        String devolver="";
        for(int i=0;i<nRows;i++){
            devolver += "| ";
            for(int j=0;j<nCols;j++){
                devolver += labyrinth[i][j] +" " ;
            }
        devolver += " |\n";
        }
        return devolver;
    }
    
    public void addMonster(int row,int col,Monster monster){boolean seguir = true;
        int veces = 0;
        while(seguir && veces < 5){
            if(posOK(row,col) && labyrinth[row][col] == EMPTY_CHAR){
                monsters[row][col] = monster;
                monster.setPos(row, col);
                labyrinth[row][col] = 'M';}
            veces++;
        }
    }
    
    public Monster putPlayer(Directions direction,Player player){
       int oldRow = player.getRow();
       int oldCol = player.getCol();
       int[] newPos = dir2Pos(oldRow,oldCol,direction);
       Monster monster = putPlayer2D(oldRow,oldCol,newPos[ROW],newPos[COL],player);
       return monster;
    }
    
    public void addBlock(Orientation orientation,int startRow,int startCol,int length){//este comprobado
        int incRow,incCol;
        
        if(orientation == Orientation.VERTICAL){
            incRow = 1;
            incCol = 0;
        }else{
            incRow = 0;
            incCol = 1;
        }
        
        int row = startRow;
        int col = startCol;
        
        while((posOK(row,col)&& emptyPos(row,col)) && (length > 0)){
            labyrinth[row][col] = BLOCK_CHAR;
            length -= 1;
            row += incRow;
            col += incCol;
        }
        
    }
    
    public void setPlayer(Player p){
        labyrinth[p.getRow()][p.getCol()] = p.getNumber();
        players[p.getRow()][p.getCol()] = p;
    }
    
    public Directions[] validMoves(int row,int col){//comprobado
        int counter = 0;
        Directions[] output = new Directions[4];
        if(canStepOn(row+1,col)){
            output[counter++] = (Directions.DOWN);
        }
        if(canStepOn(row-1,col)){
            output[counter++] = Directions.UP;
        }
        if(canStepOn(row,col+1)){
            output[counter++] = Directions.RIGHT;
        }
        if(canStepOn(row,col-1)){
            output[counter++] = Directions.LEFT;
        }
        return output;
    }
    
    private boolean posOK(int row,int col){
        return row < nRows && col < nCols && row >= 0 && col >= 0;
    }
    
    private boolean emptyPos(int row,int col){
        return labyrinth[row][col] == EMPTY_CHAR;
    }
    
    private boolean monsterPos(int row,int col){
        
        return  labyrinth[row][col] == MONSTER_CHAR;
    }
    
    private boolean exitPos(int row,int col){
        return labyrinth[row][col] == EXIT_CHAR;
    }
    
    private boolean combatPos(int row,int col){
        return labyrinth[row][col] == COMBAT_CHAR;
    }
    private boolean canStepOn(int row,int col){
        return posOK(row,col) && (emptyPos(row,col) || monsterPos(row,col) || exitPos(row,col));
    }
    
    private void updateOldPos(int row,int col){
        if(posOK(row,col)){
            if(combatPos(row,col)){
                labyrinth[row][col] = MONSTER_CHAR;
            }else{
                labyrinth[row][col] = EMPTY_CHAR;
            }
        }
    }
    
    private int[] dir2Pos(int row,int col,Directions direction){
        int[] almacena = new int[NUM_VECTOR];
        switch(direction){
            case UP:
                if(labyrinth[row-1][col] != BLOCK_CHAR){
                    row--;
                }
                break;
            case DOWN:
                if(labyrinth[row+1][col] != BLOCK_CHAR){
                    row++;
                }
                break;
            case RIGHT:
                if(labyrinth[row][col+1] != BLOCK_CHAR){
                    col++;
                }
                break;
            default:
                if(labyrinth[row][col-1] != BLOCK_CHAR){
                    col--;
                }
        }
        
        almacena[0] = row;
        almacena[1] = col;
        return almacena;
    }
    
    private int[] randomEmptyPos(){
        int[] almacena  = new int[NUM_VECTOR];
        boolean blocked = true;
        int num1=0,num2=0;
        while(blocked){
            num1 = Dice.randomPos(nRows);
            num2 = Dice.randomPos(nCols);
            if(labyrinth[num1][num2] != BLOCK_CHAR){
                blocked = false;
            }
        }
        almacena[0] = num1;
        almacena[1] = num2;
        return almacena;
    }
    
    private Monster putPlayer2D(int oldRow,int oldCol,int row,int col,Player player){
         Monster output = null;
         if(canStepOn(row,col)){
             if(posOK(oldRow,oldCol)){
                 Player p = players[oldRow][oldCol];
                 if(p==player){
                     updateOldPos(oldRow,oldCol);
                     players[oldRow][oldCol] = null;
                     
                 }
             }
             
             boolean monsterPos = monsterPos(row,col);
             if(monsterPos){
                 labyrinth[row][col] = COMBAT_CHAR;
                 output = monsters[row][col];
             }else{
                 char number = player.getNumber();
                 labyrinth[row][col] = number;
             }
             
             players[row][col] = player;
             player.setPos(row,col);
         }
         return output;
    }
}
