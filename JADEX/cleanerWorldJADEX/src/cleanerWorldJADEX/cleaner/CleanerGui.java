package cleanerWorldJADEX.cleaner;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import jadex.bridge.IComponentStep;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IMonitoringComponentFeature;
import jadex.bridge.service.types.monitoring.IMonitoringEvent;
import jadex.bridge.service.types.monitoring.IMonitoringService.PublishEventLevel;
import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.commons.gui.SGUI;
import jadex.commons.gui.future.SwingIntermediateResultListener;
import jadex.commons.transformation.annotations.Classname;


/**
 *  The gui for the cleaner world example.
 *  Shows the world from the viewpoint of a single agent.
 */
public class CleanerGui	extends JFrame
{
	//-------- constructors --------

	/**
	 *  Shows the gui, and updates it when beliefs change.
	 */
	public CleanerGui(final IExternalAccess agent)
	{
		super(agent.getComponentIdentifier().getName());
		final JPanel map = new CleanerPanel(agent);

		getContentPane().add(BorderLayout.CENTER, map);
		setSize(300, 300);
		setLocation(SGUI.calculateMiddlePosition(this));
		setVisible(true);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				agent.killComponent();
			}
		});		
		
		final Timer	timer	= new Timer(50, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				map.invalidate();
				map.repaint();
			}
		});
		
		timer.start();
		
		agent.scheduleStep(new IComponentStep<Void>()
		{
			@Classname("dispose")
			public IFuture<Void> execute(IInternalAccess ia)
			{
//				bia.addComponentListener(new TerminationAdapter()
//				{
//					public void componentTerminated()
//					{
//						SwingUtilities.invokeLater(new Runnable()
//						{
//							public void run()
//							{
//								timer.stop();
//								dispose();
//							}
//						});
//					}
//				});
				
				ia.getComponentFeature(IMonitoringComponentFeature.class).subscribeToEvents(IMonitoringEvent.TERMINATION_FILTER, false, PublishEventLevel.COARSE)
					.addResultListener(new SwingIntermediateResultListener<IMonitoringEvent>(new IntermediateDefaultResultListener<IMonitoringEvent>()
				{
					public void intermediateResultAvailable(IMonitoringEvent result)
					{
						timer.stop();
						dispose();
					}
				}));
				return IFuture.DONE;
			}
		}).addResultListener(new IResultListener<Void>()
		{
			public void resultAvailable(Void result)
			{
			}
			
			public void exceptionOccurred(Exception exception)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						timer.stop();
						dispose();
					}
				});
			}
		});
	}		
}
