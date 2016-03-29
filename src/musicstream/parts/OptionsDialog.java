package musicstream.parts;

import javax.inject.Inject;

import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;;

/**
 * @author Malek Dialog Window to be displayed when the User chooses to change
 *         Service
 */
public class OptionsDialog extends Dialog {
	@Inject
	private MDirtyable dirty;
	private Button btnSoundCloud, btnDeezer;
	private Boolean soundCloud, deezer;

	public OptionsDialog(Shell parentShell) {
		super(parentShell);

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		Label lblUser = new Label(container, SWT.NONE);
		lblUser.setText("Please Select the services the use : ");

		btnSoundCloud = new Button(container, SWT.CHECK);
		btnSoundCloud.setSelection(true);
		btnSoundCloud.setGrayed(true);
		btnSoundCloud.setText("Sound Cloud");

		btnDeezer = new Button(container, SWT.CHECK);
		btnDeezer.setSelection(true);
		btnDeezer.setText("Deezer");
		return container;
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, "Select", true);
		button.setText("Select");
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	protected void okPressed() {
		this.soundCloud = btnSoundCloud.getSelection();
		this.deezer = btnDeezer.getSelection();
		super.okPressed();
	}

	public Boolean getSoundcloud() {
		return this.soundCloud;
	}

	public void setSoundcloud(Boolean soundCloud) {
		this.soundCloud = soundCloud;
	}

	public Boolean getDeezer() {
		return deezer;
	}

	public void setDeezer(Boolean deezer) {
		this.deezer = deezer;
	}

}