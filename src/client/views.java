//1245

package client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import model.Line;
import model.ImageObject;
import model.Oval;
import model.PaintObject;
import model.Rectangle;

/**
 * A JPanel GUI for Netpaint that has all paint objects drawn on it.
 * Currently, a list of paint objects is hardcoded.  A JPanel exists
 * in this JFrame that will draw this list of paint objects.
 * 
 * @author Rick Mercer
 */

public class views extends JFrame {

	private int xInitPosition;
	private int yInitPosition;
	private int xEndPosition;
	private int yEndPosition;
	private int count;
	private boolean dragging;
	private DrawingPanel drawingPanel;
	private static Vector<PaintObject> allPaintObjects;
	private JRadioButton lineButton;
	private JRadioButton rectangleButton;
	private JRadioButton ovalButton;
	private JRadioButton imageButton;
	private ButtonGroup group = new ButtonGroup();
	private Color color;
	private JScrollPane scroll;
	private String shape;

	public static void main(String[] args) {
		views client = new views();

		allPaintObjects = new Vector<>();

		client.setVisible(true);
	}

	public views() {
		
		dragging = false;
		count = 0;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);

		//fill any screen with your app
		setLocation(20, 20);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);

		//do this to make the drawing area bigger with jscrollpane
		//this.setPreferredSize(new Dimension(2048, 1024));

		drawingPanel = new DrawingPanel();
		drawingPanel.setPreferredSize(new Dimension(2*screenSize.width, 2*screenSize.height-150));

		scroll = new JScrollPane(drawingPanel);
		scroll.setPreferredSize(new Dimension(screenSize.width, screenSize.height-150));
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		color = Color.RED; //default selection
		JButton colorChooser = new JButton();
		colorChooser.setText("Color Chooser");
		colorChooser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				JColorChooser jcc = new JColorChooser();
				Color selectedColor = jcc.showDialog(null, "Please select a color", Color.RED);
				color = selectedColor;
			}
		});

		//radio buttons for shape choice
		lineButton = new JRadioButton();
		lineButton.setSelected(true); //default selection
		lineButton.setText("Line");
		rectangleButton = new JRadioButton();
		rectangleButton.setText("Rectangle");
		ovalButton = new JRadioButton();
		ovalButton.setText("Oval");
		imageButton = new JRadioButton();
		imageButton.setText("Image");

		group.add(lineButton);
		group.add(rectangleButton);
		group.add(ovalButton);
		group.add(imageButton);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(lineButton);
		buttonPanel.add(rectangleButton);
		buttonPanel.add(ovalButton);
		buttonPanel.add(imageButton);

		this.setLayout(new FlowLayout());

		add(scroll, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.CENTER);
		add(colorChooser, BorderLayout.CENTER);

		setVisible(true);
	}

	/**
	 * This is where all the drawing goes.
	 * @author mercer
	 */
	class DrawingPanel extends JPanel {

		

		public void paintComponent(Graphics g) {

			super.paintComponent(g);
			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			MouseListener m = new MouseActionListener();
			MouseMotionListener mml = new MouseActionListener();
			this.addMouseListener(m);
			this.addMouseMotionListener(mml);
			
			this.drawShapes(g);	
		}

		private void drawShapes(Graphics g) {
			for (PaintObject ob : allPaintObjects){
				ob.draw(g);
			}
			
		}
	}

	public Point getInitPoint(){
		return new Point(this.xInitPosition, this.yInitPosition);
	}

	public Point getEndPoint(){
		return new Point(this.xEndPosition, this.yEndPosition);
	}

	public Color getColor(){
		return color;
	}

	public String getPaintObject(){
		return shape;
	}

	private class MouseActionListener implements MouseListener, MouseMotionListener{

		
		@Override
		public void mouseClicked(MouseEvent e) {
			dragging = !dragging;
			System.out.println("dragging " + dragging);
			PaintObject draw = null;
			
			if(dragging){
				System.out.println("dragging is true ? -> 1st click " + dragging);
				xInitPosition = e.getX();
				yInitPosition = e.getY();
				
				if(lineButton.isSelected()){
					draw = new Line(color, new Point(xInitPosition, yInitPosition), e.getPoint());
					shape = "Line";
				}
				else if(rectangleButton.isSelected()){
					draw = new Rectangle(color, new Point(xInitPosition, yInitPosition), e.getPoint());
					shape = "Rectangle";
				}
				else if(ovalButton.isSelected()){
					draw = new Oval(color, new Point(xInitPosition, yInitPosition), e.getPoint());
					shape = "Oval";
				}
				else if(imageButton.isSelected()){
					System.out.println("diff in image " + (e.getPoint().getX() - xInitPosition));
					draw = new ImageObject(color, new Point(xInitPosition, yInitPosition), e.getPoint());
					shape = "Image";
				}

				allPaintObjects.add(draw); 
				repaint();
				repaint();
			}
			
			else{
				System.out.println("dragging is false -> 2nd click ? " + dragging);
				allPaintObjects.remove(allPaintObjects.size()-1);
				if(lineButton.isSelected()){
					draw = new Line(color, new Point(xInitPosition, yInitPosition), e.getPoint());
					shape = "Line";
				}
				else if(rectangleButton.isSelected()){
					draw = new Rectangle(color, new Point(xInitPosition, yInitPosition), e.getPoint());
					shape = "Rectangle";
				}
				else if(ovalButton.isSelected()){
					draw = new Oval(color, new Point(xInitPosition, yInitPosition), e.getPoint());
					shape = "Oval";
				}
				else if(imageButton.isSelected()){
					draw = new ImageObject(color, new Point(xInitPosition, yInitPosition), e.getPoint());
					shape = "Image";
				}
				
				allPaintObjects.add(draw); 
				repaint();
				repaint();
			}
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if(dragging){
				PaintObject temp = null;
				//allPaintObjects.remove(temp);
				if(lineButton.isSelected()){
					temp = new Line(color, new Point(xInitPosition, yInitPosition), e.getPoint());
				}
				else if(rectangleButton.isSelected()){
					temp = new Rectangle(color, new Point(xInitPosition, yInitPosition), e.getPoint());
				}
				else if(ovalButton.isSelected()){
					temp = new Oval(color, new Point(xInitPosition, yInitPosition), e.getPoint());
				}
				else if(imageButton.isSelected()){
					temp = new ImageObject(color, new Point(xInitPosition, yInitPosition), e.getPoint());
				}

				allPaintObjects.remove(allPaintObjects.size()-1);	
				allPaintObjects.add(temp);;
				
				repaint(); //ghost repaint

			}
			
		}
		

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}

}
