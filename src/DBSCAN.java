import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by mhwong on 8/10/15.
 */
public class DBSCAN {

    private static String UNCLASSIFIED = "unclassified";
    private static String NOISE = "noise";

    private SetOfPoints setOfNoise;

    private HashMap<Integer, SetOfPoints> clusterList;

    public DBSCAN(String filePath, double Eps, int MinPts) {

        setOfNoise = new SetOfPoints();

        clusterList = new HashMap<>();

        // build set of points from file
        SetOfPoints setOfPoints = build_SetOfPoints_from_file(filePath);

        // run dbscan
        dbscan(setOfPoints, Eps, MinPts);

        // print result
        output_cluster(clusterList);
        System.out.println("Noise");
        System.out.printf("Noise size: %d\n", setOfNoise.size());
        setOfNoise.print();

    }

    public DBSCAN() {
        this("dataset/2.3_Clustering.txt", 1, 3);
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
                    point.coordinate.add(Double.parseDouble(token[i]));
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
            setOfNoise.append(point);
            return false;
        }
        else { // all points in seeds are density-reachable from point
            // remove from previous cluster
            for(int i = 0; i < seeds.size(); i++) {
                if(seeds.get(i).ClId.equals(NOISE)) {
                    setOfNoise.delete(seeds.get(i));
                }
                else if(!seeds.get(i).ClId.equals(UNCLASSIFIED) && clusterList.containsKey(Integer.parseInt(seeds.get(i).ClId))) {
                    SetOfPoints cluster = clusterList.get(Integer.parseInt(seeds.get(i).ClId));
                    cluster.delete(seeds.get(i));
                }
            }
            setOfPoints.changeClIds(seeds, String.valueOf(clusterId));
            if(clusterList.containsKey(clusterId)) {
                SetOfPoints cluster = clusterList.get(clusterId);
                cluster.addAll(seeds);
                clusterList.put(clusterId, cluster);
            }
            else {
                SetOfPoints cluster = new SetOfPoints();
                cluster.addAll(seeds);
                clusterList.put(clusterId, cluster);
            }
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

                            // remove from previous cluster list before any changes
                            if(resultP.ClId.equals(NOISE)) {
                                setOfNoise.delete(resultP);
                            }
                            else if(!resultP.ClId.equals(UNCLASSIFIED) && clusterList.containsKey(Integer.parseInt(resultP.ClId))) {
                                SetOfPoints cluster = clusterList.get(Integer.parseInt(resultP.ClId));
                                cluster.delete(resultP);
                            }
                            setOfPoints.changeClIds(resultP, String.valueOf(clusterId));
                            if(clusterList.containsKey(clusterId)) {
                                SetOfPoints cluster = clusterList.get(clusterId);
                                cluster.append(resultP);
                                clusterList.put(clusterId, cluster);
                            }
                            else {
                                SetOfPoints cluster = new SetOfPoints();
                                cluster.append(resultP);
                                clusterList.put(clusterId, cluster);
                            }
                        }
                    }
                }
                seeds.delete(currentP);
            }
            return true;
        }
    }

    private void output_cluster(HashMap<Integer, SetOfPoints> clusterList) {
        System.out.printf("Total number of clusters: %d\n", clusterList.size());
        System.out.println();
        for(int clId: clusterList.keySet()) {
            if(!clusterList.get(clId).isEmpty()) {
                System.out.printf("Cluster id: %d\n", clId);
                System.out.printf("Cluster size: %d\n", clusterList.get(clId).size());
                clusterList.get(clId).print();
                System.out.println();
            }
        }
        System.out.println();
    }
}
