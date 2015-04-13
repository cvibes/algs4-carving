import java.awt.Color;
public class SeamCarver
{
    private Picture pic;
    private double[][] energyOf;
    private    int[][] colorOf;
    private boolean isTransposed, changed, calledByFH, calledByRH;
    private int width;
    private int height;

    public SeamCarver(Picture picture) {
        pic          = new Picture(picture);
        width        = pic.width();
        height       = pic.height();
        energyOf     = new double[width][height];
        colorOf      = new int[width][height];
        isTransposed = false;
        changed      = false;
        calledByFH   = false;
        calledByRH   = false;
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                energyOf[i][j] = energy(i, j);
                colorOf[i][j]  = pic.get(i, j).getRGB();
            }
    }

    public Picture picture() {
        if (!changed) return pic;
        Picture p = new Picture(width, height);
        if (isTransposed) transpose();
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                p.set(i, j, new Color(colorOf[i][j]));
        pic = p;
        changed = false;
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

        if (x == 0 || y == 0 || x == (width - 1) || y == (height - 1))
            return 195075.0;

        double e = diff(pic.get(x + 1, y), pic.get(x - 1, y))
                 + diff(pic.get(x, y + 1), pic.get(x, y - 1));

        return e;
    }

    private double diff(Color a, Color b) {
        return (a.getRed()  - b.getRed())   * (a.getRed()   - b.getRed())
            + (a.getGreen() - b.getGreen()) * (a.getGreen() - b.getGreen())
            + (a.getBlue()  - b.getBlue())  * (a.getBlue()  - b.getBlue());
    }

    private void printEnergy() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++)
                StdOut.printf("%7.0f\t", energyOf[i][j]);
            StdOut.printf("\n");
        }
        StdOut.printf("\n");
    }

    private void transpose() {
        double[][] eOf = new double[height][width];
        int[][] cOf = new int[height][width];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                eOf[j][i] = energyOf[i][j];
                cOf[j][i] = colorOf[i][j];
            }
        energyOf = eOf;
        colorOf = cOf;
        int x = width;
        width = height;
        height = x;
        isTransposed = !isTransposed;
    }

    public int[] findHorizontalSeam() {
        if (!isTransposed) transpose();
        calledByFH = true;
        SimpleSP sp = new SimpleSP(energyOf);
        return sp.seam();
    }

    public int[] findVerticalSeam() {
        if (!calledByFH && isTransposed) transpose();
        SimpleSP sp = new SimpleSP(energyOf);
        calledByFH = false;
        return sp.seam();
    }

    public void removeHorizontalSeam(int[] seam) {
        if (!isTransposed) transpose();
        calledByRH = true;
        removeVerticalSeam(seam);
    }

    public void removeVerticalSeam(int[] seam) {
        if (!calledByRH && isTransposed) transpose();
        // for (int i = 0; i < seam.length; i++)
        //     StdOut.printf("%d\t", seam[i]);
        // StdOut.printf("\n\n");

        // StdOut.printf("width is %d, height is %d\n", width - 1, height);
        double[][] e = new double[width - 1][height];
        int[][] c = new int[width - 1][height];
        for (int i = 0; i < height; i++) {
            int j = 0, k = 0;
            while (j < width) {
                if (j == seam[i]) j++;
                e[k][i] = energyOf[j][i];
                c[k++][i] = colorOf[j++][i];
            }
        }
        energyOf = e;
        width = width - 1;
        changed = true;
        calledByRH = false;
    }

    public static void main(String[] args) {
        Picture inputImg = new Picture(args[0]);
        System.out.printf("image is %d pixels wide by %d pixels high.\n",
                          inputImg.width(), inputImg.height());

        SeamCarver sc = new SeamCarver(inputImg);
        int[] s = sc.findHorizontalSeam();
        for (int i = 0; i < s.length; i++)
            StdOut.printf("%d\t", s[i]);
        StdOut.printf("\n\n");
        sc.printEnergy();
        // sc.removeHorizontalSeam(s);
        // sc.printEnergy();
        sc.removeHorizontalSeam(s);
        sc.printEnergy();
        // sc.transpose();
        // sc.printEnergy();
        // System.out.printf("Printing energy calculated for each pixel.\n");

        // for (int j = 0; j < sc.height(); j++)
        // {
        //     for (int i = 0; i < sc.width(); i++)
        //         System.out.printf("%9.0f ", sc.energy(i, j));

        //     System.out.println();
        // }
    }
}
