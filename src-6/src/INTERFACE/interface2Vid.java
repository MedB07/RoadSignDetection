package INTERFACE;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.border.Border;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import Basic.MaBibliothequeTraitementImage;
import Basic.MaBibliothequeTraitementImageEtendue;

import INTERFACE.MainMenu;


public class interface2Vid  {
	
	static {
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	System.load("C:\\Users\\nadra\\Downloads\\opencv\\build\\x64\\vc12\\bin\\opencv_ffmpeg2413_64.dll");
	}
	
		 
		  public static void main(String[] args) {
			  EventQueue.invokeLater(() -> {
			         final interface2Vid window = new interface2Vid();
			      });
		  }
			  

			
		  public interface2Vid() {
			  

			    // Create the label
			    JFrame frame = new JFrame("Détection panneaux");
			    JLabel label = new JLabel("Sélectionnez la vidéo que vous-voulez analyser",JLabel.CENTER);
			    label.setBounds(10,10,300,50);
			    
			    //JPanel panel = new JPanel();
			    
			    frame.setTitle("Analyse vidéos");
			    
			    //Creating buttons			    
			    JRadioButton radio1 = new JRadioButton("Vidéo 1");
			    JRadioButton radio2 = new JRadioButton("Vidéo 2");
			    JButton submit = new JButton("Submit");  
			   
			    //Setting bounds
			    radio1.setBounds(40,60,200,50);
			    radio2.setBounds(40,100,200,50);
			    submit.setBounds(130,160,100,30);
			    
			    //Add radio buttons to group
			    ButtonGroup group = new ButtonGroup();
			    group.add(radio1);
			    group.add(radio2);
			    
			    //Add buttons to panel
			    frame.add(label);
			    frame.add(radio1);
			    frame.add(radio2);
			    frame.add(submit);
			    
			    //frame.getContentPane().add(panel);

			    frame.setSize(365,275);
			    frame.setLayout(null);
			    frame.setVisible(true);
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			    
			    submit.addActionListener(event -> {
				        	
				            final Thread thread = new Thread(() -> {
						        	if(radio1.isSelected()){
						        		try {
							        		analyseVideo("video1.avi");
						                 } catch (final InterruptedException | InvocationTargetException ex) {
						                    ex.printStackTrace();
						                 }
						        		
						        	}
						        	else if(radio2.isSelected()){
						        		try {
							        		analyseVideo("video2.avi");
						                 } catch (final InterruptedException | InvocationTargetException ex) {
						                    ex.printStackTrace();
						                 }
						        	}
					        
				    		});
				            
				            thread.start();
	            
			 });
				        
		  
		  	}
	 
		  private void analyseVideo(String file) throws InterruptedException, InvocationTargetException {
			  
		            // Here we process the frame.
		            // Replace this with whatever you need to do
			    JFrame jframe = new JFrame("Detection de panneaux sur un flux vidéo");
				jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JLabel vidpanel = new JLabel();
				jframe.setContentPane(vidpanel);
				jframe.setSize(720, 480);
				jframe.setVisible(true);

				Mat frame = new Mat();
				VideoCapture camera = new VideoCapture(file);
				Mat PanneauAAnalyser = null;
				
					while (camera.read(frame)) {
						//ImageIcon image = new ImageIcon(Function.Mat2bufferedImage(frame));
						ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
						vidpanel.setIcon(image);
						Mat trans_HSV=MaBibliothequeTraitementImageEtendue.transformeBGRversHSV(frame);
						Mat saturee=MaBibliothequeTraitementImage.seuillage(trans_HSV, 6, 170, 110);
						Mat objetrond = null;
						
						List<MatOfPoint> ListeContours= MaBibliothequeTraitementImageEtendue.ExtractContours(saturee);
						
						
						double [] scores=new double [6];
						
						for (MatOfPoint contour: ListeContours  ){
							objetrond=MaBibliothequeTraitementImageEtendue.DetectForm(frame,contour);
							
							//objetrond=MaBibliothequeTraitementImage.DetectForm(m,contour);
							//objetrond=utils.DetecterCercles(transformee);

							if (objetrond!=null){
								MaBibliothequeTraitementImage.afficheImage("Objet rond detécté", objetrond);
								scores[0]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref30.jpg");
								scores[1]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref50.jpg");
								scores[2]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref70.jpg");
								scores[3]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref90.jpg");
								scores[4]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref110.jpg");
								scores[5]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"refdouble.jpg");
								System.out.println(scores[0]);
								System.out.println(scores[1]);
								System.out.println(scores[2]);
								System.out.println(scores[3]);
								System.out.println(scores[4]);
								System.out.println(scores[5]);
								//recherche de l'index du maximum et affichage du panneau detecté
								double scoremax=-1;
								int indexmax=0;
								for(int j=0;j<scores.length;j++){
									if (scores[j]>scoremax){
										scoremax=scores[j];
										indexmax=j;}
									}	
								if(scoremax<0){System.out.println("Aucun Panneau détécté");}
								else{switch(indexmax){
								case -1:;break;
								case 0:System.out.println("Panneau 30 détécté");break;
								case 1:System.out.println("Panneau 50 détécté");break;
								case 2:System.out.println("Panneau 70 détécté");break;
								case 3:System.out.println("Panneau 90 détécté");break;
								case 4:System.out.println("Panneau 110 détécté");break;
								case 5:System.out.println("Panneau interdiction de dépasser détécté");break;
								}}

							}
						}

		            

		            // Update the shown image using the EDT thread, otherwise an exception will be thrown
		            EventQueue.invokeAndWait(() -> {
		    			vidpanel.setIcon(image);
		    			vidpanel.repaint();
		            });

		            // Simulate 20 FPS, to be removed
		            Thread.sleep(50);
			         
			      
					}
		  }
		  
		  
		  public static BufferedImage Mat2bufferedImage(Mat image) {
				MatOfByte bytemat = new MatOfByte();
				Highgui.imencode(".jpg", image, bytemat);
				byte[] bytes = bytemat.toArray();
				InputStream in = new ByteArrayInputStream(bytes);
				BufferedImage img = null;
				try {
					img = ImageIO.read(in);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return img;
			}



			public static int identifiepanneau(Mat objetrond){
				double [] scores=new double [6];
				int indexmax=-1;
				if (objetrond!=null){
					scores[0]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref30.jpg");
					scores[1]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref50.jpg");
					scores[2]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref70.jpg");
					scores[3]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref90.jpg");
					scores[4]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"ref110.jpg");
					scores[5]=MaBibliothequeTraitementImageEtendue.Similitude(objetrond,"refdouble.jpg");

					double scoremax=scores[0];

					for(int j=1;j<scores.length;j++){
						if (scores[j]>scoremax){scoremax=scores[j];indexmax=j;}}	


				}
				return indexmax;
			}


		}

