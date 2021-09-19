package fleaMarket;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Klondike.SolitaireK;


public class SolitaireMenu {
	
	public static final int TABLE_HEIGHT = 400;
	public static final int TABLE_WIDTH = 500;
	private static String user;
	
	// GUI COMPONENTS (top level)
	private static final JFrame frame = new JFrame("Solitaire Menu");
	protected static final JPanel menu = new JPanel();
	
	//other components
	private static JButton klondikeStart = new JButton("Klondike");
	private static JButton fleaMarketStart = new JButton("Flea Market");
	private static JButton backgroundColorButton = new JButton("Change Background Color");
	protected static JButton statisticButton = new JButton("Game Statistics");
	private static JLabel gameTypes = new JLabel();// displays the score
	private static JTextField userInput = new JTextField();
	
	//Action Listener
	private static ActionListener ae = new SetUpButtonListeners();
	
	//Color switcher
	private static Color c;
	
	//Color getter
	public static Color getColor() 
	{
		 return c;
	}
	
	private static void setUpColorChange() 
	{
		Container cP;
		
		JFrame colorFrame = new JFrame("Color Change Menu");
		JPanel colorMenu = new JPanel();
		colorFrame.setSize(300, 239);
		colorMenu.setLayout(null);
		
		cP = colorFrame.getContentPane();
		cP.add(colorMenu);
		
		JButton original = new JButton("Default");
		JButton red = new JButton("Red");
		JButton yellow = new JButton("Yellow");
		JButton cyan = new JButton("Cyan");
		JButton blue = new JButton("Blue");
		JButton green = new JButton("Green");
		JButton gray = new JButton("Dark Gray");
		JButton orange = new JButton("Orange");
		JButton pink = new JButton("Pink");
		JButton white = new JButton("White");
		
		JPanel fMTable = SolitaireFM.table;
		JPanel kTable = SolitaireK.table;
		
		class SetUpColorListeners implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(e.getSource() == original) 
				{
					menu.setBackground(Color.GRAY);
					fMTable.setBackground(Color.GRAY);
					kTable.setBackground(Color.GRAY);
					c = Color.GRAY;
				} else if(e.getSource() == red) 
				{
					menu.setBackground(Color.RED);
					fMTable.setBackground(Color.RED);
					kTable.setBackground(Color.RED);
					c = Color.RED;
				} else if(e.getSource() == yellow) 
				{
					menu.setBackground(Color.YELLOW);
					fMTable.setBackground(Color.YELLOW);
					kTable.setBackground(Color.YELLOW);
					c = Color.BLACK;
				} else if(e.getSource() == cyan) 
				{
					menu.setBackground(Color.CYAN);
					fMTable.setBackground(Color.CYAN);
					kTable.setBackground(Color.CYAN);
					c = Color.CYAN;
				} else if(e.getSource() == blue) 
				{
					menu.setBackground(Color.BLUE);
					fMTable.setBackground(Color.BLUE);
					kTable.setBackground(Color.BLUE);
					c = Color.BLUE;
				} else if(e.getSource() == green) 
				{
					menu.setBackground(new Color(0, 180, 0));
					fMTable.setBackground(new Color(0, 180, 0));
					kTable.setBackground(new Color(0, 180, 0));
					c = new Color(0, 180, 0);
				} else if(e.getSource() == gray) 
				{
					menu.setBackground(Color.DARK_GRAY);
					fMTable.setBackground(Color.DARK_GRAY);
					kTable.setBackground(Color.DARK_GRAY);
					c = Color.DARK_GRAY;
				} else if(e.getSource() == orange) 
				{
					menu.setBackground(Color.ORANGE);
					fMTable.setBackground(Color.ORANGE);
					kTable.setBackground(Color.ORANGE);
					c = Color.ORANGE;
				} else if(e.getSource() == pink) 
				{
					menu.setBackground(Color.PINK);
					fMTable.setBackground(Color.PINK);
					kTable.setBackground(Color.PINK);
					c = Color.PINK;
				} else if(e.getSource() == white) 
				{
					menu.setBackground(Color.WHITE);
					fMTable.setBackground(Color.WHITE);
					kTable.setBackground(Color.WHITE);
					c = Color.WHITE;
				}
			}
		}
		ActionListener aL = new SetUpColorListeners();
		colorFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		original.addActionListener(aL);
		original.setBounds(0, TABLE_HEIGHT - 400, 285, 20);
		red.addActionListener(aL);
		red.setBounds(0, TABLE_HEIGHT - 380, 285, 20);
		yellow.addActionListener(aL);
		yellow.setBounds(0, TABLE_HEIGHT - 360, 285, 20);
		cyan.addActionListener(aL);
		cyan.setBounds(0, TABLE_HEIGHT - 340, 285, 20);
		blue.addActionListener(aL);
		blue.setBounds(0, TABLE_HEIGHT - 320, 285, 20);
		green.addActionListener(aL);
		green.setBounds(0, TABLE_HEIGHT - 300, 285, 20);
		gray.addActionListener(aL);
		gray.setBounds(0, TABLE_HEIGHT - 280, 285, 20);
		orange.addActionListener(aL);
		orange.setBounds(0, TABLE_HEIGHT - 260, 285, 20);
		pink.addActionListener(aL);
		pink.setBounds(0, TABLE_HEIGHT - 240, 285, 20);
		white.addActionListener(aL);
		white.setBounds(0, TABLE_HEIGHT - 220, 285, 20);
		
		colorMenu.add(original);
		colorMenu.add(red);
		colorMenu.add(yellow);
		colorMenu.add(cyan);
		colorMenu.add(blue);
		colorMenu.add(green);
		colorMenu.add(gray);
		colorMenu.add(orange);
		colorMenu.add(pink);
		colorMenu.add(white);
		
		colorFrame.setVisible(true);
	}
	
	private static void enterUser()
	{
		/*
		 * JDialogs stop any other actions from occurring whilst the window is still open.
		 * For this reason, we use a Dialog box for user name 
		 * input so that a game cannot be started prior to entering.
		 */
		JDialog userFrame = new JDialog(frame, true);
		JPanel userTable = new JPanel();
		userFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		userFrame.setSize(200, 75);
		JLabel userName = new JLabel("Enter User Name: ");
		
		class userEnterListener implements KeyListener
		{
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) 
			{
				if(e.getKeyChar() == KeyEvent.VK_ENTER) 
				{
					user = userInput.getText();
					System.out.println(user);
					userFrame.dispose();
				}
			}
			
		}
	
		KeyListener kL = new userEnterListener();
		
		userName.setBounds(0,0, 120, 30);
		
		userInput.setBounds(0,0, 120, 30);
		userInput.setColumns(5);
		userInput.addKeyListener(kL);
		
		userTable.add(userName);
		userTable.add(userInput);
		
		userFrame.add(userTable);
		
		userFrame.setBackground(c);
		userFrame.setVisible(true);
	}
	
	public static String getUser()
	{
		return userInput.getText();
		
	}
	
	private static class SetUpButtonListeners implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() == klondikeStart) 
			{
				Klondike.SolitaireK.main(null);
				
			} else if(e.getSource() == fleaMarketStart) 
			{
				SolitaireFM.main(null);
				
			} else if(e.getSource() == backgroundColorButton) 
			{
				setUpColorChange();
				
			} else if(e.getSource() == statisticButton){
				enterUser();
			}
			
		}
	}
	
	private static void openMenu()
	{
		gameTypes.setText("Available Game Modes");
		gameTypes.setBounds(50, TABLE_HEIGHT - 375, 130, 30);
		
		klondikeStart.addActionListener(ae);
		klondikeStart.setBounds(55, TABLE_HEIGHT - 330, 120, 30);
		
		fleaMarketStart.addActionListener(ae);
		fleaMarketStart.setBounds(55, TABLE_HEIGHT - 285, 120, 30);
		
		backgroundColorButton.addActionListener(ae);
		backgroundColorButton.setBounds(20, TABLE_HEIGHT - 70, 200, 30);
		
		statisticButton.addActionListener(ae);
		statisticButton.setBounds(220, TABLE_HEIGHT - 70, 200, 30);
		
		menu.add(gameTypes);
		menu.add(klondikeStart);
		menu.add(fleaMarketStart);
		menu.add(backgroundColorButton);
		menu.add(statisticButton);
	}

	public static void main(String[] args)
	{

		Container contentPane;

		frame.setSize(TABLE_WIDTH, TABLE_HEIGHT);

		menu.setLayout(null);
		c = Color.GRAY;
		menu.setBackground(c);

		contentPane = frame.getContentPane();
		contentPane.add(menu);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		openMenu();

		frame.setVisible(true);

	}

}
