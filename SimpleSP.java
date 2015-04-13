public class SimpleSP {
    private double[][] distTo;
    private int[][] edgeTo;
    private double[][] e;
    private int width, height;

    public SimpleSP(double[][] energy) {
        e = energy;
        width  = e.length;
        height = e[0].length;
        distTo = new double[width][height];
        edgeTo = new int[width][height];

        for (int i = 0; i < width; i++)
            distTo[i][0] = energy[i][0];

        for (int i = 0; i < width; i++)
            for (int j = 1; j < height; j++)
                distTo[i][j] = Double.POSITIVE_INFINITY;

        for (int i = 0; i < width; i++) {
            int col = i, row = 0;
            relax(col, row);
            // StdOut.printf("%d %d\n", col, row);
            while (col > 0 && row < (height - 1)) {
                relax(--col, ++row);
                // StdOut.printf("%d %d\n", col, row);
            }
        }

        for (int i = 1; i < height; i++) {
            int row = i;
            int col = width - 1;
            relax(col, row);
            // StdOut.printf("%d %d\n", width - 1, row);
            while (row < (height - 1) && col > 0) {
                relax(--col, ++row);
                // StdOut.printf("%d %d\n", col, row);
            }
        }
    }

    private void relax(int x, int y) {
        if (y < (height - 1)) {
            relax(x, y, x, y + 1);
            if (x > 0)
                relax(x, y, x - 1, y + 1);
            if (x < width - 1)
                relax(x, y, x + 1, y + 1);
        }
    }

    private void relax(int x1, int y1, int x2, int y2) {
        if (distTo[x2][y2] > distTo[x1][y1] + e[x2][y2]) {
            double o = distTo[x2][y2];
            distTo[x2][y2] = distTo[x1][y1] + e[x2][y2];
            edgeTo[x2][y2] = x1;
        }
    }

    public int[] seam() {
        int[] p = new int[height];
        double min = Double.POSITIVE_INFINITY;
        int lastMin = 0;
        for (int i = 0; i < width; i++)
            if (distTo[i][height - 2] < min) {
                min = distTo[i][height - 2];
                lastMin = i;
            }
        p[height - 1] =  lastMin;
        if (height > 1) p[height - 2] = lastMin;
        int i = height - 3;
        while (i >= 0) {
            p[i] = edgeTo[p[i+1]][i+1];
            i--;
        }

        return p;
    }

    public static void main(String[] args) {
        int width = 5;
        int height = 6;
        double[][] e = new double[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                e[i][j] = j * width + i;

        SimpleSP sp = new SimpleSP(e);
    }
}
