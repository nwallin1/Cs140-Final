package projectview;

import java.util.Observable;

import javax.swing.JFrame;

import project.MachineModel;

@SuppressWarnings("deprecation")
public class ViewMediator extends Observable {

	private MachineModel model;
	private JFrame frame;
	
	public void step() {}
	
	public MachineModel getModel() {
		return model;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public void setModel(MachineModel model) {
		this.model = model;
	}

	public void clearJob() {
		model.clearJob();
	}

	public void makeReady(String string) {
		// TODO Auto-generated method stub
		
	}
}