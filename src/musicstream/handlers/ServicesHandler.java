package musicstream.handlers;

import java.util.HashMap;

import javax.inject.Inject;
import javax.swing.text.View;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
//import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
//import org.eclipse.ui.IWorkbenchPage;
//import org.eclipse.ui.PlatformUI;

import musicstream.parts.OptionsDialog;
import musicstream.parts.TracksPart;

/**
 * @author Malek
 *Services Menu Item Handler 
 *
 */
public class ServicesHandler   {
	@Inject EPartService partService;
	@Execute
	  public void execute(Shell shell) {
		OptionsDialog opDiloag= new OptionsDialog(shell);//Opening Dialog
		 if (opDiloag.open() == Window.OK) {
			 HashMap<String, Boolean> services= new HashMap<String, Boolean>();// Map contains the statuts of the CHeckBox Buttons
			 services.put("SoundCloud", opDiloag.getSoundcloud());
			 services.put("Deezer", opDiloag.getDeezer());
			 
			 partService.getParts();//Getting Parts 
			 MPart part = partService.findPart("com.part.globalTracks");//Getting Tracks Part
			 MPart partDeezer = partService.findPart("musicstream.part.tracksDeezer");//Getting Deezer Tracks Part
			 MPart partSc = partService.findPart("musicstream.part.tracksSc");//Getting Sound CLoud Tracks Part
			 /* Display and hiding parts basing on the User's choice*/
			 if(services.get("SoundCloud")&& services.get("Deezer") ){
				 part.setVisible(true);
				 partDeezer.setVisible(false);
				 partSc.setVisible(false);
				 partService.showPart(part, PartState.ACTIVATE);
			 }else
			 {
				 if(services.get("SoundCloud")&& !services.get("Deezer") ){
					 part.setVisible(false);
					 partSc.setVisible(true);
					 partService.showPart(partSc, PartState.ACTIVATE);
					 partDeezer.setVisible(false);
					
			 
				 }else
				 {
					 if(!services.get("SoundCloud")&& services.get("Deezer") ){
						 part.setVisible(false);
						 partDeezer.setVisible(true);
						 partSc.setVisible(false);
						 partService.showPart(partDeezer, PartState.ACTIVATE);
					 }
				 }
			 }
			 
		 }
		 
	 
	}
 
 
}
