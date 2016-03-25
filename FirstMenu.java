package squarewarrior;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.tools.Tool;

import org.omg.Messaging.SyncScopeHelper;

//import com.sun.prism.Graphics;
//import com.sun.prism.Image;

/*
 * 
 * The layout is the hardest part of the gui. I tried to make everything in respect
 * to screensize so it will maintain an aspect ratio, hopefully.
 * 
 * 
 */
public class FirstMenu extends JFrame implements ActionListener{

	//this makes widgets appear relative to screen size, I think.
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Container content;//the main container for the window
	BoxLayout layout;//the layout content will use
	Graphics2D g2;
	ActionListener buttonListener;
	SettingsWindow s = new SettingsWindow("Game Settings");
	GameOptions g = new GameOptions("Gameplay Settings");
	
	GridBagLayout buttons;//creates where the buttons will be
	Container buttonsContent;
	
	private BufferedImage logo;
	
	//put the game title on the upper row, options pane on the botto row.
	FirstMenu(String name)
	{
		
		//all the fun setup stuff, dont change this.
		super(name);
		logo = new BufferedImage(317,125,1);
		setSize((int) screenSize.getWidth()/3,(int) ((int)screenSize.getHeight()/1.5));
		content = getContentPane();
		layout = new BoxLayout(content, BoxLayout.PAGE_AXIS);
		content.setLayout(layout);
		
		 
		//makes all the buttons
		JLabel tempLogo = new JLabel();
		//put an image on the label.
		tryToAttachImage();
		tempLogo.setIcon(new ImageIcon(logo));
		
		
		
		JButton gameOptions = new JButton("Game Options");
		JButton settings = new JButton("Settings");
		JButton play = new JButton("Play");
		JButton exit = new JButton("Exit");
		
		//extra buttons for unimplemented options
		JButton placeHolder1 = new JButton("Placeholder");
		JButton placeHolder2 = new JButton("Placeholder");
		
		//will hold the Squaremagon title art
		JLabel logoHolder = new JLabel();
		logoHolder.setIcon(new ImageIcon(logo));
		//makes it centered both veritcally and hor.
		tempLogo.setHorizontalAlignment(JLabel.CENTER);
		tempLogo.setVerticalAlignment(JLabel.CENTER);
		JPanel logoPanel = new JPanel();
		logoPanel.setLayout(new GridLayout(1,0));
		logoPanel.setBackground(Color.cyan);
		logoPanel.add(logoHolder, 0,0);
		//logoPanel.setIcon(new ImageIcon(backDrop));
		content.add(logoPanel);//plop dat on the main container
		
		JPanel space1 = new JPanel();//will hold main options
		JPanel space2= new JPanel();//will hold future options
		
		//makes the bottom panel (the placeholders) look nice and not take up too much space.
		//you can adjust the height by messing with param 2.
		space2.setPreferredSize(new Dimension((int) screenSize.getWidth()/2, (int) ((screenSize.getHeight()/1.5)/5)));
		
		content.add(space1);//holds the main options
		content.add(space2);//holds the placeholder options
		
		//this adds the buttons, one ontop of another, in jpanel 2 of 3.
		space1.setLayout(new GridLayout(0, 1));//one column only
		space1.add(gameOptions);
		space1.add(settings);
		space1.add(play);
		space1.add(exit);
		
		//lets the bottom jpanel layout side by side, not ontop like above
		space2.setLayout(new BoxLayout(space2, BoxLayout.X_AXIS));//arranges left to right
		
		space2.add(placeHolder1);//boom.
		
		//add a space to make it look pretty
		//increase x param to make wider
		space2.add(Box.createRigidArea(new Dimension(200,0)));
		space2.add(placeHolder2);
		
		//Add the listeners--------------
		
		gameOptions.addActionListener(this);
		settings.addActionListener(this);
		play.addActionListener(this);
		exit.addActionListener(this);
	}
	
	public static void main(String[] args)
	{
		FirstMenu ff = new FirstMenu("tester");
		ff.setVisible(true);
	}

	private void tryToAttachImage()
	{
//		File sourceimage = new File("C:\\Users\\Ryan's PC\\Desktop\\SWE2Group\\src\\t1.png");
		try{
			File sourceimage = new File("logo.png");
			System.out.println("Image Imported!");
			try {
				
				logo = ImageIO.read(sourceimage);
				
			} catch (IOException e) {
				System.out.println("couldn't read image");
				e.printStackTrace();
			}
		}
		catch(Error e){
			System.out.println("Couldn't import image");	
			e.printStackTrace();
		}
		
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {


		String buttonText = ((JButton) arg0.getSource()).getText();
		
		//based on which button....
		switch(buttonText){
		case "Game Options":
			g.setVisible(true);
			break;
			
		case "Settings":
			s.setVisible(true);//brings up the settings menu
			break;
			
		case "Play":
			this.setVisible(false);
			break;
			
		case "Exit":
			System.exit(1);
			break;
		}
			
		
	}
	
	
	
}
