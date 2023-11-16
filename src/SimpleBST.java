import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;
import java.util.function.BiConsumer;

/**
 * A simple implementation of binary search trees.
 */
public class SimpleBST<K, V> implements SimpleMap<K, V> {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The root of our tree. Initialized to null for an empty tree.
   */
  BSTNode<K, V> root;

  /**
   * The comparator used to determine the ordering in the tree.
   */
  Comparator<? super K> comparator;

  /**
   * The size of the tree.
   */
  int size;

  /**
   * A cached value (useful in some circumstances.
   */
  V cachedValue;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new binary search tree that orders values using the specified comparator.
   */
  public SimpleBST(Comparator<? super K> comparator) {
    this.comparator = comparator;
    this.root = null;
    this.size = 0;
    this.cachedValue = null;
  } // SimpleBST(Comparator<K>)

  /**
   * Create a new binary search tree that orders values using a not-very-clever default comparator.
   */
  public SimpleBST() {
    this((k1, k2) -> k1.toString().compareTo(k2.toString()));
  } // SimpleBST()


  // +-------------------+-------------------------------------------
  // | SimpleMap methods |
  // +-------------------+

  @Override
  public V set(K key, V value) {
    this.root = setRecursive(this.root, key, value);
    return this.cachedValue;
  } // set(K,V)

  @Override
  public V get(K key) {
    if (key == null) {
      throw new NullPointerException("null key");
    } // if
    return get(key, root);
  } // get(K,V)

  @Override
  public int size() {
    return 0; // STUB
  } // size()

  @Override
  public boolean containsKey(K key) {
    return false; // STUB
  } // containsKey(K)

  @Override
  public V remove(K key) {
    return null; // STUB
  } // remove(K)

  @Override
  public Iterator<K> keys() {
    return new Iterator<K>() {
      Iterator<BSTNode<K, V>> nit = SimpleBST.this.nodes();

      @Override
      public boolean hasNext() {
        return nit.hasNext();
      } // hasNext()

      @Override
      public K next() {
        return nit.next().key;
      } // next()

      @Override
      public void remove() {
        nit.remove();
      } // remove()
    };
  } // keys()

  @Override
  public Iterator<V> values() {
    return new Iterator<V>() {
      Iterator<BSTNode<K, V>> nit = SimpleBST.this.nodes();

      @Override
      public boolean hasNext() {
        return nit.hasNext();
      } // hasNext()

      @Override
      public V next() {
        return nit.next().value;
      } // next()

      @Override
      public void remove() {
        nit.remove();
      } // remove()
    };
  } // values()

  @Override
  public void forEach(BiConsumer<? super K, ? super V> action) {
    forEach(this.root, action);
  } // forEach

  // +----------------------+----------------------------------------
  // | Other public methods |
  // +----------------------+

  /**
   * Dump the tree to some output location.
   */
  public void dump(PrintWriter pen) {
    dump(pen, root, "");
  } // dump(PrintWriter)


  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Dump a portion of the tree to some output location.
   */
  void dump(PrintWriter pen, BSTNode<K, V> node, String indent) {
    if (node == null) {
      pen.println(indent + "<>");
    } else {
      pen.println(indent + node.key + ": " + node.value);
      if ((node.left != null) || (node.right != null)) {
        dump(pen, node.left, indent + "  ");
        dump(pen, node.right, indent + "  ");
      } // if has children
    } // else
  } // dump

  BSTNode<K, V> setRecursive(BSTNode<K, V> node, K key, V value) {
    if (node == null) {
      // make new node
      BSTNode<K, V> newNode = new BSTNode<>(key, value);
      this.cachedValue = null;
      this.size++;
      return newNode;
    }
    // node is not null
    int comp = comparator.compare(key, node.key);

    // edit node
    if (comp == 0) {
      // found existing key
      this.cachedValue = node.value;
      node.value = value;
    }
    if (comp < 0) {
      node.left = setRecursive(node.left, key, value);
    }
    if (comp > 0) {
      node.right = setRecursive(node.right, key, value);
    }

    // return edited node
    return node;
  }

  /**
   * Get the value associated with a key in a subtree rooted at node. See the top-level get for more
   * details.
   */
  V get(K key, BSTNode<K, V> node) {
    if (node == null) {
      throw new IndexOutOfBoundsException("Invalid key: " + key);
    }
    int comp = comparator.compare(key, node.key);
    if (comp == 0) {
      return node.value;
    } else if (comp < 0) {
      return get(key, node.left);
    } else {
      return get(key, node.right);
    }
  } // get(K, BSTNode<K,V>)

  void forEach(BSTNode<K,V> node, BiConsumer<? super K, ? super V> action) {
    if (node == null) return;

    action.accept(node.key, node.value);
    
    forEach(node.left, action);
    forEach(node.right, action);
  }

  /**
   * Get an iterator for all of the nodes. (Useful for implementing the other iterators.)
   */
  Iterator<BSTNode<K, V>> nodes() {
    return new Iterator<BSTNode<K, V>>() {

      Stack<BSTNode<K, V>> stack = new Stack<BSTNode<K, V>>();
      boolean initialized = false;

      @Override
      public boolean hasNext() {
        checkInit();
        return !stack.empty();
      } // hasNext()

      @Override
      public BSTNode<K, V> next() {
        checkInit();
        // STUB
        return null;
      } // next();

      void checkInit() {
        if (!initialized) {
          stack.push(SimpleBST.this.root);
          initialized = true;
        } // if
      } // checkInit
    }; // new Iterator
  } // nodes()

} // class SimpleBST
