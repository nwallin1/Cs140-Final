package projectview;

import java.awt.GridLayout;

import java.util.Observable;

import java.util.Observer;



import javax.swing.JComponent;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JPanel;

import javax.swing.JTextField;



import project.MachineModel; // and other swing components



public class ProcessorViewPanel implements Observer {

	private MachineModel model;

	private JTextField acc = new JTextField();

	private JTextField ip = new JTextField();

	private JTextField memoryBase = new JTextField();



	public ProcessorViewPanel(ViewMediator gui, MachineModel model) {

		this.model = model;

		gui.addObserver(this);

	}



	public JComponent createProcessorDisplay() {

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(1,0));

		panel.add(new JLabel("Accumulator: ", JLabel.RIGHT));

		panel.add(acc);

		panel.add(new JLabel("Instruction Pointer: ", JLabel.RIGHT));

		panel.add(ip);

		panel.add(new JLabel("Memory Base: ", JLabel.RIGHT));

		panel.add(memoryBase);

		return panel;

	}



	@Override

	public void update(Observable arg0, Object arg1) {

		if(model != null) {

			acc.setText("" + model.getAccumulator());

		}

		

		if(model != null) {

			ip.setText("" + model.getInstructionPointer());

		}

		

		if(model != null) {

			memoryBase.setText("" + model.getMemoryBase());

		}

	}

	

	public static void main(String[] args) {

		ViewMediator view = new ViewMediator(); 

		MachineModel model = new MachineModel();

		ProcessorViewPanel panel = 

			new ProcessorViewPanel(view, model);

		JFrame frame = new JFrame("TEST");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(700, 60);

		frame.setLocationRelativeTo(null);

		frame.add(panel.createProcessorDisplay());

		frame.setVisible(true);

	}

}