import java.util.ArrayList;

/**
 * Created by mhwong on 8/10/15.
 */
public class SetOfPoints {
    ArrayList<Point> pointsList;    // a group of points

    public SetOfPoints() {
        this.pointsList = new ArrayList<>();
    }

    public int size() {
        return this.pointsList.size();
    }

    public void changeClIds(Point point, String ClId) {
        if(this.pointsList.contains(point)) {
            point.ClId = ClId;
        }
        else {
            // debug message
            System.out.println("From ChangeClIds: doesn't contain point " + point.id);
        }
    }

    public void changeClIds(SetOfPoints setOfPoints, String ClId) {
        for(int i = 0; i < setOfPoints.size(); i++) {
            if(this.pointsList.contains(setOfPoints.get(i))) {
                setOfPoints.get(i).ClId = ClId;
            }
            else {
                // debug message
                System.out.println("From ChangeClIds(setofpoints): doesn't contain point " + setOfPoints.get(i).id);
            }
        }
    }

    public void delete(Point point) {
        if(this.pointsList.contains(point)) {
            pointsList.remove(point);
        }
        else {
            // debug message
            System.out.println("From delete: doesn't contain point " + point.id);
        }
    }

    public void append(Point point) {
        this.pointsList.add(point);
    }

    private double calculate_distance(Point point1, Point point2) {
        double distance = 0.0;
        for(int i = 0; i < point1.coordinate.size(); i++) {
            distance += (point1.coordinate.get(i) - point2.coordinate.get(i))
                    * (point1.coordinate.get(i) - point2.coordinate.get(i));
        }
        distance = Math.sqrt(distance);
        return distance;
    }

    public SetOfPoints regionQuery(Point point, double Eps) {
        SetOfPoints setOfPoints = new SetOfPoints();
        for(Point point1: this.pointsList) {
            double distance = calculate_distance(point, point1);
            if(distance >= Eps) {
                setOfPoints.append(point1);
            }
        }

        return setOfPoints;
    }

    public Point get(int i) {
        return pointsList.get(i);
    }

    public boolean isEmpty() {
        return pointsList.isEmpty();
    }

    public Point first() {
        return pointsList.get(0);
    }

}
