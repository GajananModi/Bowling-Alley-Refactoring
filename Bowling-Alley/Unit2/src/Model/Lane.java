package Model;

import persistence.ScoreHistoryDb;

import java.util.*;

import static java.lang.Thread.sleep;

public class Lane extends Observable implements Observer, Runnable {

    private Party party;
    private String winnerName;
    private Pinsetter setter;
    private HashMap scores;
    private boolean partyAssigned;
    private Iterator bowlerIterator;
    private int ball;
    private int bowlIndex;
    private int frameNumber;
    private boolean tenthFrameStrike;
    private int[][] cumulScores;
    private boolean canThrowAgain;
    private int gameNumber;
    private int secondIndex;
    private int maxIndex;
    private Bowler currentThrower;            // = the thrower who just took a throw
    private ScoreCalculator sc;
    private boolean extraThrow;
    private boolean publishedLane = false;
    private GameStateManager gameStateManager;

    /**
     * Lane()
     * <p>
     * Constructs a new lane and starts its thread
     *
     * @pre none
     * @post a new lane has been created and its thread is executing
     */
    public Lane() {
        this.setter = new Pinsetter();
        this.setter.addObserver(this);
        this.scores = new HashMap();
        this.gameStateManager = new GameStateManager();
        this.gameStateManager.setStateObject(State.RUNNING);
        this.partyAssigned = false;
        this.gameNumber = 0;
        (new Thread(this, "Lane Thread")).start();
    }

    /**
     * run()
     * <p>
     * entry point for execution of this lane
     */
    public void run() {
        while (true) {
            if (partyAssigned && !gameStateManager.getState().getState().equals(State.FINISHED)) {    // we have a party on this lane, so next bower can take a throw
                while (gameStateManager.getState().getState().equals(State.HALTED)) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                    }
                }
                if (bowlerIterator.hasNext()) {
                    currentThrower = (Bowler) bowlerIterator.next();

                    if (!publishedLane) {
                        publishedLane = true;
                        publish();
                    }
                    canThrowAgain = true;
                    if (frameNumber == 0) {
//						System.out.println(currentThrower.getNick());
                    }
                    if ((frameNumber == 10 && bowlIndex != secondIndex) || (frameNumber > 10 && !(bowlIndex == secondIndex || bowlIndex == maxIndex))) {
//						System.out.println("Here for index:");
//						System.out.println(bowlIndex);
                        canThrowAgain = false;
                    }
                    tenthFrameStrike = false;
                    ball = 0;
                    while (canThrowAgain) {
                        try {
                            sleep(10);
                        } catch (Exception e) {
                        }
                    }
                    if (frameNumber == 9 && bowlIndex == 0 && !bowlerIterator.hasNext()) {
                        try {
                            Date date = new Date();
                            String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth() + "/" + date.getDay() + "/" + (date.getYear() + 1900);
                            ScoreHistoryDb.addScore(currentThrower.getNick(), dateString, new Integer(cumulScores[bowlIndex][9]).toString());
                        } catch (Exception e) {
                            System.err.println("Exception in addScore. " + e);
                        }
                    } else if (frameNumber == 13) {
                        try {
                            Date date = new Date();
                            String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth() + "/" + date.getDay() + "/" + (date.getYear() + 1900);
                            ScoreHistoryDb.addScore(currentThrower.getNick(), dateString, new Integer(cumulScores[bowlIndex][9]).toString());
                        } catch (Exception e) {
                            System.err.println("Exception in addScore. " + e);
                        }
                    }
                    setter.reset();
                    bowlIndex++;
                } else {
                    frameNumber++;
                    resetBowlerIterator();
                    bowlIndex = 0;
                    if (frameNumber == 11) {
                        int newHigh = new Integer(cumulScores[secondIndex][10]);
                        int maxPrev = new Integer(cumulScores[maxIndex][9]);
//						System.out.println("New high");
//						System.out.println(newHigh);
//						System.out.println(secondIndex);
                        if (newHigh < maxPrev) {
                            currentThrower = (Bowler) bowlerIterator.next();
                            while (bowlIndex != maxIndex) {
                                currentThrower = (Bowler) bowlerIterator.next();
                                bowlIndex++;
                            }
                            winnerName = currentThrower.getNick();
                            publish();
                            gameStateManager.setStateObject(State.FINISHED);
                            gameNumber++;
                        }
                    }
                    if (frameNumber == 14) {
                        int highest = new Integer(cumulScores[maxIndex][13]);
                        currentThrower = (Bowler) bowlerIterator.next();
                        if (new Integer(cumulScores[secondIndex][13]) > highest) {
                            while (bowlIndex != secondIndex) {
                                currentThrower = (Bowler) bowlerIterator.next();
                                bowlIndex++;
                            }
                            winnerName = currentThrower.getNick();
                        } else if (new Integer(cumulScores[secondIndex][13]) < highest) {
                            while (bowlIndex != maxIndex) {
                                currentThrower = (Bowler) bowlerIterator.next();
                                bowlIndex++;
                            }
                            winnerName = currentThrower.getNick();
                        } else {
                            int[] checkScore;
                            int strikecount1 = 0;
                            int strikecount2 = 0;
                            while (bowlIndex != maxIndex) {
                                currentThrower = (Bowler) bowlerIterator.next();
                                bowlIndex++;
                            }
                            checkScore = (int[]) scores.get(currentThrower);
                            winnerName = currentThrower.getNick();
                            for (int x = 0; x < 28; x++) {
                                if (x == 19 && checkScore[x] == 10)
                                    strikecount1++;
                                else if (x % 2 == 0 && checkScore[x] == 10)
                                    strikecount1++;
                            }

                            resetBowlerIterator();
                            bowlIndex = 0;
                            currentThrower = (Bowler) bowlerIterator.next();
                            while (bowlIndex != secondIndex) {
                                currentThrower = (Bowler) bowlerIterator.next();
                                bowlIndex++;
                            }
                            checkScore = (int[]) scores.get(currentThrower);
                            for (int x = 0; x < 28; x++) {
                                if (x == 19 && checkScore[x] == 10)
                                    strikecount2++;
                                else if (x % 2 == 0 && checkScore[x] == 10)
                                    strikecount2++;
                            }
                            if (strikecount2 > strikecount1)
                                winnerName = currentThrower.getNick();
                        }
                        publish();
                        gameStateManager.setStateObject(State.FINISHED);
                        gameNumber++;
                    }
                    if (frameNumber == 10) {
                        int maxScore = new Integer(cumulScores[bowlIndex][9]);
                        maxIndex = 0;
                        secondIndex = 0;
                        int second = 0;
                        currentThrower = (Bowler) bowlerIterator.next();
                        if (!bowlerIterator.hasNext()) {
                            winnerName = currentThrower.getNick();
                            publish();
                            gameStateManager.setStateObject(State.FINISHED);
                            gameNumber++;
                        } else {
                            int current;
                            while (bowlerIterator.hasNext()) {
                                currentThrower = (Bowler) bowlerIterator.next();
                                bowlIndex++;
                                current = new Integer(cumulScores[bowlIndex][9]);
                                if (current > maxScore) {
//									System.out.println("In c1");
                                    second = maxScore;
                                    maxScore = current;
                                    secondIndex = maxIndex;
                                    maxIndex = bowlIndex;
                                } else if (current > second || current == maxScore) {
                                    second = current;
                                    secondIndex = bowlIndex;
                                }
                            }
                            resetBowlerIterator();
                        }
                        bowlIndex = 0;
                        resetBowlerIterator();
                    }
                }
            } else if (partyAssigned && gameStateManager.getState().getState().equals(State.FINISHED)) {
                publish();
            }
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
    }

    public void throwBall(int score) {
        setter.ballThrown(score, extraThrow);        // simulate the thrower's ball hiting
        ball++;

    }

    public void clearLane() {
        party = null;
        partyAssigned = false;
        publishedLane = false;
    }

    /**
     * resetBowlerIterator()
     * <p>
     * sets the current bower iterator back to the first bowler
     *
     * @pre the party as been assigned
     * @post the iterator points to the first bowler in the party
     */
    public void resetBowlerIterator() {
        bowlerIterator = (party.getMembers()).iterator();
    }

    /**
     * resetScores()
     * <p>
     * resets the scoring mechanism, must be called before scoring starts
     *
     * @pre the party has been assigned
     * @post scoring system is initialized
     */
    public void resetScores() {
        Iterator bowlIt = (party.getMembers()).iterator();
        while (bowlIt.hasNext()) {
            int[] toPut = new int[32];
            for (int i = 0; i != 32; i++) {
                toPut[i] = -1;
            }
            scores.put(bowlIt.next(), toPut);
        }
        gameStateManager.setStateObject(State.RUNNING);
        frameNumber = 0;
    }

    /**
     * assignParty()
     * <p>
     * assigns a party to this lane
     *
     * @param theParty Party to be assigned
     * @pre none
     * @post the party has been assigned to the lane
     */
    public void assignParty(Party theParty) {
        party = theParty;
        resetBowlerIterator();
        partyAssigned = true;
        cumulScores = new int[party.getMembers().size()][14];
        this.sc = new ScoreCalculator(cumulScores);
        gameNumber = 0;
        resetScores();
    }

    /**
     * markScore()
     * <p>
     * Method that marks a bowlers score on the board.
     *
     * @param Cur   The current bowler
     * @param frame The frame that bowler is on
     * @param ball  The ball the bowler is on
     * @param score The bowler's score
     */
    private void markScore(Bowler Cur, int frame, int ball, int score) {
        int[] curScore;
        int index = ((frame - 1) * 2 + ball);
        if (frame == 11) {
            index = 22;
        }
        curScore = (int[]) scores.get(Cur);
        curScore[index - 1] = score;
//		if (frame == 11){
//			System.out.println("ayyyylmaqo");
//			for (int i = 0; i < curScore.length; i++) {
//				System.out.println(curScore[i]);
//			}
//		}
        scores.put(Cur, curScore);
        sc.calculateGame((int[]) this.scores.get(Cur), this.bowlIndex, this.frameNumber, ball, score);
        publish();
    }

    public int[][] getFinalScores() {
        return (cumulScores);
    }

    public int getGameNumber() {
        return (this.gameNumber);
    }

    /**
     * isPartyAssigned()
     * <p>
     * checks if a party is assigned to this lane
     *
     * @return true if party assigned, false otherwise
     */
    public boolean isPartyAssigned() {
        return partyAssigned;
    }

    /**
     * isGameFinished
     *
     * @return true if the game is done, false otherwise
     */
    public boolean isGameFinished() {
        return gameStateManager.getState().getState().equals(State.FINISHED);
    }

    public void publish() {
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Pause the execution of this game
     */
    public void pauseGame() {
        gameStateManager.setStateObject(State.HALTED);
        publish();
    }

    /**
     * Resume the execution of this game
     */
    public void unPauseGame() {
        gameStateManager.setStateObject(State.RUNNING);
        publish();
    }

    /**
     * @return the party
     */
    public Party getParty() {
        return party;
    }

    public String getWinner() {
        return winnerName;
    }

    /**
     * @return the scores
     */
    public HashMap getScores() {
        return scores;
    }

    /**
     * @return the gameIsHalted
     */
    public boolean isGameIsHalted() {
        return gameStateManager.getState().getState().equals(State.HALTED);
    }

    /**
     * @return the ball
     */
    public int getBall() {
        return ball;
    }

    /**
     * @return the bowlIndex
     */
    public int getBowlIndex() {
        return bowlIndex;
    }

    /**
     * @return the frameNumber
     */
    public int getFrameNumber() {
        return frameNumber;
    }

    /**
     * @return the cumulScores
     */
    public int[][] getCumulScores() {
        return cumulScores;
    }

    /**
     * @return the currentThrower
     */
    public Bowler getCurrentThrower() {
        return currentThrower;
    }

    /**
     * Assessor to get this Lane's pinsetter
     *
     * @return A reference to this lane's pinsetter
     */
    public Pinsetter getPinsetter() {
        return setter;
    }

    @Override
    public void update(Observable o, Object arg) {
        PinsetterEvent pe = (PinsetterEvent) arg;
        if (pe.pinsDownOnThisThrow() < 0) { // this is not a real throw
            return;
        }
        markScore(currentThrower, frameNumber + 1, pe.getThrowNumber(), pe.pinsDownOnThisThrow());
        // next logic handles the ?: what conditions dont allow them another throw?
        // handle the case of 10th frame first
        if (frameNumber == 9) {
            if (pe.totalPinsDown() == 10) {
                setter.resetPins();
                if (pe.getThrowNumber() == 1) {
                    tenthFrameStrike = true;
                }
            }
            if ((pe.totalPinsDown() != 10) && (pe.getThrowNumber() == 2 && tenthFrameStrike == false) || pe.getThrowNumber() == 3) {
                canThrowAgain = false;
            }
        } else if (pe.pinsDownOnThisThrow() == 10 || pe.getThrowNumber() == 2 || (frameNumber == 10 && pe.getThrowNumber() == 1)) {        // threw a strike
//			System.out.println("dsgfGFDSA");
//			System.out.println(frameNumber);
            canThrowAgain = false;
        }
    }
}