public class MultiSP
{
    private DirectedEdge[] edgeTo;
    private double[] distTo;
    private Topological top;
    private Iterable<Integer> source;
    private Iterable<Integer> dests;
    private int closest = -1;

    public MultiSP(EdgeWeightedDigraph g, Iterable<Integer> s,
                   Iterable<Integer> d) {
        source = s;
        dests  = d;
        top    = new Topological(g);
        edgeTo = new DirectedEdge[g.V()];
        distTo = new double[g.V()];

        // for (int v : top.order())
        //     StdOut.printf("%d\t", v);
        // StdOut.printf("\n");
        for (int i = 0; i < g.V(); i++)
            distTo[i] = Double.POSITIVE_INFINITY;
        for (int i : source) distTo[i] = 0;
        // distTo[s] = 0;

        for (int v : top.order()) relax(g, v);
    }

    private void relax(EdgeWeightedDigraph g, int v) {
        // StdOut.printf("relaxing vertex %d with dist %f\n", v, distTo[v]);
        for (DirectedEdge e : g.adj(v)) {
            int w = e.to();
            // StdOut.printf("considering %d -> %d: dist %f, weight %f, new %f...",
            //               v, w, distTo[w], e.weight(), distTo[v] + e.weight());

            if (distTo[w] > distTo[v] + e.weight()) {
                // StdOut.printf("eligible\n",
                //               distTo[w], distTo[v], e.weight());
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
            }
            // else
            //     StdOut.printf("not eligible\n");
        }
    }

    public int shortestDest() {
        double dist = Double.POSITIVE_INFINITY;
        for (int i : dests) {
            if (distTo[i] < dist) {
                dist = distTo[i];
                closest = i;
            }
        }
        return closest;
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        if (closest < 0) shortestDest();
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.push(e);
        return path;
    }
    
    public static void main(String[] args) {
    }
}
