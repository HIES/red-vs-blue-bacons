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
            for (String key : table.keySet()) {
                for (String subKey : table.get(key).keySet()) {
                    File inputF = new File("input/" + key + date + ".txt");
                    int counter = 0;
                    Scanner inputO = new Scanner(inputF);
                    inputO.nextLine(); //first line of election stuff
                    while (inputO.hasNextLine()) {
                        String line = inputO.nextLine();
                        String[] lines = line.split(",");
                        String nameOf = lines[0];
                        //table.get(key).get(subKey)

                        for (Subregion subreg : table.get(key).get(subKey)) {
                            if (nameOf.equals(subreg.getName())) {
                                int rVotes = Integer.parseInt(lines[1]);
                                int dVotes = Integer.parseInt(lines[2]);
                                int iVotes = Integer.parseInt(lines[3]);
                                table.get(key).get(subKey).get(counter).setDemVotes(dVotes);
                                table.get(key).get(subKey).get(counter).setDemVotes(rVotes);
                                table.get(key).get(subKey).get(counter).setDemVotes(iVotes);
                                String decision = addVotes(rVotes, dVotes, iVotes);
                                System.out.println("about to if");
                                if (decision.equals("r")) {
                                    StdDraw.setPenColor(StdDraw.RED);
                                } else if (decision.equals("d")) {
                                    StdDraw.setPenColor(StdDraw.BLUE);
                                } else {
                                    StdDraw.setPenColor(StdDraw.GREEN);
                                }
                            }
                        }
                    }
                    inputO.close();
                }

                for (String keyd : table.keySet()) {
                    for (String in : table.get(keyd).keySet()) {
                        for (Subregion x : table.get(keyd).get(in)) {
                            StdDraw.filledPolygon(x.getxCors(), x.getyCors());
                        }
                    }
                }
            }
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
    public ElectoralMap(){

    }

    public static void main(String[] args) throws Exception {
        ElectoralMap balls = new ElectoralMap();
        balls.getGeoData("GA","2016");
    }
}
