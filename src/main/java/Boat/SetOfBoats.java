package Boat;

public class SetOfBoats {
    private static final int[] lengths = {5, 4, 3, 3, 2};
    private static final String[] names = {"Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"};
    private final Boat[] set;

    public static class Boat {
        private final String name;
        private final int length;
        private boolean floating;

        public Boat(int index) {
            name = names[index];
            length = lengths[index];
            floating = true;
        }

        public void setDrowned(boolean toucheCoule) {
            floating = !toucheCoule;
        }

        public boolean isFloating() {
            return floating;
        }

        public int getLength() {
            return length;
        }

        public String getName() {
            return name;
        }
    }
    public SetOfBoats() {
        int n = lengths.length;
        set = new Boat[n];
        for (int i = 0; i < n; i++) {
            set[i] = new Boat(i);
        }
    }

    public int numberOfBoats() {
        return lengths.length;
    }

    public static String[] getNames() {
        return names;
    }

    public Boat[] getSet() {
        return set;
    }

    public static int[] getLengths() {
        return lengths;
    }

    public boolean hasLost() {
        for (Boat b: set) {
            if (b.floating) {
                return false;
            }
        }
        return true;
    }
}
