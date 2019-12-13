package model.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import model.data_structures.ArregloDinamico;
import model.data_structures.DijkstraUndirectedSP;
import model.data_structures.Graph;
import model.data_structures.Queue;
import model.data_structures.Stack;

/**
 * Definicion del modelo del mundo
 */
public class MVCModelo{

	/**
	 * Atributos del modelo del mundo
	 */
	private Graph<Integer, Vertice> grafo;
	private int[][] verticesEnNumeros;

	/**
	 * Constructor del modelo del mundo
	 */
	public MVCModelo()
	{}

	public ArregloDinamico<Arco> cargarGrafo(String nombreArchivo) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(new File("data/" + nombreArchivo + ".txt")));
		String linea = br.readLine();

		int filas = 0;
		int columnas = linea.length();

		Queue<Integer> vertices = new Queue<>();

		while(linea != null)
		{
			filas++;

			for(int i = 0; i < columnas; i++)
				vertices.enqueue(Integer.parseInt(linea.charAt(i) + ""));

			linea = br.readLine();
		}
		br.close();

		grafo = new Graph<>(filas, columnas);
		int[][] pVertices = new int[filas][columnas];
		verticesEnNumeros = pVertices;

		for(int i = 0; i < filas; i++)
		{
			for(int j = 0; j < columnas; j++)
			{
				if(vertices.dequeue() == 1)
				{
					grafo.addVertex(i, j, new Vertice(i, j));
					verticesEnNumeros[i][j] = 1;
				}
				else
				{
					verticesEnNumeros[i][j] = 0;
				}
			}
		}

		for(int i = 0; i < filas; i++)
		{
			for(int j = 0; j < columnas; j++)
			{
				if(verticesEnNumeros[i][j] == 1)
				{
					if(i != 0 && verticesEnNumeros[i-1][j] == 1 && grafo.arcos.buscar(new Arco(i-1, j, i, j, 0)) == null)
					{
						grafo.addEdge(i, j, i-1, j, 1);
					}
					if(i+1 != filas && verticesEnNumeros[i+1][j] == 1 && grafo.arcos.buscar(new Arco(i+1, j, i, j, 0)) == null)
					{
						grafo.addEdge(i, j, i+1, j, 1);
					}
					if(j != 0 && verticesEnNumeros[i][j-1] == 1 && grafo.arcos.buscar(new Arco(i, j-1, i, j, 0)) == null)
					{
						grafo.addEdge(i, j, i, j-1, 1);
					}
					if(j+1 != columnas && verticesEnNumeros[i][j+1] == 1 && grafo.arcos.buscar(new Arco(i, j+1, i, j, 0)) == null)
					{
						grafo.addEdge(i, j, i, j+1, 1);
					}
				}
			}
		}
		return grafo.arcos;
	}

	//----------------------------
	//METODOS
	//----------------------------

	public int darNumeroVertices()
	{
		return grafo.V();
	}

	public int darNumeroArcos()
	{
		return grafo.E();
	}

	public int darM()
	{
		return grafo.M();
	}

	public int darN()
	{
		return grafo.N();
	}

	public ArregloDinamico<Arco> ponerPesos()
	{
		Graph<Integer, Vertice> nuevoGrafo = new Graph<>(grafo.M(), grafo.N());

		for(int i = 0; i < grafo.M(); i++)
		{
			for(int j = 0; j < grafo.N(); j++)
			{				
				if(verticesEnNumeros[i][j] > 0)
				{
					if(i != 0 && i+verticesEnNumeros[i][j] != grafo.M() && (j-verticesEnNumeros[i][j] < 0 || verticesEnNumeros[i][j-verticesEnNumeros[i][j]] == 0) && (j+verticesEnNumeros[i][j] >= grafo.N() || verticesEnNumeros[i][j+verticesEnNumeros[i][j]] == 0))
					{
						verticesEnNumeros[i][j]++;
					}
					if(j != 0 && j+verticesEnNumeros[i][j] != grafo.N() && (i-verticesEnNumeros[i][j] < 0 || verticesEnNumeros[i-verticesEnNumeros[i][j]][j] == 0) && (i+verticesEnNumeros[i][j] >= grafo.M() || verticesEnNumeros[i+verticesEnNumeros[i][j]][j] == 0))
					{
						verticesEnNumeros[i][j]++;
					}
				}
			}
		}		

		int a = 0;

		while(a < Math.max(grafo.M(), grafo.N()))
		{
			a++;

			for(int i = 0; i < grafo.M(); i++)
			{
				for(int j = 0; j < grafo.N(); j++)
				{
					if(verticesEnNumeros[i][j] == 1)
					{
						if(i-1 >= 0 && i-verticesEnNumeros[i-1][j] >= 0)
						{
							if(verticesEnNumeros[i-verticesEnNumeros[i-1][j]][j] > 1)
							{
								verticesEnNumeros[i-1][j]++;
							}
						}
						if(i+1 < grafo.M() && i+verticesEnNumeros[i+1][j] < grafo.M())
						{
							if(verticesEnNumeros[i+verticesEnNumeros[i+1][j]][j] > 1)
							{
								verticesEnNumeros[i+1][j]++;
							}
						}
						if(j-1 >= 0 && j-verticesEnNumeros[i][j-1] >= 0)
						{
							if(verticesEnNumeros[i][j-verticesEnNumeros[i][j-1]] > 1)
							{
								verticesEnNumeros[i][j-1]++;							
							}
						}
						if(j+1 < grafo.N() && j+verticesEnNumeros[i][j+1] < grafo.N())
						{
							if(verticesEnNumeros[i][j+verticesEnNumeros[i][j+1]] > 1)
							{
								verticesEnNumeros[i][j+1]++;						
							}
						}
					}
				}
			}
		}
		for(int i = 0; i < grafo.M(); i++)
		{
			for(int j = 0; j < grafo.N(); j++)
			{
				if(verticesEnNumeros[i][j] == 1)
				{
					nuevoGrafo.addVertex(i, j, new Vertice(i, j));
				}
			}
		}

		for(int i = 0; i < grafo.M(); i++)
		{
			for(int j = 0; j < grafo.N(); j++)
			{
				if(verticesEnNumeros[i][j] == 1)
				{
					if(i-1 >= 0 && verticesEnNumeros[i-1][j] >= 1 && nuevoGrafo.arcos.buscar(new Arco(i-verticesEnNumeros[i-1][j], j, i, j, 0)) == null)
					{
						nuevoGrafo.addEdge(i, j, i-verticesEnNumeros[i-1][j], j, verticesEnNumeros[i-1][j]);
					}
					if(i+1 < grafo.M() && verticesEnNumeros[i+1][j] >= 1 && nuevoGrafo.arcos.buscar(new Arco(i+verticesEnNumeros[i+1][j], j, i, j, 0)) == null)
					{
						nuevoGrafo.addEdge(i, j, i+verticesEnNumeros[i+1][j], j, verticesEnNumeros[i+1][j]);
					}
					if(j-1 >= 0 && verticesEnNumeros[i][j-1] >= 1 && nuevoGrafo.arcos.buscar(new Arco(i, j-verticesEnNumeros[i][j-1], i, j, 0)) == null)
					{
						nuevoGrafo.addEdge(i, j, i, j-verticesEnNumeros[i][j-1], verticesEnNumeros[i][j-1]);
					}
					if(j+1 < grafo.N() && verticesEnNumeros[i][j+1] >= 1 && nuevoGrafo.arcos.buscar(new Arco(i, j+verticesEnNumeros[i][j+1], i, j, 0)) == null)
					{
						nuevoGrafo.addEdge(i, j, i, j+verticesEnNumeros[i][j+1], verticesEnNumeros[i][j+1]);
					}
				}
			}
		}

		grafo = nuevoGrafo;

		return grafo.arcos;
	}

	public int[][] caminoMasCorto(int fOrigen, int cOrigen, int fDestino, int cDestino)
	{
		DijkstraUndirectedSP dijkstra = new DijkstraUndirectedSP(grafo, fOrigen, cOrigen);

		Stack<Arco> arcos = (Stack<Arco>) dijkstra.pathTo(fDestino, cDestino);

		if(arcos != null)
		{
			int[][] respuesta = verticesEnNumeros;
			
			while(!arcos.isEmpty())
			{
				Arco actual = arcos.pop();
				respuesta[actual.darFOrigen()][actual.darCOrigen()] = -1;
				respuesta[actual.darFDest()][actual.darCDest()] = -1;
			}
			return respuesta;
		}
		else
			return null;
	}
}