package squarewarrior;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
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

//import com.sun.prism.Graphics;
//import com.sun.prism.Image;
public class GameOptions extends JFrame implements ActionListener{
	
	Container content;
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	GridLayout layout;


	GameOptions(String name)
	{
		super(name);
		setSize((int) screenSize.getWidth()/6,(int) ((int)screenSize.getHeight()/3));
		layout = new GridLayout(0,1,0,6);// infinite rows, 1 col.
		content = getContentPane();
		content.setLayout(layout);
		
		content.setBackground(Color.LIGHT_GRAY);
		
		JButton slowMode = new JButton("Option X");
		JButton bulletColor = new JButton("Option Y");//use a Jcolorchooser
		JButton charColor = new JButton("Option Z");//same
		JButton enemyColor = new JButton("Option W");//same
	
		
		Font ryeFont = new Font("Courier", Font.BOLD,32);
		JLabel title = new JLabel("GamePlay Options");
		title.setFont(ryeFont);
	
		content.add(title);
		content.add(slowMode);
		content.add(bulletColor);
		content.add(charColor);
		content.add(enemyColor);	
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GameOptions s = new GameOptions("Settings Window");
		s.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
