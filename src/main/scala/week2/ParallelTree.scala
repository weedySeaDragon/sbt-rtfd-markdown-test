package week2

import common._

/**
  * Created by ashleyengelund on 8/22/16.
  */
object ParallelTree {

  sealed abstract class Tree[A]
  case class Leaf[A](value: A) extends Tree[A]
  case class Node[A](left: Tree[A], right: Tree[A]) extends Tree[A]


  // Trees storing intermediate values also have values in nodes ("res" = result):
  sealed abstract class TreeRes[A] {
    val res: A
  }
  case class LeafRes[A](override val res: A) extends TreeRes[A]
  case class NodeRes[A](l: TreeRes[A],
                        override val res: A,
                        r: TreeRes[A]) extends TreeRes[A]



  // SEQUENTIAL map for a Tree:
  def mapSEQ[A, B](t: Tree[A], f: A => B):Tree[B] = t match {
    case Leaf(v) => Leaf(f(v)) // the value when the function is applied to that item
    case Node(l, r) => Node(mapSEQ[A, B](l, f), mapSEQ[A, B](r, f))
  }


  //----------------------
  //  Trees that hold Arrays:

  sealed abstract class TreeArray[A] { val size: Int}
  case class LeafArray[A](a: Array[A]) extends TreeArray[A] {
    override val size = a.length
  }
  case class NodeArray[A](left: TreeArray[A], right: TreeArray[A]) extends TreeArray[A] {
    override val size = left.size + right.size
  }

  /**
    *  parallel map for a TreeArray, where each element is an Array[A] and and where a.length is defined for each element 'a'
    * @param t TreeArray[A] - the original tree
    * @param f A=>B - a function that transforms a value:A to a newValue:B
    * @tparam A - the type of Array[A] that each LeafArray holds in t
    * @tparam B - the resulting type of the resulting Array[B] that each LeafArray in the returned TreeArray[B]
    * @return TreeArray[B] - a tree with each LeafArray transformed into an Array[B], with values:B
    */
  def mapArrayTree[A:Manifest, B:Manifest](t:TreeArray[A], f: A => B):TreeArray[B] =
  t match {
    case LeafArray(a) => {
      val len = a.length; val b = new Array[B](len)
      var i = 0
      while (i < len) { b(i) = f(a(i)); i += 1}  // iterate thru all elements in array 'a', applying function 'f'
      LeafArray(b)
    }
    case NodeArray(l, r) => {
      val (lVal, rVal) = parallel(mapArrayTree(l, f), mapArrayTree(r, f))
      NodeArray(lVal, rVal) // create a new Node (tree) and return it
    }
  }

  //------------- end Trees that hold Arrays


  // parallel reduce
  def reduce[A](t: Tree[A], f: (A, A) => A): A = t match {
    case Leaf(v) => v // the leaf is just the value
    case Node(l, r) => {
      val (leftVal, rightVal) = parallel(reduce[A](l, f), reduce[A](r, f))
      f(leftVal, rightVal)
    }
  }


  // Recall that reduce proceeds by applying the operations in a tree
  //  idea: save the intermediate results of the parallel computation

  // We first assume that input collection is also (another) tree.

  // PART 1: get the results and store them in an intermediate structure
  def reduceRes[A](t: Tree[A], f: (A, A) => A): TreeRes[A] = {
    t match {
      case Leaf(v) => LeafRes(f(v, v))
      case Node(l, r) => {
        val (lRes, rRes) = (reduceRes(l, f), reduceRes(r, f))
        NodeRes(lRes, f(lRes.res, rRes.res), rRes)
      }
    }
  }


  /**
    * Create an intermediate TreeRes -- one that can store results.  Uses for downsweep (and perhaps other functions)
    *
    * We call this "upsweep" because it suggests the bottom-up sequence of computations needed to get the result
    *
    * @param t Tree[A] - The original tree
    * @param f (A, A) => A - a function that transforms a value
    * @tparam A - The type that this tree stores (the type of the values)
    * @return TreeRes[A] - a tree that can store intermediate results (usually from applying "f")
    */
  def upsweep[A](t: Tree[A], f: (A, A) => A): TreeRes[A] = {
    t match {
      case Leaf(v) => LeafRes(f(v, v))
      case Node(l, r) => {
        val (lRes, rRes) = parallel(upsweep(l, f), upsweep(r, f))
        NodeRes(lRes, f(lRes.res, rRes.res), rRes)
      }
    }
  }


  // val t1 = Node(Node(Leaf(1), Leaf(3)), Node(Leaf(8), Leaf(50)))
  // val plus = (x:Int,y:Int) => x+y
  // upsweep(t1, plus)
  //  TreeRes[Int] = NodeRes(NodeRes(LeafRes(1),4,LeafRes(3)),62,NodeRes(LeafRes(8),58,LeafRes(50)))


  /**
    * Gather the intermediate results in TreeRes[A] and create the final collection.
    * 'a0' is the *reduce* of all elements to the LEFT of the tree 't' (= the initial element at the very start)
    * @param t TreeRes[A] - a tree with intermediate results stored for each node
    * @param a0 A - the initial value
    * @param f (A, A)=>A - the function to apply to the values of the Leafs
    * @tparam A - the type of the values stored in Tree and TreeRes
    * @return Tree[A] - a tree where each Leaf is the value from applying 'f'
    */
  def downsweep[A](t: TreeRes[A], a0: A, f: (A, A) => A): Tree[A] = t match {
    case LeafRes(a) => Leaf(f(a0, a))
    case NodeRes(l, _, r) => {
      val (tL, tR) = parallel(downsweep[A](l, a0, f),
        downsweep[A](r, f(a0, l.res), f)) // note we're passing in f(a0, l.res)
      // left sub-tree = a0 = the value to the LEFT of this tree -- and to the left of all trees
      // right sub-tree = f(a0, l.res) = the element we're give (a0) and also
      //  what happened in the left sub-tree (since the left sub-tree comes before this right sub-tree)

      Node(tL, tR)
    }
  }


  // downsweep(res0, 100, plus)
  // Tree[Int] = Node(Node(Leaf(101),Leaf(104)),Node(Leaf(112),Leaf(162)))

  // don't worry about balancing: this is approx logarithmic
  def prepend[A](x: A, t: Tree[A]): Tree[A] = t match {
    case Leaf(v) => Node(Leaf(x), Leaf(v))
    case Node(l, r) => Node(prepend(x, l), r)
  }


  def scanLeft[A](t: Tree[A], a0: A, f: (A, A) => A): Tree[A] = {
    val tRes = upsweep(t, f) // build the intermediate tree
    val scan1 = downsweep(tRes, a0, f) // generate the values for the Leafs of the tree
    prepend(a0, scan1) // add the initial value to the resulting tree that was generated
  }


}
