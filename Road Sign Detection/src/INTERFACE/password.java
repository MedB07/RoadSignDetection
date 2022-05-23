package INTERFACE;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DBase.ConnectDB;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

public class password extends JFrame {
	static String password;
	static String panref1;
	static String Username;
	static Connection con ;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					password frame = new password();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public password() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.darkGray);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setForeground(Color.ORANGE);
		lblNewLabel.setBackground(Color.ORANGE);
		lblNewLabel.setBounds(28, 50, 109, 20);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(152, 47, 146, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		JLabel lblNewLabel_1 = new JLabel("Mot de passe");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setForeground(Color.ORANGE);
		lblNewLabel_1.setBounds(28, 118, 120, 20);
		contentPane.add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		textField_1.setBounds(152, 115, 146, 26);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton = new JButton("se connecter");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton.setBackground(Color.ORANGE);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Username=textField.getText();
				password=textField_1.getText();
				
				try {
					Connection con = ConnectDB.getConnectionING(Username,password);
					panref1=ImagrefDB.main(MainMenu.path,con);
					System.out.println(panref1);
					 BufferedImage img = null;
		            img = ImageIO.read(new File(panref1));
		            Image dimg = img.getScaledInstance(MainMenu.label_2.getWidth(), MainMenu.label_2.getHeight(),
		              Image.SCALE_SMOOTH);
		            ImageIcon imageIcon = new ImageIcon(dimg);
		            MainMenu.label_2.setIcon(imageIcon);
		            String first=" résultat DB:->le panneau détectee est:\n";
		            MainMenu.textField.setText(first+panref1+"\n le nombre de keypoints est :"+ImagrefDB.nbr);
		            
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				setVisible(false);
			}
		});
		btnNewButton.setBounds(135, 181, 163, 29);
		contentPane.add(btnNewButton);
	}

}
