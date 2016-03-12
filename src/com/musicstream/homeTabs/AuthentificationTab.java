package com.musicstream.homeTabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AuthentificationTab {

	private static SoundCloudListener scListener;
	public static Display display;

	public static void main(String[] args) {
		display = new Display();
		Shell shell = new Shell(display);
		scListener = new SoundCloudListener(display, shell);
		shell.setLayout(new GridLayout());
		shell.setSize(397, 371);

		Button soundcloud = new Button(shell, SWT.CENTER);
		soundcloud.setImage(new Image(display, "icons/deezer.png"));
		soundcloud.setBounds(10, 10, 40, 200);
		soundcloud.addMouseListener((MouseListener) scListener);

		Button deezer = new Button(shell, SWT.CENTER);
		deezer.setImage(new Image(display, "icons/soundCloud.png"));
		deezer.setBounds(10, 10, 40, 200);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}

class SoundCloudListener implements MouseListener {
	Display display;
	Shell shell;

	public SoundCloudListener(Display display, Shell shell) {
		this.display = display;
		this.shell = shell;
	}

	@Override
	public void mouseDoubleClick(MouseEvent arg0) {
		new HomeTab(display);
		//shell.close();
	}

	@Override
	public void mouseDown(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseUp(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
