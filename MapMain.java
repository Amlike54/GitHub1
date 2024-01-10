import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.File;
import java.io.IOException;
public class MapMain{
	static int rowNum;
	static int colNum;
	static int squareSize;
	static int panelSize;
	static int frameWidth;
	static int frameHeight;
	static int maxSquareValue;
	
	public static void main(String[] args) {
		
		
		
		
		
		//Game presets:
		rowNum = 10;
		colNum = 10;	
		squareSize = 50;
		panelSize = 50;
		frameWidth = (colNum+1) * squareSize + panelSize*2;
		frameHeight = (rowNum+1) * squareSize + panelSize*2;
		maxSquareValue = 50;
		
		
		//canvas initialization
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameWidth, frameHeight);
		frame.setLayout(new BorderLayout());
		
		JPanel northPanel  = new JPanel();
		northPanel.setPreferredSize(new Dimension(0, panelSize));
		northPanel.setBackground(Color.DARK_GRAY);
		frame.add(northPanel, BorderLayout.NORTH);
		JPanel eastPanel = new JPanel();
		eastPanel.setPreferredSize(new Dimension(panelSize, 0));
		eastPanel.setBackground(Color.DARK_GRAY);
		frame.add(eastPanel, BorderLayout.EAST);
		JPanel southPanel = new JPanel();
		southPanel.setPreferredSize(new Dimension(0, panelSize));
		southPanel.setBackground(Color.DARK_GRAY);
		frame.add(southPanel, BorderLayout.SOUTH);
		JPanel westPanel = new JPanel();
		westPanel.setPreferredSize(new Dimension(panelSize, 0));
		westPanel.setBackground(Color.DARK_GRAY);
		frame.add(westPanel, BorderLayout.WEST);
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.BLACK);
		frame.add(centerPanel, BorderLayout.CENTER);
		
		JPanel map = new JPanel();
		map.setPreferredSize(new Dimension(colNum*squareSize+1, rowNum*squareSize+1));
		map.setBackground(Color.GREEN);
		map.setLayout(new BorderLayout());
		centerPanel.add(map);
		
		JLabel label = new JLabel();
		label.setForeground(Color.WHITE);
		label.setText("Squares captured: 1/60");
		southPanel.add(label);
		
		Canvas canvas = new Canvas(rowNum, colNum, squareSize, label);
		map.add(canvas, BorderLayout.CENTER);
		frame.setVisible(true);
		
	}
	
}
	
class Canvas extends JPanel implements MouseListener, KeyListener {
	//Additional game presets:
	int lakeSize = 20;
	int mountainNum = 10;
	int aiNum = 5;
	int unitStrength = 1;
	int cityNum = 10;
	int cityValue = 30;
	
	
	//
	
	
	grid grid1;
	int sSize1;
	JLabel label;
	ArrayList<player> playerList = new ArrayList<>();
	ArrayList<player> aiList = new ArrayList<>();
	Random rng = new Random();
	ArrayList<lake> lakeList = new ArrayList<>();
	ArrayList<mountain> mountainList = new ArrayList<>();
	ArrayList<city> cityList = new ArrayList<>();
	gridSquare lakeTarget;
	int turnNum = 1;
	
	
	Canvas(int rNum, int cNum, int sSize, JLabel label1) {
		this.setBackground(new Color(0, 75, 0)); //Dark Green
		this.grid1 = new grid(rNum, cNum, sSize);
		this.sSize1 = sSize;
		this.label = label1;
		
		//player initialization:
		
		
		player Player1 = new player(Color.GREEN);
		playerList.add(Player1);
		
		if (aiNum>=1) {
			player AI1 = new player(Color.RED);
			playerList.add(AI1);
			aiList.add(AI1);
		}
		
		if (aiNum>=2) {
			player AI2 = new player(Color.ORANGE);
			playerList.add(AI2);
			aiList.add(AI2);
		}
		
		if (aiNum>=3) {
			player AI3 = new player(Color.PINK);
			playerList.add(AI3);
			aiList.add(AI3);
		}
		
		if (aiNum>=4) {
			player AI4 = new player(Color.CYAN);
			playerList.add(AI4);
			aiList.add(AI4);
			
		}
		
		if (aiNum>=5) {
			player AI5 = new player(Color.MAGENTA);
			playerList.add(AI5);
			aiList.add(AI5);
		}
		
		
		
		
		
		//lake initialization:
		lake iLake = new lake(this.grid1.getGridArray().get(rng.nextInt(rNum)).get(rng.nextInt(cNum)));
		iLake.g1.feature = iLake;
		iLake.g1.value = 0;
		lakeList.add(iLake);
		lake cLake = iLake;	
		
		for (int i=0; i<lakeSize-1; i++) {
			HashSet<Integer> cases = new HashSet<Integer>();
			while (true){
				int d = rng.nextInt(4);
				
				cases.add(d);
				
				switch(d) {
				case 0:
					if (cLake.g1.yNum1!=0) {
						lakeTarget = grid1.getRowArray(cLake.g1.yNum1-1).get(cLake.g1.xNum1);
					}break;
				case 1:
					if (cLake.g1.xNum1!=cNum-1) {
						lakeTarget = grid1.getRowArray(cLake.g1.yNum1).get(cLake.g1.xNum1+1);
					}break;
				case 2:
					if (cLake.g1.yNum1!=rNum-1) {
						lakeTarget = grid1.getRowArray(cLake.g1.yNum1+1).get(cLake.g1.xNum1);
					}break;
				case 3:
					if (cLake.g1.xNum1!=0) {
						lakeTarget = grid1.getRowArray(cLake.g1.yNum1).get(cLake.g1.xNum1-1);
					}break;
				default:
					break;
				}
				if (cases.size()==4) {
					cLake = lakeList.get(rng.nextInt(lakeList.size()));
				}
				
				if (lakeTarget != null) {
					if (lakeTarget.getFeature() == null) {
						lake l = new lake(lakeTarget);
						lakeList.add(l);
						l.g1.feature = l;
						l.g1.value = 0;
						cLake = lakeList.get(rng.nextInt(lakeList.size()));
						lakeTarget = null;
						break;
					}				
				}				
			}	
		}
		//mountain initialization:
		int rngRow;
		int rngSquare;
		
		for(int p=0;p<mountainNum;p++) {
		
		while (true) {
			rngRow = rng.nextInt(rNum);
			rngSquare = rng.nextInt(cNum);
			if (grid1.getRowArray(rngRow).get(rngSquare).feature == null) {
				mountain mountain1 = new mountain(grid1.getRowArray(rngRow).get(rngSquare));
				mountainList.add(mountain1);
				grid1.getRowArray(rngRow).get(rngSquare).feature = mountain1;
				grid1.getRowArray(rngRow).get(rngSquare).value = 0;
				break;
			}
		}
		
		}
		
		
		
		//unit initialization:
		
		
		
		
		for (player p : playerList) {
			
			
			while (true) {
				rngRow = rng.nextInt(rNum);
				rngSquare = rng.nextInt(cNum);
				if (grid1.getRowArray(rngRow).get(rngSquare).feature == null) {
					unit unit1 = new unit(grid1.getRowArray(rngRow).get(rngSquare), unitStrength, p);
					grid1.getRowArray(rngRow).get(rngSquare).feature = unit1;
					unit1.g1.owner = p;
					
					p.pUnitList.add(unit1);
					p.pSquareList.add(grid1.getRowArray(rngRow).get(rngSquare));
					p.pCapitol = new capitol(grid1.getRowArray(rngRow).get(rngSquare));
					p.pCapitol.owner = p;
					p.pCapitol.gs.value = cityValue;
					p.pCapitol.gs.capitol = p.pCapitol;
					
					break;
				}
			}
			
		}
		
		//city initialization
		for(int i = 0; i < cityNum; i++) {
			
			while (true) {
				rngRow = rng.nextInt(rNum);
				rngSquare = rng.nextInt(cNum);
				if (grid1.getRowArray(rngRow).get(rngSquare).feature == null) {
					city city1 = new city(grid1.getRowArray(rngRow).get(rngSquare));
					cityList.add(city1);
					grid1.getRowArray(rngRow).get(rngSquare).city = city1;
					grid1.getRowArray(rngRow).get(rngSquare).value = cityValue;
					break;
				}
			}
		}
			
		
		addMouseListener(this);
		addKeyListener(this);
		this.setFocusable(true);
		System.out.println("Turn: " + turnNum);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
//		g2d.setColor(Color.BLUE);
//		BufferedImage lakeImage = null;
//		
//
//		for (lake l : lakeList) {
//			//g2d.drawImage(lakeImage,l.g1.position.x, l.g1.position.y, sSize1, sSize1, null);
//			g2d.fillRect(l.g1.position.x, l.g1.position.y, sSize1, sSize1);
//		}
//		
//
//		g2d.setColor(Color.GRAY);
//		for (mountain m : mountainList) {
//			//g2d.drawImage(mountainImage,l.g1.position.x, l.g1.position.y, sSize1, sSize1, null);
//			g2d.fillRect(m.g1.position.x, m.g1.position.y, sSize1, sSize1);
//		}
		BufferedImage lakeImage = null;
	    try {
	        File lakeFile = new File("src/lakeImage.png"); // Replace with your actual image file name and path
	        lakeImage = ImageIO.read(lakeFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    // Draw lake image
	    for (lake l : lakeList) {
	        g2d.drawImage(lakeImage, l.g1.position.x, l.g1.position.y, sSize1, sSize1, null);
	    }
	    // Load mountain image from local storage
	    BufferedImage mountainImage = null;
	    try {
	        File mountainFile = new File("src/mountainImage.png"); // Replace with your actual image file name and path
	        mountainImage = ImageIO.read(mountainFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    // Draw mountain image
	    for (mountain m : mountainList) {
	        g2d.drawImage(mountainImage, m.g1.position.x, m.g1.position.y, sSize1, sSize1, null);
	    }
		
		
		
		//Draw territories
		for (player p : playerList) {
			g2d.setColor(p.pColor);
			for (gridSquare a : p.pSquareList) {
				g2d.fillRect(a.position.x, a.position.y, sSize1, sSize1);
				
			}
		}
		
		//Draw selected grid squares
		for (unit u : playerList.get(0).pUnitList) {
			if (u.selectedGS != null) {
				g2d.setColor(Color.GRAY);
				g2d.fillRect(u.selectedGS.getPosition().x, u.selectedGS.getPosition().y, sSize1, sSize1);
				//System.out.println("Selected GS: " + u.selectedGS.xNum1 +", "+ u.selectedGS.yNum1);
			}
		}
		
		//Draw target grid squares
		for (unit u : playerList.get(0).pUnitList) {
			if (u.targetGS != null) {
				g2d.setColor(Color.yellow);
				g2d.fillRect(u.targetGS.getPosition().x, u.targetGS.getPosition().y, sSize1, sSize1);
				//System.out.println("Target GS: " + u.targetGS.xNum1 +", "+ u.targetGS.yNum1);
			}
		}
		
		//Draw units
		for (ArrayList<gridSquare> a : grid1.g3) {
			for (gridSquare b : a){
				if (b.feature instanceof unit) {
					unit c = (unit) b.feature;
					int l = String.valueOf(c.strength).length();
					g2d.setColor(Color.WHITE);
					g2d.drawOval(b.position.x + sSize1/4, b.position.y + sSize1/4, sSize1/2, sSize1/2);
					g2d.drawString(String.valueOf(c.strength), b.position.x + sSize1/2 - 4*l, b.position.y + sSize1/2 + 5);
				}
			}
		}
		
		
		
		g2d.setColor(Color.WHITE);
		this.drawGrid(grid1, g2d);
		
		//Draw capitols
		for (player p : playerList) {
			gridSquare gs = p.pCapitol.gs;
			g2d.setColor(Color.WHITE);
			g2d.drawRect(gs.position.x + 2, gs.position.y + 2, sSize1 - 4, sSize1 - 4);
			g2d.setColor(p.pColor);
			g2d.drawRect(gs.position.x + 1, gs.position.y + 1, sSize1 - 2, sSize1 - 2);
		}
		
		//Draw cities
		for (city c : cityList) {
			g2d.setColor(Color.BLACK);
			g2d.fillRect(c.gs.position.x + sSize1 - 12, c.gs.position.y + sSize1 - 12, 12, 12);
			g2d.setColor(Color.WHITE);
			g2d.drawRect(c.gs.position.x + sSize1 - 12, c.gs.position.y + sSize1 - 12, 12, 12);
		}
		
	}
	public void drawGrid(grid g2, Graphics2D g2d1) {
		for (int i = 0; i<g2.getGridArray().size(); i++) {
			for (int j = 0; j<g2.getRowArray(i).size(); j++) {
				int gsx = g2.getRowArray(i).get(j).getPosition().x;
				int gsy = g2.getRowArray(i).get(j).getPosition().y;
				g2d1.drawRect(gsx, gsy, sSize1, sSize1);
				if ((g2.getRowArray(i).get(j).value)!=0) {
					g2d1.drawString(String.valueOf(g2.getRowArray(i).get(j).value), gsx+2, gsy+12);
				}
				
			}
		
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		for (int i = 0; i<grid1.getGridArray().size(); i++) {
			for (int j = 0; j<grid1.getRowArray(i).size(); j++) {
				gridSquare gridSquareChecked = grid1.getRowArray(i).get(j);
				int gsx = gridSquareChecked.getPosition().x;
				int gsy = gridSquareChecked.getPosition().y;
				
				if (e.getX() > gsx && e.getX() < gsx + sSize1) {
					if (e.getY() > gsy && e.getY() < gsy + sSize1) {
							
						for (int k = 0; k < playerList.get(0).pUnitList.size(); k++) {
							unit u = playerList.get(0).pUnitList.get(k);
							if (gridSquareChecked == playerList.get(0).pUnitList.get(k).g1) {
								
								if (u.selectedGS != null) {
									if (u.selectedGS.equals(gridSquareChecked)) {
										u.selectedGS = null;
										u.targetGS = null;
									}
								}else u.selectedGS = gridSquareChecked;
							
							}else if (u.selectedGS != null) {
								if (legalTarget(gridSquareChecked, u)) {
									u.targetGS = gridSquareChecked;
								}
							}
						}	
							
					}
				}
				
			}
		}
		repaint();
	}
	//Note to self: legalTarget DOES NOT check for other units, as that part of the game has not been worked out yet
	private boolean legalTarget(gridSquare a, unit u) {
		if (a.xNum1 == u.selectedGS.xNum1) {
			if (a.yNum1 == u.selectedGS.yNum1-1 || a.yNum1 == u.selectedGS.yNum1+1) {
				if (!(a.feature instanceof lake) && !(a.feature instanceof mountain)) {
					return true;
				}
			}
		}else if (a.yNum1 == u.selectedGS.yNum1) {
			if (a.xNum1 == u.selectedGS.xNum1-1 || a.xNum1 == u.selectedGS.xNum1+1) {
				if (!(a.feature instanceof lake) && !(a.feature instanceof mountain)) {
					return true;
				}
			}
		}
		return false;
	}
	public interface Set extends Collection<Object>{
		
	}
	@Override
	public void mousePressed(MouseEvent e) {		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
	//THIS IS WHERE YOU GO TO THE NEXT TURN/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == '\n') {
			turnNum++;
			System.out.println("Turn: " + turnNum);
			
			//This for loop moves each one of your units that has a selected gridSquare
			//and a target gridSquare from its selected gridSquare to its target gridSquare
			player p1 = playerList.get(0);
			for (unit u : p1.pUnitList) {
				if (u.selectedGS != null && u.targetGS != null) {
					moveUnit(p1, u);
				}
				
				u.selectedGS = null;
				u.targetGS = null;
				u.strength += p1.pSquareList.size() / 5 + 1;
			}
			p1.citiesOwned = 0;
			for (gridSquare g : p1.pSquareList) {
				if (g.city != null || g.capitol != null) {
					p1.citiesOwned++;
				}
			}
			if (p1.pUnitList.size() < p1.citiesOwned) {
				if (p1.pCapitol.gs.feature == null && p1.pCapitol.gs.owner == p1) {
					unit u1 = new unit(p1.pCapitol.gs, unitStrength, p1);
					p1.pUnitList.add(u1);
					p1.pCapitol.gs.feature = u1;
				}
			}
			
			
			int excessUnits = p1.pUnitList.size() - p1.citiesOwned;
			if (excessUnits > 0) {
				for (int i = 0; i < excessUnits; i++) {
					unit smallestUnit = p1.pUnitList.get(0);
					for (unit u : p1.pUnitList) {
						if (u.strength < smallestUnit.strength) {
							smallestUnit = u;
						}
					}
					//delete smallestUnit (will repeat a number of times equal to the excessUnits)
					smallestUnit.g1.feature = null;
					smallestUnit.owner.pUnitList.remove(smallestUnit);
					smallestUnit = null;					
				}
			}
			label.setText("Squares captured: " + p1.pSquareList.size() + "/60");
			if (p1.pSquareList.size() >= 60) {
				label.setText("Squares captured: " + p1.pSquareList.size() + "/60 '\n' You Win!");
			}
			
			//ai turns:
			for (player p : aiList) {
				for (unit u : p.pUnitList) {
					//generate u.selectedGS and u.targetGS
					makeDecision(p, u);
					if (u.selectedGS != null && u.targetGS != null) {
						moveUnit(p, u);
					}
					
					u.selectedGS = null;
					u.targetGS = null;
					u.strength += p.pSquareList.size() / 5 + 1;
				}
				
				p.citiesOwned = 0;
				for (gridSquare g : p.pSquareList) {
					if (g.city != null || g.capitol != null) {
						p.citiesOwned++;
					}
				}
				if (p.pUnitList.size() < p.citiesOwned) {
					if (p.pCapitol.gs.feature == null && p.pCapitol.gs.owner == p) {
						unit u = new unit(p.pCapitol.gs, unitStrength, p);
						p.pCapitol.gs.feature = u;
						p.pUnitList.add(u);
						
					}
				}
				int aiExcessUnits = p.pUnitList.size() - p.citiesOwned;
				if (aiExcessUnits > 0) {
					for (int i = 0; i < aiExcessUnits; i++) {
						unit smallestUnit = p.pUnitList.get(0);
						for (unit u : p.pUnitList) {
							if (u.strength < smallestUnit.strength) {
								smallestUnit = u;
							}
						}
						//delete smallestUnit (will repeat a number of times equal to the excessUnits)
						smallestUnit.g1.feature = null;
						smallestUnit.owner.pUnitList.remove(smallestUnit);
						smallestUnit = null;					
					}
				}
			}
			
			
			repaint();
		}		
	}
	public void moveUnit(player p, unit u) {
		
		//if the unit's targetGS gridSquare's feature is NOT a unit, run.
		if (!(u.targetGS.feature instanceof unit)) {	
			//if the unit's targetGS gridSquare's owner is player 1, run.
			if (u.targetGS.owner != null && u.targetGS.owner.equals(p)) {
				u.selectedGS.feature = null;
				u.g1 = u.targetGS;
				u.targetGS.feature = u;
			//otherwiase, run.
			}else if (u.strength >= u.targetGS.value)	{
				u.selectedGS.feature = null;
				u.g1 = u.targetGS;
				u.targetGS.feature = u;
				u.strength -= u.targetGS.value;
				
				
				if(u.targetGS.owner != null) {
					u.targetGS.owner.pSquareList.remove(u.targetGS);
				}
				p.pSquareList.add(u.targetGS);
				u.targetGS.owner = p;
				
			}
		//if it is a unit, run.
		}else if (u.targetGS.owner != p) {
			unit u1 = (unit) u.targetGS.feature;
			if (u.strength >= u.targetGS.value + u1.strength)	{
				
				u.targetGS.feature = null;
				u.strength -= (u.targetGS.value + u1.strength);
				
				u.targetGS.owner.pSquareList.remove(u.targetGS);
				u.targetGS.owner = p;
				
				u1.owner.pUnitList.remove(u1);
				u1 = null;
				u.selectedGS.feature = null;
				u.g1 = u.targetGS;
				u.targetGS.feature = u;
				
		
				p.pSquareList.add(u.targetGS);
			}
		}
	}
	
	public void makeDecision(player p, unit u) {
		//generate list of legal targets
		ArrayList<gridSquare> targetable = new ArrayList<>();
		ArrayList<gridSquare> neighbors = new ArrayList<>();
				
		if (u.g1.yNum1 != 0) {
			neighbors.add(grid1.getRowArray(u.g1.yNum1 - 1).get(u.g1.xNum1));
		}
		if (u.g1.xNum1 != grid1.getRowArray(0).size() - 1) {
			neighbors.add(grid1.getRowArray(u.g1.yNum1).get(u.g1.xNum1 + 1));
		}
		if (u.g1.yNum1 != grid1.getGridArray().size() - 1) {
			neighbors.add(grid1.getRowArray(u.g1.yNum1 + 1).get(u.g1.xNum1));
		}
		if (u.g1.xNum1 != 0) {
			neighbors.add(grid1.getRowArray(u.g1.yNum1).get(u.g1.xNum1 - 1));
		}
		
		for (gridSquare g : neighbors) {
			if (!(g.feature instanceof lake) && !(g.feature instanceof mountain) && g.owner != p) {
				targetable.add(g);
			}
		}
		
		//find any smaller neigboring enemy units
		for (gridSquare g : targetable) {
			if (g.feature instanceof unit) {
				unit u1 = (unit) g.feature;
				if (u1.owner != p && u1.strength + u1.g1.value <= u.strength) {
					u.targetGS = g;
				}
			}
		}
		
		
		//find any neighboring cities
		if (u.targetGS == null) {
			for (gridSquare g : targetable) {
				if (g.city != null && g.owner != p) {
					u.targetGS = g;
				}else if (g.capitol != null && g.owner != p) {
					u.targetGS = g;
				}
			}
		}
		
		//find the lowest neighboring gridSquare
		gridSquare l = null;
		if (u.targetGS == null) {
			for (gridSquare g : targetable) {
				if (l == null) {
					l = g;
				}else if (g.value < l.value) {
					l = g;
				}
			}
		}
		if (l != null) {
			if (u.strength >= l.value) {
				u.targetGS = l;
			}
		}
		
		if (targetable.size() == 0) {
			int c = 0;
			while (u.targetGS == null) {
				gridSquare g = neighbors.get(rng.nextInt(neighbors.size()));
				if (!(g.feature instanceof lake) && !(g.feature instanceof mountain) && !(g.feature instanceof unit)) {
					u.targetGS = g;
				}
				c++;
				if (c > 20) {
					break;
				}
			}
			
		}
		
		
		u.selectedGS = u.g1;
	}
	@Override
	public void keyReleased(KeyEvent e) {
	}
	
}
class gridSquare {
	
	capitol capitol;
	city city;
	Point position;
	int xNum1;
	int yNum1;
	int size1;
	player owner;
	Object feature;
	int value;
	
	gridSquare(int xNum, int yNum, int size, int value1) {
		this.xNum1 = xNum;
		this.yNum1 = yNum;
		this.size1 = size;
		this.value = value1;
		this.position = new Point(this.xNum1*this.size1, this.yNum1*this.size1);
		
	}
	
	Point getPosition() {
		return this.position;
	}
	
	Object getFeature() {
		return this.feature;
	}
	
	void setPosition(int c, int d) {
		this.position = new Point(c,d);
	}
	
	void setFeature(Object o) {
		this.feature = o;
	}
	
}
class grid {
	ArrayList<ArrayList<gridSquare>> g3 = new ArrayList<>();
	
	grid(int gridRows, int gridCols, int gridSquareSize) {
		//add empty arraylists to "g3" and then add objects to each arraylist
	Random rng = new Random();
	for (int i = 0; i<gridRows; i++) {
		ArrayList<gridSquare> a = new ArrayList<>();
		for (int j = 0; j<gridCols; j++) {
			gridSquare b = new gridSquare(j, i, gridSquareSize, rng.nextInt(20) + 1);
			a.add(b);
		}
		g3.add(a);
	
	}
	//System.out.println(g3);
	
	
	}
	
	
	ArrayList<ArrayList<gridSquare>> getGridArray(){
		return g3;
	}
	
	ArrayList<gridSquare> getRowArray(int x){
		return g3.get(x);
	}
	
}
class feature {
	gridSquare g1;
	
	feature(gridSquare g) {
		this.g1 = g;
	}
	
}
class unit extends feature {
	
	
	int strength;
	player owner;
	int xNum1;
	int yNum1;
	gridSquare selectedGS;
	gridSquare targetGS;
	
	
	unit(gridSquare g, int strength1, player p) {
		super(g);
		this.strength = strength1;
		this.owner = p;
		
		
	}
}
class lake extends feature {
	
	
	lake(gridSquare g) {
		super(g);
	}
}
class mountain extends feature {
	
	
	mountain(gridSquare g) {
		super(g);
	}
}
class player {
	Color pColor;
	ArrayList<unit> pUnitList;
	ArrayList<gridSquare> pSquareList;
	capitol pCapitol;
	int citiesOwned;
	
	player(Color pColor1) {
		this.pColor = pColor1;
		this.pUnitList = new ArrayList<unit>();
		this.pSquareList = new ArrayList<gridSquare>();
	}
	
}
class capitol extends city{
	
	
	
	capitol(gridSquare gs) {
		super(gs);
	}
	
}
class city {
	gridSquare gs;
	player owner;
	
	city(gridSquare gs1){
		this.gs = gs1;
	}
	
}
class presets {
	
}












































































