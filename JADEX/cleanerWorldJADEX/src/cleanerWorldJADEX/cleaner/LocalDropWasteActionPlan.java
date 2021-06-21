package cleanerWorldJADEX.cleaner;

import cleanerWorldJADEX.IEnvironment;
import cleanerWorldJADEX.Waste;
import cleanerWorldJADEX.Wastebin;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.bdiv3x.runtime.Plan;


/**
 *  Pick up a piece of waste in the environment.
 */
public class LocalDropWasteActionPlan extends	Plan
{
	//-------- methods --------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		IEnvironment	environment	= (IEnvironment)getBeliefbase().getBelief("environment").getFact();
		Waste waste = (Waste)getParameter("waste").getValue();
		Wastebin wastebin = (Wastebin)getParameter("wastebin").getValue();

		boolean	success	= environment.dropWasteInWastebin(waste, wastebin);

		if(!success)
			throw new PlanFailureException();
	}
}
