package cleanerWorldJADEX.cleaner;

import cleanerWorldJADEX.RequestPickUpWaste;
import cleanerWorldJADEX.Waste;


/**
 *  Pick up a piece of waste in the environment.
 */
public class RemotePickUpWasteActionPlan extends RemoteActionPlan
{
	//-------- methods --------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		Waste waste = (Waste)getParameter("waste").getValue();
		RequestPickUpWaste rp = new RequestPickUpWaste();
		rp.setWaste(waste);
		requestAction(rp);
	}
}
