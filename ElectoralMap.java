import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ElectoralMap{

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
        public void setDemVotes(int votes){
            demVotes = votes;
        }
        public void setRepVotes(int votes){
            repVotes = votes;
        }
        public void setIndVotes(int votes){
            indVotes = votes;
        }
        public String getName(){
            return name;
        }
        public void setColor(){
            if(demVotes > repVotes && demVotes > indVotes)
                color = StdDraw.BLUE;
            else if(repVotes > demVotes && repVotes > indVotes)
                color = StdDraw.RED;
            else
                color = StdDraw.GREEN;
            }

        }


        public void getGeoData(String fileName, String date) throws Exception {
            double[] bounds = new double[4];
            File inputFile = new File("input/" + fileName + ".txt");
            Scanner inputObject = new Scanner(inputFile);
            String[] firstLine = inputObject.nextLine().split("   "); //1st bounds
            String[] secondLine = inputObject.nextLine().split("   "); //2nd bounds
            bounds[0] = Double.parseDouble(firstLine[0]);
            bounds[1] = Double.parseDouble(firstLine[1]);
            bounds[2] = Double.parseDouble(secondLine[0]);
            bounds[3] = Double.parseDouble(secondLine[1]);
            StdDraw.setCanvasSize(1000, 1000);
            StdDraw.setCanvasSize((((int) bounds[2] - (int) bounds[0]) * 512) / ((int) bounds[3] - (int) bounds[1]), 512);
            StdDraw.setXscale(bounds[0], bounds[2]);
            StdDraw.setYscale(bounds[1], bounds[3]);

            double subRegions = Double.parseDouble(inputObject.nextLine()); //subregions
            while (inputObject.hasNextLine()) {
                ArrayList<Subregion> subs = new ArrayList<>();
                inputObject.nextLine(); // blank line
                String subRegionName = inputObject.nextLine();
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
                        table.get(regionName).put(subRegionName, subs);
                    } else {
                        table.get(regionName).put(subRegionName, subs);
                    }
                } else {
                    HashMap<String, ArrayList<Subregion>> newMap = new HashMap<>();
                    newMap.put(subRegionName, subs);
                    table.put(regionName, newMap);
                }
            }
            inputObject.close();

        }
    public String addVotes(int rep, int dem, int ind){
        if(rep > dem && rep > ind){
            return "r";
        }
        else if(dem > rep && dem > ind){
            return "d";
        }
        else{
            return "i";
        }

    }

    public void getVote(String region, String date) throws Exception{
        for(String biggestRegion: table.keySet()){
            File file = new File("input/" + region + date + ".txt");
            Scanner inputObject = new Scanner(file);
            inputObject.nextLine(); //get rid of first line
            while(inputObject.hasNextLine()) {
                String line = inputObject.nextLine();
                String[] lines = line.split(",");
                String countyName = lines[0];
                for (Subregion subreg : table.get(biggestRegion).get(countyName)) {
                    subreg.setRepVotes(Integer.parseInt(lines[1]));
                    subreg.setDemVotes(Integer.parseInt(lines[2]));
                    subreg.setIndVotes(Integer.parseInt(lines[3]));
                    subreg.setColor();
                }
            }
            inputObject.close();
        }
    }

    public void visualize() {
        for (String state : table.keySet()) {
            for (String county : table.get(state).keySet()) {
                for (Subregion x : table.get(state).get(county)) {
                    StdDraw.setPenColor(x.color);
                    StdDraw.filledPolygon(x.getxCors(), x.getyCors());
                }
            }
        }

    }

    public ElectoralMap(){

    }

    public static void main(String[] args) throws Exception {
        ElectoralMap balls = new ElectoralMap();
        balls.getGeoData("GA","2016");
        balls.getVote("GA","2016");
        balls.visualize();
        System.out.println(table.get("GA").keySet());

        //TAYLOR AND BROOKS COUNTY ARE MISSING
    }
}
