package controller;

import java.util.InputMismatchException;
import java.util.Scanner;

import model.data_structures.ArregloDinamico;
import model.logic.Arco;
import model.logic.MVCModelo;
import view.MVCView;

public class Controller {

	/* Instancia del Modelo*/
	private MVCModelo modelo;

	/* Instancia de la Vista*/
	private MVCView view;

	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller()
	{
		view = new MVCView();
		modelo = new MVCModelo();
	}

	/**
	 * Hilo de ejecución del programa
	 */
	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;

		while(!fin)
		{
			view.printMenu();

			String in;
			in = lector.next();

			int option;
			try
			{
				option = Integer.parseInt(in);
			}
			catch(NumberFormatException e)
			{
				option = 0;
			}

			switch(option){
			case 1:

				System.out.println("--------- \nEscribir nombre del archivo (sin el .txt): ");
				System.out.println("El archivo debe estar en la carpeta /data");
				String nombreArchivo = lector.next();

				try
				{
					ArregloDinamico<Arco> arcos = modelo.cargarGrafo(nombreArchivo);
					System.out.println("Grafo cargado");
					System.out.println("Cantidad de arcos cargados: " + modelo.darNumeroArcos() + "\n---------");

					for (int i = 0; i < arcos.darTamano(); i++)
					{
						Arco actual = arcos.darElemento(i);
						System.out.println("(" + actual.darFOrigen() + "," + actual.darCOrigen() + ") - (" + actual.darFDest() + "," + actual.darCDest() + ")");
					}
					System.out.println("---------");

				}
				catch(Exception e)
				{					
					System.out.println("Se produjo un error al cargar el grafo.");
					e.printStackTrace();
				}

				break;

			case 2:

				ArregloDinamico<Arco> arcos = modelo.ponerPesos();

				System.out.println("Grafo cargado");
				System.out.println("Cantidad de arcos cargados: " + modelo.darNumeroArcos() + "\n---------");

				for (int i = 0; i < arcos.darTamano(); i++)
				{
					Arco actual = arcos.darElemento(i);
					System.out.println("(" + actual.darFOrigen() + "," + actual.darCOrigen() + ") - (" + actual.darFDest() + "," + actual.darCDest() + ") // Peso: " + actual.darCosto());
				}
				System.out.println("---------");

				break;

			case 3:

				int fIni;
				int cIni;
				int fFin;
				int cFin;
				try
				{
					System.out.println("---------\nDar fila del vértice de origen: ");
					fIni = lector.nextInt();
					System.out.println("---------\nDar columna del vértice de origen: ");
					cIni = lector.nextInt();
					System.out.println("---------\nDar fila del vértice de destino: ");
					fFin = lector.nextInt();
					System.out.println("---------\nDar columna del vértice de destino: ");
					cFin = lector.nextInt();
				}
				catch(InputMismatchException e)
				{
					option = 0;
					break;
				}

				if(fIni >= 0 && fIni < modelo.darM() && fFin >= 0 && fFin < modelo.darM() && cIni >= 0 && cIni < modelo.darN() && cFin >= 0 && cFin < modelo.darN())
				{
					int[][] vertices = modelo.caminoMasCorto(fIni, cIni, fFin, cFin);

					if(vertices != null)
					{
						for(int i = 0; i < modelo.darM(); i++)
						{
							for(int j = 0; j < modelo.darN(); j++)
							{	
								if(vertices[i][j] >= 0)
									System.out.print(vertices[i][j]);
								else
									System.out.print("X");
							}
							System.out.println();
						}
						System.out.println("---------");
					}
					else
					{
						System.out.println("No hay ningun camino entre los vértices\n---------");
					}
				}
				else
				{
					System.out.println("Ingrese un valor válido de filas y columnas\n---------");	
				}

				break;

			case 4: 
				System.out.println("--------- \n Hasta pronto !! \n---------"); 
				lector.close();
				fin = true;
				break;

			default: 
				System.out.println("--------- \n Opcion Invalida !! \n---------");
				break;
			}
		}
	}	
}