//Sydney Dlhopolsky
//project 3


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
// Import any package as required


public class HuffmanSubmit implements Huffman{
  
		 public class Node implements Comparable<Node> {
			 int data;
			 Node left;
			 Node right;
			 Node parent; 
			 String leafChar;
			
			public Node(int d, Node l, Node r) {
				data = d;
				left = l;
				right = r;
				parent = null;
				//leafChar = null;
			}
			public Node(int d, Node l, Node r, Node p) {
				data = d;
				left = l;
				right = r;
				parent = p;
				//leafChar = null;
			}
			
			public Node(int d) {
				data = d;
				left = null;
				right = null;
				parent = null;

				//leafChar =  ;
			}
			public Node(int d, Node l, Node r, String c) {
				data = d;
				left = l;
				right = r;
				leafChar = c;
				parent = null;

				
			}
			
			public Node(int d, String c) {
				data = d;
				left = null;
				right = null;
				leafChar =c;
				parent = null;

			}
			
			@Override
			public int compareTo(Node n1) {
				if (n1.data > this.data)
		        {
		            return -1;
		        }
		        if (n1.data < this.data)
		        {
		            return 1;
		        }
		        return 0;
			} 
			
			public void setParent(Node p) {
				parent = p;
			}
			
		}
		 
		
		
	// Feel free to add more methods and variables as required. 
	static HashMap<String,Integer> c = new HashMap<String, Integer>();
	static HashMap<String, ArrayList<Integer>> leafPaths = new HashMap<String,ArrayList<Integer>>(); 
	static HashMap<String,Integer> decodemap = new HashMap<String, Integer>();

	
	public static PriorityQueue<Node> makeQueue(HashMap<String,Integer> c) {
		//Comparator<Node> comp = new NodeComparator();
		 PriorityQueue<Node> pq = new PriorityQueue<Node>();
		for (String key: c.keySet()) {
			pq.offer((new HuffmanSubmit()).new Node(c.get(key),key));
		}
		return pq;
	}
	
	public static Node makeTree(PriorityQueue<Node> pq) {
		while (pq.size()>1) {
			Node n1 = (Node) pq.poll();
			Node n2 = (Node) pq.poll();
			Node p = new HuffmanSubmit().new Node(n1.data+n2.data,n2,n1);
			n1.setParent(p);
			n2.setParent(p);
			pq.offer(p);
		}
		//System.out.println("q: "+ ((Node)pq.peek()).left.parent.data);
		//System.out.println("q: "+ ((Node)pq.peek()).right.right);
		//System.out.println("q: "+ ((Node)pq.peek()).left.left);

		
		return (Node) pq.poll();
		
	} 
	
	/* public static void word_count(String inputFile, String outputFile) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(inputFile));
		while (bf.ready()){
			String next = Integer.toBinaryString(bf.read());
			if(!c.containsKey(next)) {
				c.put(next, 1);
			}
			else {
				c.replace(next,c.get(next)+1);
			}
		}
		
		printO(outputFile);
		bf.close();
		} */
	public static void word_count(String inputFile, String outputFile) {
		BinaryIn bi = new BinaryIn(inputFile);
		while (!bi.isEmpty()){
			int b = bi.readChar();
			//System.out.println("char: " + b );
			String next = Integer.toBinaryString(b);
			//System.out.println("char: " + next);
			if(!c.containsKey(next)) {
				c.put(next, 1);
			}
			else {
				c.replace(next,c.get(next)+1);
			}
		}
		
		printO(outputFile);
		//bi.close();
		}
	
	public static void printO(String outputFile)  {
		PrintWriter outFile;
		try {
			outFile = new PrintWriter(outputFile);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File Not Found");
		}
		//System.out.print(words.get("Alice"));
		for (String key: c.keySet()) {
			outFile.println(key + " : " + c.get(key));
		}
		outFile.close();
	}
	public static void inOrder(Node root) {
		if (root==null) {
			return;
		}
		else {
			inOrder(root.left);
			//System.out.print(root.data+ " ");
			inOrder(root.right);
		}
	}
	public static void preOrder(Node root) {
		if (root==null) {
			return;
		}
		else {
			//System.out.print(root.data + " ");
			preOrder(root.left);
			preOrder(root.right);
		}
	}
	
	public static boolean findLeaf(String next, Node r, ArrayList<Integer> path) {
		//inOrder(r);
		 if (r.left == null && r.right == null && r.leafChar.equals(next)) {
			//path.add(r);		
			//System.out.println(1);
			return true;
		}
		if (r.left == null && r.right == null && !r.leafChar.equals(next)) {
			//System.out.println(2);
			return false;
		}
		if (findLeaf(next,r.left,path)) {
			path.add(0,0);
			//System.out.println(3);
			return true;
		}
		 if (findLeaf(next,r.right,path)) {
			path.add(0,1);
		//	System.out.println(4);
			return true;
		} 
		return false;
		
	}
	public static void writeCode(String next, BinaryOut bo, Node r) {
		ArrayList<Integer> path = new ArrayList<Integer>();
		if (!leafPaths.containsKey(next)) {
			findLeaf(next,r,path);
			leafPaths.put(next, path);
		}
		//System.out.println(next + " : " );
		for(int n: leafPaths.get(next)) {
			//System.out.print(n);
			if (n == 0) {
				//System.out.print(0);
				bo.write(false);
			}
			else if (n == 1) {
				//System.out.print(1);
				bo.write(true);
			}
		}
		//System.out.println("");
	}
	
	public void encode(String inputFile, String outputFile, String freqFile){
		word_count(inputFile,freqFile);
		Node root = makeTree(makeQueue(c));
		//inOrder(root);
		//System.out.println("");
		//preOrder(root);
		BinaryIn bi = new BinaryIn(inputFile);
		int counter = 0;
		BinaryOut bo = new BinaryOut(outputFile);
		while (!bi.isEmpty()){
		//	System.out.println(counter++);
			String next = Integer.toBinaryString(bi.readChar());
			writeCode(next,bo,root);

		}
		bo.flush();
		bo.close();
   }

	public static void makeHashMap(String freqFile)  {
		Scanner sc;
		try {
			sc = new Scanner(new File(freqFile));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File Not Found");
		}
		while (sc.hasNext()){
			String next = sc.next();
			String next2 = sc.next();
			String next3 = sc.next();
			decodemap.put(next, Integer.parseInt(next3));
	}
		sc.close();
	}
	public static int countChar(HashMap<String,Integer> map) {
		int total = 0;
		for (String key : map.keySet()) {
		    total +=  map.get(key);
		}
		return total;
	}
	
   public void decode(String inputFile, String outputFile, String freqFile){
	   makeHashMap(freqFile);
	   Node root = makeTree(makeQueue(decodemap));
	   //inOrder(root);
	   //System.out.println(decodemap.size());
	   Node curr = makeTree(makeQueue(decodemap));
	   BinaryIn bi = new BinaryIn(inputFile);
	   BinaryOut bo = new BinaryOut(outputFile);
	   int  count = 0;
	   int  total = countChar(decodemap);
	   while (count < total) {
		   boolean boo = bi.readBoolean();
		   if (curr.right == null && curr.left==null) {
				  // System.out.println("test1");
				   int n = Integer.parseInt(curr.leafChar, 2);
				   bo.write((char)n);
				   curr = root;
				   count++;
			   }
		   if(boo == (false) && !(curr.left == null)) {
			   curr = curr.left; 
			  // System.out.println("test2");

		   }
		   if(boo == (true) && !(curr.right == null)) {
			   curr = curr.right; 
			   //System.out.println("test3");
		   }
		  
	   }
	   bo.flush();
	   bo.close();   
   }




   public static void main(String[] args) {
	   Huffman  huffman = new HuffmanSubmit();
		huffman.encode("ur.jpg", "ur.enc", "freq.txt");
		huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");
		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same. 
   }

}
