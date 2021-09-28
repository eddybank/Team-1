package fleaMarket;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

//import javax.sound.*;
import javax.sound.sampled.*;

import Klondike.SolitaireK;
import fleaMarket.StatisticAnalysis.User;


public class SolitaireMenu {
	
	public static final int TABLE_HEIGHT = 400;
	public static final int TABLE_WIDTH = 500;
	private static User user;
	
	// GUI COMPONENTS (top level)
	private static final JFrame frame = new JFrame("Solitaire Menu");
	protected static final JPanel menu = new JPanel();
	
	//other components
	private static JButton klondikeStart = new JButton("Klondike");
	private static JButton fleaMarketStart = new JButton("Flea Market");
	private static JButton backgroundColorButton = new JButton("Change Background Color");
	private static JButton enable = new JButton("Enable Sound");
	private static JButton disable = new JButton("Disable Sound");
	private static JButton records = new JButton("Look At Records");
	//protected static JButton statisticButton = new JButton("Game Statistics");
	private static JTextPane gameTypes = new JTextPane();// displays the score
	private static JTextField userInput = new JTextField();
	private static JTextPane statusBox = new JTextPane();// status messages
	private static boolean soundO = true;
	
	//Styled docs to allow for text color changes
	static StyledDocument game = gameTypes.getStyledDocument();
	static StyledDocument status = statusBox.getStyledDocument();
	static StyledDocument score = SolitaireFM.scoreBox.getStyledDocument();
	static StyledDocument time = SolitaireFM.timeBox.getStyledDocument();
	static StyledDocument gameStatus = SolitaireFM.statusBox.getStyledDocument();
	static StyledDocument scoreK = SolitaireK.scoreBox.getStyledDocument();
	static StyledDocument timeK = SolitaireK.timeBox.getStyledDocument();
	static StyledDocument gameStatusK = SolitaireK.statusBox.getStyledDocument();
	
	static SimpleAttributeSet center = new SimpleAttributeSet();
	
	static Style style = gameTypes.addStyle("I'm a Style", null);
	
	//Action Listener
	private static ActionListener ae = new SetUpButtonListeners();
	
	//Color switcher
	private static Color c;
	private static String col;
	private static final Color N_GREEN = new Color(0, 180, 0);
	
	//sound boolean getter
	public static boolean getSoundState()
	{
		return soundO;
	}
	
	//Color getter
	public static Color getColor() 
	{
		 return c;
	}
	public static String getColorS() 
	{
		 return col;
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
				String sco = SolitaireFM.scoreBox.getText();
				String times = SolitaireFM.timeBox.getText();
				String gameS = SolitaireFM.statusBox.getText();

				String sco1 = SolitaireK.scoreBox.getText();
				String times1 = SolitaireK.timeBox.getText();
				String gameS1 = SolitaireK.statusBox.getText();
				
				SolitaireFM.toggleTimer();
				try {
					if(e.getSource() == original) 
					{
						menu.setBackground(Color.GRAY);
						fMTable.setBackground(Color.GRAY);
						kTable.setBackground(Color.GRAY);
						c = Color.GRAY;
						col = "GRAY";
						
					} else if(e.getSource() == red) 
					{
						menu.setBackground(Color.RED);
						fMTable.setBackground(Color.RED);
						kTable.setBackground(Color.RED);
						c = Color.RED;
						col = "RED";

					} else if(e.getSource() == yellow) 
					{
						menu.setBackground(Color.YELLOW);
						fMTable.setBackground(Color.YELLOW);
						kTable.setBackground(Color.YELLOW);
						c = Color.YELLOW;
						col = "YELLOW";
						
					} else if(e.getSource() == cyan) 
					{
						menu.setBackground(Color.CYAN);
						fMTable.setBackground(Color.CYAN);
						kTable.setBackground(Color.CYAN);
						c = Color.CYAN;
						col = "CYAN";
						
					} else if(e.getSource() == blue) 
					{
						menu.setBackground(Color.BLUE);
						fMTable.setBackground(Color.BLUE);
						kTable.setBackground(Color.BLUE);
						c = Color.BLUE;
						col = "BLUE";
						
					} else if(e.getSource() == green) 
					{
						menu.setBackground(new Color(0, 180, 0));
						fMTable.setBackground(new Color(0, 180, 0));
						kTable.setBackground(new Color(0, 180, 0));
						c = N_GREEN;
						col = "N_GREEN";
						
					} else if(e.getSource() == gray || e.getSource() == green || e.getSource() == blue) 
					{
						menu.setBackground(Color.DARK_GRAY);
						fMTable.setBackground(Color.DARK_GRAY);
						kTable.setBackground(Color.DARK_GRAY);
						c = Color.DARK_GRAY;
						col = "DARK_GRAY";
						
					} else if(e.getSource() == orange) 
					{
						menu.setBackground(Color.ORANGE);
						fMTable.setBackground(Color.ORANGE);
						kTable.setBackground(Color.ORANGE);
						c = Color.ORANGE;
						col = "ORANGE";
						
					} else if(e.getSource() == pink) 
					{
						menu.setBackground(Color.PINK);
						fMTable.setBackground(Color.PINK);
						kTable.setBackground(Color.PINK);
						c = Color.PINK;
						col = "PINK";
						
					} else if(e.getSource() == white) 
					{
						menu.setBackground(Color.WHITE);
						fMTable.setBackground(Color.WHITE);
						kTable.setBackground(Color.WHITE);
						c = Color.WHITE;
						col = "WHITE";
				        	
					}
					
					if(e.getSource() == gray || e.getSource() == green || e.getSource() == blue)
					{
						gameTypes.setText("");
						statusBox.setText("");
						SolitaireFM.scoreBox.setText("");
						SolitaireFM.timeBox.setText("");
						SolitaireFM.statusBox.setText("");
						SolitaireFM.gameTitle.setText("");
						
						StyleConstants.setForeground(style, Color.WHITE);
				        
						game.insertString(game.getLength(), "Available Game Modes", style);
						status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
				        score.insertString(score.getLength(), sco, style);
				        time.insertString(time.getLength(), times, style);
				        gameStatus.insertString(gameStatus.getLength(), gameS, style);
				        SolitaireFM.gameTitle.setText("<span style =\"color:white\"><b>Team 1<br>Flea Market Solitaire</b> <br> CPSC 4900 <br> Fall 2021</span>");
				        SolitaireFM.recordBox.setText("<span style =\"color:white\"><b>User Record<br>Klondike Solitaire</b> <br> "
								+ "Best Win Time: "+SolitaireFM.getBestRecord().getTime()+" seconds <br> Highest Score: "+SolitaireFM.getBestRecord().getScore()+"</span>");
				        
				        SolitaireK.scoreBox.setText("");
						SolitaireK.timeBox.setText("");
						SolitaireK.statusBox.setText("");
						SolitaireK.gameTitle.setText("");

				        System.out.println(sco);
				        scoreK.insertString(scoreK.getLength(), sco1, style);
				        timeK.insertString(timeK.getLength(), times1, style);
				        gameStatusK.insertString(gameStatusK.getLength(), gameS1, style);
				        SolitaireK.gameTitle.setText("<span style =\"color:white\"><b>Team 1<br>Klondike Solitaire</b> <br> CPSC 4900 <br> Fall 2021</span>");
				        
					} else if(e.getSource() == white || e.getSource() == pink || e.getSource() == orange || 
							e.getSource() == cyan || e.getSource() == yellow || e.getSource() == red || e.getSource() == original)
					{
						gameTypes.setText("");
						statusBox.setText("");
						SolitaireFM.scoreBox.setText("");
						SolitaireFM.timeBox.setText("");
						SolitaireFM.statusBox.setText("");
						SolitaireFM.gameTitle.setText("");
						
						StyleConstants.setForeground(style, Color.BLACK);
						
				        game.insertString(game.getLength(), "Available Game Modes", style);
						status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
						score.insertString(score.getLength(), sco, style);
				        time.insertString(time.getLength(), times, style);
				        gameStatus.insertString(gameStatus.getLength(), gameS, style);
				        SolitaireFM.gameTitle.setText("<span style =\"color:black\"><b>Team 1<br>Flea Market Solitaire</b> <br> CPSC 4900 <br> Fall 2021</span>");
				        SolitaireFM.recordBox.setText("<span style =\"color:black\"><b>User Record<br>Klondike Solitaire</b> <br> "
								+ "Best Win Time: "+SolitaireFM.getBestRecord().getTime()+" seconds <br> Highest Score: "+SolitaireFM.getBestRecord().getScore()+"</span>");
				        
				        SolitaireK.scoreBox.setText("");
						SolitaireK.timeBox.setText("");
						SolitaireK.statusBox.setText("");
						SolitaireK.gameTitle.setText("");
						
						scoreK.insertString(scoreK.getLength(), sco1, style);
				        timeK.insertString(timeK.getLength(), times1, style);
				        gameStatusK.insertString(gameStatusK.getLength(), gameS1, style);
				        SolitaireK.gameTitle.setText("<span style =\"color:black\"><b>Team 1<br>Klondike Solitaire</b> <br> CPSC 4900 <br> Fall 2021</span>");
				        
					}
				} catch (BadLocationException e1) {
					System.out.println("Error occurred - Printing stack trace");
					e1.printStackTrace();
				}
				SolitaireFM.toggleTimer();
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
		
		colorFrame.setLocationRelativeTo(frame);
		colorFrame.setVisible(true);
	}
	
	public static void enterUser()
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
				try {
				if(e.getKeyChar() == KeyEvent.VK_ENTER) 
				{
					String username = userInput.getText();
					if(StatisticAnalysis.doesUserExist(username))
					{
						user = StatisticAnalysis.setUser(username);
						String n = StatisticAnalysis.getUserColor(user);
						System.out.println();
						
						if(n.equals("GRAY"))
						{
							c = Color.GRAY;

							gameTypes.setText("");
							statusBox.setText("");
							
							StyleConstants.setForeground(style, Color.WHITE);
					        
					        game.insertString(game.getLength(), "Available Game Modes", style);
					        status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
					        
					        
						} else if(n.equals("RED"))
						{
							c = Color.RED;
							
							gameTypes.setText("");
							statusBox.setText("");
							
							StyleConstants.setForeground(style, Color.BLACK);
							
					        game.insertString(game.getLength(), "Available Game Modes", style);
							status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
					        
						} else if(n.equals("YELLOW"))
						{
							c = Color.BLACK;

							gameTypes.setText("");
							statusBox.setText("");
							
							StyleConstants.setForeground(style, Color.BLACK);
							
					        game.insertString(game.getLength(), "Available Game Modes", style);
							status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
							
					        
						} else if(n.equals("CYAN"))
						{
							c = Color.CYAN;

							gameTypes.setText("");
							statusBox.setText("");
							
							StyleConstants.setForeground(style, Color.BLACK);
							
					        game.insertString(game.getLength(), "Available Game Modes", style);
							status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
							
					        
						} else if(n.equals("BLUE"))
						{
							c = Color.BLUE;
							
							gameTypes.setText("");
							statusBox.setText("");
							
							StyleConstants.setForeground(style, Color.WHITE);
					        
					        game.insertString(game.getLength(), "Available Game Modes", style);
					        status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
							
						} else if(n.equals("N_GREEN"))
						{
							c = new Color(0, 180, 0);

							gameTypes.setText("");
							statusBox.setText("");
							
							StyleConstants.setForeground(style, Color.WHITE);
					        
					        game.insertString(game.getLength(), "Available Game Modes", style);
					        status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
					        
					        
						} else if(n.equals("DARK_GRAY"))
						{
							c = Color.DARK_GRAY;

							gameTypes.setText("");
							statusBox.setText("");
							
							StyleConstants.setForeground(style, Color.BLACK);
							
					        game.insertString(game.getLength(), "Available Game Modes", style);
							status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
							
					        
						} else if(n.equals("ORANGE"))
						{
							c = Color.ORANGE;

							gameTypes.setText("");
							statusBox.setText("");
							
							StyleConstants.setForeground(style, Color.BLACK);
							
					        game.insertString(game.getLength(), "Available Game Modes", style);
							status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
							
					        
						} else if(n.equals("PINK"))
						{
							c = Color.PINK;

							gameTypes.setText("");
							statusBox.setText("");
							
							StyleConstants.setForeground(style, Color.BLACK);
							
					        game.insertString(game.getLength(), "Available Game Modes", style);
							status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
							
					        
						} else if(n.equals("WHITE"))
						{
							c = Color.WHITE;

							gameTypes.setText("");
							statusBox.setText("");
							
							StyleConstants.setForeground(style, Color.BLACK);
							
					        game.insertString(game.getLength(), "Available Game Modes", style);
							status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
							
					        
						} else
						{
							c = Color.LIGHT_GRAY;

							gameTypes.setText("");
							statusBox.setText("");
							
							StyleConstants.setForeground(style, Color.BLACK);
							
					        game.insertString(game.getLength(), "Available Game Modes", style);
							status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
							
						}
						
						menu.setBackground(c);
						statusBox.setText("Welcome "+user.getUser());
						userFrame.dispose();
					} else {
						user = new User(username, 0);
						statusBox.setText("New User Created--Welcome: "+username);
						System.out.println(user);
						c = Color.LIGHT_GRAY;
						menu.setBackground(c);
						userFrame.dispose();
					}
				}
				} catch (BadLocationException e2)
				{
					System.out.println("Error occurred - Printing stack trace");
					e2.printStackTrace();
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
		userFrame.setLocationRelativeTo(frame);
		userFrame.setVisible(true);
	}
	
	public static User getUser()
	{
		return user;
		
	}
	
	private static class SetUpButtonListeners implements ActionListener
	{	
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			
			Style style = gameTypes.addStyle("I'm a Style", null);
			
			try {
				if(e.getSource() == klondikeStart) 
				{
					Klondike.SolitaireK.main(null);
					
					String sco = SolitaireK.scoreBox.getText();
					String times = SolitaireK.timeBox.getText();
					String gameS = SolitaireK.statusBox.getText();
					if(getColor() == Color.GRAY || getColor() == Color.GREEN || getColor() == Color.BLUE)
					{
						SolitaireK.scoreBox.setText("");
						SolitaireK.timeBox.setText("");
						SolitaireK.statusBox.setText("");
						SolitaireK.gameTitle.setText("");
						
						StyleConstants.setForeground(style, Color.WHITE);
				        System.out.println(sco);
				        scoreK.insertString(scoreK.getLength(), sco, style);
				        timeK.insertString(timeK.getLength(), times, style);
				        gameStatusK.insertString(gameStatusK.getLength(), gameS, style);
				        SolitaireK.gameTitle.setText("<span style =\"color:white\"><b>Team 1<br>Klondike Solitaire</b> <br> CPSC 4900 <br> Fall 2021</span>");
				        
					} else if (getColor() == Color.WHITE || getColor() == Color.PINK || getColor() == Color.ORANGE || 
							getColor() == Color.CYAN || getColor() == Color.YELLOW || getColor() == Color.RED || getColor() == Color.LIGHT_GRAY)
					{
						SolitaireK.scoreBox.setText("");
						SolitaireK.timeBox.setText("");
						SolitaireK.statusBox.setText("");
						SolitaireK.gameTitle.setText("");
						
						StyleConstants.setForeground(style, Color.BLACK);
						
						scoreK.insertString(scoreK.getLength(), sco, style);
				        timeK.insertString(timeK.getLength(), times, style);
				        gameStatusK.insertString(gameStatusK.getLength(), gameS, style);
				        SolitaireK.gameTitle.setText("<span style =\"color:black\"><b>Team 1<br>Klondike Solitaire</b> <br> CPSC 4900 <br> Fall 2021</span>");
					}
					
				} else if(e.getSource() == fleaMarketStart) 
				{
					System.out.println(getColor());
					SolitaireFM.main(null);
					
					String sco = SolitaireFM.scoreBox.getText();
					String times = SolitaireFM.timeBox.getText();
					String gameS = SolitaireFM.statusBox.getText();
					
					if(getColor() == Color.GRAY || getColor() == Color.GREEN || getColor() == Color.BLUE)
					{
						//gameTypes.setText("");
						//statusBox.setText("");
						SolitaireFM.scoreBox.setText("");
						SolitaireFM.timeBox.setText("");
						SolitaireFM.statusBox.setText("");
						SolitaireFM.gameTitle.setText("");
						System.out.println(sco);
						StyleConstants.setForeground(style, Color.WHITE);
				        
				        //game.insertString(game.getLength(), "Game Modes", style);
				        //status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
				        score.insertString(score.getLength(), sco, style);
				        time.insertString(time.getLength(), times, style);
				        gameStatus.insertString(gameStatus.getLength(), gameS, style);
				        SolitaireFM.gameTitle.setText("<span style =\"color:white\"><b>Team 1<br>Flea Market Solitaire</b> <br> CPSC 4900 <br> Fall 2021</span>");
				        SolitaireFM.recordBox.setText("<span style =\"color:white\"><b>User Record<br>Klondike Solitaire</b> <br> "
								+ "Best Win Time: "+SolitaireFM.getBestRecord().getTime()+" seconds <br> Highest Score: "+SolitaireFM.getBestRecord().getScore()+"</span>");
				        
					} else if (getColor() == Color.WHITE || getColor() == Color.PINK || getColor() == Color.ORANGE || 
							getColor() == Color.CYAN || getColor() == Color.YELLOW || getColor() == Color.RED || getColor() == Color.LIGHT_GRAY)
					{
						//gameTypes.setText("");
						//statusBox.setText("");
						SolitaireFM.scoreBox.setText("");
						SolitaireFM.timeBox.setText("");
						SolitaireFM.statusBox.setText("");
						SolitaireFM.gameTitle.setText("");
						System.out.println(sco);
						StyleConstants.setForeground(style, Color.BLACK);
						
				        //game.insertString(game.getLength(), "Available Game Modes", style);
						//status.insertString(status.getLength(), "Welcome "+user.getUser(), style);
						score.insertString(score.getLength(), sco, style);
				        time.insertString(time.getLength(), times, style);
				        gameStatus.insertString(gameStatus.getLength(), gameS, style);
				        SolitaireFM.gameTitle.setText("<span style =\"color:black\"><b>Team 1<br>Flea Market Solitaire</b> <br> CPSC 4900 <br> Fall 2021</span>");
				        SolitaireFM.recordBox.setText("<span style =\"color:black\"><b>User Record<br>Klondike Solitaire</b> <br> "
								+ "Best Win Time: "+SolitaireFM.getBestRecord().getTime()+" seconds <br> Highest Score: "+SolitaireFM.getBestRecord().getScore()+"</span>");
					}
					
				} else if(e.getSource() == backgroundColorButton) 
				{
					setUpColorChange();
					
				} else if(e.getSource() == SolitaireFM.newGameButton)
				{
					if(SolitaireFM.gameOver == true)
					{
						user.createRecord(SolitaireFM.score, SolitaireFM.time);
					}
				} else if(e.getSource() == disable) {
					enable.setVisible(true);
					soundO = false;
					disable.setVisible(false);
				} else if(e.getSource() == enable) {
					enable.setVisible(false);
					soundO = true;
					disable.setVisible(true);
				} else if (e.getSource() == records)
				{
					Container cP;
					
					JFrame recordFrame = new JFrame("Records Menu");
					JPanel recordMenu = new JPanel();

					JScrollPane scroll;
					recordFrame.setSize(800, TABLE_HEIGHT);
					recordMenu.setSize(800, TABLE_HEIGHT);
					recordMenu.setLayout(new GridBagLayout()); //new GridLayout(0,4)
					GridBagConstraints c = new GridBagConstraints();
					
					cP = recordFrame.getContentPane();
					recordFrame.add(scroll = new JScrollPane(recordMenu, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
					
					JEditorPane recordBox;
					//JEditorPane recordBox = new JEditorPane("text/html", "");
					ArrayList<fleaMarket.StatisticAnalysis.Record> rec = user.getRecords();
					c.fill = GridBagConstraints.HORIZONTAL;
					c.gridx = 0;
					c.gridy = 0;
					JEditorPane box = new JEditorPane("text/html", "<pre><b>Klondike Solitaire</b><br>"+user.getUser()+"'s Records <br>Score for Win = 416</pre>");
					//box.setBounds(0, 5, 120, 20);
					box.setEditable(false);
					box.setOpaque(false);
					
					recordMenu.add(box, c);
					System.out.println(rec.size());
					for(int r = 0; r < rec.size() ; r++)
					{
						c.fill = GridBagConstraints.HORIZONTAL;
						c.gridx = r%4;
						c.gridy = (r/4)+1;
						recordBox = new JEditorPane("text/html", "");
						recordBox.setText("<pre><b>'"+user.getUser()+"'<br> Record "+(r+1)+"</b><br>"
								+ "  - Time: "+rec.get(r).getTime()+" seconds<br>  - Highest Score: "+rec.get(r).getScore()+"</pre><br>");
						//recordBox.setBounds(((TABLE_WIDTH/4)*(r%4)), ((r/4)*100)+50, 100, 100);
						recordBox.setEditable(false);
						recordBox.setOpaque(false);

						recordMenu.add(recordBox, c);
					}
					SwingUtilities.invokeLater(new Runnable() {

			            @Override
			            public void run() {
			                scroll.getVerticalScrollBar().setValue(0);
			                scroll.getVerticalScrollBar().setUnitIncrement(16);
			            }
			        });
					recordFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					recordFrame.setLocationRelativeTo(frame);
					recordFrame.setVisible(true);
					
				}
			} catch (BadLocationException e3) {
				System.out.println("Error occurred - Printing stack trace");
				e3.printStackTrace();
			}
		}
	}
	
	private static class windowListener implements WindowListener
	{

		@Override
		public void windowOpened(WindowEvent e) {
			enterUser();
		}
		@Override
		public void windowClosing(WindowEvent e) {}
		@Override
		public void windowClosed(WindowEvent e) {}
		@Override
		public void windowIconified(WindowEvent e) {}
		@Override
		public void windowDeiconified(WindowEvent e) {}
		@Override
		public void windowActivated(WindowEvent e) {}
		@Override
		public void windowDeactivated(WindowEvent e) {}

	}
	
	private static void openMenu() throws BadLocationException
	{
		statusBox.setBounds(220, TABLE_HEIGHT - 100, 200, 30);
		statusBox.setEditable(false);
		statusBox.setOpaque(false);
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		status.setParagraphAttributes(0, status.getLength(), center, false);
		
		gameTypes.setText("Available Game Modes");
		gameTypes.setEditable(false);
		gameTypes.setOpaque(false);
		gameTypes.setBounds(50, TABLE_HEIGHT - 375, 150, 30);
		
		klondikeStart.addActionListener(ae);
		klondikeStart.setBounds(55, TABLE_HEIGHT - 330, 120, 30);
		
		fleaMarketStart.addActionListener(ae);
		fleaMarketStart.setBounds(55, TABLE_HEIGHT - 285, 120, 30);
		
		backgroundColorButton.addActionListener(ae);
		backgroundColorButton.setBounds(20, TABLE_HEIGHT - 70, 200, 30);
		
		enable.addActionListener(ae);
		enable.setBounds(220, TABLE_HEIGHT - 70, 200, 30);
		enable.setVisible(false);
		
		disable.addActionListener(ae);
		disable.setBounds(220, TABLE_HEIGHT - 70, 200, 30);
		
		records.setBounds(240, TABLE_HEIGHT - 200, 150, 30);
		records.addActionListener(ae);
		
		menu.add(records);
		menu.add(enable);
		menu.add(disable);
		menu.add(gameTypes);
		menu.add(klondikeStart);
		menu.add(fleaMarketStart);
		menu.add(backgroundColorButton);
		menu.add(statusBox);
	}

	public static void main(String[] args) throws BadLocationException
	{

		Container contentPane;

		frame.setSize(TABLE_WIDTH, TABLE_HEIGHT);
		
		frame.addWindowListener(new windowListener());
		
		menu.setLayout(null);
		//menu.setBackground(Color.GRAY);
		
		contentPane = frame.getContentPane();
		contentPane.add(menu);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		openMenu();

		frame.setVisible(true);
		StatisticAnalysis.main(null);

	}

}
