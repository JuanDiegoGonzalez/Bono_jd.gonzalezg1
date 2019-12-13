package model.data_structures;

import model.logic.Arco;
import model.logic.Vertice;

/** 
 * Implementación tomada de Algorithms 4th edition by Robert Sedgewick and Kevin Wayne (2011)
 * Consultado el 20/11/19
 * Disponible en https://algs4.cs.princeton.edu/code/
 */
public class DijkstraUndirectedSP {
	private double[][] distTo;          // distTo[v] = distance  of shortest s->v path
	private Arco[][] edgeTo;            // edgeTo[v] = last edge on shortest s->v path
	private IndexMinPQ<Double> pq;   // priority queue of vertices
	private int M, N;

	/**
	 * Computes a shortest-paths tree from the source vertex {@code s} to every
	 * other vertex in the edge-weighted graph {@code G}.
	 *
	 * @param  G the edge-weighted digraph
	 * @param  s the source vertex
	 * @throws IllegalArgumentException if an edge weight is negative
	 * @throws IllegalArgumentException unless {@code 0 <= s < V}
	 */
	public DijkstraUndirectedSP(Graph G, int x, int y)
	{
		M = G.M();
		N = G.N();
		for (Arco e : (Iterable<Arco>) G.arcos)
		{
			if (e.darCosto() < 0)
				throw new IllegalArgumentException("edge " + e + " has negative weight");
		}

		distTo = new double[M][N];
		edgeTo = new Arco[M][N];

		validateVertex(x, y);

		for (int f = 0; f < M; f++)
		{
			for (int c = 0; c < N; c++)
				distTo[f][c] = Double.POSITIVE_INFINITY;
		}
		distTo[x][y] = 0.0;

		// relax vertices in order of distance from s
		pq = new IndexMinPQ<Double>(M*M + N*N);
		if(M>N)
			pq.insert(x*M+y, distTo[x][y]);
		else
			pq.insert(y*N+x, distTo[x][y]);
		int m;
		int n;
		while (!pq.isEmpty()) {
			if(M>N)
			{
				int h = pq.delMin();
				n = h%M;
				m = (h-n)/M;
			}
			else
			{
				int h = pq.delMin();
				m = h%N;
				n = (h-m)/N;
			}
			for (Arco e : ((Vertice) G.getInfoVertex(m, n)).darArcos())
				relax(e, m, n);
		}

		// check optimality conditions
		assert check(G, x, y);
	}

	// relax edge e and update pq if changed
	private void relax(Arco e, int m, int n) {
		int f = e.other(m, n)[0];
		int c = e.other(m, n)[1];
		if (distTo[f][c] > distTo[m][n] + e.darCosto()) {
			distTo[f][c] = distTo[m][n] + e.darCosto();
			edgeTo[f][c] = e;
			if(M>N)
			{
				if (pq.contains(f*M+c))
				{
					pq.decreaseKey(f*M+c, distTo[f][c]);
				}
				else
				{
					pq.insert(f*M+c, distTo[f][c]);
				}
			}
			else
			{
				if (pq.contains(c*N+f))
				{
					pq.decreaseKey(c*N+f, distTo[f][c]);
				}
				else
				{
					pq.insert(c*N+f, distTo[f][c]);
				}
			}
		}
	}

	/**
	 * Returns the length of a shortest path between the source vertex {@code s} and
	 * vertex {@code v}.
	 *
	 * @param  v the destination vertex
	 * @return the length of a shortest path between the source vertex {@code s} and
	 *         the vertex {@code v}; {@code Double.POSITIVE_INFINITY} if no such path
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public double distTo(int m, int n) {
		validateVertex(m, n);
		return distTo[m][n];
	}

	/**
	 * Returns true if there is a path between the source vertex {@code s} and
	 * vertex {@code v}.
	 *
	 * @param  v the destination vertex
	 * @return {@code true} if there is a path between the source vertex
	 *         {@code s} to vertex {@code v}; {@code false} otherwise
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public boolean hasPathTo(int m, int n) {
		validateVertex(m, n);
		return distTo[m][n] < Double.POSITIVE_INFINITY;
	}

	/**
	 * Returns a shortest path between the source vertex {@code s} and vertex {@code v}.
	 *
	 * @param  v the destination vertex
	 * @return a shortest path between the source vertex {@code s} and vertex {@code v};
	 *         {@code null} if no such path
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public Iterable<Arco> pathTo(int m, int n) {
		validateVertex(m, n);
		if (!hasPathTo(m, n)) return null;
		Stack<Arco> path = new Stack<Arco>();
		int x = m;
		int y = n;
		for (Arco e = edgeTo[m][n]; e != null; e = edgeTo[m][n]) {
			path.push(e);
			m = e.other(m, n)[0];
			n = e.other(m, n)[1];

		}
		return path;
	}


	// check optimality conditions:
	// (i) for all edges e = v-w:            distTo[w] <= distTo[v] + e.weight()
	// (ii) for all edge e = v-w on the SPT: distTo[w] == distTo[v] + e.weight()
	private boolean check(Graph G, int x ,int y) {

		// check that edge weights are nonnegative
		for (Arco e : (Iterable<Arco>) G.arcos) {
			if (e.darCosto() < 0) {
				System.err.println("negative edge weight detected");
				return false;
			}
		}

		// check that distTo[v] and edgeTo[v] are consistent
		if (distTo[x][y] != 0.0 || edgeTo[x][y] != null) {
			System.err.println("distTo[s] and edgeTo[s] inconsistent");
			return false;
		}
		for (int f = 0; f < M; f++) {
			for (int c = 0; c < N; c++) {
				if (f == x && c == y) continue;
				if (edgeTo[f][c] == null && distTo[f][c] != Double.POSITIVE_INFINITY) {
					System.err.println("distTo[] and edgeTo[] inconsistent");
					return false;
				}
			}
		}

		// check that all edges e = v-w satisfy distTo[w] <= distTo[v] + e.weight()
		for (int f = 0; f < M; f++) {
			for (int c = 0; c < N; c++) {
				for (Arco e : ((Vertice) G.getInfoVertex(f, c)).darArcos()) {
					int m = e.other(f, c)[0];
					int n = e.other(f, c)[1];
					if (distTo[f][c] + e.darCosto() < distTo[m][n]) {
						System.err.println("edge " + e + " not relaxed");
						return false;
					}
				}
			}
		}

		// check that all edges e = v-w on SPT satisfy distTo[w] == distTo[v] + e.weight()
		for (int f = 0; f < M; f++) {
			for (int c = 0; c < N; c++) {
				if (edgeTo[f][c] == null) continue;
				Arco e = edgeTo[f][c];
				if (f != e.either()[0] && f != e.other(e.either()[0], e.either()[1])[0] && c != e.either()[1] && c != e.other(e.either()[0], e.either()[1])[1]) return false;
				int m = e.other(f, c)[0];
				int n = e.other(f, c)[1];
				if (distTo[m][n] + e.darCosto() != distTo[f][c]) {
					System.err.println("edge " + e + " on shortest path not tight");
					return false;
				}
			}
		}
		return true;
	}

	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertex(int f, int c) {
		if (f < 0 || f >= M)
			throw new IllegalArgumentException("vertex " + f + " is not between 0 and " + (M-1));
		if (c < 0 || c >= N)
			throw new IllegalArgumentException("vertex " + c + " is not between 0 and " + (N-1));
	}
}