package model.logic;

import model.data_structures.ArregloDinamico;

public class Vertice implements Comparable<Vertice>
{
	private int F, C;
	
	public ArregloDinamico<Arco> arcos;

	public Vertice(int f, int c)
	{
		F = f;
		C = c;
		arcos = new ArregloDinamico<>(1);
	}

	public int F()
	{
		return F;
	}
	
	public int C()
	{
		return C;
	}

	@Override
	public int compareTo(Vertice o)
	{
		if(this.F == o.F && this.C == o.C)
			return 0;
		else
			return 1;
	}
	
	public void agregarArco(Arco param)
	{
		arcos.agregar(param);
	}

	public ArregloDinamico<Arco> darArcos()
	{
		return arcos;
	}
}