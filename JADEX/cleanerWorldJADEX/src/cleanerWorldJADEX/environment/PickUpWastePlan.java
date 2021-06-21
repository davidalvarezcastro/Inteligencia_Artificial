package cleanerWorldJADEX.environment;

import cleanerWorldJADEX.Environment;
import cleanerWorldJADEX.RequestPickUpWaste;
import cleanerWorldJADEX.Waste;
import jadex.bdiv3x.runtime.Plan;
import jadex.bridge.fipa.Done;

/**
 *  Pick up some piece of waste.
 */
public class PickUpWastePlan extends Plan
{
	//------ methods -------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		RequestPickUpWaste op = (RequestPickUpWaste)getParameter("action").getValue();
		Waste waste = op.getWaste();

		Environment env = (Environment)getBeliefbase().getBelief("environment").getFact();
		boolean success = env.pickUpWaste(waste);

		if(!success)
			fail();

		Done done = new Done();
		done.setAction(op);
		getParameter("result").setValue(done);
	}

}
