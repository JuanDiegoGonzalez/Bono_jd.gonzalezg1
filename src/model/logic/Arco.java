package model.logic;

public class Arco implements Comparable<Arco>
{
	private int fOrigen, cOrigen;

	private int fDestino, cDestino;

	private int costo;

	public Arco(int pfOrigen, int pcOrigen, int pfDestino, int pcDestino, int pcosto)
	{
		fOrigen = pfOrigen;
		cOrigen = pcOrigen;
		fDestino = pfDestino;
		cDestino = pcDestino;
		costo = pcosto;
	}

	public int darFOrigen()
	{
		return fOrigen;
	}

	public int darCOrigen()
	{
		return cOrigen;
	}

	public int darFDest()
	{
		return fDestino;
	}

	public int darCDest()
	{
		return cDestino;
	}

	public double darCosto()
	{
		return costo;
	}

	public void cambiarcosto(int param)
	{
		costo = param;
	}

	@Override
	public int compareTo(Arco o)
	{
		if(this.fOrigen == o.fOrigen && this.cOrigen == o.cOrigen && this.fDestino == o.fDestino && this.cDestino == o.cDestino)
			return 0;
		else
			return 1;
	}

	public int[] either() {

		int[] respuesta = new int[2];
		respuesta[0] = fDestino;
		respuesta[1] = cDestino;
		return respuesta;
	}

	public int[] other(int fvertex, int cvertex) {
		if (fvertex == fOrigen && cvertex == cOrigen)
		{
			int[] respuesta = new int[2];
			respuesta[0] = fDestino;
			respuesta[1] = cDestino;
			return respuesta;
		}
		else if (fvertex == fDestino && cvertex == cDestino)
		{
			int[] respuesta = new int[2];
			respuesta[0] = fOrigen;
			respuesta[1] = cOrigen;
			return respuesta;
		}
		else throw new IllegalArgumentException("Illegal endpoint");
	}
}