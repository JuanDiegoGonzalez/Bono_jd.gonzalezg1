package model.data_structures;

import java.util.Iterator;

import model.logic.Vertice;

/** 
 * Implementación tomada de Algorithms 4th edition by Robert Sedgewick and Kevin Wayne (2011)
 * Consultado el 04/11/19
 * Disponible en https://algs4.cs.princeton.edu/code/
 */
public class NonrecursiveDFS {
	public boolean[][] marked;  // marked[v] = is there an s-v path?
	public int M, N;
	
	/**
	 * Computes the vertices connected to the source vertex {@code s} in the graph {@code G}.
	 * @param G the graph
	 * @param s the source vertex
	 * @throws IllegalArgumentException unless {@code 0 <= s < V}
	 */
	public NonrecursiveDFS(Graph G, int x, int y) {
		marked = new boolean[G.M()][G.N()];
		M = G.M();
		N = G.N();

		validateVertex(x, y);

		// to be able to iterate over each adjacency list, keeping track of which
		// vertex in each adjacency list needs to be explored next
		Iterator<Vertice>[][] adj = (Iterator<Vertice>[][]) new Iterator[G.M()][G.N()];
		
		for (int f = 0; f < G.M(); f++)
		{
			for (int c = 0; c < G.N(); c++)
			{
				adj[f][c] = G.adj(f, c).iterator();
			}
		}
			
		// depth-first search using an explicit stack
		Stack<int[]> stack = new Stack<int[]>();
		marked[x][y] = true;
		
		int[] nuevo = new int[2];
		nuevo[0] = x;
		nuevo[1] = y;		
		
		stack.push(nuevo);
		while (!stack.isEmpty())
		{
			int v[] = stack.peek();

			if (adj[x][y].hasNext())
			{
				int w[] = new int[2];

				w[0] = ((Vertice)adj[x][y].next()).F();
				w[1] = ((Vertice)adj[x][y].next()).C();	
				
				if (!marked[x][y])
				{
					// discovered vertex w for the first time
					marked[x][y] = true;
					// edgeTo[w] = v;
					stack.push(w);
				}
			}
			else
			{
				stack.pop();
			}
		}
	}

	/**
	 * Is vertex {@code v} connected to the source vertex {@code s}?
	 * @param v the vertex
	 * @return {@code true} if vertex {@code v} is connected to the source vertex {@code s},
	 *    and {@code false} otherwise
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public boolean marked(int m, int n)
	{
		validateVertex(m, n);
		return marked[m][n];
	}

	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertex(int m, int n)
	{		
		if (m < 0 || m >= M)
			throw new IllegalArgumentException("vertex " + m + " is not between 0 and " + (M-1));
		if (n < 0 || n >= N)
			throw new IllegalArgumentException("vertex " + n + " is not between 0 and " + (N-1));
	}
}