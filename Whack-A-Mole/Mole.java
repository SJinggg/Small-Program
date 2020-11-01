//gui
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//random seed and game timer
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.text.NumberFormat;
//audio input
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.File;

public class Mole extends JFrame{
	//components of the frame
	private JPanel holePane;
	private JDialog gameOver = new JDialog();
	private JButton[][] holes = new JButton[3][3];
	private JButton startBtn;
	
	//holder for data while game is started 
	private String mId = "";
	private static int hitTimes = 0, missTimes = 0;
	private final int GAMETIME = 15;
	private static String pressedBtn = "";
	
	//import image
	Image img = new ImageIcon("mole.png").getImage();
	Image mole = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	
	//create Frame
	public Mole() {
		super("Hit A Mole");
		setResizable(false);
		setSize(500,570);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		appInit();

		setVisible(true);
	}
	
	//initiate components inside the frame
	private void appInit(){
		JPanel contentPane = new JPanel();
		contentPane.setLayout(null);
		
		startBtn = new JButton("Start");
		startBtn.setBounds(10,10,80,20);
		contentPane.add(startBtn);
		startBtn.addActionListener(new startClickedListener());
		
		JLabel lbl1 = new JLabel("< Click to start new game");
		lbl1.setBounds(100,10,200,20);
		contentPane.add(lbl1);
		
		holePane = new JPanel(new GridLayout(3,3));
		holePane.setBounds(0, 40, 500, 500);
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				holes[i][j] = new JButton();
				holes[i][j].setName(Integer.toString(i) + Integer.toString(j));
				holes[i][j].setBackground(new Color(170, 89, 45));
				holePane.add(holes[i][j]);

			}
		}
		
		holesListener();
		
		contentPane.add(holePane);
		
		setContentPane(contentPane);
	}
	
	//generate random Mole
	public void randomMole(){
		Random rnd = new Random(System.currentTimeMillis()); 
		int a = rnd.nextInt(3);
		int b = rnd.nextInt(3);
		mId = Integer.toString(a) + Integer.toString(b);
			
		holes[a][b].setIcon(new ImageIcon(mole));
	}
	
	//create ActionListener for each holes
	void holesListener(){
		for(int i = 0; i < holes.length; i++){
			for(int j = 0; j < holes[i].length; j++){
				holes[i][j].addActionListener(new clickedMoleListener());
			}
		}
	}
	
	//clear random generated Mole for next frame
	private void clearMole(){
		for(int i = 0; i < holes.length; i++)
			for(int j = 0; j< holes[i].length; j++)
				holes[i][j].setIcon(null);
		mId = "";
	}
	
	//invoked when start button clicked
	public void gameStart(){
		startBtn.setEnabled(false);
		final Timer timer = new Timer();
		
		int period = 1000;
		//start generate mole while timer started
		timer.scheduleAtFixedRate(new TimerTask() {
			int i = GAMETIME;
			public void run() {
				randomMole();
				// Sleep until it's time to draw the next frame 
				try{
					Thread.sleep(600);	
					checkWhack();
					clearMole();	
					i--;					
				}
				catch(InterruptedException e){
					return;
				}
				if (i <= 0){
                    timer.cancel();
					gameOver();
				}
			}
		}, 0, period);
		
	}
	
	//import audio
	public void playSound(String soundName){
		try{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile( ));	
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch(Exception ex){
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}
	
	//check if whacked or missed and display sound
	public void checkWhack(){
		if(mId.equals(pressedBtn)){
			hitTimes += 1;
			playSound("whack.wav");
		}
		else{
			if(!mId.equals(null))
				missTimes += 1;
			playSound("laugh.wav");
		}
		pressedBtn = "";
	}
	
	//display marks and clear data
	public void gameOver(){
		gameOver.setTitle("Game Over");
		gameOver.setSize(200,120);
		gameOver.setLayout(null);
		gameOver.setLocationRelativeTo(holePane);
		
		JLabel result = new JLabel("Hit " + hitTimes + " and missed " + missTimes + " times!");
		result.setBounds(10,10,200,20);
		
		JButton okBtn = new JButton("OK");
		okBtn.setBounds(50,50,80,20);
		okBtn.addActionListener(new disposeJDialog());
		
		gameOver.add(result);
		gameOver.add(okBtn);
		
		gameOver.setVisible(true);
		
		gameOver.addWindowListener(new disposeJDialog());
		
		hitTimes = 0; missTimes = 0;
		startBtn.setEnabled(true);
		
	}
	
	public static void main(String[] args){
		Mole game = new Mole();
	}
	
	//start game while start clicked
	class startClickedListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			gameStart();
		
		}
	}
	
	//get the holes id clicked for checking
	class clickedMoleListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			JButton clickedBtn = (JButton)evt.getSource();
			pressedBtn = clickedBtn.getName();
		}
	}
	
	//dispose JDialog
	class disposeJDialog implements ActionListener, WindowListener{
		public void actionPerformed(ActionEvent evt){
			gameOver.dispose();
			gameOver = new JDialog();
		}
		public void windowClosing(WindowEvent evt){
			gameOver.dispose();
			gameOver = new JDialog();
		}
		public void windowActivated(WindowEvent e){};
		public void windowClosed(WindowEvent e){};
		public void windowDeactivated(WindowEvent e){};
		public void windowDeiconified(WindowEvent e){};
		public void windowIconified(WindowEvent e){};
		public void windowOpened(WindowEvent e){};
	}
}

