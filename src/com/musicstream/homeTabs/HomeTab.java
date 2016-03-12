package com.musicstream.homeTabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.musicstream.utils.AppUtils;

public class HomeTab {

	public HomeTab(Display display) {
		// Display display = new Display();
		Shell shell = new Shell(display);
		AppUtils appU = new AppUtils();
		shell.setLayout(new GridLayout());

		// SWT.BOTTOM to show at the bottom
		CTabFolder folder = new CTabFolder(shell, SWT.CENTER);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		folder.setLayoutData(data);
		CTabItem cTabItem1 = new CTabItem(folder, SWT.NONE);
		cTabItem1.setText("Tracks");
		CTabItem cTabItem2 = new CTabItem(folder, SWT.NONE);
		cTabItem2.setText("Playlists");
		CTabItem cTabItem3 = new CTabItem(folder, SWT.NONE);
		cTabItem3.setText("Search");

		TracksTab tracks = new TracksTab(folder);
	//	cTabItem1.setControl(tracks.getComposite());
		PlaylistsTab playlists = new PlaylistsTab(folder);
		cTabItem2.setControl(playlists.getComposite());
		SearchTab search = new SearchTab(folder);
		cTabItem3.setControl(search.getComposite());

		shell.setSize(appU.getScreenWidth(), appU.getScreenHeight());
		// centrerSurEcran(display,shell);
		// cTabItem1.setControl(text);
		// cTabItem1.setControl(parent);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public static void centrerSurEcran(Display display, Shell shell) {

		Rectangle rect = display.getClientArea();
		Point size = shell.getSize();
		int x = (rect.width - size.x) / 2;
		int y = (rect.height - size.y) / 2;
		shell.setLocation(new Point(x, y));
	}
}
