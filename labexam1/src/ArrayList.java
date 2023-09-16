public class ArrayList<E> implements List<E> {
    public static final int CAPACITY = 1000;
    private int capacity;
    private int size = 0;
    private E[] l;


    public ArrayList(){
        this(CAPACITY);
    }

    public ArrayList(int capacity) {
        this.capacity = capacity;
        this.l = (E[]) new Object[capacity];
    }

    @Override
    public String toString(){
        String output = " ";
        for (int i = 0; i < size; i++){
            output += l[i] + " ";
        }
        return output;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        return l[index];
    }

    @Override
    public boolean add(E element) {
        l[size++] = element;
        return true;
    }

    @Override
    public void add(int index, E element) {
        System.arraycopy(l, index, l, index + 1, size - index);
        l[index] = element;
        size ++;
    }
}