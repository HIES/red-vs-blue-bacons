import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ElectoralMap{

    private HashMap<String, HashMap<String, ArrayList<Subregion>>> table = new HashMap<>();

    static class Subregion {
        private int points;
        private int demVotes;
        private int repVotes;
        private int indVotes;
        private Color color;
        private double[] xCors;
        private double[] yCors;

        public Subregion(double[] xs, double[] ys) {
            xCors = xs;
            yCors = ys;
        }
    }

        public void getGeoData(String fileName) throws Exception{
            ArrayList<Subregion> subs = new ArrayList<>();
            double[] bounds = new double[4];
            File inputFile = new File("input/" + fileName);
            Scanner inputObject = new Scanner(inputFile);
            String[] firstLine = inputObject.nextLine().split("   "); //1st bounds
            String[] secondLine = inputObject.nextLine().split("   "); //2nd bounds
            bounds[0] = Double.parseDouble(firstLine[0]);
            bounds[1] = Double.parseDouble(firstLine[1]);
            bounds[2] = Double.parseDouble(secondLine[0]);
            bounds[3] = Double.parseDouble(secondLine[1]);
            StdDraw.setCanvasSize(1000,1000);
            StdDraw.setCanvasSize((((int)bounds[2]-(int)bounds[0])*512)/((int)bounds[3]-(int)bounds[1]),512);
            StdDraw.setXscale(bounds[0],bounds[2]);
            StdDraw.setYscale(bounds[1],bounds[3]);

            double subRegions = Double.parseDouble(inputObject.nextLine()); //subregions


            while(inputObject.hasNextLine()){
                inputObject.nextLine(); // blank line
                String subRegionName = inputObject.nextLine();
                String regionName = inputObject.nextLine();
                int inputs = Integer.parseInt(inputObject.nextLine());
                double[] xPoints = new double[inputs];
                double[] yPoints = new double[inputs];

                for(int i = 0;i<inputs;i++){
                    String[] line = new String[2];
                    line = inputObject.nextLine().split("   ");
                    double x1 = Double.parseDouble(line[0]);
                    xPoints[i] = x1;
                    double y1 = Double.parseDouble(line[1]);
                    yPoints[i] = y1;
                }

                Subregion s = new Subregion(xPoints,yPoints);
                subs.add(s);
                StdDraw.filledPolygon(xPoints, yPoints);

                if(table.containsKey(regionName)){
                    if(table.get(regionName).containsKey(subRegionName)){
                        table.get(regionName).get(subRegionName).add(s);
                    }
                }
                else{
                    HashMap<String, ArrayList> newMap = new HashMap<>();
                    newMap.put(subRegionName,subs);
                    table.get(regionName).put(subRegionName,newMap);
                }
            }
            inputObject.close();
            //table.put(regionName, subs);
        }

    public ElectoralMap(){

    }
}
