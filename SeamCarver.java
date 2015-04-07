import java.awt.Color;
public class SeamCarver
{
    private Picture pic;
    private double[][] energyOf;
    private EdgeWeightedDigraph dg;
    private int width;
    private int height;

    public SeamCarver(Picture picture) {
        pic = new Picture(picture);
        width = pic.width();
        height = pic.height();
        energyOf = new double[pic.width()][pic.height()];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                if (i == 0 || j == 0 || i == (width - 1) || j == (height - 1))
                    energyOf[i][j] = 195075.0;
                else
                    energyOf[i][j] = -1.0;
        dg = new EdgeWeightedDigraph(width * (height - 1));
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < (height - 2); j++) {
                int s = j * width + i;
                dg.addEdge(new DirectedEdge(s, s + width,
                                           energy(i, j + 1)));
                if (i > 0)
                    dg.addEdge(new DirectedEdge(s, s + width - 1,
                                                energy(i - 1, j + 1)));
                if (i < (width - 1))
                    dg.addEdge(new DirectedEdge(s, s + width + 1,
                                                energy(i + 1, j + 1)));
            }
        }
        Topological top = new Topological(dg);
        for (int v : top.order()) relax(dg, v);
        
    }

    public Picture picture() {
        return pic;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException();
        if (energyOf[x][y] >= 0) return energyOf[x][y];

        energyOf[x][y] = diff(pic.get(x + 1, y), pic.get(x - 1, y))
                       + diff(pic.get(x, y + 1), pic.get(x, y - 1));
        return energyOf[x][y];
    }

    private double diff(Color a, Color b) {
        return (a.getRed()  - b.getRed())   * (a.getRed()   - b.getRed())
            + (a.getGreen() - b.getGreen()) * (a.getGreen() - b.getGreen())
            + (a.getBlue()  - b.getBlue())  * (a.getBlue()  - b.getBlue());
    }

    public int[] findHorizontalSeam() {
        return new int[1];
    }

    public int[] findVerticalSeam() {
        return new int[1];
    }

    public void removeHorizontalSeam(int[] seam) {
    }

    public void removeVerticalSeam(int[] seam) {
    }

    // public static void main(String[] args) {
    //     if (args.length < 1 || args[0] == null)
    //         throw new IllegalArgumentException("Need picture file name");
    //     SeamCarver sc = new SeamCarver(new Picture(args[0]));
    //     StdOut.printf("%s has width %d and height %d\n", args[0], sc.width(),
    //                   sc.height());
    // }
    public static void main(String[] args)
    {
        Picture inputImg = new Picture(args[0]);
        System.out.printf("image is %d pixels wide by %d pixels high.\n",
                          inputImg.width(), inputImg.height());
        
        SeamCarver sc = new SeamCarver(inputImg);
        
        System.out.printf("Printing energy calculated for each pixel.\n");        

        for (int j = 0; j < sc.height(); j++)
        {
            for (int i = 0; i < sc.width(); i++)
                System.out.printf("%9.0f ", sc.energy(i, j));

            System.out.println();
        }
    }
    
}
