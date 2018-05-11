/**
 * @author Ayoub SANHAJI
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public class Demineur extends JFrame implements MouseListener,ActionListener
{
	//1
	private ArrayList<ImageIcon> images;
	
	//2
	private JMenuBar menuBar;
	private JMenu jeu;
	private JMenu aide;
	private JMenuItem nvjeu;
	private JMenuItem exit;
	  
	//4
	private int larg;
	private int haut;
	private int lignes;
	private int colonnes;
	private int nbreMines;
	private int succes;
	private NumberFormat formatter;
	
	private JPanel haute;
	private JPanel basse;
	
	private JButton cellules[][];
	private int minesCellule[][];
	private int visitee[][];
	
	private JTextField mine;
	private JLabel souriant;
	
	public Demineur()
	{
		//1
		images = new ArrayList<ImageIcon>();
		ImageIcon i1 = new ImageIcon("./images/1.gif");
		images.add(i1);
		ImageIcon i2 = new ImageIcon("./images/2.gif");
		images.add(i2);
		ImageIcon i3 = new ImageIcon("./images/3.gif");
		images.add(i3);
		ImageIcon i4 = new ImageIcon("./images/4.gif");
		images.add(i4);
		ImageIcon i5 = new ImageIcon("./images/5.gif");
		images.add(i5);
		ImageIcon i6 = new ImageIcon("./images/6.gif");
		images.add(i6);
		ImageIcon i7 = new ImageIcon("./images/7.gif");
		images.add(i7);
		ImageIcon i8 = new ImageIcon("./images/8.gif");
		images.add(i8);
		ImageIcon i9 = new ImageIcon("./images/drapeau.gif");
		images.add(i9);
		ImageIcon i10 = new ImageIcon("./images/erreur.gif");
		images.add(i10);
		ImageIcon i11 = new ImageIcon("./images/mine.gif");
		images.add(i11);
		ImageIcon i12 = new ImageIcon("./images/nouveau jeu.gif");
		images.add(i12);
		
		//2
		menuBar = new JMenuBar();
		jeu = new JMenu("Jeu");
		nvjeu = new JMenuItem("Nouvelle Partie");
		exit = new JMenuItem("Fermer");
		nvjeu.addActionListener(this);
		exit.addActionListener(this);
		jeu.add(nvjeu);
		jeu.add(exit);
		aide = new JMenu("Aide");
		menuBar.add(jeu);
		menuBar.add(aide);
		setJMenuBar(menuBar);
		
		//4
		larg = 320;
		haut = 416;
		lignes = 15;
		colonnes = 15;
		nbreMines = 70;
		succes=(lignes*colonnes)-nbreMines;
		
		haute = new JPanel();
		add(haute , BorderLayout.NORTH);
		haute.setLayout(new BorderLayout());
		haute.setBorder(BorderFactory.createLoweredBevelBorder());
		basse = new JPanel();
		add(basse , BorderLayout.CENTER);
		basse.setLayout(new GridLayout(lignes,colonnes));
		basse.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		
		cellules = new JButton [lignes][colonnes];
		minesCellule = new int [lignes][colonnes];
		visitee = new int [lignes][colonnes];
		
		//Panel de partie haute
		formatter = new DecimalFormat("000");
		mine = new JTextField("   " + formatter.format(nbreMines) , 4);
        mine.setEditable(false);
        mine.setFont(new Font("DigtalFont.TTF", Font.BOLD, 25));
        mine.setBackground(Color.BLACK);
        mine.setForeground(Color.RED);
        mine.setBorder(BorderFactory.createLoweredBevelBorder());
        haute.add(mine , BorderLayout.LINE_START);
        souriant = new JLabel(images.get(11));
		haute.add(souriant , BorderLayout.CENTER);
        
		//Panel de partie basse
		for(int i=0 ; i<lignes ; i++)
			for(int j=0 ; j<colonnes ; j++)
			{
				cellules[i][j] = new JButton("");
				cellules[i][j].addMouseListener(this);
				basse.add(cellules[i][j]);
			}
		
		//3
		setTitle("Démineur");
		setSize(larg,haut);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	public void setMines()
	{
		Random r = new Random();
		int cpt = 0;
		
		for(cpt=0 ; cpt<nbreMines ; cpt++)
		{
			int a = r.nextInt(lignes);
			int b = r.nextInt(colonnes);
			if(minesCellule[a][b] == -1)
				cpt--;
			else
				minesCellule[a][b] = -1;
		}
	}
	
	public int calcul(int x , int y)
	{
		if(x>-1 && x<lignes && y>-1 && y<colonnes)
		{
			if(minesCellule[x][y] == -1)
				return 1;
			return 0;
		}
		else
			return 0;
	}
	
	public int calculs(int i , int j)
	{
		int sum=0;
		for(int a=i-1 ; a<=i+1 ; a++)
			for(int b=j-1 ; b<=j+1 ; b++)
					sum += calcul(a,b);
		return sum;
	}
	
	public void blanc(int i , int j)
	{
			if(calculs(i,j) != 0)
			{
				cellules[i][j].setIcon(images.get(calculs(i,j)-1));
				cellules[i][j].removeMouseListener(this);
				visitee[i][j] = 1;
			}
			else	
			{
				cellules[i][j].setBackground(Color.GRAY);
				for(int a=i-1 ; a<=i+1 ; a++)
					for(int b=j-1 ; b<=j+1 ; b++)	
						if(a>-1 && a<lignes && b>-1 && b<colonnes)
							if(visitee[a][b] != 1)
							{
								visitee[a][b] = 1;
								blanc(a,b);
							}	
			}
	}

	public int nbreValTab(int [][]t , int val)
	{
		int n=0;
		for(int i=0 ; i<t.length ; i++)
			for(int j=0 ; j<t[0].length ; j++)
				if(t[i][j] == val)
					n = n+1;
		return n;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		for(int i=0 ; i<lignes ; i++)
			for(int j=0 ; j<colonnes ; j++)
			{
				if(e.getSource() == cellules[i][j])
				{
					if(SwingUtilities.isLeftMouseButton(e) && cellules[i][j].getIcon() != images.get(8))
					{
						//Clic sur mine
						if(minesCellule[i][j] == -1)
						{
							for(int a=0 ; a<lignes ; a++)
								for(int b=0 ; b<colonnes ; b++)
								{
									if(cellules[a][b].getIcon() != images.get(8))
									{
											if(minesCellule[a][b] == -1)
											{
												cellules[a][b].setIcon(images.get(10));
												souriant.setIcon(images.get(9));
											}
											else 
												if(visitee[a][b] == 0)
													cellules[a][b].setEnabled(false);	
									}
									cellules[a][b].removeMouseListener(this);
								}
						}
						
						else
						{
							if(calculs(i,j) != 0)
							{
								cellules[i][j].setIcon(images.get(calculs(i,j)-1));
								cellules[i][j].removeMouseListener(this);
								visitee[i][j] = 1;
							}
							
							else
							{
								blanc(i,j);
							}
						}
						
						if(nbreValTab(visitee,1) == succes)
						{
							JOptionPane.showMessageDialog(null, "Bravo! \nT'as gagne ^^");
							for(int a=0 ; a<lignes ; a++)
								for(int b=0 ; b<colonnes ; b++)
								{
									if(visitee[a][b] == 0)
										cellules[a][b].setEnabled(false);	
									cellules[a][b].removeMouseListener(this);
								}
						}
					}
					
					if(SwingUtilities.isRightMouseButton(e))
					{
						if(cellules[i][j].getIcon() == images.get(8)) //drapeau exist
						{
							cellules[i][j].setIcon(null);
							nbreMines++;
						}
						
						else
							if(calculs(i,j) > 0)
							{
								cellules[i][j].setIcon(images.get(8));
								nbreMines--;
							}
						
						mine.setText("    " + formatter.format(nbreMines));
					}
				}
			}
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == nvjeu)
		{
			this.dispose();
			Demineur t = new Demineur();
			t.setVisible(true);
			t.setMines();
		}
		
		if(e.getSource() == exit)
		{
			this.dispose();
		}
	}
	
	public static void main(String [] args)
	{
		Demineur t = new Demineur();
		t.setVisible(true);
		t.setMines();
	}
}