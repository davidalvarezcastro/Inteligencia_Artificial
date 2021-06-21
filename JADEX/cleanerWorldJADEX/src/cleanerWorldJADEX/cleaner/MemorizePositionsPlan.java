package cleanerWorldJADEX.cleaner;

import cleanerWorldJADEX.Location;
import cleanerWorldJADEX.MapPoint;
import jadex.bdiv3x.runtime.Plan;


/**
 *  Memorize the visited positions.
 */
public class MemorizePositionsPlan extends Plan
{
	//-------- attributes --------

	/** The forget factor. */
	protected double forget = 0.01;

	//-------- methods --------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		while(true)
		{
			Location	my_location	= (Location)getBeliefbase().getBelief("my_location").getFact();
			// CAMBIO IV: el campo de visión del agente ya no se utiliza para estimar ubicaciones próximas
			// double	my_vision	= ((Double)getBeliefbase().getBelief("my_vision").getFact()).doubleValue();
			MapPoint[] mps = (MapPoint[])getBeliefbase().getBeliefSet("visited_positions").getFacts();
			for(int i=0; i<mps.length; i++)
			{
				// if(my_location.isNear(mps[i].getLocation(), my_vision))
				// CAMBIO IV: se establece un valor fijo de 0.03 para estimar proximidad
				if(my_location.isNear(mps[i].getLocation(), 0.03))
				{
					// pongo el comentario para mostrar en la demo la salida y comprobar que está bien (descomentar)
					System.out.println("location: " + mps[i].getLocation() + " is near to me: " + my_location + "!!");
					mps[i].setQuantity(mps[i].getQuantity()+1);
					mps[i].setSeen(1);
				}
				else
				{
					double oldseen = mps[i].getSeen();
					double newseen = oldseen - oldseen*forget;
					mps[i].setSeen(newseen);
				}
			}

			//System.out.println("inc: "+SUtil.arrayToString(mps));
			waitFor(300);
		}
	}
}
