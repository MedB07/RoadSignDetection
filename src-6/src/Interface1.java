import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;


public class Interface1 extends JFrame {

		  public Interface1() {
			    this.setTitle("Test radio boutons");
			    
			    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    JPanel panel = new JPanel(new GridLayout(0,1));
			    Border border = BorderFactory.createTitledBorder("Choisi la video à charger");
			    panel.setBorder(border);
			    ButtonGroup group = new ButtonGroup();
			    JRadioButton radio1 = new JRadioButton("Vidéo 1", true);
			    JRadioButton radio2 = new JRadioButton("Vidéo 2");
			    group.add(radio1);
			    panel.add(radio1);
			    group.add(radio2);
			    panel.add(radio2);
			    Container contentPane = this.getContentPane();
			    contentPane.add(panel, BorderLayout.CENTER);
			    this.setSize(500, 500);
			    this.setVisible(true);
	   }
	
	
		  public static void main(String[] args) {
			  JFrame frame = new Interface1();

	}

}
