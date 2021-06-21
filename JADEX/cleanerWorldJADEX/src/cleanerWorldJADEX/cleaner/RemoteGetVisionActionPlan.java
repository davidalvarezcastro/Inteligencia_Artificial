package cleanerWorldJADEX.cleaner;

import cleanerWorldJADEX.Cleaner;
import cleanerWorldJADEX.Location;
import cleanerWorldJADEX.RequestVision;
import cleanerWorldJADEX.Vision;
import cleanerWorldJADEX.Waste;
import jadex.bdiv3.runtime.IGoal;
import jadex.bridge.fipa.Done;


/**
 *  Get the vision.
 */
public class RemoteGetVisionActionPlan extends RemoteActionPlan
{
	//-------- methods --------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		Cleaner cl = new Cleaner((Location)getBeliefbase().getBelief("my_location").getFact(), getComponentName(),
			(Waste)getBeliefbase().getBelief("carriedwaste").getFact(),
			((Number)getBeliefbase().getBelief("my_vision").getFact()).doubleValue(),
			((Number)getBeliefbase().getBelief("my_chargestate").getFact()).doubleValue());

		RequestVision rv = new RequestVision();
		rv.setCleaner(cl);
		IGoal	result	= requestAction(rv);

		Vision vision = ((RequestVision)(((Done)result.getParameter("result").getValue()).getAction())).getVision();
		getParameter("vision").setValue(vision);
	}
}
