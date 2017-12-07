import sun.tools.tree.DoubleExpression;

import java.io.File;
import java.util.Scanner;

public class EmptyMap {
     public EmptyMap(){

     }

     public void draw(String fileName) throws Exception{
         double[] bounds = new double[4];
         File inputFile = new File("input/" + fileName);
         Scanner inputObject = new Scanner(inputFile);
         String[] firstLine = inputObject.nextLine().split("   "); //1st bounds
         String[] secondLine = inputObject.nextLine().split("   "); //2nd bounds
         bounds[0] = Double.parseDouble(firstLine[0]);
         bounds[1] = Double.parseDouble(firstLine[1]);
         bounds[2] = Double.parseDouble(secondLine[0]);
         bounds[3] = Double.parseDouble(secondLine[1]);
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
                 inputObject.nextLine();
             }
             StdDraw.filledPolygon(xPoints, yPoints);
         }
     }

    public static void main(String[] args) throws Exception {
        EmptyMap a = new EmptyMap();
        a.draw("USA.txt");
    }
}
