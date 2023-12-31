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
    // set recursive
    // this.root = setRecursive(this.root, key, value);
    // return this.cachedValue;

    return setIterative(key, value);
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
    this.root = remove(this.root, key);
    return this.cachedValue;
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

  V setIterative (K key, V value) {
    if (this.root == null) {
      this.root = new BSTNode<>(key, value);
      return null;
    }

    BSTNode<K,V> current = this.root;
    while (true) {
      int comp = comparator.compare(key, current.key);
      if (comp == 0) {
        this.cachedValue = current.value;
        current.value = value;
        return this.cachedValue;
      }
      if (comp < 0) {
        if (current.left != null) {
          current = current.left;
          continue;
        }
        current.left = new BSTNode<>(key, value);
        this.size++;
        return null;
      }
      if (comp > 0) {
        if (current.right != null) {
          current = current.right;
          continue;
        }
        current.right = new BSTNode<>(key, value);
        this.size++;
        return null;
      }
    }
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

  BSTNode<K,V> remove (BSTNode<K,V> node, K key) {
    if (node == null) {
      // key doesn't exist
      this.cachedValue = null;
      return null;
    }

    this.cachedValue = node.value;
    
    // edit the node
    int comp = comparator.compare(key, node.key);
    if (comp == 0) {
      // remove this node
      if (node.left == null && node.right == null) return null;
      if (node.left == null) return node.right;
      if (node.right == null) return node.left;

      // neither branch is empty, restructure the node
      node.left = putRightmost(node.left, node.right);
      return node.left;
    }
    if (comp < 0) {
      // go left
      node.left = remove(node.left, key);
    }
    if (comp > 0) {
      // go right
      node.right = remove(node.right, key);
    }

    // return edited node
    return node;
  }

  BSTNode<K,V> putRightmost(BSTNode<K,V> node, BSTNode<K,V> toInsert) {
    if (node == null) {
      return toInsert;
    }

    node.right = putRightmost(node.right, toInsert);

    return node;
  }

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
        
        BSTNode<K,V> outNode = stack.pop();
        if (outNode.right != null) stack.push(outNode.right);
        if (outNode.left != null) stack.push(outNode.left);

        return outNode;
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
