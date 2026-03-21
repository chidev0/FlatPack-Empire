package structures;

import exceptions.*;

public class ArrayStack<T> {
    private T[] stack;
    private int top;
    private int capacity;
    private T[] newStack;

    // Default constructor: size 100
    @SuppressWarnings("unchecked")
    public ArrayStack() {
        capacity = 100;
        stack = (T[]) new Object[capacity];
        top = -1;
    }

    // User-defined constructor: custom size
    @SuppressWarnings("unchecked")
    public ArrayStack(int size) {
        capacity = size;
        stack = (T[]) new Object[capacity];
        top = -1;
    }

    public void push(T item) {
        if (isFull()) {
            throw new CapacityExceededException(capacity);
        }
        stack[++top] = item;
    }


    public T pop() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        T temp = stack[top];
        stack[top] = null;
        top--;
        return temp;
    }

    public T peek() {
        if (isEmpty()) {
            throw new EmptyStructureException();
        }
        return stack[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return size() == capacity;
    }

    public int size() {
        return top + 1;
    }

    // TO DO : Fix stack resize boundary copy issue

    public void upgradeCapacity(int newCapacity) {
        if (newCapacity < capacity) {
            throw new RuntimeException("Capacity size cannot be less than current capacity.");
        }
        newStack = (T[]) new Object[newCapacity];
        for (int i = 0; i <= top; i++) {
            newStack[i] = stack[i];
        }

        this.capacity = newCapacity;
        stack = newStack;

    }
}
