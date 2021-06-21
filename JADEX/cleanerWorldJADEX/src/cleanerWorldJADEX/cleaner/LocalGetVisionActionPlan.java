package cleanerWorldJADEX.cleaner;

import cleanerWorldJADEX.Cleaner;
import cleanerWorldJADEX.IEnvironment;
import cleanerWorldJADEX.Location;
import cleanerWorldJADEX.Vision;
import cleanerWorldJADEX.Waste;
import jadex.bdiv3x.runtime.Plan;


/**
 *  Pick up a piece of waste in the environment.
 */
public class LocalGetVisionActionPlan extends	Plan
{
	//-------- methods --------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		IEnvironment	environment	= (IEnvironment)getBeliefbase().getBelief("environment").getFact();
		Cleaner cl = new Cleaner((Location)getBeliefbase().getBelief("my_location").getFact(),
			getComponentName(),
			(Waste)getBeliefbase().getBelief("carriedwaste").getFact(),
			((Number)getBeliefbase().getBelief("my_vision").getFact()).doubleValue(),
			((Number)getBeliefbase().getBelief("my_chargestate").getFact()).doubleValue());

		Vision	vision	= (Vision)environment.getVision(cl).clone();
//		Vision	vision	= (Vision)environment.getVision(cl);

		getParameter("vision").setValue(vision);
	}
}
