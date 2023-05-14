package ataxx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import static ataxx.PieceState.EMPTY;

// Final Project Part A.2 Ataxx AI Player (A group project)

/** A Player that computes its own moves. */
class AIPlayer extends Player {

    
    /** A new AIPlayer for GAME that will play MYCOLOR.
     *  SEED is used to initialize a random-number generator,
	 *  increase the value of SEED would make the AIPlayer move automatically.
     *  Identical seeds produce identical behaviour. */
    AIPlayer(Game game, PieceState myColor, long seed) {
        super(game, myColor);
    }

    @Override
    boolean isAuto() {
        return true;
    }

    @Override
    String getAtaxxMove() {
        Move move = findMove();
        getAtaxxGame().reportMove(move, getMyState());
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(getAtaxxBoard());
        lastFoundMove = null;

        // Here we just have the simple AI to randomly move.
        // However, it does not meet with the requirements of Part A.2.
        // Therefore, the following codes should be modified
        // in order to meet with the requirements of Part A.2.
        // You can create add your own method and put your method here.

        // get all possible Moves
        ArrayList<Move>[] listOfMovesArray = possibleMoves(b, b.nextMove());
        ArrayList<Move> aiListOfMoves = listOfMovesArray[0];
        ArrayList<Move> manualListOfMoves = listOfMovesArray[1];
        System.out.println("    aiListOfMoves = " + aiListOfMoves);
        System.out.println("manualListOfMoves = " + manualListOfMoves);
        // get all clone moves;
        ArrayList<Move> aiCloneListOfMoves = (ArrayList<Move>) aiListOfMoves.stream().filter(Move::isClone).collect(Collectors.toList());
        ArrayList<Move> jumpOutOfRangeListOfMoves = new ArrayList<>();
        int moveArrayLength;
        // get random index in all moves
        int randomIndex ;
        // if no clone moves
        if(aiCloneListOfMoves.isEmpty()) {
            // find jump moves that out of manual's attack range
            for (Move move: aiListOfMoves) {
                // If it doesn't exist in manualListOfMoves, it is explained out of manual's attack range
                if(!manualListOfMoves.contains(move)) {
                    jumpOutOfRangeListOfMoves.add(move);
                }
            }
        }else {
            // find jump moves that out of manual's attack range
            for (Move move: aiCloneListOfMoves) {
                // If it doesn't exist in manualListOfMoves, it is explained out of manual's attack range
                if(!manualListOfMoves.contains(move)) {
                    jumpOutOfRangeListOfMoves.add(move);
                }
            }
        }
        System.out.println("jumpOutOfRangeListOfMoves = " + jumpOutOfRangeListOfMoves);
        System.out.println("aiCloneListOfMoves = " + aiCloneListOfMoves);
        // if jumpOutOfRangeListOfMoves not empty
        if(!jumpOutOfRangeListOfMoves.isEmpty()) {
            //select random move
            moveArrayLength = jumpOutOfRangeListOfMoves.size();
            randomIndex = (int) (Math.random() * moveArrayLength);
            //simplify above code to follow
            b.createMove(jumpOutOfRangeListOfMoves.get(randomIndex));
            lastFoundMove = jumpOutOfRangeListOfMoves.get(randomIndex);
        }

        // Please do not change the codes below
        if (lastFoundMove == null) {
            lastFoundMove = Move.pass();
        }
        return lastFoundMove;
    }


    /** The move found by the last call to the findMove method above. */
    private Move lastFoundMove;


    /** Return all possible moves for a color.
     * @param board the current board.
     * @param myColor the specified color.
     * @return an ArrayList of all possible moves for the specified color. */
    private ArrayList<Move>[] possibleMoves(Board board, PieceState myColor) {
        ArrayList<Move>[] movesArray = new ArrayList[2];
        movesArray[0] = new ArrayList<>();
        movesArray[1] = new ArrayList<>();

        for (char row = '7'; row >= '1'; row--) {
            for (char col = 'a'; col <= 'g'; col++) {
                int index = Board.index(col, row);
                if (board.getContent(index) == myColor) {
                    ArrayList<Move> addMoves = assistPossibleMoves(board, row, col);
                    movesArray[0].addAll(addMoves);
                }else if(board.getContent(index) == myColor.opposite()) {
                    ArrayList<Move> addMoves = oppositePossibleMoves(board, row, col);
                    movesArray[1].addAll(addMoves);
                }
            }
        }
        return movesArray;
    }

    /** Returns an Arraylist of legal moves.
     * @param board the board for testing
     * @param row the row coordinate of the center
     * @param col the col coordinate of the center */
    private ArrayList<Move>
        assistPossibleMoves(Board board, char row, char col) {
        ArrayList<Move> assistPossibleMoves = new ArrayList<>();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 || j != 0) {
                    char row2 = (char) (row + j);
                    char col2 = (char) (col + i);
                    Move currMove = Move.move(col, row, col2, row2);
                    if (board.moveLegal(currMove)) {
                        assistPossibleMoves.add(currMove);
                    }
                }
            }
        }
        return assistPossibleMoves;
    }

    /** Returns an Arraylist of legal moves.
     * @param board the board for testing
     * @param row the row coordinate of the center
     * @param col the col coordinate of the center */
    private ArrayList<Move>
    oppositePossibleMoves(Board board, char row, char col) {
        ArrayList<Move> oppositePossibleMoves = new ArrayList<>();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 || j != 0) {
                    char row2 = (char) (row + j);
                    char col2 = (char) (col + i);
                    Move currMove = Move.move(col, row, col2, row2);
                    if (oppositeMoveLegal(currMove, board)) {
                        oppositePossibleMoves.add(currMove);
                    }
                }
            }
        }
        return oppositePossibleMoves;
    }

    /** Return true if MOVE is legal on the current board. */
    private boolean oppositeMoveLegal(Move move, Board board) {
        if (move == null || board == null) {
            return false;
        } else{
            if (move.isPass()) {
                return !board.couldMove(board.nextMove());
            } else if (move.col1() < 'a'
                    || move.col1() > 'g'
                    || move.row1() < '1'
                    || move.row1() > '7') {
                return false;
            }
            PieceState curState = board.getContent(move.fromIndex());
            PieceState destState = board.getContent(move.toIndex());
            if (curState != board.nextMove().opposite()) {
                return false;
            }

            return destState == EMPTY;
        }
    }
}
