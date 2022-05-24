import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

public class JuliaProgram extends JPanel implements AdjustmentListener,ActionListener,MouseListener
{
	boolean doLess;
	float maxIterations = 25f;
	int imageW = 450;
	int imageH = 450;
	Color defaultColor = new Color(200, 0, 0);
	JFrame frame;
	ButtonGroup shapeButtons;
	JScrollBar hueBar, saturationBar, brightnessBar, aBar, bBar, zoomBar, iterationsBar;
	JPanel scrollPanel, buttonPanel, southPanel, textPanel;
	JLabel hueLabel, brightnessLabel, saturationLabel, aLabel, bLabel, zoomLabel, iterationsLabel;
	JButton reset;
	JRadioButton circle, diamond, peanut;
	BufferedImage output;
	int a, b;
	String shape = "Circle";
	double zoom=0.0;
	float hue, brightness, saturation;

	public JuliaProgram()
	{
		frame = new JFrame("Julia Set");
		frame.setSize(1200, 800);
							//orentation, init value, size of bar, begin, end
		hueBar = new JScrollBar(JScrollBar.HORIZONTAL,360,0,0,360);
		hueBar.addAdjustmentListener(this);
		hueBar.addMouseListener(this);
		saturationBar = new JScrollBar(JScrollBar.HORIZONTAL,1000,0,0,1000);
		saturationBar.addAdjustmentListener(this);
		saturationBar.addMouseListener(this);
		brightnessBar = new JScrollBar(JScrollBar.HORIZONTAL,1000,0,0,1000);
		brightnessBar.addAdjustmentListener(this);
		brightnessBar.addMouseListener(this);
		aBar = new JScrollBar(JScrollBar.HORIZONTAL,0,0,-2000,2000);
		aBar.addAdjustmentListener(this);
		aBar.addMouseListener(this);
		bBar = new JScrollBar(JScrollBar.HORIZONTAL,0,0,-2000,2000);
		bBar.addAdjustmentListener(this);
		bBar.addMouseListener(this);
		zoomBar = new JScrollBar(JScrollBar.HORIZONTAL,0,0,-99,100);
		zoomBar.addAdjustmentListener(this);
		zoomBar.addMouseListener(this);
		iterationsBar = new JScrollBar(JScrollBar.HORIZONTAL,25,0,1,300);
		iterationsBar.addAdjustmentListener(this);
		iterationsBar.addMouseListener(this);

		hue = hueBar.getValue();
		saturation = saturationBar.getValue();
		brightness = brightnessBar.getValue();

		scrollPanel = new JPanel();
		scrollPanel.setLayout(new GridLayout(7,1));
		scrollPanel.add(aBar);
		scrollPanel.add(bBar);
		scrollPanel.add(brightnessBar);
		scrollPanel.add(saturationBar);
		scrollPanel.add(hueBar);
		scrollPanel.add(zoomBar);
		scrollPanel.add(iterationsBar);

		zoomLabel = new JLabel("Zoom: "+(zoom+100)+"%");
		aLabel = new JLabel("A: "+a);
		bLabel = new JLabel("B: "+b);
		hueLabel = new JLabel("Hue: "+hue/360);
		saturationLabel = new JLabel("Saturation: "+saturation/1000.0);
		brightnessLabel = new JLabel("Brightness: "+brightness/1000.0);
		iterationsLabel = new JLabel("Iterations: "+maxIterations);

		textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(7,1));
		textPanel.add(aLabel);
		textPanel.add(bLabel);
		textPanel.add(brightnessLabel);
		textPanel.add(saturationLabel);
		textPanel.add(hueLabel);
		textPanel.add(zoomLabel);
		textPanel.add(iterationsLabel);

		reset = new JButton("Reset");
		reset.addActionListener(this);
		shapeButtons = new ButtonGroup();
		circle = new JRadioButton("Circle", true);
		circle.addActionListener(this);
		peanut = new JRadioButton("Peanut");
		peanut.addActionListener(this);
		diamond = new JRadioButton("Diamond");
		diamond.addActionListener(this);
		shapeButtons.add(circle);
		shapeButtons.add(peanut);
		shapeButtons.add(diamond);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(new JLabel("Shape: "));
		buttonPanel.add(circle);
		buttonPanel.add(peanut);
		buttonPanel.add(diamond);
		buttonPanel.add(reset);

		southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());
		southPanel.add(scrollPanel, BorderLayout.CENTER);
		southPanel.add(buttonPanel, BorderLayout.EAST);
		southPanel.add(textPanel, BorderLayout.WEST);


		frame.setLayout(new BorderLayout());

		frame.add(this, BorderLayout.CENTER);
		frame.add(southPanel, BorderLayout.SOUTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(shape.equalsIgnoreCase("Circle"))
		{
			g.drawImage(drawJuliaCircle(),0,0,null);
		}
		if(shape.equalsIgnoreCase("Diamond"))
		{
			g.drawImage(drawJuliaDiamond(),0,0,null);
		}
		if(shape.equalsIgnoreCase("Peanut"))
		{
			g.drawImage(drawJuliaPeanut(),0,0,null);
		}

	}

	public BufferedImage drawJuliaPeanut()
	{
	 	imageW = frame.getWidth();
	 	imageH = frame.getHeight();
		BufferedImage image = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
		if(!doLess)
		{
			for(int x = 0; x < imageW; x++)
			{
				for(int y = 0; y < imageH; y++)
				{
					double zx = 1.5*((x-imageW*0.5)/(0.5*(zoom+100)/100*imageW));
					double zy = (y-0.5*imageH)/(0.5*(zoom+100)/100*imageH);
					if(zoom == 0)
					{
						zx = 1.5*((x-imageW*0.5)/(0.5*1*imageW));
						zy = (y-0.5*imageH)/(0.5*1*imageH);
					}
					float iterations = maxIterations;
					while((zy*zx) < 60 && iterations > 0)
					{
						double temp = zy*zx + (a/1000.0);
						zy = 2*zy/zx+(b/1000.0);
						zx = temp;
						iterations--;

					}

						int col;

						if(iterations>0)
							col = Color.HSBtoRGB(hue/360.0f*(iterations/maxIterations)%1, saturation/1000.0f, brightness/1000);
						else
							col = Color.HSBtoRGB(0, saturation/1000.0f, 0);
						image.setRGB(x,y,col);
				}
			}
		}
		else
		{
			for(int x = 0; x < imageW; x+=3)
			{
				for(int y = 0; y < imageH; y+=3)
				{
					double zx = 1.5*((x-imageW*0.5)/(0.5*(zoom+100)/100*imageW));
					double zy = (y-0.5*imageH)/(0.5*(zoom+100)/100*imageH);
					if(zoom == 0)
					{
						zx = 1.5*((x-imageW*0.5)/(0.5*1*imageW));
						zy = (y-0.5*imageH)/(0.5*1*imageH);
					}
					float iterations = maxIterations;
					while((zy*zx) < 60 && iterations > 0)
					{
						double temp = zy*zx + (a/1000.0);
						zy = 2*zy/zx+(b/1000.0);
						zx = temp;
						iterations--;

					}

						int col;

						if(iterations>0)
							col = Color.HSBtoRGB(hue/360.0f*(iterations/maxIterations)%1, saturation/1000.0f, brightness/1000);
						else
							col = Color.HSBtoRGB(0, saturation/1000.0f, 0);
						image.setRGB(x,y,col);
				}
			}
		}
		return image;
	}

	public BufferedImage drawJuliaDiamond()
	{
	 	imageW = frame.getWidth();
	 	imageH = frame.getHeight();
		BufferedImage image = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
		if(!doLess)
		{
			for(int x = 0; x < imageW; x++)
			{
				for(int y = 0; y < imageH; y++)
				{
					double zx = 1.5*((x-imageW*0.5)/(0.5*(zoom+100)/100*imageW));
					double zy = (y-0.5*imageH)/(0.5*(zoom+100)/100*imageH);
					if(zoom == 0)
					{
						zx = 1.5*((x-imageW*0.5)/(0.5*1*imageW));
						zy = (y-0.5*imageH)/(0.5*1*imageH);
					}
					float iterations = maxIterations;
					while((zx*zy) < 6 && iterations > 0)
					{
						double temp = zx*zy + (a/1000.0);
						zy = 2*(zy*zx)+(b/1000.0);
						zx = temp;
						iterations--;

					}

						int col;

						if(iterations>0)
							col = Color.HSBtoRGB(hue/360.0f*(iterations/maxIterations)%1, saturation/1000.0f, brightness/1000);
						else
							col = Color.HSBtoRGB(0, saturation/1000.0f, 0);
						image.setRGB(x,y,col);
				}
			}
		}
		else
		{
			for(int x = 0; x < imageW; x+=3)
			{
				for(int y = 0; y < imageH; y+=3)
				{
					double zx = 1.5*((x-imageW*0.5)/(0.5*(zoom+100)/100*imageW));
					double zy = (y-0.5*imageH)/(0.5*(zoom+100)/100*imageH);
					if(zoom == 0)
					{
						zx = 1.5*((x-imageW*0.5)/(0.5*1*imageW));
						zy = (y-0.5*imageH)/(0.5*1*imageH);
					}
					float iterations = maxIterations;
					while((zx*zy) < 6 && iterations > 0)
					{
						double temp = zx*zy + (a/1000.0);
						zy = 2*(zy*zx)+(b/1000.0);
						zx = temp;
						iterations--;

					}

						int col;

						if(iterations>0)
							col = Color.HSBtoRGB(hue/360.0f*(iterations/maxIterations)%1, saturation/1000.0f, brightness/1000);
						else
							col = Color.HSBtoRGB(0, saturation/1000.0f, 0);
						image.setRGB(x,y,col);
				}
			}
		}
		return image;
	}

	public BufferedImage drawJuliaCircle()
	{
	 	imageW = frame.getWidth();
	 	imageH = frame.getHeight();
		BufferedImage image = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
		if(!doLess)
		{
			for(int x = 0; x < imageW; x++)
			{
				for(int y = 0; y < imageH; y++)
				{
					double zx = 1.5*((x-imageW*0.5)/(0.5*(zoom+100)/100*imageW));
					double zy = (y-0.5*imageH)/(0.5*(zoom+100)/100*imageH);
					if(zoom == 0)
					{
						zx = 1.5*((x-imageW*0.5)/(0.5*1*imageW));
						zy = (y-0.5*imageH)/(0.5*1*imageH);
					}
					float iterations = maxIterations;
					while((zx*zx+zy*zy) < 6 && iterations > 0)
					{
						double temp = zx*zx-zy*zy + (a/1000.0);
						zy = 2*zy*zx+(b/1000.0);
						zx = temp;
						iterations--;

					}

						int col;

						if(iterations>0)
							col = Color.HSBtoRGB(hue/360.0f*(iterations/maxIterations)%1, saturation/1000.0f, brightness/1000);
						else
							col = Color.HSBtoRGB(0, saturation/1000.0f, 0);
						image.setRGB(x,y,col);
				}
			}
		}
		else
		{
			for(int x = 0; x < imageW; x+=3)
			{
				for(int y = 0; y < imageH; y+=3)
				{
					double zx = 1.5*((x-imageW*0.5)/(0.5*(zoom+100)/100*imageW));
					double zy = (y-0.5*imageH)/(0.5*(zoom+100)/100*imageH);
					if(zoom == 0)
					{
						zx = 1.5*((x-imageW*0.5)/(0.5*1*imageW));
						zy = (y-0.5*imageH)/(0.5*1*imageH);
					}
					float iterations = maxIterations;
					while((zx*zx+zy*zy) < 6 && iterations > 0)
					{
						double temp = zx*zx-zy*zy + (a/1000.0);
						zy = 2*zy*zx+(b/1000.0);
						zx = temp;
						iterations--;

					}

						int col;

						if(iterations>0)
							col = Color.HSBtoRGB(hue/360.0f*(iterations/maxIterations)%1, saturation/1000.0f, brightness/1000);
						else
							col = Color.HSBtoRGB(1, saturation/1000.0f, 0);
						image.setRGB(x,y,col);
				}
			}
		}
		return image;
	}

	public void mouseExited(MouseEvent e)
	{

	}
	public void mouseEntered(MouseEvent e)
	{

	}
	public void mouseReleased(MouseEvent e)
	{
		doLess = false;
	}
	public void mousePressed(MouseEvent e)
	{
		doLess = true;
	}
	public void mouseClicked(MouseEvent e)
	{

	}


	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == reset)
		{
			aBar.setValue(0);
			bBar.setValue(0);
			hueBar.setValue(360);
			saturationBar.setValue(1000);
			brightnessBar.setValue(1000);
			zoomBar.setValue(0);
			iterationsBar.setValue(25);
			repaint();
			shape = "Circle";
		}
		if(e.getSource() == circle)
		{
			shape = "Circle";
		}
		if(e.getSource() == peanut)
		{
			shape = "Peanut";
		}
		if(e.getSource() == diamond)
		{
			shape = "Diamond";
		}
		repaint();
	}

	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		if(e.getSource() == aBar)
		{
			a = aBar.getValue();
			aLabel.setText("A: "+(a/2000.0));
		}
		if(e.getSource() == bBar)
		{
			b = bBar.getValue();
			bLabel.setText("B: "+(b/2000.0));
		}
		if(e.getSource() == hueBar)
		{
			hue = (float)hueBar.getValue();
			hueLabel.setText("Hue: "+hue);
		}

		if(e.getSource() == saturationBar)
		{
			saturation = (float)saturationBar.getValue();
			saturationLabel.setText("Saturation: " + saturation/1000.0);
		}

		if(e.getSource() == brightnessBar)
		{
			brightness = (float)brightnessBar.getValue();
			brightnessLabel.setText("Brightness: " + brightness/1000.0);
		}
		if(e.getSource() == zoomBar)
		{
			zoom = zoomBar.getValue();
			zoomLabel.setText("Zoom: "+(zoom+100)+"%");
		}
		if(e.getSource() == iterationsBar)
		{
			maxIterations = iterationsBar.getValue();
			iterationsLabel.setText("Iterations: "+maxIterations);
		}

		repaint();
	}

	public static void main(String[] args)
	{
		JuliaProgram app = new JuliaProgram();
	}
}