package cma.cimiss2.dpc.indb.general.vo;

import java.util.Objects;

/**
  * An immutable pair of values.  The values may be null.  The values
  * themselves may be mutable.
  *
  * @param <A> the type of the first element of the pair
  * @param <B> the type of the second element of the pair
  *
  * @since 1.7
  */
public final class Pair<A, B> {
     private final A first;
     private final B second;

     private Pair(A first, B second) {
    	 this.first = first;
    	 this.second = second;
     }

     /**
      * Returns a pair whose elements are the first and second
      * arguments, respectively.
      * @return a pair constructed from the arguments
      */
     public static <C, D> Pair<C, D> valueOf(C c, D d) {
    	 // Don't mandate new values.
    	 return new Pair<C, D>(c, d);
     }

     /**
      * Returns the value of the first element of the pair.
      * @return the value of the first element of the pair
      */
     public A getFirst() {
    	 return first;
     }

     /**
      * Returns the value of the second element of the pair.
      * @return the value of the second element of the pair
      */
     public B getSecond() {
    	 return second;
     }

     /**
      * TBD
      */
     @Override
     public String toString() {
    	 return "[" + Objects.toString(first) + ", " + Objects.toString(second) + "]";
     }

     /**
      * TBD
      */
     @Override
     public boolean equals(Object x) {
    	 if (!(x instanceof Pair))
    		 return false;
    	 else {
    		 Pair<?,?> that = (Pair<?,?>) x;
    	 return Objects.equals(this.first, that.first) && Objects.equals(this.second, that.second);
	}
     }

     /**
      * TBD
      */
     @Override
     public int hashCode() {
    	 return Objects.hash(first, second);
     }
}
