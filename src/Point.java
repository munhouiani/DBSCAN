import java.util.LinkedList;

/**
 * Created by mhwong on 8/10/15.
 */
public class Point {
    String id;          // point's id
    String ClId;        // which cluster does this point belong to
    LinkedList<Integer> coordinate;   // the coordinate of this point

    public Point(String id, int ... coordinates) {
        this.id = id;
        this.coordinate = new LinkedList<>();
        for (int coordinate1 : coordinates) {
            this.coordinate.add(coordinate1);
        }
    }

    public Point(String id, LinkedList<Integer> coordinate) {
        this.id = id;
        this.coordinate = coordinate;
    }

    public Point(String id) {
        this.id = id;
        this.coordinate = new LinkedList<>();
    }

    public Point() {
        this("");
    }
}
