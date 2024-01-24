import java.util.ArrayList;
import java.util.Collections;

public class Segregation {
    // city
    private House[][] city;
    private int size = 25;

    // construct a city of size x size houses
    // each house is empty w.p emptyRate
    // each occupied house is HEADS w.p 0.5 and TAILS w.p 0.5
    public Segregation(int size, double emptyRate) {
        this.size = size;
        city = new House[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int e = (int) (Math.random() * 100 + 1); // emptyrate... e: 1~100
                /* make empty house w.p emptyRate */
                if (e <= emptyRate * 100) // e<=20
                    city[i][j] = new House(myType.EMPTY);

                else { /* when house is occupied */
                    int p = (int) (Math.random() * 2); // Head or Tail... p: 0~1
                    if (p == 0) // Head
                        city[i][j] = new House(myType.HEADS);
                    else // p == 1 Tail
                        city[i][j] = new House(myType.TAILS);
                }
            }
        }
    } // complete constructing city.

    // update the state of each house
    public void updateState(double tolerance, boolean diversity) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                House[] neighbors = new House[8];
                // todo : set neighbors of each house.
                // neighbors (N, E, W, S, NE, NW, SE, SW)
                if (i == 0 || i == size - 1 || j == 0 || j == size - 1) { // 가장자리
                    if (i == 0 && j == 0) {
                        neighbors[0] = city[size - 1][0];
                        neighbors[1] = city[0][1];
                        neighbors[2] = city[0][size - 1];
                        neighbors[3] = city[1][0];
                        neighbors[4] = city[size - 1][1];
                        neighbors[5] = city[size - 1][size - 1];
                        neighbors[6] = city[1][1];
                        neighbors[7] = city[1][size - 1];
                    }
                    // 꼭짓점1
                    else if (i == size - 1 && j == 0) {
                        neighbors[0] = city[size - 2][0];
                        neighbors[1] = city[size - 1][1];
                        neighbors[2] = city[size - 1][size - 1];
                        neighbors[3] = city[0][0];
                        neighbors[4] = city[size - 2][1];
                        neighbors[5] = city[size - 2][size - 1];
                        neighbors[6] = city[0][1];
                        neighbors[7] = city[0][size - 1];
                    }
                    // 꼭짓점2
                    else if (i == 0 && j == size - 1) {
                        neighbors[0] = city[size - 1][size - 1];
                        neighbors[1] = city[0][0];
                        neighbors[2] = city[0][size - 2];
                        neighbors[3] = city[1][size - 1];
                        neighbors[4] = city[size - 1][0];
                        neighbors[5] = city[size - 1][size - 2];
                        neighbors[6] = city[1][0];
                        neighbors[7] = city[1][size - 2];
                    }
                    // 꼭짓점3
                    else if (i == size - 1 && j == size - 1) {
                        neighbors[0] = city[size - 2][size - 1];
                        neighbors[1] = city[size - 1][0];
                        neighbors[2] = city[size - 1][size - 2];
                        neighbors[3] = city[0][size - 1];
                        neighbors[4] = city[size - 2][0];
                        neighbors[5] = city[size - 2][size - 2];
                        neighbors[6] = city[0][0];
                        neighbors[7] = city[0][size - 2];
                    }
                    // 꼭짓점4
                    else {
                        if (j == 0) {
                            neighbors[0] = city[i - 1][j];
                            neighbors[1] = city[i][1];
                            neighbors[2] = city[i][size - 1];
                            neighbors[3] = city[i + 1][j];
                            neighbors[4] = city[i - 1][j + 1];
                            neighbors[5] = city[i - 1][size - 1];
                            neighbors[6] = city[i + 1][j + 1];
                            neighbors[7] = city[i + 1][size - 1];
                        }
                        // 왼쪽 가장자리
                        else if (i == 0) {
                            neighbors[0] = city[size - 1][j];
                            neighbors[1] = city[i][j + 1];
                            neighbors[2] = city[i][j - 1];
                            neighbors[3] = city[i + 1][j];
                            neighbors[4] = city[size - 1][j + 1];
                            neighbors[5] = city[size - 1][j - 1];
                            neighbors[6] = city[i + 1][j + 1];
                            neighbors[7] = city[i + 1][j - 1];
                        }
                        // 위 가장자리
                        else if (j == size - 1) {
                            neighbors[0] = city[i - 1][j];
                            neighbors[1] = city[i][0];
                            neighbors[2] = city[i][j - 1];
                            neighbors[3] = city[i + 1][j];
                            neighbors[4] = city[i - 1][0];
                            neighbors[5] = city[i - 1][0];
                            neighbors[6] = city[i + 1][0];
                            neighbors[7] = city[i + 1][j - 1];
                        }
                        // 오른쪽 가장자리
                        else if (i == size - 1) {
                            neighbors[0] = city[i - 1][j];
                            neighbors[1] = city[i][j + 1];
                            neighbors[2] = city[i][j - 1];
                            neighbors[3] = city[0][j];
                            neighbors[4] = city[i - 1][j + 1];
                            neighbors[5] = city[i - 1][j - 1];
                            neighbors[6] = city[0][j + 1];
                            neighbors[7] = city[0][j - 1];
                        }
                        // 밑 가장자리
                    } // 꼭짓점 아닌 가장자리
                }

                else if (i != 0 && i != size - 1 && j != 0 && j != size - 1) { // 가장자리X
                    neighbors[0] = city[i - 1][j];
                    neighbors[1] = city[i][j + 1];
                    neighbors[2] = city[i][j - 1];
                    neighbors[3] = city[i + 1][j];
                    neighbors[4] = city[i - 1][j + 1];
                    neighbors[5] = city[i - 1][j - 1];
                    neighbors[6] = city[i + 1][j + 1];
                    neighbors[7] = city[i + 1][j - 1];
                }

                city[i][j].setNeighbors(neighbors); // setNeighbors for every house in city
                city[i][j].updateState(tolerance, diversity); // updateState for every house in city
            }
        }
    }

    // move to a randomly chosen empty house if a house is unhappy
    public void move() {

        ArrayList<House> unhappyHouse = new ArrayList<>();
        ArrayList<House> emptyHouse = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (city[i][j].isUnhappy())
                    unhappyHouse.add(city[i][j]);
            }
        } // add every unhappyHouse in ArrayList

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (city[i][j].isEmpty())
                    emptyHouse.add(city[i][j]);
            }
        } // add every emptyHouse in ArrayList

        Collections.shuffle(emptyHouse);
        Collections.shuffle(unhappyHouse); //shuffle them for moving randomly

        for (House uHouse : unhappyHouse) { // for each uhouse in unhappyHouse 
            for (int i = 0; i < emptyHouse.size(); i++)
                uHouse.moveTo(emptyHouse.get(i)); // unhappyhouse move to random emptyhouse
        }
    }

    // return an array of type counts
    // [HEADS, TAILS, EMPTY]
    public int[] countTypes() {
        int h = 0;
        int t = 0;
        int e = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getTypeAt(i, j) == myType.HEADS)
                    h++; // head
                else if (getTypeAt(i, j) == myType.TAILS)
                    t++; // tail
                else
                    e++; // empty
            }
        }
        int[] countType = new int[3];
        countType[0] = h;
        countType[1] = t;
        countType[2] = e;
        return countType;
    }

    // return an array of state counts
    // [NONE, UNHAPPY, HAPPY]
    public int[] countStates() {
        int n = 0;
        int u = 0;
        int h = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (city[i][j].isHappy())
                    h++; // happy
                else if (city[i][j].isUnhappy())
                    u++; // unhappy
                else
                    n++; // none
            }
        }
        int[] countState = new int[3];
        countState[0] = n;
        countState[1] = u;
        countState[2] = h;
        return countState;
    }

    // return the number of segregated houses
    public int countSegregated() {
        int s = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (city[i][j].isSegregated())
                    s++;
            }
        }
        return s;
    }

    // return the house type at [r, c]
    public myType getTypeAt(int r, int c) {
        return city[r][c].getType();
    }

    // return the house state at [r, c]
    public State getStateAt(int r, int c) {
        return city[r][c].getState();
    }

    // print the types of houses: size x size
    public void printCity() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(city[i][j].toString());
            }
            System.out.println();
        }
    }

    // print the statistics and the types of houses
    // step (empty / unhappy / happy / segregated)
    public void printStep(int n) { // n is setp
        System.out.println("=====" + n + "=====\n" + "(" + countTypes()[2] + " / " + countStates()[1] + " / "
                + countStates()[2] + " / " + countSegregated() + ")");
        printCity();
    }

    public static void main(String[] args) {
        int size = 20;
        double emptyRate = 0.2;
        double tolerance = 0.3;
        boolean diversity = false;
        Segregation sim = new Segregation(size, emptyRate);
        sim.updateState(tolerance, diversity);
        sim.printStep(0);
        for (int n = 0; n < 200; n++) {
            sim.move();
            sim.updateState(tolerance, diversity); // move한 뒤 updatestate를 수동으로 실행하므로 move는 옮기기만 하면 됨
            sim.printStep(n + 1);
            int[] counts = sim.countStates();
            if (counts[State.UNHAPPY.ordinal()] == 0)
                break;
        }
    }
}
