package fleaMarket;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class SolitaireFM
{
	// CONSTANTS
	public static final int TABLE_HEIGHT = CardFM.CARD_HEIGHT * (8);
	public static final int TABLE_WIDTH = (CardFM.CARD_WIDTH * 8);
	public static final int NUM_FINAL_DECKS = 8;
	public static final int NUM_PLAY_DECKS = 13;
	public static final Point DECK_POS = new Point(5, 5);
	public static final Point SHOW_POS = new Point(DECK_POS.x + CardFM.CARD_WIDTH + 5, DECK_POS.y);
	public static final Point FINAL_POS = new Point(SHOW_POS.x + CardFM.CARD_WIDTH + 150, DECK_POS.y);
	public static final Point PLAY_POS = new Point(DECK_POS.x, FINAL_POS.y + CardFM.CARD_HEIGHT + 30);

	// GAMEPLAY STRUCTURES
	private static FinalStackFM[] final_cards;// Foundation Stacks
	private static CardStackFM[] playCardStack; // Tableau stacks
	private static CardStackFM deck; // populated with 2 standard 52 card decks
	private static FinalStackFM[] deal_deck;// Foundation Stacks

	// GUI COMPONENTS (top level)
	private static final JFrame frame = new JFrame("Flea Market Solitaire");
	protected static final JPanel table = new JPanel();
	protected static final JPanel menu = new JPanel();
	// other components
	private static JEditorPane gameTitle = new JEditorPane("text/html", "");
	private static JButton showRulesButton = new JButton("Show Rules");
	private static JButton newGameButton = new JButton("New Game");
	private static JButton toggleTimerButton = new JButton("Pause Timer");
	private static JTextField scoreBox = new JTextField();// displays the score
	private static JTextField timeBox = new JTextField();// displays the time
	private static JTextField statusBox = new JTextField();// status messages
	private static final CardFM newCardButton = new CardFM();
	
	//Action Listener for buttons
	private static ActionListener ae = new setUpButtonListeners();
	private static CardMovementManager cm = new CardMovementManager();
	

	// TIMER UTILITIES
	private static Timer timer = new Timer();
	private static ScoreClock scoreClock = new ScoreClock();

	// MISC TRACKING VARIABLES
	private static boolean timeRunning = false;// timer running?
	private static int score = -64;// keep track of the score, start of $-64
	private static int time = 0;// keep track of seconds elapsed
	private static int deal_deck_pos = 0;

	// moves a card to abs location within a component
	protected static CardFM moveCard(CardFM c, int x, int y)
	{
		c.setBounds(new Rectangle(new Point(x, y), new Dimension(CardFM.CARD_WIDTH + 10, CardFM.CARD_HEIGHT + 10)));
		c.setXY(new Point(x, y));
		return c;
	}

	// add/subtract points based on gameplay actions
	protected static void setScore(int deltaScore)
	{
		SolitaireFM.score += deltaScore;
		String newScore = "Score: " + SolitaireFM.score;
		scoreBox.setText(newScore);
		scoreBox.repaint();
	}

	// GAME TIMER UTILITIES
	protected static void updateTimer()
	{
		SolitaireFM.time += 1;
		// every 10 seconds elapsed we take away 2 points
		/*if (SolitaireFM.time % 10 == 0)
		{
			setScore(-2);
		}*/
		String time = "Seconds: " + SolitaireFM.time;
		timeBox.setText(time);
		timeBox.repaint();
	}

	protected static void startTimer()
	{
		scoreClock = new ScoreClock();
		// set the timer to update every second
		timer.scheduleAtFixedRate(scoreClock, 1000, 1000);
		timeRunning = true;
	}
	
	//reset timer
	protected static void resetTimer()
	{
		scoreClock.cancel();
		time = 0;
		timeRunning = false;
	}

	// the pause timer button uses this
	protected static void toggleTimer()
	{
		if (timeRunning && scoreClock != null)
		{
			scoreClock.cancel();
			timeRunning = false;
		} else
		{
			startTimer();
		}
	}

	private static class ScoreClock extends TimerTask
	{
		@Override
		public void run()
		{
			updateTimer();
		}
	}

	// BUTTON LISTENERS
	private static class setUpButtonListeners implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == newGameButton) {
				SolitaireFM.playFMNewGame();
			} else if(e.getSource() == toggleTimerButton) {
					toggleTimer();
					if (!timeRunning)
					{
						toggleTimerButton.setText("Start Timer");
					} else
					{
						toggleTimerButton.setText("Pause Timer");
					}
			} else if(e.getSource() == showRulesButton){
					JDialog ruleFrame = new JDialog(frame, true);
					ruleFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					ruleFrame.setSize(TABLE_HEIGHT - (TABLE_HEIGHT/3), TABLE_WIDTH - (TABLE_WIDTH/3));
					JScrollPane scroll;
					JEditorPane rulesTextPane = new JEditorPane("text/html", "");
					rulesTextPane.setEditable(false);
					String rulesText = "<b>Flea Market Solitaire</b>"
							+ "<b><br><br>Notes On My Implementation</b><br>"
							+ "Drag cards to and from any stack. As long as the move is valid the card, or stack of "
							+ "cards, will be repositioned in the desired spot. The game starts with a score of $-64 and each move will result in 5 points."
							+ "The timer starts running as soon as "
							+ "the game begins, but it may be paused by pressing the pause button at the bottom of "
							+ "the screen. "
							+ "<b><br><br>Technical Aspects</b><br>"
							+ "This solitaire uses 104 cards (2 decks). You have 12 main tableau piles with one card per pile, one additional tableau piles with one card and 8 foundations."
							+ "At the start of the game an Ace and King of each suit is dealt on the foundations."
							+ "<b><br><br>The object of the game</b><br>"
							+ "To build the foundation Aces up in suit to Kings, to build the foundation Kings down in suit to Aces."
							+ "<b><br><br>The rules</b><br>"
							+ "The top cards of tableau piles are available for play on foundations. You may build tableau piles up or down by suit. Only one card at a time can be moved from pile to pile. Spaces are filled automatically from the stock pile. "
							+ "When you have made all the moves initially available, click on the stock to deal one card on each main tableau pile. "
							+ "When the stock pile is empty the spaces may be filled with the top cards of tableaus. "
							+ "There is no redeal. ";
					rulesTextPane.setText(rulesText);
					ruleFrame.add(scroll = new JScrollPane(rulesTextPane));

					ruleFrame.setVisible(true);
			}
		}
	} 
	private static class windowListener implements WindowListener
	{

		@Override
		public void windowOpened(WindowEvent e) {}
		@Override
		public void windowClosing(WindowEvent e) {
			
			newGameButton.removeActionListener(ae);
			
			toggleTimerButton.removeActionListener(ae);
			
			showRulesButton.removeActionListener(ae);
			
			frame.dispose();
		}
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
	
	/*
	 * This class handles all of the logic of moving the Card components as well
	 * as the game logic. This determines where Cards can be moved according to
	 * the rules of Klondike solitiaire
	 */
	private static class CardMovementManager extends MouseAdapter
	{
		private boolean checkForWin = false;// should we check if game is over?
		private boolean gameOver = true;// easier to negate this than affirm it
		private Point start = null;// where mouse was clicked
		private Point stop = null;// where mouse was released
		private CardFM card = null; // card to be moved
		// used for moving single cards
		private CardStackFM source = null;
		private CardStackFM dest = null;
		// used for moving a stack of cards
		private CardStackFM transferStack = new CardStackFM(false);
		

		private boolean validPlayStackMove(CardFM source, CardFM dest)
		{
			if(dest != null) {
				int s_val = source.getValue().ordinal();
				int d_val = dest.getValue().ordinal();
				CardFM.Suit s_suit = source.getSuit();
				CardFM.Suit d_suit = dest.getSuit();
	
				// destination card should be one higher value
				if (s_val  == (d_val + 1))
				{
					// destination card should be same color
					switch (s_suit)
					{
					case SPADES:
						if (d_suit == CardFM.Suit.HEARTS && d_suit == CardFM.Suit.DIAMONDS && d_suit == CardFM.Suit.CLUBS)
							return false;
						else if(s_suit == d_suit)
							return true;
						
					case CLUBS:
						if (d_suit == CardFM.Suit.HEARTS && d_suit == CardFM.Suit.DIAMONDS && d_suit == CardFM.Suit.SPADES)
							return false;
						else if(s_suit == d_suit)
							return true;
						
					case HEARTS:
						if (d_suit == CardFM.Suit.SPADES && d_suit == CardFM.Suit.CLUBS && d_suit == CardFM.Suit.DIAMONDS)
							return false;
						else if(s_suit == d_suit)
							return true;
						
					case DIAMONDS:
						if (d_suit == CardFM.Suit.SPADES && d_suit == CardFM.Suit.CLUBS && d_suit == CardFM.Suit.HEARTS)
							return false;
						else if(s_suit == d_suit)
							return true;
						
					}
					return false; // this never gets reached
				} else if ((s_val + 1) == d_val )
				{
					// destination card should be same color
					switch (s_suit)
					{
					case SPADES:
						if (d_suit == CardFM.Suit.HEARTS && d_suit == CardFM.Suit.DIAMONDS && d_suit == CardFM.Suit.CLUBS)
							return false;
						else if(s_suit == d_suit)
							return true;
						
					case CLUBS:
						if (d_suit == CardFM.Suit.HEARTS && d_suit == CardFM.Suit.DIAMONDS && d_suit == CardFM.Suit.SPADES)
							return false;
						else if(s_suit == d_suit)
							return true;
						
					case HEARTS:
						if (d_suit == CardFM.Suit.SPADES && d_suit == CardFM.Suit.CLUBS && d_suit == CardFM.Suit.DIAMONDS)
							return false;
						else if(s_suit == d_suit)
							return true;
						
					case DIAMONDS:
						if (d_suit == CardFM.Suit.SPADES && d_suit == CardFM.Suit.CLUBS && d_suit == CardFM.Suit.HEARTS)
							return false;
						else if(s_suit == d_suit)
							return true;
						
					}
					return false; // this never gets reached 
				}
					else return false;
			} else
				return true;
		}

		private boolean validFinalStackMove(CardFM source, CardFM dest)
		{
			int s_val = source.getValue().ordinal();
			int d_val = dest.getValue().ordinal();
			CardFM.Suit s_suit = source.getSuit();
			CardFM.Suit d_suit = dest.getSuit();
			if (s_val == (d_val + 1)) // destination must one lower
			{
				if (s_suit == d_suit)
					return true;
				else
					return false;
			} else if (s_val == (d_val - 1)) // destination must one lower
			{
				if (s_suit == d_suit)
					return true;
				else
					return false;
			} else
				return false;
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			start = e.getPoint();
			boolean stopSearch = false;
			statusBox.setText("");
			transferStack.makeEmpty();

			/*
			 * Here we use transferStack to temporarily hold the one card that
			 * was clicked on by the user for transfer
			 */
			for (int x = 0; x < NUM_PLAY_DECKS; x++)
			{
				if (stopSearch)
					break;
				source = playCardStack[x];
				// pinpointing exact card pressed

				if(!source.empty()) {
					CardFM c = (CardFM) source.getFirst();
					if (c.contains(start) && source.contains(start))
					{
						transferStack.putFirst(c);
					}
					if (c.contains(start) && source.contains(start) && c.getFaceStatus())
					{
						card = c;
						stopSearch = true;
						System.out.println("Transfer Size: " + transferStack.showSize());
						break;
					}
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			stop = e.getPoint();
			// used for status bar updates
			boolean validMoveMade = false;

			// PLAY STACK OPERATIONS
			if (card != null && source != null)
			{ // Moving from PLAY TO PLAY
				for (int x = 0; x < NUM_PLAY_DECKS; x++)
				{
					dest = playCardStack[x];

					// MOVING TO POPULATED STACK
					if (card.getFaceStatus() == true && dest.contains(stop) && source != dest &&
							validPlayStackMove(card, dest.getFirst()) && transferStack.showSize() == 1)
					{
						if(source.showSize() > 1 && card != source.getLast()) {
							CardFM c = null;
							c = source.popFirst();
							c.repaint();
							
							table.repaint();
							// if playstack, turn next card up
							if (source.getFirst() != null)
							{
								CardFM temp = source.getFirst().setFaceup();
								temp.repaint();
								source.repaint();
							}
	
							dest.setXY(dest.getXY().x, dest.getXY().y);
							dest.putFirst(c);
	
							dest.repaint();
	
							table.repaint();
	
							System.out.print("Destination ");
							dest.showSize();
							
							setScore(5);
							validMoveMade = true;
							break;
						} else {
							CardFM c = null;
							c = source.popFirst();
							c.repaint();
							
							table.repaint();
							if (source.getFirst() != null)
							{
								CardFM temp = source.getFirst().setFaceup();
								temp.repaint();
								source.repaint();
							}
	
							dest.setXY(dest.getXY().x, dest.getXY().y);
							dest.putFirst(c);
	
							dest.repaint();
	
							table.repaint();
	
							System.out.print("Destination ");
							dest.showSize();
							
							setScore(5);
							validMoveMade = true;
							break;
						}
					} else if (dest.empty() && dest.contains(stop) &&
							validPlayStackMove(card, dest.getFirst()) && transferStack.showSize() == 1)
					{// MOVING TO EMPTY STACK
						if(source.showSize() > 1 && card != source.getLast()) {
							CardFM c = null;
							c = source.popFirst();
							c.repaint();
							
							// if playstack, turn next card up
							if (source.getFirst() != null)
							{
								CardFM temp = source.getFirst();//.setFaceup();
								temp.repaint();
								source.repaint();
							}
	
							dest.setXY(dest.getXY().x, dest.getXY().y);
							dest.putFirst(c);
	
							dest.repaint();
	
							table.repaint();
	
							System.out.print("Destination ");
							dest.showSize();
							setScore(5);
							validMoveMade = true;
							break;
						} else {
							CardFM c = null;
							c = source.popFirst();
							c.repaint();
							
							// if playstack, turn next card up
							if (source.getFirst() != null)
							{
								CardFM temp = source.getFirst();//.setFaceup();
								temp.repaint();
								source.repaint();
							}
	
							dest.setXY(dest.getXY().x, dest.getXY().y);
							dest.putFirst(c);
	
							dest.repaint();
	
							table.repaint();
	
							System.out.print("Destination ");
							dest.showSize();
							setScore(5);
							validMoveMade = true;
							break;
						}
					}
				}
				// from PLAY TO FINAL
				for (int x = 0; x < NUM_FINAL_DECKS; x++)
				{
					dest = final_cards[x];

					if (card.getFaceStatus() == true && source != null && dest.contains(stop) && source != dest)
					{
						if (validFinalStackMove(card, dest.getLast()))
						{
							if(dest.getFirst().getValue() == CardFM.Value.ACE && card.getValue().ordinal() == (dest.getLast().getValue().ordinal() + 1)) {
								CardFM c = source.popFirst();
									c.repaint();
	
								if (source.getFirst() != null)
								{
	
									CardFM temp = source.getFirst().setFaceup();
									temp.repaint();
									source.repaint();
								}
	
								dest.setXY(dest.getXY().x, dest.getXY().y);
								dest.push(c);
	
								dest.repaint();
	
								table.repaint();
	
								System.out.print("Destination ");
								dest.showSize();
								card = null;
								checkForWin = true;
								setScore(5);
								validMoveMade = true;
								break;
							}  else if(dest.getFirst().getValue() == CardFM.Value.KING && card.getValue().ordinal() == (dest.getLast().getValue().ordinal() - 1)) {
								CardFM c = source.popFirst();
								c.repaint();

								if (source.getFirst() != null)
								{
	
									CardFM temp = source.getFirst().setFaceup();
									temp.repaint();
									source.repaint();
								}
	
								dest.setXY(dest.getXY().x, dest.getXY().y);
								dest.push(c);
	
								dest.repaint();
	
								table.repaint();
	
								System.out.print("Destination ");
								dest.showSize();
								card = null;
								checkForWin = true;
								setScore(5);
								validMoveMade = true;
								break;
							}
						}
					}

				}
			}// end cycle through play decks

			try {
				if (deal_deck[deal_deck_pos].contains(start) && deck.showSize() > 0)
				{
					for (int x = 1; x < NUM_PLAY_DECKS; x++)
					{
						
						System.out.print("poping deck ");
						deck.showSize();
						CardFM c = deck.pop().setFaceup();
						playCardStack[x].putFirst(c);
						table.repaint();
					}
					table.remove(deal_deck[deal_deck_pos]);
					table.repaint();
					deal_deck_pos++;
				}
			} catch (Exception ex) {
				statusBox.setText("No more cards to deal!");
			}
			
			//Deal a card to an empty tableau whilst there still is cards on the deck
			if (deck.showSize() > 0)
			{
				for (int x = 0; x < NUM_PLAY_DECKS; x++)
				{
					if(playCardStack[x].empty()) {
						System.out.print("poping deck ");
						deck.showSize();
						CardFM c = deck.pop().setFaceup();
						playCardStack[x].putFirst(c);
						c.repaint();
						statusBox.setText("Card dealt to tableau "+(x+1));
						table.repaint();
					}
				}
			}
			
			
			// SHOWING STATUS MESSAGE IF MOVE INVALID
			if (!validMoveMade && dest != null && card != null)
			{
				statusBox.setText("That Is Not A Valid Move");
			}
			// CHECKING FOR WIN
			if (checkForWin)
			{
				boolean gameNotOver = false;
				// cycle through final decks, if they're all full then game over
				for (int x = 0; x < NUM_FINAL_DECKS; x++)
				{
					dest = final_cards[x];
					System.out.println("Final deck size "+dest.showSize());
					if (dest.showSize() < 13)
					{
						// one deck is not full, so game is not over
						gameNotOver = true;
						gameOver = false;
						break;
					} else { 
						gameOver = true;
					}
					if (!gameNotOver)
						gameOver = true;
				}
			}
			
			System.out.println(gameOver);
			if (checkForWin && gameOver)
			{
				JOptionPane.showMessageDialog(table, "Congratulations! You've Won!");
				statusBox.setText("Game Over!");
			}
			// RESET VARIABLES FOR NEXT EVENT
			start = null;
			stop = null;
			source = null;
			dest = null;
			card = null;
			checkForWin = false;
			gameOver = false;
		}// end mousePressed()
	}

	

	private static void playFMNewGame()
	{
		newGameButton.removeActionListener(ae);
		
		toggleTimerButton.removeActionListener(ae);
		
		showRulesButton.removeActionListener(ae);
		
		table.removeMouseListener(cm);
		table.removeMouseMotionListener(cm);
		
		frame.dispose();
		
		resetTimer();
		
		deal_deck_pos = 0;
		
		deck = new CardStackFM(true); // deal 104 cards
		deck.shuffle();
		
		table.removeAll();
		// reset stacks if user starts a new game in the middle of one
		if (playCardStack != null && final_cards != null)
		{
			for (int x = 0; x < NUM_PLAY_DECKS; x++)
			{
				playCardStack[x].makeEmpty();
			}
			for (int x = 0; x < NUM_FINAL_DECKS; x++)
			{
				final_cards[x].makeEmpty();
			}
		}
		// initialize & place final (foundation) decks/stacks
		final_cards = new FinalStackFM[NUM_FINAL_DECKS];
		for (int x = 0; x < NUM_FINAL_DECKS; x++)
		{
			final_cards[x] = new FinalStackFM();

			//Bottom Up: A to K
			if(x >= 0 && x <= 3) 
			{
				final_cards[x].setXY(200 + (x * (CardFM.CARD_WIDTH)), (FINAL_POS.y));
				for(int y = 0; y < deck.showSize(); y++) {
					
					CardFM c = deck.pop().setFaceup();
					
					if(c.getValue().equals(CardFM.Value.ACE) && c.getSuit().equals(CardFM.Suit.SPADES) 
							&& x == 0){
						
						final_cards[x].putFirst(c);
						break;
						
					}
					else if(c.getValue().equals(CardFM.Value.ACE) && c.getSuit().equals(CardFM.Suit.HEARTS) 
							&& x == 1) {
						
						final_cards[x].putFirst(c);
						break;
						
					} 
					else if(c.getValue().equals(CardFM.Value.ACE) && c.getSuit().equals(CardFM.Suit.DIAMONDS) 
							&& x == 2) {
						
						final_cards[x].putFirst(c);
						break;
						
					}
					else if(c.getValue().equals(CardFM.Value.ACE) && c.getSuit().equals(CardFM.Suit.CLUBS) 
							&& x == 3) {
	
						final_cards[x].putFirst(c);
						break;
						
					} else {
						System.out.println("Putting back on show stack: ");
						c.getValue();
						c.getSuit();
						deck.putFirst(c);
					}
				}
			}
				//Top Down: K to A
			else if(x >= 4 && x <= 7) 
				{
				final_cards[x].setXY(200 + ((x-4) * (CardFM.CARD_WIDTH)), ((65/2)*FINAL_POS.y));
					for(int y = 0; y < deck.showSize(); y++) {
						
						CardFM c = deck.pop().setFaceup();
						
						if(c.getValue().equals(CardFM.Value.KING) && c.getSuit().equals(CardFM.Suit.SPADES)
								&& x == 4){
							
							final_cards[x].putFirst(c);
							break;
							
						}
						else if(c.getValue().equals(CardFM.Value.KING) && c.getSuit().equals(CardFM.Suit.HEARTS)
								&& x == 5) {
							
							final_cards[x].putFirst(c);
							break;
							
						} 
						else if(c.getValue().equals(CardFM.Value.KING) && c.getSuit().equals(CardFM.Suit.DIAMONDS)
								&& x == 6) {
							
							final_cards[x].putFirst(c);
							break;
							
						}
						else if(c.getValue().equals(CardFM.Value.KING) && c.getSuit().equals(CardFM.Suit.CLUBS)
								&& x == 7) {
		
							final_cards[x].putFirst(c);
							break;
							
						} else {
							System.out.println("Putting back on show stack: ");
							c.getValue();
							c.getSuit();
							deck.putFirst(c);
						}
					}
				}
			
			table.add(final_cards[x]);

		}
		
		// initialize & place play (tableau) decks/stacks
		playCardStack = new CardStackFM[NUM_PLAY_DECKS];
		for (int x = 0; x < NUM_PLAY_DECKS; x++)
		{
			playCardStack[x] = new CardStackFM(false);
			playCardStack[x].setXY(350, (2*PLAY_POS.y));
			
			table.add(playCardStack[x]);
			
			if(x >= 1 && x <=4) 
			{
				
				playCardStack[x] = new CardStackFM(false);
				playCardStack[x].setXY((125/2) + (x * (CardFM.CARD_WIDTH + 15)), (3*PLAY_POS.y));

				table.add(playCardStack[x]);
				
			} 
			if(x >= 5 && x <= 8) 
			{
				
				playCardStack[x] = new CardStackFM(false);
				playCardStack[x].setXY((125/2) + ((x-4) * (CardFM.CARD_WIDTH + 15)), (4*PLAY_POS.y));

				table.add(playCardStack[x]);
				
			} if(x >= 9 && x <= 13) 
			{
				
				playCardStack[x] = new CardStackFM(false);
				playCardStack[x].setXY((125/2) + ((x-8) * (CardFM.CARD_WIDTH + 15)), (5*PLAY_POS.y));

				table.add(playCardStack[x]);
				
			}
		}
		
		
		deal_deck = new FinalStackFM[7];
		for (int x = 0; x < deal_deck.length; x++)
		{
			deal_deck[x] = new FinalStackFM();
			deal_deck[x].setXY(50 - (5 + (3*x)) + ((110/2)/7), (2*PLAY_POS.y));
			table.add(deal_deck[x]);
		}
		
		// Dealing new game
		for (int x = 0; x < NUM_PLAY_DECKS; x++)
		{
			CardFM c = deck.pop().setFaceup();
			playCardStack[x].putFirst(c);

		}
		
		newGameButton.addActionListener(ae);
		newGameButton.setBounds(0, TABLE_HEIGHT - 70, 120, 30);

		showRulesButton.addActionListener(ae);
		showRulesButton.setBounds(120, TABLE_HEIGHT - 70, 120, 30);

		gameTitle.setText("<b>Team 2<br>Flea Market Solitaire</b> <br> CPSC 4900 <br> Fall 2021");
		gameTitle.setEditable(false);
		gameTitle.setOpaque(false);
		gameTitle.setBounds(32, 20, 100, 100);

		scoreBox.setBounds(240, TABLE_HEIGHT - 70, 120, 30);
		scoreBox.setText("Score: 0");
		scoreBox.setEditable(false);
		scoreBox.setOpaque(false);

		timeBox.setBounds(360, TABLE_HEIGHT - 70, 120, 30);
		timeBox.setText("Seconds: 0");
		timeBox.setEditable(false);
		timeBox.setOpaque(false);

		startTimer();

		toggleTimerButton.setBounds(480, TABLE_HEIGHT - 70, 125, 30);
		toggleTimerButton.addActionListener(ae);

		statusBox.setBounds(605, TABLE_HEIGHT - 70, 180, 30);
		statusBox.setEditable(false);
		statusBox.setOpaque(false);
		
		Container contentPane;

		frame.setSize(TABLE_WIDTH, TABLE_HEIGHT);

		table.setLayout(null);
		table.setBackground(SolitaireMenu.getColor());
		
		scoreBox.setText("Score: "+score);
		
		table.add(statusBox);
		table.add(toggleTimerButton);
		table.add(gameTitle);
		table.add(timeBox);
		table.add(newGameButton);
		table.add(newCardButton);
		table.add(showRulesButton);
		table.add(scoreBox);
		
		table.repaint();
		
		contentPane = frame.getContentPane();
		contentPane.add(table);
		frame.addWindowListener(new windowListener());

		table.addMouseListener(cm);
		table.addMouseMotionListener(cm);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}


	public static void main(String[] args)
	{
		
		playFMNewGame();
		
	}
}