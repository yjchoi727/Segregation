public class House {
    private myType type; // type
    private State state; // state
    private House[] neighbors; // neighbors

    // constructor
    public House(myType type) {
        this.type = type;
    }

    // neighbors (N, E, W, S, NE, NW, SE, SW)
    public void setNeighbors(House[] neighbors) {
        this.neighbors = neighbors;
    }

    // update state
    // unhappy if # of neighbors is 0 or # of alike < # of neighbors * tolerance
    // unhappy if diversity is true and all the neighbors(>1) are like this
    public void updateState(double tolerance, boolean diversity) {
        {
            int cnt_e = 0; // empty
            int cnt_s = 0; // same
            int cnt_f = 0; // fiil

            for (House house : neighbors) { // for each house in neighbors
                if (house.getType() == myType.EMPTY)
                    cnt_e++;
                else if (house.getType() == myType.HEADS || house.getType() == myType.TAILS)
                    cnt_f++;

                if (house.getType() == type && type != myType.EMPTY)
                    cnt_s++;
            }

            // unhappy if # of neighbors is 0 or # of alike < # of neighbors * tolerance
            if (cnt_s < cnt_f * tolerance || cnt_e == neighbors.length)
                state = State.UNHAPPY;
            else
                state = State.HAPPY;
/*--------------------------------------Diversity------------------------------------- */
            if (diversity) { // diversity is true
                if (cnt_f > 1) { 
                    if(cnt_f == cnt_s) 
                        state = State.UNHAPPY;// when all neighbors(>1) are like this
                }

                else if (cnt_f == 1) { // when only 1 neighbor
                    if(cnt_f == cnt_s)
                        state = State.HAPPY; // same 1 neighbor -> happy
                    else
                        state = State.UNHAPPY; //diff 1 neighbor -> unhappy
                }
            }

            if (type == myType.EMPTY)
                state = State.NONE;
        }
    }

    // move to a new place (swap type and state)
    public void moveTo(House other) {
        myType tmp_Type = null;
        State tmp_State = null;

        tmp_Type = type;
        type = other.type;
        other.type = tmp_Type; // swaping type

        tmp_State = state;
        state = other.state;
        other.state = tmp_State; // swaping state
    }

    // return type
    public myType getType() {
        return type;
    }

    public boolean isEmpty() {
        return type == myType.EMPTY;
    }

    // return state
    public State getState() {
        return state;
    }

    public boolean isHappy() {
        return state == State.HAPPY;
    }

    public boolean isUnhappy() {
        return state == State.UNHAPPY;
    }

    // return true if all neighbors are like this
    public boolean isSegregated() {
        int cnt_s = 0; // same
        int cnt_f = 0; // fiil
        if(this.isEmpty())
            return false;

        for (House house : neighbors) { // for each house in neighbors
            if (house.getType() == myType.HEADS || house.getType() == myType.TAILS)
                cnt_f++;

            if (house.getType() == type && type != myType.EMPTY)
                cnt_s++;
        }
        return cnt_f == cnt_s;
    }

    // return string representation
    // HEAD 0, TAIL #
    public String toString() {
        if (type == myType.HEADS)
            return "0";
        else if (type == myType.TAILS)
            return "#";
        else
            return " ";
    }
}