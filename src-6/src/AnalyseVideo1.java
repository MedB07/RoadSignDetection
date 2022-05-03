import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
//import utilitaireAgreg.*;

public class AnalyseVideo1 {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.load("C:\\Program Files\\opencv\\build\\x64\\vc14\\bin\\opencv_ffmpeg2413_64.dll");
	}

	static Mat imag = null;

	public static void main(String[] args) {
		JFrame jframe = new JFrame("Detection de panneaux sur un flux vid�o");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel vidpanel = new JLabel();
		jframe.setContentPane(vidpanel);
		jframe.setSize(720, 480);
		jframe.setVisible(true);

		Mat frame = new Mat();
		VideoCapture camera = new VideoCapture("video1.wm");
		Mat PanneauAAnalyser = null;
		
			while (camera.read(frame)) {
			//A completer
				//ImageIcon image = new ImageIcon(Function.Mat2bufferedImage(frame));
				ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
				vidpanel.setIcon(image);
				Mat trans_HSV=MaBibliothequeTraitementImageEtendue.transformeBGRversHSV(frame);
				Mat saturee=MaBibliothequeTraitementImage.seuillage(trans_HSV, 6, 170, 110);
				Mat objetrond = null;
				
				List<MatOfPoint> ListeContours= MaBibliothequeTraitementImageEtendue.ExtractContours(saturee);
				
				int i=0;
				double [] scores=new double [6];
				
				for (MatOfPoint contour: ListeContours  ){
					objetrond=MaBibliothequeTraitementImageEtendue.DetectForm(frame,contour);
					i++;
					//objetrond=MaBibliothequeTraitementImage.DetectForm(m,contour);
					//objetrond=utils.DetecterCercles(transformee);

					if (objetrond!=null){
						MaBibliothequeTraitementImage.afficheImage("Objet rond det�ct�", objetrond);
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
						//recherche de l'index du maximum et affichage du panneau detect�
						double scoremax=-1;
						int indexmax=0;
						for(int j=0;j<scores.length;j++){
							if (scores[j]>scoremax){
								scoremax=scores[j];
								indexmax=j;}
							}	
						if(scoremax<0){System.out.println("Aucun Panneau d�t�ct�");}
						else{switch(indexmax){
						case -1:;break;
						case 0:System.out.println("Panneau 30 d�t�ct�");break;
						case 1:System.out.println("Panneau 50 d�t�ct�");break;
						case 2:System.out.println("Panneau 70 d�t�ct�");break;
						case 3:System.out.println("Panneau 90 d�t�ct�");break;
						case 4:System.out.println("Panneau 110 d�t�ct�");break;
						case 5:System.out.println("Panneau interdiction de d�passer d�t�ct�");break;
						}}

					}
				}

			//ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
			vidpanel.setIcon(image);
			vidpanel.repaint();
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