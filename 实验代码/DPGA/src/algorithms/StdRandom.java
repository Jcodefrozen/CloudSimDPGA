package algorithms;


import java.util.Random;

/** 生成随机数
 */
public final class StdRandom {

    private static Random random;    // 伪随机数生成器
    private static long seed;        // 伪随机数生成器种子

    //静态初始化器
    static {
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }

    // 不实例化
    private StdRandom() { }

    /**
     * 设置伪随机数生成器的种子。此方法使您能够为程序的每次执行生成相同的“随机”数字序列。通常，每个程序最多应该调用这个方法一次。
     *
     * @param s 种子
     */
    public static void setSeed(long s) {
        seed   = s;
        random = new Random(seed);
    }

    /**
     * 返回伪随机数生成器的种子。
     *
     * @return 种子
     */
    public static long getSeed() {
        return seed;
    }

    /**
     * 返回 [0, 1)随机值.
     */
    public static double uniform() {
        return random.nextDouble();
    }

    /**
     * 在[0,n]中均匀地返回一个随机整数
     * 
     * @param n number of possible integers
     * @return a random integer uniformly between 0 (inclusive) and <tt>N</tt> (exclusive)
     * @throws IllegalArgumentException if <tt>n <= 0</tt>
     */
    public static int uniform(int n) {
        if (n <= 0) throw new IllegalArgumentException("Parameter N must be positive");
        return random.nextInt(n);
    }


    /**
     * 在[0,n)中均匀地返回一个随机整数
     * 
     * @return     在[0,n)中均匀地返回一个随机整数
     * @deprecated 有{@link #uniform()}代替.
     */
    @Deprecated
	public static double random() {
        return uniform();
    }

    /**
     * 在[a,b)中均匀地返回一个随机整数
     * 
     * @param  a 左端点
     * @param  b 右端点
     * @return 均匀分布于[a, b)中的随机整数
     * @throws IllegalArgumentException if b <= a
     * @throws IllegalArgumentException if b - a >= Integer.MAX_VALUE
     */
    public static int uniform(int a, int b) {
        if (b <= a) throw new IllegalArgumentException("Invalid range");
        if ((long) b - a >= Integer.MAX_VALUE) throw new IllegalArgumentException("Invalid range");
        return a + uniform(b - a);
    }

    /**
     * 在[a,b)中均匀地返回一个随机整数
     *
     * @param  a 左端点
     * @param  b 右端点
     * @return 均匀分布于[a, b)中的随机整数
     * @throws IllegalArgumentException unless a < b
     */
    public static double uniform(double a, double b) {
        if (!(a < b)) throw new IllegalArgumentException("Invalid range");
        return a + uniform() * (b-a);
    }

    /**
     * 从成功概率为p的伯努利分布中返回一个随机布尔值。
     *
     * @param  p 返回true的概率
     * @return 对概率p为真，概率1-p为假
     * @throws IllegalArgumentException unless <tt>p >= 0.0</tt> and <tt>p <= 1.0</tt>
     */
    public static boolean bernoulli(double p) {
        if (!(p >= 0.0 && p <= 1.0))
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0");
        return uniform() < p;
    }

    /**
     * 从成功概率为1/2的伯努利分布中返回一个随机布尔值。
     *
     */
    public static boolean bernoulli() {
        return bernoulli(0.5);
    }

    /**
     * 从标准高斯分布返回一个随机实数
     * 
     * @return 一个来自标准高斯分布的随机实数(均值为0，标准差为1)。
     */
    public static double gaussian() {
        // use the polar form of the Box-Muller transform
        double r, x, y;
        do {
            x = uniform(-1.0, 1.0);
            y = uniform(-1.0, 1.0);
            r = x*x + y*y;
        } while (r >= 1 || r == 0);
        return x * Math.sqrt(-2 * Math.log(r) / r);

        // Remark:  y * Math.sqrt(-2 * Math.log(r) / r)
        // is an independent random gaussian
    }

    /**
     *从具有均值和标准差的高斯分布中返回一个随机实数。
     * 
     * @param  mu 均值
     * @param  sigma 标准偏差
     * @return 一个实数按高斯分布，均值mu，标准差sigma
     */
    public static double gaussian(double mu, double sigma) {
        return mu + sigma * gaussian();
    }

    /**
     * 从几何分布中返回一个随机整数，成功概率p。
     * 
     * @param  p 几何分布的参数
     * @return 一个随机整数从一个几何分布的成功概率p;Integer.MAX_VALUE(如果p等于1.0)。
     * @throws IllegalArgumentException unless p >= 0.0 and p <= 1.0
     */
    public static int geometric(double p) {
        if (!(p >= 0.0 && p <= 1.0))
            throw new IllegalArgumentException("Probability must be between 0.0 and 1.0");
        // using algorithm given by Knuth
        return (int) Math.ceil(Math.log(uniform()) / Math.log(1.0 - p));
    }

    /**
         * 从具有均值的泊松分布中返回一个随机整数。
     *
     * @param  lambda 泊松分布的均值
     * @return 一个来自泊松分布的随机整数，其均值lambda
     */
    public static int poisson(double lambda) {
        if (!(lambda > 0.0))
            throw new IllegalArgumentException("Parameter lambda must be positive");
        if (Double.isInfinite(lambda))
            throw new IllegalArgumentException("Parameter lambda must not be infinite");
        // using algorithm given by Knuth
        // see http://en.wikipedia.org/wiki/Poisson_distribution
        int k = 0;
        double p = 1.0;
        double L = Math.exp(-lambda);
        do {
            k++;
            p *= uniform();
        } while (p >= L);
        return k-1;
    }

    /**
     * @return 一个来自标准帕累托分布的随机实数
     */
    public static double pareto() {
        return pareto(1.0);
    }

    /**
     * 从形状参数alpha的Pareto分布中返回一个随机实数;
     *
     * @param  alpha 形参
     * @return 形势参数alpha的帕累托分布中的一个随机实数
     * @throws IllegalArgumentException unless alpha > 0.0
     */
    public static double pareto(double alpha) {
        if (!(alpha > 0.0))
            throw new IllegalArgumentException("Shape parameter alpha must be positive");
        return Math.pow(1 - uniform(), -1.0/alpha) - 1.0;
    }

    /**
     * 从柯西分布中返回一个随机实数。
     *
     * @return 柯西分布中返回一个随机实数。
     */
    public static double cauchy() {
        return Math.tan(Math.PI * (uniform() - 0.5));
    }

    /**
     * 从指定的离散分布返回一个随机整数。
     *
     * @param  probabilities 每个整数出现的概率
     * @return 一个离散分布的随机整数:
     */
    public static int discrete(double[] probabilities) {
        if (probabilities == null) throw new NullPointerException("argument array is null");
        double EPSILON = 1E-14;
        double sum = 0.0;
        for (int i = 0; i < probabilities.length; i++) {
            if (!(probabilities[i] >= 0.0))
                throw new IllegalArgumentException("array entry " + i + " must be nonnegative: " + probabilities[i]);
            sum += probabilities[i];
        }
        if (sum > 1.0 + EPSILON || sum < 1.0 - EPSILON)
            throw new IllegalArgumentException("sum of array entries does not approximately equal 1.0: " + sum);

        // 当r(接近)1.0和累积和小于1.0(由于浮点舍入错误)时，for循环可能不会返回值
        while (true) {
            double r = uniform();
            sum = 0.0;
            for (int i = 0; i < probabilities.length; i++) {
                sum = sum + probabilities[i];
                if (sum > r) return i;
            }
        }
    }

    /**
     * 从指定的离散分布返回一个随机整数。
     *
     * @param  frequencies 每个整数出现的频率
     * @return 一个离散分布的随机整数:
     */
    public static int discrete(int[] frequencies) {
        if (frequencies == null) throw new NullPointerException("argument array is null");
        long sum = 0;
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] < 0)
                throw new IllegalArgumentException("array entry " + i + " must be nonnegative: " + frequencies[i]);
            sum += frequencies[i];
        }
        if (sum == 0)
            throw new IllegalArgumentException("at least one array entry must be positive");
        if (sum >= Integer.MAX_VALUE)
            throw new IllegalArgumentException("sum of frequencies overflows an int");

        double r = uniform((int) sum);
        sum = 0;
        for (int i = 0; i < frequencies.length; i++) {
            sum += frequencies[i];
            if (sum > r) return i;
        }

        // can't reach here
        assert false;
        return -1;
    }

    /**
     * 从速率为的指数分布中返回一个随机实数。
     * 
     * @param  lambda 指数分布的速率
     * @return 一个随机实数从指数分布与速率
     * @throws IllegalArgumentException
     */
    public static double exp(double lambda) {
        if (!(lambda > 0.0))
            throw new IllegalArgumentException("Rate lambda must be positive");
        return -Math.log(1 - uniform()) / lambda;
    }

    /**
     * 以均匀随机的顺序重新排列指定数组的元素。
     *
     * @param  a 要混合的数组
     * @throws NullPointerException
     */
    public static void shuffle(Object[] a) {
        if (a == null) throw new NullPointerException("argument array is null");
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int r = i + uniform(n-i);     // between i and n-1
            Object temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     *以均匀随机的顺序重新排列指定数组的元素。
     *
     * @param  a 要混合的数组
     * @throws NullPointerException
     */
    public static void shuffle(double[] a) {
        if (a == null) throw new NullPointerException("argument array is null");
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int r = i + uniform(n-i);     // between i and n-1
            double temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 以均匀随机的顺序重新排列指定子数组的元素。
     *
     * @param  a 要混合的数组
     */
    public static void shuffle(int[] a) {
        if (a == null) throw new NullPointerException("argument array is null");
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int r = i + uniform(n-i);     // between i and n-1
            int temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }


    /**
     * 以均匀随机的顺序重新排列指定子数组的元素。
     *
     * @param  a 要混合的数组
     * @param  lo 左端点(包括)
     * @param  hi 右端点(包括)
     * 
     */
    public static void shuffle(Object[] a, int lo, int hi) {
        if (a == null) throw new NullPointerException("argument array is null");
        if (lo < 0 || lo > hi || hi >= a.length) {
            throw new IndexOutOfBoundsException("Illegal subarray range");
        }
        for (int i = lo; i <= hi; i++) {
            int r = i + uniform(hi-i+1);     // between i and hi
            Object temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 以均匀随机的顺序重新排列指定子数组的元素。
     *
     * @param  a 要混合的数组
     * @param  lo 左端点(包括)
     * @param  hi 右端点(包括)
     */
    public static void shuffle(double[] a, int lo, int hi) {
        if (a == null) throw new NullPointerException("argument array is null");
        if (lo < 0 || lo > hi || hi >= a.length) {
            throw new IndexOutOfBoundsException("Illegal subarray range");
        }
        for (int i = lo; i <= hi; i++) {
            int r = i + uniform(hi-i+1);     // between i and hi
            double temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 以均匀随机的顺序重新排列指定子数组的元素。
     *
     * @param  a 要混合的数组
     * @param  lo 左端点(包括)
     * @param  hi 右端点(包括)
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    public static void shuffle(int[] a, int lo, int hi) {
        if (a == null) throw new NullPointerException("argument array is null");
        if (lo < 0 || lo > hi || hi >= a.length) {
            throw new IndexOutOfBoundsException("Illegal subarray range");
        }
        for (int i = lo; i <= hi; i++) {
            int r = i + uniform(hi-i+1);     // between i and hi
            int temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        if (args.length == 2) StdRandom.setSeed(Long.parseLong(args[1]));
        double[] probabilities = { 0.5, 0.3, 0.1, 0.1 };
        int[] frequencies = { 5, 3, 1, 1 };
        String[] a = "A B C D E F G".split(" ");

        System.out.println("seed = " + StdRandom.getSeed());
        for (int i = 0; i < n; i++) {
            System.out.format("%2d "  , uniform(100));
            System.out.format("%8.5f ", uniform(10.0, 99.0));
            System.out.format("%5b "  , bernoulli(0.5));
            System.out.format("%7.5f ", gaussian(9.0, 0.2));
            System.out.format("%1d "  , discrete(probabilities));
            System.out.format("%1d "  , discrete(frequencies));
            StdRandom.shuffle(a);
            for (String s : a)
                System.out.println(s);
            System.out.println();;
        }
    }

}
