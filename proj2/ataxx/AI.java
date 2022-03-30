/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.util.Random;

import static ataxx.PieceColor.*;
import static java.lang.Math.min;
import static java.lang.Math.max;
import java.util.ArrayList;

/** A Player that computes its own moves.
 *  @author Tyler Le
 */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 4;
    /** A position magnitude indicating a win (for red if positive, blue
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI for GAME that will play MYCOLOR. SEED is used to initialize
     *  a random-number generator for use in move computations.  Identical
     *  seeds produce identical behaviour. */
    AI(Game game, PieceColor myColor, long seed) {
        super(game, myColor);
        _random = new Random(seed);
    }

    @Override
    boolean isAuto() {
        return true;
    }

    @Override
    String getMove() {
        if (!getBoard().canMove(myColor())) {
            game().reportMove(Move.pass(), myColor());
            return "-";
        }
        Main.startTiming();
        Move move = findMove();
        Main.endTiming();
        game().reportMove(move, myColor());
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(getBoard());
        _lastFoundMove = null;
        if (myColor() == RED) {
            if (b.whoseMove() != RED) {
                b.changePlayer();
            }
            minMax(b, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            if (b.whoseMove() != BLUE) {
                b.changePlayer();
            }
            minMax(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to the findMove method
     *  above. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int minMax(Board board, int depth, boolean saveMove, int sense,
                       int alpha, int beta) {
        /* We use WINNING_VALUE + depth as the winning value so as to favor
         * wins that happen sooner rather than later (depth is larger the
         * fewer moves have been made. */
        if (depth == 0 || board.getWinner() != null) {
            return staticScore(board, WINNING_VALUE + depth);
        }
        Move best;
        best = null;
        int bestScore;
        if (sense == 1) {
            bestScore = -INFTY;
        } else {
            bestScore = INFTY;
        }
        ArrayList<Move> moves = moveGenerator(board);
        for (Move mov: moves) {
            char c = mov.col0();
            char r = mov.row0();
            char nc = mov.col1();
            char nr = mov.row1();
            board.makeMove(c, r, nc, nr);
            Move currMove = board.allMoves().get(board.allMoves().size() - 1);
            int response = minMax(
                    board, depth - 1, false, -1 * sense, alpha, beta);
            board.undo();
            if (sense == 1) {
                if (response > bestScore) {
                    bestScore = response;
                    alpha = max(alpha, bestScore);
                }
            } else {
                if (response < bestScore) {
                    bestScore = response;
                    beta = min(beta, bestScore);
                }
            }
            best = currMove;
            if (alpha >= beta) {
                break;
            }
        }
        if (saveMove) {
            _lastFoundMove = best;
        }
        return bestScore;
    }

    /** Helper for the minMax function.
     * @return return all the legal moves that a player can make.
     * @param board is the current state of the game. */
    private ArrayList<Move> moveGenerator(Board board) {
        ArrayList<Move> moves = new ArrayList<>();
        char[] col = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        char[] row = {'1', '2', '3', '4', '5', '6', '7'};
        for (char c : col) {
            for (char r : row) {
                if (board.get(c, r) != EMPTY) {
                    for (int i = -2; i <= 2; i++) {
                        for (int k = -2; k <= 2; k++) {
                            int ind = board.index(c, r);
                            int destination = board.neighbor(ind, i, k);
                            if (destination != ind
                                    && board.get(destination) == EMPTY) {
                                if (legality(board, c, r, destination)) {
                                    moves.add(moveValue(
                                            board, c, r, destination));
                                }
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    /** Legality finer for the moveGenerator function.
     * @return return the legality of a move.
     * @param board of the current game.
     * @param c of the from column position.
     * @param r of the from row position.
     * @param destination of the to position */
    private boolean legality(Board board, char c, char r, int destination) {
        char[] col = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        char[] row = {'1', '2', '3', '4', '5', '6', '7'};
        for (char nc : col) {
            for (char nr : row) {
                if (board.index(nc, nr) == destination) {
                    if (board.legalMove(c, r, nc, nr)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** Legality finer for the moveGenerator function.
     * @return return the move of the linearized index.
     * @param board of the current game.
     * @param c of the from column position.
     * @param r of the from row position.
     * @param destination of the to position */
    private Move moveValue(Board board, char c, char r, int destination) {
        char[] col = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        char[] row = {'1', '2', '3', '4', '5', '6', '7'};
        for (char nc : col) {
            for (char nr : row) {
                if (board.index(nc, nr) == destination) {
                    return Move.move(c, r, nc, nr);
                }
            }
        }
        return null;
    }

    /** Return a heuristic value for BOARD.  This value is +- WINNINGVALUE in
     *  won positions, and 0 for ties. */
    private int staticScore(Board board, int winningValue) {
        PieceColor winner = board.getWinner();
        if (winner != null) {
            return switch (winner) {
            case RED -> winningValue;
            case BLUE -> -winningValue;
            default -> 0;
            };
        }
        int heuristicRed = board.redPieces();
        int heuristicBlue = board.bluePieces();
        return heuristicRed - heuristicBlue;
    }

    /** Pseudo-random number generator for move computation. */
    private Random _random = new Random();
}
