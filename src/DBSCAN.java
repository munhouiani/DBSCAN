import java.io.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by mhwong on 8/10/15.
 */
public class DBSCAN {

    private static String UNCLASSIFIED = "unclassified";
    private static String NOISE = "noise";

    private ArrayList<Point> noiseList;

    public DBSCAN(String filePath, double Eps, int MinPts) {

        noiseList = new ArrayList<>();

        // build set of points from file
        SetOfPoints setOfPoints = build_SetOfPoints_from_file(filePath);

        // run dbscan
        dbscan(setOfPoints, Eps, MinPts);

    }

    public DBSCAN() {
        this("/home/mhwong/Desktop/dbscan_dataset/2.3_Clustering.txt", 50, 50);
    }

    private SetOfPoints build_SetOfPoints_from_file(String filePath) {
        SetOfPoints setOfPoints = new SetOfPoints();

        try {
            File file = new File(filePath);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file)));

            String line;
            // skip first two line
            line = bufferedReader.readLine();
            line = bufferedReader.readLine();

            while((line = bufferedReader.readLine()) != null) {
                String[] token = line.split("[, ]+");
                // first token is id
                Point point = new Point(token[0]);
                point.ClId = UNCLASSIFIED;

                // next few tokens are the coordinate
                for(int i = 1; i < token.length; i++) {
                    point.coordinate.add(Integer.parseInt(token[i]));
                }

                setOfPoints.append(point);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return setOfPoints;
    }

    private void dbscan(SetOfPoints setOfPoints, double Eps, int Minpts) {
        int clusterId = 0;
        for(int i = 0; i < setOfPoints.size(); i++) {
            Point point = setOfPoints.get(i);
            if(point.ClId.equals(UNCLASSIFIED)) {
                if(expand_cluster(setOfPoints, point, clusterId, Eps, Minpts)) {
                    clusterId++;
                }
            }
        }
    }

    private boolean expand_cluster(SetOfPoints setOfPoints, Point point, int clusterId, double Eps, int Minpts) {
        SetOfPoints seeds = setOfPoints.regionQuery(point, Eps);
        if(seeds.size() < Minpts) { // no core point
            setOfPoints.changeClIds(point, NOISE);
            noiseList.add(point);
            return false;
        }
        else { // all points in seeds are density-reachable from point
            setOfPoints.changeClIds(seeds, String.valueOf(clusterId));
            seeds.delete(point);
            while(!seeds.isEmpty()) {
                Point currentP = seeds.first();
                SetOfPoints result = setOfPoints.regionQuery(currentP, Eps);

                if(result.size() >= Minpts) {
                    for(int i = 0; i < result.size(); i++) {
                        Point resultP = result.get(i);
                        if(resultP.ClId.equals(UNCLASSIFIED) || resultP.ClId.equals(NOISE)) {
                            if(resultP.ClId.equals(UNCLASSIFIED)) {
                                seeds.append(resultP);
                            }
                            setOfPoints.changeClIds(resultP, String.valueOf(clusterId));
                        }
                    }
                }
                seeds.delete(currentP);
            }
            return true;
        }
    }
}
