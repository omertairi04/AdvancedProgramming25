package Lab2;

import java.util.*;

class ResizableArray<T> {
    T[] array;
    int size;
    int elementsStored;

    @SuppressWarnings("unchecked")
    ResizableArray() {
        this.size = 10;
        this.array = (T[]) new Object[size];
        elementsStored = 0;
    }

    void addElement(T element) {
        if (elementsStored == size) {
            int oldSize = size;
            size *= 2;
            T[] newArr = (T[]) new Object[size];
            System.arraycopy(array, 0, newArr, 0, oldSize);
            array = newArr;
        }
        array[elementsStored++] = element;
    }

    boolean removeElement(T element) {
        for (int i = 0; i < elementsStored; i++) {
            if (array[i] != null &&  array[i].equals(element)) {
                for (int j=i; j<elementsStored-1; j++) {
                    array[j] = array[j+1];
                }
                array[elementsStored-1] = null;
                elementsStored--;
                return true;
            }
        }
        return false;
    }

    boolean contains (T element) {
        for (int i=0; i<elementsStored; i++) {
            if (array[i] != null &&  array[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    Object[] toArray() {
        return  Arrays.copyOf(array, elementsStored);
    }

    int count() { return elementsStored; }

    T elementAt(int idx) throws ArrayIndexOutOfBoundsException {
        if (idx < 0 || idx > elementsStored) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            return array[idx];
        }
    }

    static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src) {
        for (int i = 0; i < src.count(); i++) {
            dest.addElement(src.elementAt(i));
        }
    }

}

class IntegerArray extends ResizableArray<Integer> {

    public IntegerArray() {}

    double sum() {
        double sum = 0.0;
        for (int i=0; i<size; i++) {
            sum += array[i];
        }

        return sum;
    }

    double mean() {
        return elementsStored == 0 ? 0 : sum() / elementsStored;
    }

    int countNonZero() {
        int count = 0;
        for (int i = 0; i < elementsStored; i++) {
            if (array[i] != 0) count++;
        }
        return count;
    }

    IntegerArray distinct() {
        IntegerArray distinct = new IntegerArray();
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i=0; i<size; i++) {
            if (!map.containsKey(array[i])) {
                map.put(array[i], i);
            }
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer key = entry.getKey();
            distinct.addElement(key);
        }

        return distinct;
    }

    IntegerArray increment(int offset) {
        IntegerArray newArr = new IntegerArray();

        for (int i=0; i<size; i++) {
            newArr.addElement(array[i] +  offset);
        }

        return newArr;
    }

    boolean isEmpty() {
        return elementsStored == 0;
    }

}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if ( a.sum() > 100 )
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}
