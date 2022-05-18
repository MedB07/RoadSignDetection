package DBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Keypoints {
	int idKeyPoint;
    String ref;
	double x;
	double y;
	double size;
	double angle;
	double response;
	double octave;

	public Keypoints(String refpann, double x, double y,  double size, double angle, double response, double octave) {
		this.ref=refpann;
		this.idKeyPoint = -1;
		this.x = x;
		this.y=y;
		this.size=size;
		this.angle=angle;
		this.response=response;
		this.octave=octave;
	}
	
	public static List<KeyPoint> Keypointsgenerate(String objectfile) {

		// Conversion du signe de reference en niveaux de gris et normalisation
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				Mat sroadSign= Highgui.imread(objectfile);
				Mat sObject=new Mat();
				//Conversion du panneau extrait de l'image en gris et normalisation et redimensionnement à la taille du panneau de réference
				
				Mat graySign = new Mat(sroadSign.rows(),  sroadSign.cols(), sroadSign.type());
				Imgproc.cvtColor(sroadSign,graySign,Imgproc.COLOR_RGB2GRAY);
				Core.normalize(graySign, graySign, 0, 255, Core.NORM_MINMAX);
				//Extraction des descripteurs et keypoints
				FeatureDetector orbDetector= FeatureDetector.create(FeatureDetector.ORB);
				DescriptorExtractor orbExtractor=DescriptorExtractor.create(DescriptorExtractor.ORB);
				MatOfKeyPoint signKeypoints=new MatOfKeyPoint();
				orbDetector.detect(graySign, signKeypoints);
				List<KeyPoint> myKeys;
				myKeys =signKeypoints.toList();
				return myKeys;
					
	}
	public static List<Keypoints>  createkeypoints(String ref) {
		List<KeyPoint>  kypectracted;
		List<Keypoints>  kypgenereted = new ArrayList<Keypoints>();
		kypectracted=Keypointsgenerate(ref);
		Keypoints kyp;
		KeyPoint key;
		    for (Iterator<KeyPoint> iterator = kypectracted.iterator(); iterator.hasNext(); ) {
		     key = iterator.next();
		     kyp=new Keypoints(ref,key.pt.x,key.pt.y,key.size,key.angle,key.response,key.octave);
		     kypgenereted.add(kyp);
		    }
		return  kypgenereted;
		
	}
	public void Savekeypoints(Connection con) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		 float x,y,size,angle,response,octave;
		 String SQLPrep = "INSERT INTO Keypoints (panneauref,x,y,size,angle,response,octave) VALUES (?,?,?,?,?,?,?);";
		 PreparedStatement ps=con.prepareStatement(SQLPrep);
		 ps.setString(1, this.ref);
		 ps.setDouble(2, this.x);
		 ps.setDouble(3,  this.y);
		 ps.setDouble(4,  this.size);
		 ps.setDouble(5, this.angle);
		 ps.setDouble(6, this.response);
		 ps.setDouble(7, this.octave);
		 int status=ps.executeUpdate();
			if (status!=0) {
				System.out.println("db updated and keypoints inserted ");
			}
			System.out.println("db updated and keypoints inserted ");
		
}
	
	
	

}
