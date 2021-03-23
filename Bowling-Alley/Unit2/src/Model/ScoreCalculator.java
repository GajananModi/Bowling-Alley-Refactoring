package Model;


public class ScoreCalculator {

    private int[][] cumulScores;

    public ScoreCalculator(int[][] scores) {
        this.cumulScores = scores;  // Operate on Lane's reference
    }

    protected void calculateGame(int[] bowlersScores, int bowlerIndex, int frameNumber, int roll, int pinsDown) {
        int currentRoll = (2 * frameNumber) + roll;
        if(frameNumber == 10){
            currentRoll = (2 * frameNumber) + roll + 1;
        }

        // Reset the previous record of scores
        for (int i = 0; i < cumulScores[bowlerIndex].length; i++) {
            cumulScores[bowlerIndex][i] = 0;
        }
        int frameIndex;
        int prevScore = 0;
        int penalty = 0;
        int flag = 0;

        for (int i = 0; i < currentRoll; i++) {
            boolean even = i % 2 == 0;

            // Check Strike
            if (even && bowlersScores[i] == 10 && bowlersScores[i + 1] == -1) { // If all ten are down, and the roll we are on is even (starting at zero)
                prevScore += strike(i, bowlersScores);
            }
            // Check Spare
            else if (!even && (bowlersScores[i - 1] + bowlersScores[i] == 10) && i!=21) { // If the roll we are on an odd roll, and the past two rolls add to ten
                prevScore += spare(i, bowlersScores);
            }
            // Check if Normal
            else if (bowlersScores[i] >= 0) { // Only display after the second throw of the frame
                if (!even || i == 20) {
                    prevScore += normal(i, bowlersScores);
                }
            }
//            System.out.println("Turn " + i + ", " + bowlerIndex + ": " + bowlersScores[i]);
//            System.out.println(frameNumber);
            if (i > 1) {
                penalty = calculatePenalty(i, bowlersScores);
                prevScore = prevScore - penalty;

            }
            if (i == 2 && bowlersScores[0] == 0 && bowlersScores[1] == 0) {
                penalty = bowlersScores[2] / 2;
                prevScore = prevScore - penalty;
            }
            frameIndex = i / 2;
            if (i == 20) { // Last frame could have three throws
                frameIndex = 9;
            }
            this.cumulScores[bowlerIndex][frameIndex] = prevScore;
            if(i>=22 && bowlersScores[21]>=0){
                this.cumulScores[bowlerIndex][frameIndex] = prevScore - bowlersScores[21];
            }
        }
    }

    private int strike(int index, int[] currentScores) {
        if ((index > 17 && index<=21) || index>25) {
            return (10);
        }
        int count = 0;
        int scoreSum = 10; // Start at ten since it was a strike
        for (int i = 1; i <= 4; i++) { // If a strike is thrown, then also add the next two rolls
            if (currentScores[index + i] >= 0 && count < 2) { // Add only the next two valid scores
                scoreSum += currentScores[index + i];
                count++;
            }
        }
        return scoreSum;
    }

    private int spare(int index, int[] currentScores) {
        if ((index > 17 && index<=21) || index>25) {
            return (10);
        }
        int count = 0;
        int scoreSum = 10; // Start at ten since it was a strike
        for (int i = 1; i <= 4; i++) { // If a strike is thrown, then also add the next two rolls
            if (currentScores[index + i] >= 0 && count < 1) { // Add only the next two valid scores
                scoreSum += currentScores[index + i];
                count++;
            }
        }
        return scoreSum;
    }

    private int normal(int index, int[] currentScores) {
        if (index % 2 == 1 && currentScores[index - 1] >= 0 && index!=21) {
            return (currentScores[index] + currentScores[index - 1]);
        }
        return (currentScores[index]);
    }

    private int calculatePenalty(int index, int[] currentScores) {
        if (currentScores[index] == currentScores[index - 1] && currentScores[index] == 0) {
            int max = currentScores[0];
            for (int i = 0; i < index - 1; i++)
                if (currentScores[i] > max) {
                    max = currentScores[i];
                }
            return max / 2;
        }
        return 0;
    }
}
