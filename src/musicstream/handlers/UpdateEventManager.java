//package musicstream.handlers;
//
//import java.util.Dictionary;
//import java.util.Hashtable;
//
//import javax.inject.Inject;
//
//import org.eclipse.e4.core.contexts.IEclipseContext;
//import org.eclipse.e4.core.services.events.IEventBroker;
//import org.osgi.framework.BundleContext;
//import org.osgi.framework.FrameworkUtil;
//import org.osgi.service.event.Event;
//import org.osgi.service.event.EventConstants;
//import org.osgi.service.event.EventHandler;
//
//public class UpdateEventManager  {
//	@Inject
//	private IEventBroker broker;
//	private IEclipseContext ctx ;
//	private EventsConstants evc;
//
//	  public void UpdateEventManager() {
//	 
//	  
//	    BundleContext ctx = FrameworkUtil.getBundle(UpdateEventManager.class).getBundleContext();
//	    EventHandler handler = new EventHandler() {
//	    public void handleEvent(final Event event) {
//	     	event.getProperty("SoundCloud");
//    		System.out.println(event.getProperty("SoundCloud"));
//		
//	    	}
//	    };
//	    Dictionary<String,String> properties = new Hashtable<String, String>();
//	    properties.put(EventConstants.EVENT_TOPIC, "viewcommunication/*");
//	    ctx.registerService(EventHandler.class, handler, properties);
//	  }
//
//}
