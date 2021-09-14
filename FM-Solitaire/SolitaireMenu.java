
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
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
	
	private static class NewKGameListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Klondike.SolitaireK.playNewGame();
		}

	}
	private static class NewFMGameListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			SolitaireFM.playFMNewGame();
		}

	}
	
	private static void openMenu()
	{
		klondikeStart.addActionListener(new NewKGameListener());
		klondikeStart.setBounds(400, TABLE_HEIGHT - 600, 120, 30);
		
		fleaMarketStart.addActionListener(new NewFMGameListener());
		fleaMarketStart.setBounds(280, TABLE_HEIGHT - 600, 120, 30);
		
		menu.add(klondikeStart);
		menu.add(fleaMarketStart);
	}

	public static void main(String[] args)
	{

		Container contentPane;

		frame.setSize(TABLE_WIDTH, TABLE_HEIGHT);

		menu.setLayout(null);
		menu.setBackground(new Color(0, 180, 0));

		contentPane = frame.getContentPane();
		contentPane.add(menu);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		openMenu();

		frame.setVisible(true);

	}

}
