// for syntax reference:
// https://www.tutorialspoint.com/java/index.htm
package textPage;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import textPage.FileImporter;

public class MainWindow extends JFrame implements ActionListener {
	// build window contents
	JPanel cards;
	private static final long serialVersionUID = 1L;
	JTextField employNameInput = new JTextField();
	JTextField keyInput = new JTextField("Key(s) on the user's profile: ");
	JTextField recordInput = new JTextField("Request record number and date: ");
	JTextField requestChangeName = new JTextField("Enter Requesters Name");
	JTextField requestChangeDptOld = new JTextField("Previous Department");
	JTextField requestChangeDptNew = new JTextField("Current Department");
	JTextField requestChangeAuthFor = new JTextField("Department/Building Requester Authoized For");
	JButton process = new JButton();
	JLabel directions = new JLabel("Enter name into text box, select type of request and click button to generate email.");
	Font font = new Font("SERIF", Font.PLAIN, 13);
	String[] buttonString = { "Changed Employment", "Left Employment", "Failure to Pick Up After 30 Days", "Requester Changed Department" };
	String[] buttonCommand = { "0", "1", "2", "3" };
	JRadioButton[] radioB = new JRadioButton[buttonString.length];
	ButtonGroup radioGroup = new ButtonGroup();
	String header = "Hello, \n\nEmployee: ";
	// Setting buttons for the email signature
	String[] eSigName = { "Jeff", "Nicole" };
	JRadioButton[] eSig = new JRadioButton[eSigName.length];
	ButtonGroup eSigGroup = new ButtonGroup();

	public MainWindow() { // Create all panels and buttons
		super();
		setSize(800, 1000);
		setTitle("Company X Email Generator");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		UIManager.put("TitledBorder.border", new LineBorder(new Color(00, 00, 00), 2));
		TitledBorder title;
		JPanel keyCard = new JPanel();
		JPanel requestCard = new JPanel();
		JPanel midPanel = new JPanel();
		JPanel midPanel2 = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel sigPanel = new JPanel();
		JPanel insidePanel = new JPanel();
		JPanel requesterPanel = new JPanel();
		JPanel allPanel = new JPanel();

		sigPanel.setLayout(new GridLayout(1, eSigName.length));
		insidePanel.setLayout(new BorderLayout());
		keyCard.setLayout(new BorderLayout());
		requestCard.setLayout(new BorderLayout());
		buttonPanel.setLayout(new GridLayout(1, buttonString.length));
		midPanel.setLayout(new BorderLayout());
		midPanel2.setLayout(new BorderLayout());
		requesterPanel.setLayout(new GridLayout(1, 4));
		allPanel.setLayout(new BorderLayout());
		// Text instructions
		title = BorderFactory.createTitledBorder("Select Sig");
		title.setTitleColor(Color.BLACK);
		sigPanel.setBorder(title);

		title = BorderFactory.createTitledBorder("Select an Email Type");
		insidePanel.setBorder(title);

		title = BorderFactory.createTitledBorder("Enter Request Date and Record # in Text Field");
		midPanel.setBorder(title);

		title = BorderFactory.createTitledBorder("Keys Not Returned: Enter Key(s) in Text Field");
		midPanel2.setBorder(title);

		title = BorderFactory.createTitledBorder("Keys Not Picked Up: Enter Name in Text Field");
		keyCard.setBorder(title);

		title = BorderFactory.createTitledBorder("Enter Info for Requester who Changed Department");
		requesterPanel.setBorder(title);

		process.setText("Hit 'Enter' or Click Here to Generate Email");
		process.setFont(font);
		process.addActionListener(this);
		// Set panel and text box positions
		midPanel.add(recordInput);
		midPanel2.add(keyInput, BorderLayout.NORTH);
		midPanel2.add(insidePanel, BorderLayout.SOUTH);
		midPanel2.add(midPanel, BorderLayout.CENTER);
		keyCard.add(employNameInput, BorderLayout.NORTH);
		keyCard.add(midPanel2, BorderLayout.CENTER);
		keyCard.add(process, BorderLayout.SOUTH);
		requesterPanel.add(requestChangeName);
		requesterPanel.add(requestChangeDptOld);
		requesterPanel.add(requestChangeDptNew);
		requesterPanel.add(requestChangeAuthFor);
		allPanel.add(requesterPanel, BorderLayout.NORTH);
		allPanel.add(keyCard, BorderLayout.CENTER);

		// Set radio button layout for signature and email type.
		for (int i = 0; i < eSigName.length; i++) {
			eSig[i] = new JRadioButton(eSigName[i]);
			eSig[i].setActionCommand(buttonCommand[i]);
			eSigGroup.add(eSig[i]);
			sigPanel.add(eSig[i]);
		}
		insidePanel.add(sigPanel, BorderLayout.SOUTH);

		for (int i = 0; i < buttonString.length; i++) {
			radioB[i] = new JRadioButton(buttonString[i]);
			radioB[i].setActionCommand(buttonCommand[i]);
			radioGroup.add(radioB[i]);
			buttonPanel.add(radioB[i]);
		}
		radioB[0].setSelected(true);
		insidePanel.add(buttonPanel, BorderLayout.CENTER);

		add(allPanel, BorderLayout.CENTER);
		this.getRootPane().setDefaultButton(process); // Make the 'Enter' button on keyboard process the 'process' button.
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		StringBuilder s = new StringBuilder();
		String emailBody = radioGroup.getSelection().getActionCommand();
		String emailSig = null;
		String selection = "";
		String finalSig = "";
		String emailTitleKey = "mailto:?subject=Key%20Retrieval&body=";
		String emailTitleID = "mailto:?subject=Key%20Not%20Picked%20Up&body=";
		String emailTitleRequesterChange = "mailto:?subject=Requester%20Department%20Change&body=";
		ArrayList<String> fileArr = new ArrayList<>();
		StringBuilder convert;
		// Setting signature based on selection, if nothing give a warning
		try {
			emailSig = eSigGroup.getSelection().getActionCommand();
			switch (emailSig) {
			case "0":
				finalSig = "Jeff.txt";
				break;
			case "1":
				finalSig = "Nicole.txt";
				break;
			default:
				JOptionPane.showMessageDialog(null, "A email signature needs to be selected");
			}
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, "A email signature needs to be selected", "Error Message", JOptionPane.INFORMATION_MESSAGE);
		}

		// Selecting the email context based on radio button selection
		if (emailBody == "3") {

			String in1 = "requesterChangeEmp1.txt";
			String in2 = "requesterChangeEmp2.txt";
			String in3 = "requesterChangeEmp3.txt";
			String in4 = "requesterChangeEmp4.txt";

			s.append("Hello,\n\n");
			convert = new FileToString(s, fileArr, in1).getString();
			s.append(requestChangeName.getText());
			convert = new FileToString(s, fileArr, in2).getString();
			s.append(requestChangeDptOld.getText());
			s.append(" to ");
			s.append(requestChangeDptNew.getText());
			convert = new FileToString(s, fileArr, in3).getString();
			s.append(requestChangeAuthFor.getText());
			s.append(".\n\n");
			convert = new FileToString(s, fileArr, in4).getString();
			s.append("\n\n");

		} else {

			s.append(employNameInput.getText() + "\n");
			switch (emailBody) {
			case "0":
				selection = "changedEmployment.txt";
				s.append(keyInput.getText() + "\n");
				s.insert(0, header);
				break;
			case "1":
				selection = "leftEmployment.txt";
				s.append(keyInput.getText() + "\n");
				s.insert(0, header);
				break;
			case "2":
				selection = "30DaysFailToPickUp.txt";
				s.append(recordInput.getText() + "\n");
				s.insert(0, header);
				break;
			}
			// Importing the email body
			s.append("\n");	
			convert = new FileToString(s, fileArr, selection).getString();
		}
		s.append("\n\n Thank you,");
		// Importing the signature footer
		FileImporter sigIn = new FileImporter(finalSig);
		ArrayList<String> sigArr = new ArrayList<>();
		try {
			sigArr = sigIn.getFile();
			for (int i = 0; i < sigArr.size(); i++) {
				s.append(sigArr.get(i));
				s.append("%0D%0A");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// Setting email to string
		s.toString().replaceAll(" ", "%20").replaceAll("\n", "%0D%0A");
		// Checking that user computer will allow mailto and will send message to computers default email
		Desktop desktop;
		if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL) && emailSig != null) {

			URI mailto = null;
			try {
				switch (emailBody) {
				case "0":
				case "1":
					mailto = new URI(emailTitleKey + s.toString().replaceAll(" ", "%20").replaceAll("\n", "%0D%0A"));
					break;
				case "2":
					mailto = new URI(emailTitleID + s.toString().replaceAll(" ", "%20").replaceAll("\n", "%0D%0A"));
					break;
				case "3":
					mailto = new URI(emailTitleRequesterChange + s.toString().replaceAll(" ", "%20").replaceAll("\n", "%0D%0A"));
					break;
				}
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}

			try {
				desktop.mail(mailto);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("desktop doesn't support mailto or no emailSig selected; mail is dead");
		}
	}
}
