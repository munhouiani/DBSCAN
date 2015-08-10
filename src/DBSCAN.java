import java.io.*;
import java.util.Set;

/**
 * Created by mhwong on 8/10/15.
 */
public class DBSCAN {

    public DBSCAN(String filePath) {

        // build set of points from file
        SetOfPoints setOfPoints = build_SetOfPoints_from_file(filePath);


    }

    public DBSCAN() {
        this("/home/mhwong/Desktop/dbscan_dataset/1.3_Clustering.txt");
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
}
