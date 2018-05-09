package projectview;

import javax.swing.Timer;



public class Animator {



	private static final int TICK = 500;

	private boolean autoStepOn = false;

	private Timer timer;

	private ViewMediator view;

	

	public Animator(ViewMediator view) {

		this.view = view;

	}

	

	void toggleAutoStep() {

		autoStepOn = !autoStepOn;

	}

	

	void setPeriod(int period) {

		timer.setDelay(period);

	}

	

	public void start() {

		timer = new Timer(TICK, e -> {if(autoStepOn) view.step();});

		timer.start();

	}



	public boolean isAutoStepOn() {

		return autoStepOn;

	}



	public void setAutoStepOn(boolean autoStepOn) {

		this.autoStepOn = autoStepOn;

	}

}