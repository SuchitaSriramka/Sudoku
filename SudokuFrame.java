package sudoku;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class SudokuFrame extends JFrame {

	private JPanel buttonSelectionPanel;
	private SudokuPanel sPanel;
	
	public SudokuFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Sudoku");
		this.setMinimumSize(new Dimension(800,600));
		
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("Game");
		JMenu newGame = new JMenu("New Game");
		JMenuItem sixBySixGame = new JMenuItem("6 by 6 Game");
		sixBySixGame.addActionListener(new NewGameListener(SudokuPuzzleType.SIXBYSIX,30));
		JMenuItem nineByNineGame = new JMenuItem("9 by 9 Game");
		nineByNineGame.addActionListener(new NewGameListener(SudokuPuzzleType.NINEBYNINE,26));
		JMenuItem twelveByTwelveGame = new JMenuItem("12 by 12 Game");
		twelveByTwelveGame.addActionListener(new NewGameListener(SudokuPuzzleType.TWELVEBYTWELVE,20));
		
		newGame.add(sixBySixGame);
		newGame.add(nineByNineGame);
		newGame.add(twelveByTwelveGame);
		
		file.add(newGame);
		menuBar.add(file);
		this.setJMenuBar(menuBar);
		
		JPanel windowPanel = new JPanel();
		windowPanel.setLayout(new FlowLayout());
		windowPanel.setPreferredSize(new Dimension(800,600));
		
		this.buttonSelectionPanel = new JPanel();
		this.buttonSelectionPanel.setPreferredSize(new Dimension(100,500));
		
		this.sPanel = new SudokuPanel();
		
		windowPanel.add(this.sPanel);
		windowPanel.add(this.buttonSelectionPanel);
		this.add(windowPanel);
		
		rebuildInterface(SudokuPuzzleType.NINEBYNINE,26);
	}
	
	public void rebuildInterface(SudokuPuzzleType puzzleType, int fontSize) {
		SudokuPuzzle generatedPuzzle = new SudokuGenerator().generateRandomSudoku(puzzleType);
		this.sPanel.newSudokuPuzzle(generatedPuzzle);
		this.sPanel.setFontSize(fontSize);
		this.buttonSelectionPanel.removeAll();
		for(String value : generatedPuzzle.getValidValues()) {
			JButton jb = new JButton(value);
			jb.setPreferredSize(new Dimension(45,45));
			jb.addActionListener(sPanel.new NumActionListener());
			this.buttonSelectionPanel.add(jb);
		}
		this.sPanel.repaint();
		this.buttonSelectionPanel.revalidate();
		this.buttonSelectionPanel.repaint();
	}
	
	private class NewGameListener implements ActionListener{
		 private SudokuPuzzleType puzzleType;
		 private int fontSize;
		 
		 public NewGameListener(SudokuPuzzleType puzzleType, int fontSize) {
			 this.puzzleType = puzzleType;
			 this.fontSize = fontSize;
		 }
		 
		 @Override
		 public void actionPerformed(ActionEvent e) {
			 rebuildInterface(puzzleType,fontSize);
		 }
	}

}

