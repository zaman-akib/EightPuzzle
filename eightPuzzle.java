import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashSet;

public class EightPuzzle {

    public static void main(String[] args) {

        int start[][] = {{1, 2, 3}, {4, 0, 6}, {7, 5, 8}};
        int goal[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
       
        State.goal = goal;
        State s1 = new State(start);

        Solver s = new Solver();
        s.A_star(s1);

    }

}
class Solver {

    PriorityQueue<State> open_list;
    HashSet<String> closed_list;

    Solver() {
        open_list = new PriorityQueue<>(new StateComparator());
        closed_list = new HashSet<String>();
    }

    private class StateComparator implements Comparator<State> {

        @Override
        public int compare(State t, State t1) {
            // sorting f value in ascending order 
            if (t.f > t1.f) {
                return 1;
            } else if (t.f < t1.f) {
                return -1;
            } else {
                return 0;
            }

        }
    }

    private void printPath(State s) {
        if (s == null) {
            return;
        }
        printPath(s.parent);
        System.out.println(s.toString());
        return;
    }

    public boolean A_star(State start) {
        start.g = 0;
        start.f = start.g + start.h_score();

        open_list.add(start);
        while (!open_list.isEmpty()) {
            State current = open_list.poll();

            if (current.isGoal()) {

                System.out.println("Total Steps taken: " + current.g + "\n");
                printPath(current);

                return true;
            }

            closed_list.add(current.toString());

            for (State neighbor : current.generateNextState()) {
                if (closed_list.contains(neighbor.toString())) {
                    continue;
                }

                neighbor.f = neighbor.g + neighbor.h_score();

                if (open_list.contains(neighbor) == false) {
                    open_list.add(neighbor);
                }// else { If path cost is considered different then implement this part }

            }
        }

        return false;
    }
}


class State {

    static int goal[][];
    int board[][];

    int g;
    int f;
    State parent;

    State() {

        parent = null;
        board = new int[3][3];

    }

    public State(State b) {

        parent = null;
        g = b.g;
        board = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = b.board[i][j];
            }
        }
    }

    public State(int[][] blocks) {

        parent = null;
        board = new int[3][3];
        g = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = blocks[i][j];
            }
        }

    }

    public int h_score() {                   // hamming distance: number of blocks out of place
        int distance = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != goal[i][j]) {
                    distance++;
                }
            }
        }
        return distance;
    }

  

    public boolean isGoal() {                // is this board the goal board?
        if (h_score() > 0) {
            return false;
        } else {
            return true;
        }
    }

   

    public State[] generateNextState() {
        State[] neighbors = new State[4];

        for (int i = 0; i < 4; i++) {
            neighbors[i] = new State(this);
            neighbors[i].parent = this;
            neighbors[i].g = this.g + 1;
        }

        int a;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (board[i][j] == 0) {
                    //moving blank tiles to left
                    if (j > 0) {
                        a = neighbors[0].board[i][j - 1];
                        neighbors[0].board[i][j - 1] = neighbors[0].board[i][j];
                        neighbors[0].board[i][j] = a;
                    }

                    //moving blank tiles to right
                    if (j < 2) {
                        a = neighbors[1].board[i][j + 1];
                        neighbors[1].board[i][j + 1] = neighbors[1].board[i][j];
                        neighbors[1].board[i][j] = a;
                    }

                    //moving blank tiles to top
                    if (i > 0) {
                        a = neighbors[2].board[i - 1][j];
                        neighbors[2].board[i - 1][j] = neighbors[2].board[i][j];
                        neighbors[2].board[i][j] = a;
                    }

                    //moving blank tiles to bottom
                    if (i < 2) {
                        a = neighbors[3].board[i + 1][j];
                        neighbors[3].board[i + 1][j] = neighbors[3].board[i][j];
                        neighbors[3].board[i][j] = a;
                    }

                }

            }

        }

        return neighbors;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                s += board[i][j] + " ";
            }
            s += "\n";
        }
        return s;
    }

}