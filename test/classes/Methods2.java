// JSwat test for finding methods in classes (part 2).
// $Id$

/**
 * Second part of the Methods test.
 */
public class Methods2 extends Methods {

    /**
     * A sample method.
     *
     * @param  i  integer argument.
     */
    public void print(int i) {
        System.out.println("i2 = " + i);
    }

    /**
     * A sample method.
     *
     * @param  l  long argument.
     */
    public void print(long l) {
        System.out.println("l2 = " + l);
    }

    public String toString() {
        return "me2";
    }
}
