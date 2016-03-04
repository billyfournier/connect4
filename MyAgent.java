import java.util.Random;
import java.lang.*;

public class MyAgent extends Agent
{
    Random r;
    int numColumns;
    int numRows;

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
        numColumns = game.getColumnCount();
        numRows = game.getRowCount();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     * 
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     * 
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     * 
     */
    public void move()
    {
        // so far take winning move if available, else random mo
        int winVal = iCanWin(iAmRed);
        int loseVal = iCanWin(!iAmRed);
        int rand = randomMove();
        
        System.out.println("numRows = " + numRows + "\tnumCol = " + numColumns);
        //System.out.println("winVal: " + winVal + "\tloseVal: " + loseVal + "\tRand: " + rand );
        //System.out.println("========================================");
        if(winVal != -1) 
        {
            System.out.println("### My Move was on Column: " + winVal + "\tWinning");
            moveOnColumn(winVal);            
        }
        else if(loseVal != -1)
        {
            System.out.println("### My Move was on Column: " + loseVal + "\tBlocking");
            moveOnColumn(loseVal);            
        }
        else    
        {
            System.out.println("My Move was on Column: " + rand + "\tRandomed.");
            moveOnColumn(rand);            
        }
    }
    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int playableIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                                  // If the column is full, lowestEmptySlot will be -1
        if (playableIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(playableIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < numRows; i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(numColumns);
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(numColumns);
        }
        return i;
    }
    
    
    /**
     *  
     *  
     */ 
        
    
    /**
     * Returns the column that would allow the agent to win.
     * 
     * You might want your agent to check to see if it has a winning move available to it so that
     * it can go ahead and make that move. Implement this method to return what column would
     * allow the agent to win.
     *
     * @return the column that would allow the agent to win or a -1 if no winning move is available.
     */
    public int iCanWin(boolean myColor)
    {
        //checking for vertical win
        int verticle = checkVerticalWin(myColor,3);
        int horizontal = checkHorizontalWin(myColor);
        int diagonal = checkDiagonalWin(myColor);
        int horizontalTwo = winInTwo(myColor);
        
        if(verticle != -1)
        {
            return verticle; 
        }
        else if(horizontal != -1)
        {
            return horizontal;
        }
        else if(diagonal != -1)
        {
            return diagonal;
        }
        else if(horizontalTwo > -1)
        {
            return horizontalTwo;
        } 
        else
        {
            return -1;
        }
    }


    
    /**
     * Checks to see if a win can be had vertically, within a single column.
     * 
     * @param myColor the color of the player. (red being true)
     * @param numOfTokens the number of matching tokens played consecutively and immediately below the next playable slot.
     * @return the index of the winning ( building/blocking ) move, or -1 if the check fails.
     */
     public int checkVerticalWin(boolean myColor, int numOfTokens)
     {     
         int playableIndex = -1;
         for(int i = 0; i < numColumns; i++) // iterates over the columns
         {
             playableIndex = getLowestEmptyIndex(myGame.getColumn(i));                  // Playable index of given column(i);
             int tokensPlayed = numRows - 1 - playableIndex;  // Tokens Already Played.
             int count = 0;
             int slot = playableIndex;                                                  /// !!!redundant, may clean later!!! ///
             int column = i;
             if( slotIsPlayable(column,slot) && tokensPlayed > numOfTokens )    // Making sure a play on this column is valid.  && checking at least nth (numberOfTokens) slots have been played in this column
             {
                 for(int j = 1; j <= numOfTokens; j++) //Iterates to check if the n tokens below belong to this player.
                 {
                     slot = playableIndex + j;
                     if( isMyToken(column, slot, myColor) == 1) 
                     {
                         count++;
                     }
                     if(count == numOfTokens) 
                     {
                         return i;
                     }
                }
            }
        }
        return -1;
    }
    
    
    
    
    
    
    
    
     /**
     * Checks to see if a win can be had horizontally.
     * 
     * @param myColor the color of the player. (red being true)
     * @return the index of the winning move, or -1 if the check fails.
     */
    public int checkHorizontalWin(boolean myColor)
    {
        for(int i = 0; i < numColumns; i++) // iterates over the columns
        {
            int playableIndex = getLowestEmptyIndex(myGame.getColumn(i));       // Playable index of given column(i);
            int count = 0;                                                      // int count;           -- the counter to check for 3 tokens of player connected to the move.
            int slot = playableIndex;
            int column = i;
            int colToLeft = Math.min(3,i);                                                  // int colToLeft;       -- number of spaces to the left.
            int colToRight = Math.min(3,numColumns - i - 1);                   // int colToRight;      -- number of spaces to the right.
            if(slotIsPlayable(column,slot)) // This column is playable
            {
                for(int j = 1; j <= colToRight; j++)   // Loop to check, up to, 3 spaces to the right.
                {
                    if( myGame.getColumn(i+j).getSlot(playableIndex).getIsFilled() == true               // Checking to see if slot to right (+j) is filled
                            && myColor == myGame.getColumn(i+j).getSlot(playableIndex).getIsRed() )      // Checking to see if slot to right (+j) is players token
                    {
                        count++;                        
                        if(count == 3)
                        {
                            return i;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
                
                for(int k = 1; k <= colToLeft; k++)
                {
                    if( myGame.getColumn(i-k).getSlot(playableIndex).getIsFilled() == true
                            && myColor == myGame.getColumn(i-k).getSlot(playableIndex).getIsRed() )
                    {
                        count++;                
                        if(count == 3)
                        {
                            return i;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
        return -1;    
    }
    
  
     /**
     * Checks to see if a win can be had diagonally.
     * 
     * @param myColor the color of the player. (red being true)
     * @return the index of the winning move, or -1 if the check fails.
     */
    public int checkDiagonalWin(boolean myColor)
    {
        for(int i = 0; i < numColumns; i++) // iterates over the columns
        {
            int playableIndex = getLowestEmptyIndex(myGame.getColumn(i));           // this columns playable slot index .
            int count = 0;                                                      // -- the counter to check for 3 tokens of player connected to the move.
            
            int column = i;
            int slot = playableIndex;
            
            int colToRight = numColumns - i - 1;                   // int colToRight;      -- number of spaces to the right.
            int colToLeft = i;                                                  // int colToLeft;       -- number of spaces to the left.
            int topRight = Math.min(slot,Math.min(colToRight,3));
            int topLeft =  Math.min(slot,Math.min(colToLeft,3));
      
            int toTheBot = numRows - playableIndex - 1;   // int toTheBot;        -- number of spaces tot he bottom.            
            int botRight = Math.min(toTheBot,Math.min(colToRight,3));
            int botLeft = Math.min(toTheBot,Math.min(colToLeft,3));
            
            if(slotIsPlayable(column,slot)) // This column is playable
            {
                for(int j = 1; j <= topRight; j++)    // Right-Top
                {
                    if( myGame.getColumn(i+j).getSlot(playableIndex - j).getIsFilled() == true              // Checking to see if slot to right is filled
                            && myColor == myGame.getColumn(i+j).getSlot(playableIndex - j).getIsRed() )      // Checking to see if slot to right is my token
                    {
                        count++;
                        if(count > 2)
                        {
                            return i;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
                for(int k = 1; k <= botLeft; k++)      // Left - Bottom
                {                   
                    if( myGame.getColumn(i-k).getSlot(playableIndex + k).getIsFilled() == true
                            && myColor == myGame.getColumn(i-k).getSlot(playableIndex + k).getIsRed() )
                    {
                        count++;                
                        if(count > 2)
                        {
                            return i;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            
                count = 0;
                for(int j = 1; j <= botRight; j++)   // Right - Bottom
                {
                    if( myGame.getColumn(i+j).getSlot(playableIndex + j).getIsFilled() == true              // Checking to see if slot to right is filled
                            && myColor == myGame.getColumn(i+j).getSlot(playableIndex + j).getIsRed() )      // Checking to see if slot to right is my token
                    {
                        count++;
                        if(count > 2)
                        {
                            return i;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
                for(int k = 1; k <= topLeft; k++)       // Left - Top
                {
                    if( myGame.getColumn(i-k).getSlot(playableIndex - k).getIsFilled() == true
                            && myColor == myGame.getColumn(i-k).getSlot(playableIndex - k).getIsRed() )
                    {
                        count++;                
                        if(count > 2)
                        {
                            return i;
                        }
                    }
                    else
                    {
                        break;
                    }
                }             
            }
        }
        return -1;
    }
          
      
   
     /**
     * Checks for the six cases "4 choose 2" which a win/lose can be had 
     * within two moves on the horizontal plane.
     * 
     * (_,X,_,X)    (_,X,X,_)   (X,_,_,X)     
     * 
     * (_,_,X,X)    (X,X,_,_)   (X,_,X,_) 
     * 
     * Also checks for two tokens stacked.
     * 
     *  (_)
     *  (X)
     *  (X)
     * 
     * @param myColor the color of the player. (red being true)
     * @return the index of the winning move, or -1 if the check fails.
     */
    public int winInTwo(boolean myColor)
    {
       // Horizontal Check
        for(int i = 0; i < numColumns; i++) // iterates over the columns
        {
            int playableIndex = getLowestEmptyIndex(myGame.getColumn(i));          // this columns playable slot index .
            int count = 0;
            int tokenCount = 0;                                                 // int count;           -- the counter to check for 3 tokens of player connected to the move.
            int validEmptyCount = 0;
            int colToLeft = i;                                                  // int colToLeft;       -- number of spaces to the left.
            int colToRight = numColumns - i - 1;                   // int colToRight;      -- number of spaces to the right.
            int indexOfFilledRight = 0;
            int indexOfFilledLeft = 0;
            boolean firstFilled = false;
            
            
            if(playableIndex != -1) // This column is playable
            {
                for(int j = 1; j <= Math.min(colToRight,3); j++)   // Loop to check, up to, 3 spaces to the right.
                {
                    int crossCheckIndex = getLowestEmptyIndex(myGame.getColumn(i+j));           // Not necessary, but makes if statements easier to read.
                    if( myGame.getColumn(i+j).getSlot(playableIndex).getIsFilled() == true                // Checking to see if slot to right (+j) is empty
                            && myColor == myGame.getColumn(i+j).getSlot(playableIndex).getIsRed() )       // Checking to see if slot to right (+j) is players token
                    {
                        indexOfFilledRight = i+j;
                        tokenCount++;
                        count = tokenCount + validEmptyCount;
                        if(myGame.getColumn(i+1).getSlot(playableIndex).getIsFilled() == true)     // Checking if first checked element to the right is filled 
                        {
                            firstFilled = true;
                        }
                        if(count > 2 && tokenCount > 1)
                        {
                            if(myColor == true) 
                            {
                                if(firstFilled == true)
                                {
                                    return i;
                                }
                                else                            //This is returning the 2nd open space in the event (_,_,X,X). Instead of playing (+,_,X,X) it will play (_,+,X,X).
                                {
                                    return i+1;                                    
                                }
                            }
                            if(myColor == false)
                            {
                                if(firstFilled == true)
                                {
                                    return i;
                                }
                                else                            //This is returning the 2nd open space in the event (_,_,X,X). Instead of playing (+,_,X,X) it will play (_,+,X,X).
                                {
                                    return i+1;                                    
                                }
                                

                            }
                        }
                    }
                    else if( myGame.getColumn(i+j).getSlot(playableIndex).getIsFilled() == false        //  Slot is empty
                                &&  crossCheckIndex == playableIndex)                                   //  Slot is available for next play
                    {
                        validEmptyCount++;
                        count = tokenCount + validEmptyCount;
                        if(count > 2 && tokenCount > 1)
                        {
                            return i;
                        }
                    } 
                    else
                    {
                        break;
                    }
                    
             
                }
            }
        }
        
        // Check vertically
        int verticalTwo = checkVerticalWin(myColor,2);  
        if(verticalTwo != -1)                           // Pre-emptively blocking verticle build ups. If two of the opposing players tokens are stacked.
        {
            return verticalTwo;
        }
        
        return -1;
    }
    
    /**
     * Checks color of token at a specific column and slot(row).
     * 
     * @param slot the value of the row(slot) to check
     * @param column the value of the column to check
     * @param myColor the color of the player. (red being true)
     * @return the color of token in the slot. 1 = My Color, 0 = empty, -1 = Opponents Color
    */
    public int isMyToken(int column, int slot, boolean myColor)
    {
        if(myGame.getColumn(column).getSlot(slot).getIsFilled() == false)
        {
            return 0;
        }
        else if(myColor == myGame.getColumn(column).getSlot(slot).getIsRed())
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }
    
    /**
     * Checks if the slot at the specified column and slot(row) is playable.
     * 
     * @param slot the value of the slot(row) to check
     * @param column the value of the column to check
     * @return the color of token in the slot. (true = Playable) (False = Not Playable)
    */    
    public boolean slotIsPlayable(int column, int slot)
    {
        if(slot < 0)
        {
            return false;
        }
        else if(slot == getLowestEmptyIndex(myGame.getColumn(column)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    
    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "Billy's Agent";
    }
}
