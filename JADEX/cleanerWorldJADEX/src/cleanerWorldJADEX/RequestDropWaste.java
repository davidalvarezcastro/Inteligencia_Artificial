/*
 * RequestDropWaste.java Generated by Protege plugin Beanynizer. Changes will be lost!
 */
package cleanerWorldJADEX;

import jadex.bridge.fipa.IComponentAction;


/**
 *  Java class for concept RequestDropWaste of cleaner_beans ontology.
 */
public class RequestDropWaste implements IComponentAction
{
	//-------- attributes ----------

	/** Attribute for slot waste. */
	protected Waste waste;

	/** Attribute for slot wastebinname. */
	protected String wastebinname;

	//-------- constructors --------

	/**
	 *  Default Constructor. <br>
	 *  Create a new <code>RequestDropWaste</code>.
	 */
	public RequestDropWaste()
	{ 
	}

	//-------- accessor methods --------

	/**
	 *  Get the waste of this RequestDropWaste.
	 * @return waste
	 */
	public Waste getWaste()
	{
		return this.waste;
	}

	/**
	 *  Set the waste of this RequestDropWaste.
	 * @param waste the value to be set
	 */
	public void setWaste(Waste waste)
	{
		this.waste = waste;
	}

	/**
	 *  Get the wastebinname of this RequestDropWaste.
	 * @return wastebinname
	 */
	public String getWastebinname()
	{
		return this.wastebinname;
	}

	/**
	 *  Set the wastebinname of this RequestDropWaste.
	 * @param wastebinname the value to be set
	 */
	public void setWastebinname(String wastebinname)
	{
		this.wastebinname = wastebinname;
	}

	//-------- object methods --------

	/**
	 *  Get a string representation of this RequestDropWaste.
	 *  @return The string representation.
	 */
	public String toString()
	{
		return "RequestDropWaste(" + ")";
	}

}
