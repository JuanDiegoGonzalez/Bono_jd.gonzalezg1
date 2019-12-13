package model.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

import model.logic.Arco;
import model.logic.Vertice;

/** 
 * Implementación tomada de Algorithms 4th edition by Robert Sedgewick and Kevin Wayne (2011)
 * Consultado el 04/11/19
 * Disponible en https://algs4.cs.princeton.edu/code/
 */
public class Graph<K,V>
{
	private static final String NEWLINE = System.getProperty("line.separator");

	private int V;
	private int M;
	private int N;
	private int E;
	public boolean[][] Marked;
	private Bag<V>[][] adj;
	public ArregloDinamico<Arco> arcos;

	/**
	 * Initializes an empty graph with {@code V} vertices and 0 edges.
	 * param V the number of vertices
	 *
	 * @param  V number of vertices
	 * @throws IllegalArgumentException if {@code V < 0}
	 */
	public Graph(int m, int n) {
		if (m*n < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
		arcos = new ArregloDinamico<>(1);
		this.M = m;
		this.N = n;
		this.E = 0;
		adj = new Bag[m][n];
		for (int f = 0; f < M; f++)
		{
			for(int c = 0; c < N; c++)
			{
				adj[f][c] = new Bag<V>();
			}
		}

		Marked = new boolean[m][n];
	}

	/**
	 * Returns the number of vertices in this graph.
	 * @return the number of vertices in this graph
	 */
	public int V() {
		return V;
	}

	/**
	 * Returns the number of edges in this graph.
	 * @return the number of edges in this graph
	 */
	public int E() {
		return E;
	}

	public int M() {
		return M;
	}

	public int N() {
		return N;
	}

	public int size() {
		return M*N;
	}

	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertex(int m, int n) {
		if (m < 0 || m > M)
			throw new IllegalArgumentException("vertex " + m + " is not between 0 and " + (M-1));
		if (n < 0 || n > N)
			throw new IllegalArgumentException("vertex " + n + " is not between 0 and " + (N-1));
	}

	/**
	 * Returns the vertices adjacent to vertex {@code v}.
	 * @param  v the vertex
	 * @return the vertices adjacent to vertex {@code v}, as an iterable
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public Iterable<V> adj(int m, int n) {
		validateVertex(m, n);
		return adj[m][n];
	}

	/**
	 * Returns the degree of vertex {@code v}.
	 *
	 * @param  v the vertex
	 * @return the degree of vertex {@code v}
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public int degree(int m, int n)
	{
		validateVertex(m, n);
		return adj[m][n].size();
	}

	public void addEdge(K fVertexIni, K cVertexIni, K fVertexFin, K cVertexFin, int costo)
	{
		validateVertex((int) fVertexIni, (int) cVertexIni);
		validateVertex((int) fVertexFin, (int) cVertexFin);

		E++;

		if(adj[(int) fVertexIni][(int)cVertexIni].size() != 0 && adj[(int) fVertexFin][(int) cVertexFin].size() != 0)
		{
			adj[(int) fVertexIni][(int) cVertexIni].add((V) new Vertice((int)fVertexFin, (int)cVertexFin));
			adj[(int) fVertexFin][(int) cVertexFin].add((V) new Vertice((int)fVertexIni, (int)cVertexIni));

			Arco nuevo = new Arco((int)fVertexIni, (int)cVertexIni, (int)fVertexFin, (int)cVertexFin, costo);
			
			arcos.agregar(nuevo);
			
			Vertice ini = (Vertice) adj[(int) fVertexIni][(int) cVertexIni].iterator().next();
			Vertice fin = (Vertice) adj[(int) fVertexFin][(int) cVertexFin].iterator().next();
			
			ini.agregarArco(nuevo);
			fin.agregarArco(nuevo);
		}
	}

	public V getInfoVertex(K fVertex, K cVertex)
	{
		if(adj[(int) fVertex][(int) cVertex].size() != 0)
			return (V) adj[(int) fVertex][(int) cVertex].iterator().next();
		else
			return null;
	}

	public void setInfoVertex(K fVertex, K cVertex, V infoVertex)
	{
		adj[(int) fVertex][(int) cVertex].cambiarPrimero(infoVertex);

		Iterator<V> x = adj[(int) fVertex][(int) cVertex].iterator();
		x.next();
		while(x.hasNext())
		{
			Vertice actual1 = (Vertice) x.next();

			adj[(int) actual1.F()][(int) actual1.C()].cambiarItem((Vertice) infoVertex);			
		}		
	}

	public double getCostArc(K fVertexIni, K cVertexIni, K fVertexFin, K cVertexFin)
	{
		return arcos.buscar(new Arco((int)fVertexIni, (int)cVertexIni, (int)fVertexFin, (int)cVertexFin, 0)).darCosto();
	}

	public void setCostArc(K fVertexIni, K cVertexIni, K fVertexFin, K cVertexFin, int cost)
	{
		arcos.buscar(new Arco((int)fVertexIni, (int)cVertexIni, (int)fVertexFin, (int)cVertexFin, 0)).cambiarcosto(cost);
	}

	public void addVertex(K fVertex, K cVertex, V infoVertex)
	{
		adj[(int) fVertex][(int) cVertex].add(infoVertex);
		V++;
	}

	public Iterable<K> adj(K fVertex, K cVertex)
	{
		Stack<Integer[]> respuesta = new Stack<>();

		Iterator<V> x = adj[(int) fVertex][(int) cVertex].iterator();
		x.next();

		while(x.hasNext())
		{
			Vertice actual = (Vertice) x.next();
			Integer[] dato = new Integer[2];
			dato[0] = actual.F();
			dato[1] = actual.C();

			respuesta.push(dato);
		}
		return (Iterable<K>) respuesta;		
	}

	public void uncheck()
	{
		for(int i = 0; i < M; i++)
		{
			for(int j = 0; j < N; j++)
			{
				Marked[i][j] = false;
			}
		}
	}

	public void dfs(int x, int y)
	{
		NonrecursiveDFS dfs = new NonrecursiveDFS(this, x, y);
		Marked = dfs.marked;
	}

	public int cc()
	{
		int count = 0; 
		for (int f = 0; f < M; f++)
		{
			for (int c = 0; c < N; c++)
			{
				System.out.println("Revisando vértice #" + f + ", " + c);
				if(!adj[f][c].isEmpty() && !Marked[f][c]) 
				{
					dfs(f, c);
					count++;
				}
			}
		}
		return count; 
	}

	public Iterable<K> getCC(K fVertex, K cVertex) {

		dfs((int)fVertex, (int)cVertex);

		Stack<Integer[]> respuesta = new Stack<>();

		for (int f = 0; f < M; f++)
		{
			for (int c = 0; c < N; c++)
			{
				if(Marked[f][c] == true)
				{
					Vertice actual = (Vertice) adj[f][c].iterator().next();
		
					Integer[] dato = new Integer[2];
					dato[0] = actual.F();
					dato[1] = actual.C();

					respuesta.push(dato);
				}			
			}		
		}

		return (Iterable<K>) respuesta;	
	}
}