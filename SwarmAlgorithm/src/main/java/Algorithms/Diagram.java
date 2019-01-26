package Algorithms;

import Algorithms.Function;
import Algorithms.Particle;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.ArrayList;
import java.util.List;

/*        Diagram diagram = new Diagram(new Function(function));
        List<Point> points = (List<Point>) inputFromServer.readObject();
        diagram.plot(diagram.getFunction(), points, new Range(-1000, 1000), 100);
*/

public class Diagram {


    public Diagram() {
    }

    public void plot(final Mapper function, final Particle[] particles, final Range range, final int precision, int func) {
        // Create a surface drawing that function
        Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(range, precision, range, precision), function);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);
        surface.setWireframeColor(Color.BLACK);

        Coord3d[] cords = convertPointsToCords(particles,func);
        Scatter scatter = new Scatter( cords, Color.RED );
        scatter.setWidth(5f);

        // Create a chart and add the surface
        Chart chart = AWTChartComponentFactory.chart(Quality.Advanced);
        chart.getScene().getGraph().add(surface);
        chart.getScene().add(scatter);
        ChartLauncher.openChart(chart);
    }

    public Coord3d[] convertPointsToCords(final Particle[] particles ,int function) {
        List<Coord3d> cords = new ArrayList<>();
        if (function == 1) {
            for (Particle p : particles) {
                cords.add(new Coord3d(p.position.getX() , p.position.getY(), Function.RosenBrock(p.position.getX(), p.position.getY())));
            }
        }
        if (function == 2) {
        for(Particle p : particles){
            cords.add(new Coord3d(p.position.getX() , p.position.getY(), Function.ackleysFunction(p.position.getX(), p.position.getY())));
        }}

        if (function == 3) {
            for(Particle p : particles){
            cords.add(new Coord3d(p.position.getX() , p.position.getY(),Function.boothsFunction(p.position.getX(), p.position.getY())));
        }}
        if (function == 4) {
            for(Particle p : particles){
            cords.add(new Coord3d(p.position.getX() , p.position.getY(),Function.XsqerYsqer(p.position.getX(), p.position.getY())));
        }}

        Coord3d[] jzyCords = new Coord3d[cords.size()];
        jzyCords = cords.toArray(jzyCords);

        System.out.println("jzy coords: " + jzyCords.length);

        return jzyCords;
    }




}