
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DummyClass implements ActionListener{
	private JFrame jFrame;
	private JPanel jPanel;
	private JButton jButton;
	private JLabel jLabel;
	private int anInt1;
	public static final String GUI_TITLE = "Dummeh!";

	private void buildGUI()
	{
		jFrame = new JFrame(DummyClass.GUI_TITLE);
		jPanel = new JPanel(new BorderLayout());

		jButton = new JButton("Could you please press me as I wish to show you anInt1");
		jButton.addActionListener(this);
		jButton.setActionCommand("Moose");
		jLabel = new JLabel("The int stored in anInt1 is: ");

		JButton jButton1 = new JButton("Modify Class");
		jButton1.addActionListener(this);
		jButton1.setActionCommand("Modify");

		jPanel.add(jLabel, BorderLayout.NORTH);
		jPanel.add(jButton, BorderLayout.SOUTH);
		jPanel.add(jButton1, BorderLayout.WEST);

		jFrame.add(jPanel);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.pack();
		jFrame.setVisible(true);
	}

	private void setInt(int newInt)
	{
		anInt1 = newInt;
	}

	private DummyClass()
	{
		buildGUI();
		setInt(1235);
	}

	public static void main(String[] args)
	{
		new DummyClass();
	}

	@Override
	public void actionPerformed(ActionEvent evt)
	{
		if(evt.getActionCommand().equals("Spoon")) {
			jLabel.setText("Moose!!");
		} else if(evt.getActionCommand().equals("Modify")){
			//jLabel.setText("Modify Class");
			try {
				ModifyClass.modifyClass();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			jLabel.setText("The int stored in anInt1 is: " + anInt1);
		}
	}
}