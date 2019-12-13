package view;

public class MVCView 
{
	/**
	 * Metodo constructor
	 */
	public MVCView()
	{}

	/**
	 * Método que imprime el menú por consola
	 */
	public void printMenu()
	{
		System.out.println("1. a) Cargar grafo de un archivo de texto");
		System.out.println("2. b) Convertir el grafo inicial a un grafo con pesos");
		System.out.println("3, c) y d) Encontrar y mostrar el camino más corto entre dos vértices");
		System.out.println("4. Exit");
		System.out.println("Dar el numero de opcion a resolver, luego oprimir tecla Return: (e.g., 1):");
	}
}