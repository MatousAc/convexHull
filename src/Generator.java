import java.util.ArrayList;
import java.util.Random;
import enums.*;

public class Generator {
	private static Random rand = new Random();

	/**
	 * Generates points on canvas in a radial, rectangular,
	 * or circular pattern. restricts it to the
	 * screen size when in visual mode.
	 */
	public static void generateProblem(int n) {
		Core.unsolve();
		ArrayList<Point> dest;
		dest = (Core.isCH()) ? HullBase.points: CoverBase.vertices;
		
		if (!Core.isAuto() && Core.genFx == GenFx.RADIAL) {
			radialScreen(n, dest);
		} else if (!Core.isAuto() && Core.genFx == GenFx.RECTANGULAR) {
			rectangularScreen(n, dest);
		} else if (!Core.isAuto() && Core.genFx == GenFx.CIRCULAR) {
			circularScreen(n, dest);
		} else if (Core.isAuto() && Core.genFx == GenFx.RADIAL) {
			radialAuto(n, dest);
		} else if (Core.isAuto() && Core.genFx == GenFx.RECTANGULAR) {
			rectangularAuto(n, dest);
		} else if (Core.isAuto() && Core.genFx == GenFx.CIRCULAR) {
			circularAuto(n, dest);
		}

		if (!Core.isCH()) {
			genEdges(CoverBase.vertices, CoverBase.edges);
		}
	}

	public static void generateProblem() { generateProblem(Core.genSize); }

	public static void radialScreen(int n, ArrayList<Point> dest) {
		Canvass c = Core.canvass;
		// set min && max
		Point midPoint = new Point(c.getWidth() / 2, c.getHeight() / 2);
		int maxRad = (int) (Math.min(c.getWidth() * 0.95, c.getHeight() * 0.95) / 2);
		
		// generate points using a midpoint and radius
		for (int i = 0; i < n; i++) {
			double angle = rand.nextDouble(Math.PI * 2);
			double radius = Math.max(rand.nextInt(maxRad), rand.nextInt(maxRad));
			int x = (int) (Math.cos(angle) * radius + midPoint.x);
			int y = (int) (Math.sin(angle) * radius + midPoint.y);
			dest.add(new Point(x, y));
		}
	}

	public static void rectangularScreen(int n, ArrayList<Point> dest) {
		int width = Core.canvass.getWidth();
		int height = Core.canvass.getHeight();
		for (int i = 0; i < n; i++) {
			int x = rand.nextInt((int) (width * 0.96));
			int y = rand.nextInt((int) (height * 0.96));
			x += width * 0.02;
			y += height * 0.02;
			dest.add(new Point(x, y));
		}
	}

	public static void circularScreen(int n, ArrayList<Point> dest) {
		Canvass c = Core.canvass;
		Point midPoint = new Point(c.getWidth() / 2, c.getHeight() / 2);
		int radius = (int) (Math.min(c.getWidth() * 0.95, c.getHeight() * 0.95) / 2);
		
		for (int i = 0; i < n; i++) {
			double angle = rand.nextDouble(Math.PI * 2);
			int x = (int) (Math.cos(angle) * radius + midPoint.x);
			int y = (int) (Math.sin(angle) * radius + midPoint.y);
			dest.add(new Point(x, y));
		}
	}

	public static void radialAuto(int n, ArrayList<Point> dest) {
		int halfMax = (int) (Integer.MAX_VALUE / 2.1);
		// I divide by 2.1 to make sure to avoid integer overflow
		Point midPoint = new Point(halfMax, halfMax);
		
		for (int i = 0; i < n; i++) {
			double angle = rand.nextDouble(Math.PI * 2);
			double radius = Math.max(rand.nextInt(halfMax), rand.nextInt(halfMax));
			int x = (int) (Math.cos(angle) * radius + midPoint.x);
			int y = (int) (Math.sin(angle) * radius + midPoint.y);
			dest.add(new Point(x, y));
		}
	}

	public static void rectangularAuto(int n, ArrayList<Point> dest) {
		for (int i = 0; i < n; i++) {
			int x = rand.nextInt() & Integer.MAX_VALUE; // make positive
			int y = rand.nextInt() & Integer.MAX_VALUE;
			dest.add(new Point(x, y));
		}
	}

	public static void circularAuto(int n, ArrayList<Point> dest) {
		int halfMax = (int) (Integer.MAX_VALUE / 2.1);
		Point midPoint = new Point(halfMax, halfMax);
		int radius = halfMax;
		
		for (int i = 0; i < n; i++) {
			double angle = rand.nextDouble(Math.PI * 2);
			int x = (int) (Math.cos(angle) * radius + midPoint.x);
			int y = (int) (Math.sin(angle) * radius + midPoint.y);
			dest.add(new Point(x, y));
		}
	}

	public static void genEdges(ArrayList<Point> vertices, ArrayList<Edge> edges) {
		edges.clear(); // first we want to wipe away any past edges
		for (Point u : vertices) {
			for (Point v : vertices) {
				if (u != v && rand.nextDouble() <= Core.density) {
					Edge newEdge = new Edge(u, v);
					if (!Utility.containsDeep(edges, newEdge)) {
						edges.add(newEdge);
					}
				}
			}
		}
	}

}
