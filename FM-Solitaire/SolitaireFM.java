import Klondike.SolitaireK;

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

//test
public class SolitaireFM
{
	// CONSTANTS
	public static final int TABLE_HEIGHT = CardFM.CARD_HEIGHT * 8;
	public static final int TABLE_WIDTH = (CardFM.CARD_WIDTH * 8) + 100;
	public static final int NUM_FINAL_DECKS = 8;
	public static final int NUM_PLAY_DECKS = 13;
	public static final Point DECK_POS = new Point(5, 5);
	public static final Point SHOW_POS = new Point(DECK_POS.x + CardFM.CARD_WIDTH + 5, DECK_POS.y);
	public static final Point FINAL_POS = new Point(SHOW_POS.x + CardFM.CARD_WIDTH + 150, DECK_POS.y);
	public static final Point PLAY_POS = new Point(DECK_POS.x, FINAL_POS.y + CardFM.CARD_HEIGHT + 30);

	// GAMEPLAY STRUCTURES
	private static FinalStackFM[] final_cards;// Foundation Stacks
	private static CardStackFM[] playCardStack; // Tableau stacks
	private static final CardFM newCardPlace = new CardFM();// waste card spot
	private static CardStackFM deck; // populated with standard 52 card deck

	// GUI COMPONENTS (top level)
	private static final JFrame frame = new JFrame("Klondike Solitaire");
	private static final JFrame frame1 = new JFrame("Solitaire Menu");
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
	private static JButton newCardButton = new JButton("Deal Cards");// deal waste cards
	
	private static ActionListener ae = new setUpButtonListeners();
	
	
	//NEW ADDITIONS
	private static JButton klondikeStart = new JButton("Klondike");
	private static JButton fleaMarketStart = new JButton("fleaMarket");

	// TIMER UTILITIES
	private static Timer timer = new Timer();
	private static ScoreClock scoreClock = new ScoreClock();

	// MISC TRACKING VARIABLES
	private static boolean timeRunning = false;// timer running?
	private static int score = 0;// keep track of the score
	private static int time = 0;// keep track of seconds elapsed

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
		if (SolitaireFM.time % 10 == 0)
		{
			setScore(-2);
		}
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
			} else if (e.getSource() == newCardButton) {
				// SHOW (WASTE) CARD OPERATIONS
				// deal new card to each tableau -- needs updating, will add multiple cards because of previous mouse presses
				if (deck.showSize() > 0)
					{
					for (int x = 0; x < NUM_PLAY_DECKS; x++)
						{
						System.out.print("poping deck ");
						deck.showSize();
						CardFM c = deck.pop().setFaceup();
						playCardStack[x].putFirst(c);
						table.repaint();
					}
				}
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
					ruleFrame.setSize(TABLE_HEIGHT, TABLE_WIDTH);
					JScrollPane scroll;
					JEditorPane rulesTextPane = new JEditorPane("text/html", "");
					rulesTextPane.setEditable(false);
					String rulesText = "<b>Klondike Solitaire Rules</b>"
							+ "<br><br> (From Wikipedia) Taking a shuffled standard 52-card deck of playing cards (without Jokers),"
							+ " one upturned card is dealt on the left of the playing area, then six downturned cards"
							+ " (from left to right).<p> On top of the downturned cards, an upturned card is dealt on the "
							+ "left-most downturned pile, and downturned cards on the rest until all piles have an "
							+ "upturned card. The piles should look like the figure to the right.<p>The four foundations "
							+ "(light rectangles in the upper right of the figure) are built up by suit from Ace "
							+ "(low in this game) to King, and the tableau piles can be built down by alternate colors,"
							+ " and partial or complete piles can be moved if they are built down by alternate colors also. "
							+ "Any empty piles can be filled with a King or a pile of cards with a King.<p> The point of "
							+ "the game is to build up a stack of cards starting with 2 and ending with King, all of "
							+ "the same suit. Once this is accomplished, the goal is to move this to a foundation, "
							+ "where the player has previously placed the Ace of that suit. Once the player has done this, "
							+ "they will have \"finished\" that suit- the goal being, of course, to finish all suits, "
							+ "at which time the player will have won.<br><br><b> Scoring </b><br><br>"
							+ "Moving cards directly from the Waste stack to a Foundation awards 10 points. However, "
							+ "if the card is first moved to a Tableau, and then to a Foundation, then an extra 5 points "
							+ "are received for a total of 15. Thus in order to receive a maximum score, no cards should be moved "
							+ "directly from the Waste to Foundation.<p>	Time can also play a factor in Windows Solitaire, if the Timed game option is selected. For every 10 seconds of play, 2 points are taken away."
							+ "<b><br><br>Notes On My Implementation</b><br><br>"
							+ "Drag cards to and from any stack. As long as the move is valid the card, or stack of "
							+ "cards, will be repositioned in the desired spot. The game follows the standard scoring and time"
							+ " model explained above with only one waste card shown at a time."
							+ "<p> The timer starts running as soon as "
							+ "the game begins, but it may be paused by pressing the pause button at the bottom of"
							+ "the screen. ";
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
			
			newCardButton.removeActionListener(ae);
			
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
		private CardFM prevCard = null;// tracking card for waste stack
		private CardFM movedCard = null;// card moved from waste stack
		private boolean sourceIsFinalDeck = false;
		private boolean putBackOnDeck = true;// used for waste card recycling
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
			int s_val = source.getValue().ordinal();
			int d_val = dest.getValue().ordinal();
			CardFM.Suit s_suit = source.getSuit();
			CardFM.Suit d_suit = dest.getSuit();

			// destination card should be one higher value
			if ((s_val + 1) == d_val)
			{
				// destination card should be opposite color
				switch (s_suit)
				{
				case SPADES:
					if (d_suit != CardFM.Suit.HEARTS && d_suit != CardFM.Suit.DIAMONDS)
						return false;
					else
						return true;
				case CLUBS:
					if (d_suit != CardFM.Suit.HEARTS && d_suit != CardFM.Suit.DIAMONDS)
						return false;
					else
						return true;
				case HEARTS:
					if (d_suit != CardFM.Suit.SPADES && d_suit != CardFM.Suit.CLUBS)
						return false;
					else
						return true;
				case DIAMONDS:
					if (d_suit != CardFM.Suit.SPADES && d_suit != CardFM.Suit.CLUBS)
						return false;
					else
						return true;
				}
				return false; // this never gets reached
			} else
				return false;
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
			 * Here we use transferStack to temporarily hold all the cards above
			 * the selected card in case player wants to move a stack rather
			 * than a single card
			 */
			for (int x = 0; x < NUM_PLAY_DECKS; x++)
			{
				if (stopSearch)
					break;
				source = playCardStack[x];
				// pinpointing exact card pressed
				for (Component ca : source.getComponents())
				{
					CardFM c = (CardFM) ca;
					if (c.getFaceStatus() && source.contains(start))
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
			

			// preparing to move show card
			if (newCardPlace.contains(start) && prevCard != null)
			{
				movedCard = prevCard;
			}

			// FINAL (FOUNDATION) CARD OPERATIONS
			for (int x = 0; x < NUM_FINAL_DECKS; x++)
			{

				if (final_cards[x].contains(start))
				{
					source = final_cards[x];
					card = source.getLast();
					transferStack.putFirst(card);
					sourceIsFinalDeck = true;
					break;
				}
			}
			putBackOnDeck = true;

			
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			stop = e.getPoint();
			// used for status bar updates
			boolean validMoveMade = false;

			// SHOW CARD MOVEMENTS
			if (movedCard != null)
			{
				// Moving from SHOW TO PLAY
				for (int x = 0; x < NUM_PLAY_DECKS; x++)
				{
					dest = playCardStack[x];
					// to empty play stack, only kings can go
					if (dest.empty() && movedCard != null && dest.contains(stop)
							&& movedCard.getValue() == CardFM.Value.KING)
					{
						System.out.print("moving new card to empty spot ");
						movedCard.setXY(dest.getXY());
						table.remove(prevCard);
						dest.putFirst(movedCard);
						table.repaint();
						movedCard = null;
						putBackOnDeck = false;
						setScore(5);
						validMoveMade = true;
						break;
					}
					// to populated play stack
					if (movedCard != null && dest.contains(stop) && !dest.empty() && dest.getFirst().getFaceStatus()
							&& validPlayStackMove(movedCard, dest.getFirst()))
					{
						System.out.print("moving new card ");
						movedCard.setXY(dest.getFirst().getXY());
						table.remove(prevCard);
						dest.putFirst(movedCard);
						table.repaint();
						movedCard = null;
						putBackOnDeck = false;
						setScore(5);
						validMoveMade = true;
						break;
					}
				}
				// Moving from SHOW TO FINAL
				for (int x = 0; x < NUM_FINAL_DECKS; x++)
				{
					dest = final_cards[x];
					// only aces can go first
					if (dest.empty() && dest.contains(stop))
					{
						if (movedCard.getValue() == CardFM.Value.ACE || movedCard.getValue() == CardFM.Value.KING)
						{
							dest.push(movedCard);
							table.remove(prevCard);
							dest.repaint();
							table.repaint();
							movedCard = null;
							putBackOnDeck = false;
							setScore(10);
							validMoveMade = true;
							break;
						}
					}
					if (!dest.empty() && dest.contains(stop) && validFinalStackMove(movedCard, dest.getLast()))
					{
						System.out.println("Destin" + dest.showSize());
						dest.push(movedCard);
						table.remove(prevCard);
						dest.repaint();
						table.repaint();
						movedCard = null;
						putBackOnDeck = false;
						checkForWin = true;
						setScore(10);
						validMoveMade = true;
						break;
					}
				}
			}// END SHOW STACK OPERATIONS

			// PLAY STACK OPERATIONS
			if (card != null && source != null)
			{ // Moving from PLAY TO PLAY
				for (int x = 0; x < NUM_PLAY_DECKS; x++)
				{
					dest = playCardStack[x];
					// MOVING TO POPULATED STACK
					if (card.getFaceStatus() == true && dest.contains(stop) && source != dest && !dest.empty()
							&& validPlayStackMove(card, dest.getFirst()) && transferStack.showSize() == 1)
					{
						CardFM c = null;
						if (sourceIsFinalDeck)
							c = source.pop();
						else
							c = source.popFirst();

						c.repaint();
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
						if (sourceIsFinalDeck)
							setScore(15);
						else
							setScore(10);
						validMoveMade = true;
						break;
					} else if (dest.empty() && card.getValue() == CardFM.Value.KING && transferStack.showSize() == 1)
					{// MOVING TO EMPTY STACK, ONLY KING ALLOWED
						CardFM c = null;
						if (sourceIsFinalDeck)
							c = source.pop();
						else
							c = source.popFirst();

						c.repaint();
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
					}
					// Moving STACK of cards from PLAY TO PLAY
					// to EMPTY STACK
					if (dest.empty() && dest.contains(stop) && !transferStack.empty()
							&& transferStack.getFirst().getValue() == CardFM.Value.KING)
					{
						System.out.println("King To Empty Stack Transfer");
						while (!transferStack.empty())
						{
							System.out.println("popping from transfer: " + transferStack.getFirst().getValue());
							dest.putFirst(transferStack.popFirst());
							source.popFirst();
						}
						if (source.getFirst() != null)
						{
							CardFM temp = source.getFirst().setFaceup();
							temp.repaint();
							source.repaint();
						}

						dest.setXY(dest.getXY().x, dest.getXY().y);
						dest.repaint();

						table.repaint();
						setScore(5);
						validMoveMade = true;
						break;
					}
					// to POPULATED STACK
					if (dest.contains(stop) && !transferStack.empty() && source.contains(start)
							&& validPlayStackMove(transferStack.getFirst(), dest.getFirst()))
					{
						System.out.println("Regular Stack Transfer");
						while (!transferStack.empty())
						{
							System.out.println("popping from transfer: " + transferStack.getFirst().getValue());
							dest.putFirst(transferStack.popFirst());
							source.popFirst();
						}
						if (source.getFirst() != null)
						{
							CardFM temp = source.getFirst().setFaceup();
							temp.repaint();
							source.repaint();
						}

						dest.setXY(dest.getXY().x, dest.getXY().y);
						dest.repaint();

						table.repaint();
						setScore(5);
						validMoveMade = true;
						break;
					}
				}
				// from PLAY TO FINAL
				for (int x = 0; x < NUM_FINAL_DECKS; x++)
				{
					dest = final_cards[x];

					if (card.getFaceStatus() == true && source != null && dest.contains(stop) && source != dest)
					{// TO EMPTY STACK
						//Flea Market doesn't have empty final stacks so this shouldn't be used but will keep for reference
						if (dest.empty())// empty final should only take an ACE
						{
							if (card.getValue() == CardFM.Value.ACE || card.getValue() == CardFM.Value.KING)
							{
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
								setScore(10);
								validMoveMade = true;
								break;
							}// TO POPULATED STACK
						} else if (validFinalStackMove(card, dest.getLast()))
						{
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
							setScore(10);
							validMoveMade = true;
							break;
						}
					}

				}
			}// end cycle through play decks

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
					if (dest.showSize() != 13)
					{
						// one deck is not full, so game is not over
						gameNotOver = true;
						break;
					}
				}
				if (!gameNotOver)
					gameOver = true;
			}

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
			sourceIsFinalDeck = false;
			checkForWin = false;
			gameOver = false;
		}// end mousePressed()
	}

	public static void playFMNewGame()
	{
		newCardButton.removeActionListener(ae);
		
		newGameButton.removeActionListener(ae);
		
		toggleTimerButton.removeActionListener(ae);
		
		showRulesButton.removeActionListener(ae);
		
		frame.dispose();
		
		deck = new CardStackFM(true); // deal 52 cards
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
				final_cards[x].setXY((FINAL_POS.x + (x * CardFM.CARD_WIDTH)) + 10, (FINAL_POS.y));
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
				final_cards[x].setXY((FINAL_POS.x + ((x-4) * CardFM.CARD_WIDTH)) + 10, ((65/2)*FINAL_POS.y));
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
		// place new card distribution button
		table.add(newCardButton);
		// initialize & place play (tableau) decks/stacks
		playCardStack = new CardStackFM[NUM_PLAY_DECKS];
		for (int x = 0; x < NUM_PLAY_DECKS; x++)
		{
			playCardStack[x] = new CardStackFM(false);
			playCardStack[x].setXY((DECK_POS.x + (TABLE_WIDTH/8 + (3 * (CardFM.CARD_WIDTH) - 25))), (2*PLAY_POS.y));
			
			table.add(playCardStack[x]);
			
			if(x >= 1 && x <=4) 
			{
				
				playCardStack[x] = new CardStackFM(false);
				playCardStack[x].setXY((DECK_POS.x + (TABLE_WIDTH/8 + (x * (CardFM.CARD_WIDTH + 10)))), (3*PLAY_POS.y));
				
				table.add(playCardStack[x]);
				
			} 
			if(x >= 5 && x <= 8) 
			{
				
				playCardStack[x] = new CardStackFM(false);
				playCardStack[x].setXY((DECK_POS.x + (TABLE_WIDTH/8 + ((x-4) * (CardFM.CARD_WIDTH + 10)))), (4*PLAY_POS.y));
				
				table.add(playCardStack[x]);
				
			} if(x >= 9 && x <= 13) 
			{
				
				playCardStack[x] = new CardStackFM(false);
				playCardStack[x].setXY((DECK_POS.x + (TABLE_WIDTH/8 + ((x-8) * (CardFM.CARD_WIDTH + 10)))), (5*PLAY_POS.y));
				
				table.add(playCardStack[x]);
				
			}
		}

		// Dealing new game
		for (int x = 0; x < NUM_PLAY_DECKS; x++)
		{
			CardFM c = deck.pop().setFaceup();
			playCardStack[x].putFirst(c);

		}
		// reset time
		time = 0;
		
		
		
		newCardButton.addActionListener(ae);
		newCardButton.setBounds(15, TABLE_HEIGHT - 600, 120, 120);
		
		newGameButton.addActionListener(ae);
		newGameButton.setBounds(0, TABLE_HEIGHT - 70, 120, 30);

		showRulesButton.addActionListener(ae);
		showRulesButton.setBounds(120, TABLE_HEIGHT - 70, 120, 30);

		gameTitle.setText("<b>Team 2<br>Flea Market Solitaire</b> <br> CPSC 4900 <br> Fall 2021");
		gameTitle.setEditable(false);
		gameTitle.setOpaque(false);
		gameTitle.setBounds(245, 20, 100, 100);

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
		//changes
		Container contentPane;

		frame.setSize(TABLE_WIDTH, TABLE_HEIGHT);

		table.setLayout(null);
		table.setBackground(new Color(0, 180, 0));
		//changes
		
		table.add(statusBox);
		table.add(toggleTimerButton);
		table.add(gameTitle);
		table.add(timeBox);
		table.add(newGameButton);
		table.add(newCardButton);
		table.add(showRulesButton);
		table.add(scoreBox);
		table.repaint();
		
		//changes 
		contentPane = frame.getContentPane();
		contentPane.add(table);
		frame.addWindowListener(new windowListener());

		table.addMouseListener(new CardMovementManager());
		table.addMouseMotionListener(new CardMovementManager());

		frame.setVisible(true);
	}


	public static void main(String[] args)
	{
		SolitaireMenu.main(args);
		//playFMNewGame();
	}
}