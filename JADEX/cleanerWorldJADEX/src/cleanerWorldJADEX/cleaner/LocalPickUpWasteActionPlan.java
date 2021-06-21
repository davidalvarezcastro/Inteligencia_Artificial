package cleanerWorldJADEX.cleaner;

import cleanerWorldJADEX.IEnvironment;
import cleanerWorldJADEX.Waste;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.bdiv3x.runtime.Plan;


/**
 *  Pick up a piece of waste in the environment.
 */
public class LocalPickUpWasteActionPlan extends	Plan
{
	//-------- methods --------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		IEnvironment	environment	= (IEnvironment)getBeliefbase().getBelief("environment").getFact();
		Waste waste = (Waste)getParameter("waste").getValue();

		boolean	success	= environment.pickUpWaste(waste);

		if(!success)
			throw new PlanFailureException();
	}
}
