
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class SolitaireMenu {
	
	public static final int TABLE_HEIGHT = 600;
	public static final int TABLE_WIDTH = 800;
	
	// GUI COMPONENTS (top level)
	private static final JFrame frame = new JFrame("Solitaire Menu");
	protected static final JPanel menu = new JPanel();
	
	//other components
	private static JButton klondikeStart = new JButton("Klondike");
	private static JButton fleaMarketStart = new JButton("Flea Market");
	protected static JButton backgroundColorButton = new JButton("Change Background");
	
	//Action Listener
	private static ActionListener ae = new SetUpButtonListeners();
	
	private static Color c;
	
	public static Color getColor() {
		 return c;
	}
	
	private static class SetUpButtonListeners implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == klondikeStart) {
				
				Klondike.SolitaireK.main(null);
				
			} else if(e.getSource() == fleaMarketStart) {
				
				SolitaireFM.main(null);
				
			} else if(e.getSource() == backgroundColorButton) {
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
				
				JPanel gameTable = SolitaireFM.table;
				class SetUpColorListeners implements ActionListener
				{
					@Override
					public void actionPerformed(ActionEvent e) {
						if(e.getSource() == original) {
							menu.setBackground(Color.GRAY);
							gameTable.setBackground(Color.GRAY);
							c = Color.GRAY;
						} else if(e.getSource() == red) {
							menu.setBackground(Color.RED);
							gameTable.setBackground(Color.RED);
							c = Color.RED;
						} else if(e.getSource() == yellow) {
							menu.setBackground(Color.YELLOW);
							gameTable.setBackground(Color.YELLOW);
							c = Color.BLACK;
						} else if(e.getSource() == cyan) {
							menu.setBackground(Color.CYAN);
							gameTable.setBackground(Color.CYAN);
							c = Color.CYAN;
						} else if(e.getSource() == blue) {
							menu.setBackground(Color.BLUE);
							gameTable.setBackground(Color.BLUE);
							c = Color.BLUE;
						} else if(e.getSource() == green) {
							menu.setBackground(new Color(0, 180, 0));
							gameTable.setBackground(new Color(0, 180, 0));
							c = new Color(0, 180, 0);
						} else if(e.getSource() == gray) {
							menu.setBackground(Color.DARK_GRAY);
							gameTable.setBackground(Color.DARK_GRAY);
							c = Color.DARK_GRAY;
						} else if(e.getSource() == orange) {
							menu.setBackground(Color.ORANGE);
							gameTable.setBackground(Color.ORANGE);
							c = Color.ORANGE;
						} else if(e.getSource() == pink) {
							menu.setBackground(Color.PINK);
							gameTable.setBackground(Color.PINK);
							c = Color.PINK;
						} else if(e.getSource() == white) {
							menu.setBackground(Color.WHITE);
							gameTable.setBackground(Color.WHITE);
							c = Color.WHITE;
						}
					}
				}
				ActionListener aL = new SetUpColorListeners();
				colorFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				
				original.addActionListener(aL);
				original.setBounds(0, TABLE_HEIGHT - 600, 285, 20);
				red.addActionListener(aL);
				red.setBounds(0, TABLE_HEIGHT - 580, 285, 20);
				yellow.addActionListener(aL);
				yellow.setBounds(0, TABLE_HEIGHT - 560, 285, 20);
				cyan.addActionListener(aL);
				cyan.setBounds(0, TABLE_HEIGHT - 540, 285, 20);
				blue.addActionListener(aL);
				blue.setBounds(0, TABLE_HEIGHT - 520, 285, 20);
				green.addActionListener(aL);
				green.setBounds(0, TABLE_HEIGHT - 500, 285, 20);
				gray.addActionListener(aL);
				gray.setBounds(0, TABLE_HEIGHT - 480, 285, 20);
				orange.addActionListener(aL);
				orange.setBounds(0, TABLE_HEIGHT - 460, 285, 20);
				pink.addActionListener(aL);
				pink.setBounds(0, TABLE_HEIGHT - 440, 285, 20);
				white.addActionListener(aL);
				white.setBounds(0, TABLE_HEIGHT - 420, 285, 20);
				
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
			
		}
	}
	
	private static void openMenu()
	{
		klondikeStart.addActionListener(ae);
		klondikeStart.setBounds(20, TABLE_HEIGHT - 545, 120, 30);
		
		fleaMarketStart.addActionListener(ae);
		fleaMarketStart.setBounds(20, TABLE_HEIGHT - 580, 120, 30);
		
		backgroundColorButton.addActionListener(ae);
		backgroundColorButton.setBounds(260, TABLE_HEIGHT - 580, 180, 30);
		
		menu.add(klondikeStart);
		menu.add(fleaMarketStart);
		menu.add(backgroundColorButton);
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
