import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
 
/**
 * 
 * @author jervincruz
 *
 * The Model part of MVC - 
 */
public class GameModel {
 
    public static enum Player {A, B};
     
    private int[] pits;
    private int[] lastPit;
    private int playerAUndos;
    private int playerBUndos;
    private Player currentPlayer;
    private boolean gameOver;
    private boolean canUndo;
     
    private ArrayList<ChangeListener> listeners;
     
    /**
     * GameModel - default constructor
     */
    public GameModel(int stones)
    {
        pits = new int[14];
        lastPit = new int[14];
        playerAUndos = 0;
        playerBUndos = 0;
        currentPlayer = Player.A;
        gameOver = false;
        listeners = new ArrayList<ChangeListener>();
        canUndo = false;
                setStones(stones);
 
    }
     
    /**
     * attach - attach a listener to the model
     * @param listener - listener that is notified when a change is made to the model
     */
    public void attach(ChangeListener listener)
    {
        listeners.add(listener);
    }
     
    /**
     * notifyAllListeners - signals all listeners that a change has been made
     */
    public void notifyAllListeners()
    {
        for (ChangeListener listener: listeners)
        {
            listener.stateChanged(new ChangeEvent(this));
        }
    }
     
    /**
     * GameModel constructor
     * @param newModel - the old Mancala board model
     */
    private GameModel(GameModel newModel)
    {
        pits = new int[14];
        pits = Arrays.copyOf(newModel.pits, 14);
        currentPlayer = newModel.getCurrentPlayer();
        gameOver = false;
         
    }
     
    /**
     * setStones - sets the board with the number of stones indicated into each pit
     * @param numOfStones - number of stones
     */
    public void setStones(int numOfStones){
        for (int i = 0; i < 14; i++){
            if (!isMancala(i)){
                pits[i] = numOfStones;
            }
        }
        this.notifyAllListeners();
    }
     
    /**
     * getCurrentPlayer - returns the current player of the turn
     * @return the current player
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }
     
    /**
     * canUndo - looks at the amount of undos the player has, and returns true or false
     * @return whether or not the player can undo
     */
    public boolean canUndo(){
        switch(currentPlayer)
        {
            case A:
                return (playerAUndos < 3);
                                 
            case B:
                return (playerBUndos < 3);
                                 
            default:
                throw new RuntimeException("Runtime Error!");
        }
    }
     
    /**
     * undo - does the actual undoing of the board
     */
    public void undo()
    {
        // if the player has exceeded the amount of undos, method exists
        if (!canUndo() || this.pits.equals(lastPit))
            return;
         
        switch(currentPlayer)
        {
            case A:
            {
                this.playerAUndos++; 
                break;
            }
            case B:
            {
                this.playerBUndos++;
                break;
            }
        }
         
        this.pits = Arrays.copyOf(lastPit,14);
        notifyAllListeners();               
    }
     
    /**
     * gameOverCheck - checks if the game is over by looking at if the pits are empty
     * @return whether or not the game is over
     */
    private boolean gameOverCheck(){
        int aSide = 0;
        int bSide = 0;
         
        for (int i = 0; i < 14; i++)
        {
            if (!isMancala(i))
            {
                 
                if (getOwner(i).equals(Player.A))
                    aSide += pits[i];
                else
                    bSide += pits[i];
            }
        }
         
        return (aSide == 0 || bSide == 0);
    }
     
    /**
     * switchTurn - switches turn to the other player
     */
    public void switchTurn(){   
                switch(currentPlayer)
        {
            case A:
                currentPlayer = Player.B;
                                break;
            case B:
                currentPlayer = Player.A;
                            break;
        }
                System.out.println(currentPlayer + " is up.");
    }
     
    /**
     * save - will save the current state of the board
     */
    public void save()
    {
        lastPit = pits.clone();
    }
     
    /**
     * getOwner - checks which player can use the pit
     * @param indexOfPit - the index of the pit (0-5 A, 6 Mancala pit ofA, 7-12 B, 13 Mancala pit of B)
     * @return either Player A or B
     */
    private Player getOwner(int indexOfPit)
    {
        if (indexOfPit >= 0 && indexOfPit <= 6)
            return Player.A;
        else
            return Player.B;
    }
     
    /**
     * opponentPitEquiv - looks at the equivalent spot of the player, on the other player's side
     * @param indexOfPit
     * @return the corresponding pit on the other side
     */
    private int opponentPitEquiv(int indexOfPit)
    {
        return Math.abs(12 - indexOfPit);
    }
     
    /**
     * collectStones - after winning the game, the stones will be put into Mancala pit
     */
    private void collectStones()
    {
        for (int i = 0; i < 14; i++)
        {
            if (!isMancala(i))
            {
                if (getOwner(i).equals(Player.A))
                {
                    pits[6] += pits[i];
                    pits[i] = 0;
                }
                else
                {
                    pits[13] += pits[i];
                    pits[i] = 0;
                }
            }
        }
    }
         
    /**
     * getStone - returns number of stones in a given pit
     * @param index
     * @return pits[index]
     */
    public int getStone(int index){
          return(pits[index]);
        }
    
    /**
     * makeMove - makes move for the current player
     * @param indexOfPit
     */   
     public void makeMove(int indexOfPit){
    	 if (getOwner(indexOfPit).equals(currentPlayer)){
    		 this.save();
    		 int stonesInHand = pits[indexOfPit];
    		 pits[indexOfPit] = 0; //set adding stone pit to 0
         
    		 int index = indexOfPit;
    		 	while (stonesInHand > 0){
    		 		index++;
    		 		if (index >= 14){
    		 			index = 0;
    		 		}
    		 		// skip if not player's Mancala pit
    		 		if (isMancala(index) && getOwner(index) != currentPlayer){
    		 			continue;
    		 			}
    		 		// place stones in pit
    		 		pits[index]++;
    		 		stonesInHand--;
    		 		}
         
        // depending on the last stone, determines if turn is continued, or switches players
         
        // empty pit on player's own side
        if (getOwner(index).equals(currentPlayer) && pits[index] == 1 
        		&& pits[opponentPitEquiv(index)] > 0 && !isMancala(index)){
        	
            // taking opponent's stones
            int taken = pits[index] + pits[opponentPitEquiv(index)];
            pits[index] = 0;
            pits[opponentPitEquiv(index)] = 0;
             
            if (currentPlayer == Player.A){
                pits[6] += taken;
            }
            else{
                pits[13] += taken;
            }
            switchTurn();} 
        else if(!isMancala(index)){
        	switchTurn();
        	}
         
        if (gameOverCheck()){
            collectStones();
            gameOver = true;
        }
        	this.notifyAllListeners();
        }
    	 
    	 else
    	 {System.out.println("Error. Invalid move!");}
    }
     
    /**
     * checks whether the indicated pit is a Mancala pit
     * @param indexOfPit
     * @return true or false whether it is a Mancala pit
     */
    public boolean isMancala(int indexOfPit)
    {
        return (indexOfPit == 6 || indexOfPit == 13);
    }

    public int[] getData()
    {
        return pits;
    }
}