import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class BlendedAmerica{

    static HashMap<String, HashMap<String, ArrayList<Subregion>>> table = new HashMap<>();

    static class Subregion {
        private int points;
        private int demVotes;
        private int repVotes;
        private int indVotes;
        private Color color;
        private double[] xCors;
        private double[] yCors;
        private String decision;
        private String name;
        private int r;
        private int g;
        private int b;

        public Subregion(double[] xs, double[] ys, String tname) {
            xCors = xs;
            yCors = ys;
            name = tname;
        }

        public double[] getxCors(){
            return xCors;
        }
        public double[] getyCors(){
            return yCors;
        }
        public int getR(){return r;}
        public int getG(){return g;}
        public int getB(){return b;}
        public void setDemVotes(int votes){
            demVotes = votes;
        }
        public void setRepVotes(int votes){
            repVotes = votes;
        }
        public void setIndVotes(int votes){ indVotes = votes; }
        public String getName(){
            return name;
        }

        public void setColor(){
            double bottom = repVotes+demVotes+indVotes;
            double rPer = 1.0 * (repVotes/bottom);
            double gPer = indVotes/bottom;
            double bPer = demVotes/bottom;
            r = (int)(255 * rPer);
            g = (int)(255 * gPer);
            b = (int)(255 * bPer);
        }

    }


    public void getGeoData(String fileName) throws Exception {
        double[] bounds = new double[4];
        File inputFile = new File("input/" + fileName + ".txt");
        Scanner inputObject = new Scanner(inputFile);
        String[] firstLine = inputObject.nextLine().split("   "); //1st bounds
        String[] secondLine = inputObject.nextLine().split("   "); //2nd bounds
        bounds[0] = Double.parseDouble(firstLine[0]);
        bounds[1] = Double.parseDouble(firstLine[1]);
        bounds[2] = Double.parseDouble(secondLine[0]);
        bounds[3] = Double.parseDouble(secondLine[1]);
        StdDraw.setCanvasSize((((int) bounds[2] - (int) bounds[0]) * 512) / ((int) bounds[3] - (int) bounds[1]), 512);
        StdDraw.setXscale(bounds[0], bounds[2]);
        StdDraw.setYscale(bounds[1], bounds[3]);

        double subRegions = Double.parseDouble(inputObject.nextLine()); //subregions
        for(int x = 0;x<subRegions;x++) {
            ArrayList<Subregion> subs = new ArrayList<>();
            inputObject.nextLine(); // blank line
            String subRegionName = inputObject.nextLine();
            subRegionName = subRegionName.toLowerCase();
            String regionName = inputObject.nextLine();
            int inputs = Integer.parseInt(inputObject.nextLine());
            double[] xPoints = new double[inputs];
            double[] yPoints = new double[inputs];

            for (int i = 0; i < inputs; i++) {
                String[] line = new String[2];
                line = inputObject.nextLine().split("   ");
                double x1 = Double.parseDouble(line[0]);
                xPoints[i] = x1;
                double y1 = Double.parseDouble(line[1]);
                yPoints[i] = y1;
            }
            Subregion s = new Subregion(xPoints, yPoints, subRegionName);
            subs.add(s);
            if (table.containsKey(regionName)) {
                if (table.get(regionName).containsKey(subRegionName)) {
                    table.get(regionName).get(subRegionName).add(s);
                } else {
                    ArrayList<Subregion> subl = new ArrayList<>();
                    subl.add(s);
                    table.get(regionName).put(subRegionName, subl);
                }
            } else {
                HashMap<String, ArrayList<Subregion>> newMap = new HashMap<>();
                ArrayList<Subregion> subList = new ArrayList<>();
                subList.add(s);
                newMap.put(subRegionName, subList);
                table.put(regionName, newMap);
            }
        }
        inputObject.close();

    }

    public void getVote(String date) throws Exception{
        for(String biggestRegion: table.keySet()){
            File file = new File("input/" + biggestRegion + date + ".txt");
            Scanner inputObject = new Scanner(file);
            inputObject.nextLine(); //get rid of first line
            while(inputObject.hasNextLine()) {
                String line = inputObject.nextLine();
                if(line.equals(""))
                    inputObject.close();
                else {
                    String[] lines = line.split(",");
                    String countyName = lines[0];
                    countyName = countyName.toLowerCase();
                    try {
                        if (table.get(biggestRegion).containsKey(countyName + " city")) {
                            for (Subregion subreg : table.get(biggestRegion).get(countyName + " city")) {
                                subreg.setRepVotes(Integer.parseInt(lines[1]));
                                subreg.setDemVotes(Integer.parseInt(lines[2]));
                                subreg.setIndVotes(Integer.parseInt(lines[3]));
                                subreg.setColor();
                            }
                        }
                        if (table.get(biggestRegion).containsKey(countyName + " parish")) {
                            for (Subregion subreg : table.get(biggestRegion).get(countyName + " parish")) {
                                subreg.setRepVotes(Integer.parseInt(lines[1]));
                                subreg.setDemVotes(Integer.parseInt(lines[2]));
                                subreg.setIndVotes(Integer.parseInt(lines[3]));
                                subreg.setColor();
                            }
                        } else {
                            for (Subregion subreg : table.get(biggestRegion).get(countyName)) {
                                subreg.setRepVotes(Integer.parseInt(lines[1]));
                                subreg.setDemVotes(Integer.parseInt(lines[2]));
                                subreg.setIndVotes(Integer.parseInt(lines[3]));
                                subreg.setColor();
                            }
                        }
                    }
                    catch(NullPointerException e) {
                        System.out.println(countyName);
                    }
                }
            }
            inputObject.close();
        }
    }

    public void visualize() {
        for (String state : table.keySet()) {
            for (String county : table.get(state).keySet()) {
                for (Subregion x : table.get(state).get(county)) {
                    int red = x.getR();
                    int green = x.getG();
                    int blue = x.getB();
                    StdDraw.setPenColor(red,green,blue);
                    StdDraw.filledPolygon(x.getxCors(), x.getyCors());
                }
            }
        }
    }
    public static void drawIt(String regionAsked, String year) throws Exception{
        BlendedAmerica frame = new BlendedAmerica();
        frame.getGeoData(regionAsked);
        frame.getVote(year);
        frame.visualize();
    }
    public BlendedAmerica(){

    }

    public static void main(String[] args) throws Exception {
        BlendedAmerica baba = new BlendedAmerica();
        baba.getGeoData(args[0]);
        baba.getVote(args[1]);
        baba.visualize();
    }
}
