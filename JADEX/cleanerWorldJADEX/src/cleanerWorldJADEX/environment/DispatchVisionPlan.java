package cleanerWorldJADEX.environment;

import cleanerWorldJADEX.Cleaner;
import cleanerWorldJADEX.Environment;
import cleanerWorldJADEX.RequestVision;
import cleanerWorldJADEX.Vision;
import jadex.bdiv3x.runtime.Plan;
import jadex.bridge.fipa.Done;

/**
 *  The dispatch vision plan calculates the vision for a
 *  participant and send it back.
 */
public class DispatchVisionPlan extends Plan
{
	//------ methods -------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		RequestVision rv = (RequestVision)getParameter("action").getValue();
		Cleaner cl = rv.getCleaner();
		Environment env = (Environment)getBeliefbase().getBelief("environment").getFact();
		Vision v = env.getVision(cl);

		rv.setVision(v);
		Done done = new Done();
		done.setAction(rv);
		getParameter("result").setValue(done);
	}
}
